import { clearForm, DATATABLE_DEFAULT_OPTIONS, formatDateTime, setButtonLoadingState, validateField } from "../helper/utility.js";
import { handleFetchError, postRequest, putRequest } from "../helper/fetch-helper.js";
import Toast from "../helper/toast.js";
import { CategoryAPI } from "../helper/api-helper.js";

const addCategoryBtn = document.getElementById("add_category_btn");
const categoryModal = document.getElementById("category_modal");
const categoryModalInstance = bootstrap.Modal.getOrCreateInstance(categoryModal);
const categoryForm = document.getElementById("category_form");
const categoryName = document.getElementById("category_name");
const categoryDescription = document.getElementById("category_description");
const categoryStatus = document.getElementById("category_status");
const categoryFormSubmitBtn = document.getElementById("category_form_submit_btn");
let categoryTable;

function initializeCategoryModal() {
    resetCategoryModal();
    updateCategoryModalContent(false);
}

function resetCategoryModal() {
    clearForm(categoryForm);
    categoryForm.dataset.method = "POST";
    delete categoryForm.dataset.categoryUuid;
    categoryStatus.checked = true;
}

function validateCategoryName() {
    return validateField(
        categoryName,
        [
            value => value.trim() !== "",
            value => value.length >= 3,
            value => value.length <= 100,
            value => /^[A-Za-z0-9\-'/()& ]+$/.test(value)
        ],
        [
            "Category Name is required.",
            "Category Name must be at least 1 character long.",
            "Category Name must be at most 100 characters long.",
            "Category Name can only contain letters, numbers, hyphens (-), apostrophes ('), slashes (/), parentheses (()), and ampersands (&)."
        ]
    );
}

function validateCategoryDescription() {
    return validateField(
        categoryDescription,
        [
            value => value.length <= 500
        ],
        [
            "Category Description must be at most 500 characters long."
        ]
    );
}

function initializeCategoryTable() {
    // Dynamically set the search placeholder
    DATATABLE_DEFAULT_OPTIONS.language.searchPlaceholder = "Search categories...";

    categoryTable = new DataTable("#category_table", {
        ...DATATABLE_DEFAULT_OPTIONS,
        "ajax": {
            ...DATATABLE_DEFAULT_OPTIONS.ajax,
            "url": "/admin/categories",
            "error": function (xhr, error, thrown) {
                Toast.error("Category datatables ajax error");
                if (xhr.status >= 400 && xhr.status < 600) {
                    const response = xhr.responseJSON;
                    const message = response ? response.message : "An error occurred";
                    Toast.error(message);
                }
            }
        },
        "order": [[5, "desc"]],
        "columnDefs": [
            {
                "defaultContent": "-",
                "targets": "_all"
            },
            {
                "targets": 0,
                "data": null,
                "className": "text-center",
                "orderable": false,
                "render": function (data, type, row, meta) {
                    return `${meta.row + 1}.`;
                }
            },
            {
                "targets": 1,
                "data": "name",
                "createdCell": function (td, cellData, rowData) {
                    // Create main name element
                    const nameDiv = document.createElement("div");
                    nameDiv.className = "text-capitalize fw-bold"
                    nameDiv.textContent = rowData.name;

                    // Create normalized name element (small and muted)
                    const normalizedDiv = document.createElement("div");
                    normalizedDiv.textContent = rowData.normalizedName;
                    normalizedDiv.className = "text-muted small";

                    // Append both elements to the cell
                    td.replaceChildren(nameDiv, normalizedDiv);
                }
            },
            {
                "targets": 2,
                "data": "isActive",
                "className": "text-center",
                "createdCell": function (td, cellData) {
                    td.textContent = ""; // Clear existing content

                    const statusSpan = document.createElement("span");
                    statusSpan.classList.add("badge", "rounded-pill");
                    statusSpan.classList.add(cellData ? "bg-success" : "bg-secondary");
                    statusSpan.textContent = cellData ? "Active" : "Inactive";

                    td.appendChild(statusSpan);
                }
            },
            {
                "targets": 3,
                "data": "totalProducts",
                "className": "text-center",
                "createdCell": function (td, cellData) {
                    td.textContent = ""; // Clear existing content

                    const badge = document.createElement("span");
                    badge.className = "badge bg-primary";
                    badge.textContent = cellData ?? 0;

                    td.appendChild(badge);
                }
            },
            {
                "targets": 4,
                "data": "createdAt",
                "createdCell": function (td, cellData) {
                    td.textContent = formatDateTime(cellData) || "-";
                }
            },
            {
                "targets": 5,
                "data": "updatedAt",
                "createdCell": function (td, cellData) {
                    td.textContent = formatDateTime(cellData) || "-";
                }
            },
            {
                "targets": 6,
                "data": "uuid",
                "className": "text-center",
                "orderable": false,
                "createdCell": function (td, cellData) {
                    const editButton = document.createElement("button");
                    editButton.className = "btn btn-sm btn-warning btn-edit";
                    const editIcon = document.createElement("i");
                    editIcon.className = "fa-solid fa-pen";
                    editButton.appendChild(editIcon);
                    editButton.addEventListener("click", async () => await initializeEditCategoryModal(cellData));

                    const deleteButton = document.createElement("button");
                    deleteButton.className = "btn btn-sm btn-danger btn-delete ms-2";
                    const deleteIcon = document.createElement("i");
                    deleteIcon.className = "fa fa-trash";
                    deleteButton.appendChild(deleteIcon);
                    deleteButton.addEventListener("click", () => handleDeleteCategory(cellData));

                    td.replaceChildren(editButton, deleteButton);
                }
            },
        ],
    });

    return categoryTable;
}

async function initializeEditCategoryModal(categoryId) {
    try {
        resetCategoryModal();
        updateCategoryModalContent(true);
        categoryForm.dataset.method = "PUT";
        categoryForm.dataset.categoryUuid = categoryId;

        const { name, description, isActive } = await CategoryAPI.getCategoryByUuid(categoryId);
        categoryName.value = name;
        categoryDescription.value = description;
        categoryStatus.checked = isActive;

        categoryModalInstance.show();
    } catch (e) {
        handleFetchError(e);
    }
}

async function handleDeleteCategory(categoryId) {
    const confirmDelete = confirm("Are you sure you want to delete this category?");
    if (!confirmDelete) return;

    try {
        await CategoryAPI.deleteCategoryByUuid(categoryId);

        Toast.success("Category deleted successfully");

        // Reload DataTable after deletion
        if (categoryTable) {
            categoryTable.ajax.reload(null, false); // Keep the current page
        }
    } catch (error) {
        handleFetchError(error);
    }
}

async function handleCategoryFormSubmit() {

    if (!validateCategoryName() || !validateCategoryDescription()) return;

    const requestData = {
        name: categoryName.value.trim(),
        description: categoryDescription.value.trim(),
        isActive: categoryStatus.checked
    };

    // Determine if it's an update or create
    const isEdit = categoryForm.dataset.method === "PUT";
    const categoryId = categoryForm.dataset.categoryUuid
    const url = isEdit ? `/admin/categories/${categoryId}` : "/admin/categories";
    const requestFunction = isEdit ? putRequest : postRequest;

    try {
        setButtonLoadingState(categoryFormSubmitBtn, true);

        await requestFunction(url, requestData);
        Toast.success(`Category ${isEdit ? 'updated' : 'created'} successfully`);

        categoryModalInstance.hide();

        if (categoryTable) {
            categoryTable.ajax.reload(null, false); // Pass `false` to stay on the current page
        }

    } catch (error) {
        handleFetchError(error);
    } finally {
        setButtonLoadingState(categoryFormSubmitBtn, false);
    }
}

function updateCategoryModalContent(isEdit) {
    const modalTitle = document.getElementById("category_modal_title");

    const createIcon = (iconClass) => {
        const icon = document.createElement("i");
        icon.className = iconClass;
        return icon;
    };

    // Update Modal Title with Icon
    modalTitle.replaceChildren(
        createIcon(`fa-solid ${isEdit ? "fa-pen" : "fa-circle-plus"}`),
        document.createTextNode(` ${isEdit ? "Edit" : "Add"} Category`)
    );

    // Update Submit Button with Icon
    categoryFormSubmitBtn.replaceChildren(
        createIcon("fa-solid fa-save"),
        document.createTextNode(` ${isEdit ? "Update" : "Create"} Category`)
    );
}

export {
    addCategoryBtn, categoryForm, categoryName, categoryDescription, categoryStatus, categoryFormSubmitBtn,
    validateCategoryName, validateCategoryDescription, handleCategoryFormSubmit, initializeCategoryModal, categoryModal,
    initializeCategoryTable
}