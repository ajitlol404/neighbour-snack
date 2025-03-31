import { CategoryAPI, ProductAPI } from "../helper/api-helper.js";
import { handleFetchError } from "../helper/fetch-helper.js";
import Toast from "../helper/toast.js";
import { clearForm, createSelectOption, DATATABLE_DEFAULT_OPTIONS, formatDateTime, setButtonLoadingState, setupDefaultOption, validateField } from "../helper/utility.js";

const addProductBtn = document.getElementById("add_product_btn");
const productModal = document.getElementById("product_modal");
const productModalInstance = bootstrap.Modal.getOrCreateInstance(productModal);
const productForm = document.getElementById("product_form");
const productUnitWeight = document.getElementById("product_unit_weight");
const productUnitPiece = document.getElementById("product_unit_piece");
const priceLabel = document.getElementById("price_label");
const priceInput = document.getElementById("price_input");
const productName = document.getElementById("product_name")
const productDescription = document.getElementById("product_description")
const productImage = document.getElementById("product_image")
const productCategory = document.getElementById("product_category")
const productFormSubmitBtn = document.getElementById("product_form_submit_btn")
const productInstockStatus = document.getElementById("product_instock_status");
let productTable;


function initializeProductTable() {

    // Dynamically set the search placeholder
    DATATABLE_DEFAULT_OPTIONS.language.searchPlaceholder = "Search products...";

    productTable = new DataTable("#product_table", {
        ...DATATABLE_DEFAULT_OPTIONS,
        "ajax": {
            ...DATATABLE_DEFAULT_OPTIONS.ajax,
            "url": "/admin/products",
            "error": function (xhr, error, thrown) {
                Toast.error("Product datatables ajax error");
                if (xhr.status >= 400 && xhr.status < 600) {
                    const response = xhr.responseJSON;
                    const message = response ? response.message : "An error occurred";
                    Toast.error(message);
                }
            }
        },
        "order": [[8, "desc"]],
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
                    return `${meta.row + 1}.`; // Serial Number
                }
            },
            {
                "targets": 1,
                "data": "productImage",
                "className": "text-center",
                "orderable": false,
                "render": DataTable.render.text(),
                "createdCell": function (td, cellData, rowData) {
                    td.textContent = ""; // Clear previous content

                    if (rowData.productImage) {
                        const img = document.createElement("img");
                        img.src = `/admin/products/${rowData.uuid}/images`;
                        img.alt = "Product Image";
                        img.width = 50;
                        img.className = "img-thumbnail";
                        td.appendChild(img);
                    } else {
                        const span = document.createElement("span");
                        span.className = "text-muted";
                        span.textContent = "No Image";
                        td.appendChild(span);
                    }
                }
            },
            {
                "targets": 2,
                "data": "name",
                "render": DataTable.render.text(),
                "createdCell": function (td, cellData, rowData) {
                    const nameDiv = document.createElement("div");
                    nameDiv.className = "fw-bold text-capitalize";
                    nameDiv.textContent = rowData.name;

                    const normalizedDiv = document.createElement("div");
                    normalizedDiv.className = "text-muted small";
                    normalizedDiv.textContent = rowData.normalizedName;

                    td.replaceChildren(nameDiv, normalizedDiv);
                }
            },
            {
                "targets": 3,
                "data": "price",
                "className": "text-end",
                "render": DataTable.render.text(),
                "createdCell": function (td, cellData) {
                    td.textContent = "";

                    const span = document.createElement("span");
                    span.className = "fw-bold";
                    span.textContent = `₹ ${parseFloat(cellData).toFixed(2)}`;
                    td.appendChild(span);
                }
            },
            {
                "targets": 4,
                "data": "inStock",
                "className": "text-center",
                "render": DataTable.render.text(),
                "createdCell": function (td, cellData) {
                    td.textContent = "";

                    const span = document.createElement("span");
                    span.className = `badge ${cellData ? 'bg-success' : 'bg-secondary'} rounded-pill`;
                    span.textContent = cellData ? "In Stock" : "Out of Stock";
                    td.appendChild(span);
                }
            },
            {
                "targets": 5,
                "data": "categoryName",
                "className": "text-capitalize"
            },
            {
                "targets": 6,
                "data": "unitType",
                "className": "text-center",
                "render": DataTable.render.text(),
                "createdCell": function (td, cellData) {
                    td.textContent = "";

                    const span = document.createElement("span");
                    span.className = `badge ${cellData === 'WEIGHT' ? 'bg-info' : 'bg-primary'}`;
                    span.textContent = cellData;
                    td.appendChild(span);
                }
            },
            {
                "targets": 7,
                "data": "createdAt",
                "render": DataTable.render.text(),
                "createdCell": function (td, cellData) {
                    td.textContent = formatDateTime(cellData) || "-";
                }
            },
            {
                "targets": 8,
                "data": "updatedAt",
                "render": DataTable.render.text(),
                "createdCell": function (td, cellData) {
                    td.textContent = formatDateTime(cellData) || "-";
                }
            },
            {
                "targets": 9,
                "data": "uuid",
                "className": "text-center",
                "orderable": false,
                "render": DataTable.render.text(),
                "createdCell": function (td, cellData) {
                    const editButton = document.createElement("button");
                    editButton.className = "btn btn-sm btn-warning btn-edit";
                    const editIcon = document.createElement("i");
                    editIcon.className = "fa-solid fa-pen";
                    editButton.appendChild(editIcon);
                    editButton.addEventListener("click", async () => await initializeEditProductModal(cellData));

                    const deleteButton = document.createElement("button");
                    deleteButton.className = "btn btn-sm btn-danger btn-delete ms-2";
                    const deleteIcon = document.createElement("i");
                    deleteIcon.className = "fa fa-trash";
                    deleteButton.appendChild(deleteIcon);
                    deleteButton.addEventListener("click", () => handleDeleteProduct(cellData));

                    td.replaceChildren(editButton, deleteButton);
                }
            },
        ],
    });

    return productTable;
}

async function initializeEditProductModal(productId) {
    try {
        await initializeProductModal();
        updateProductModalContent(true);
        productForm.dataset.method = "PUT";
        productForm.dataset.productUuid = productId;

        const { name, description, price, inStock, categoryUuid, unitType } = await ProductAPI.getProductByUuid(productId);

        productName.value = name;
        productDescription.value = description;
        productUnitWeight.checked = unitType === "WEIGHT";
        productUnitPiece.checked = unitType === "PIECE";
        priceInput.value = price;
        productInstockStatus.checked = inStock;
        productCategory.value = categoryUuid;

        productModalInstance.show();
    } catch (e) {
        handleFetchError(e);
    }
}

function updateProductModalContent(isEdit) {
    const productModalTitle = document.getElementById("product_modal_title");

    const createIcon = (iconClass) => {
        const icon = document.createElement("i");
        icon.className = iconClass;
        return icon;
    };

    // Update Modal Title with Icon
    productModalTitle.replaceChildren(
        createIcon(`fa-solid ${isEdit ? "fa-pen" : "fa-circle-plus"}`),
        document.createTextNode(` ${isEdit ? "Edit" : "Add"} Product`)
    );

    // Update Submit Button with Icon
    productFormSubmitBtn.replaceChildren(
        createIcon("fa-solid fa-save"),
        document.createTextNode(` ${isEdit ? "Update" : "Create"} Product`)
    );
}


async function handleDeleteProduct(productId) {
    const confirmDelete = confirm("Are you sure you want to delete this product?");
    if (!confirmDelete) return;

    try {
        await ProductAPI.deleteProductByUuid(productId);

        Toast.success("Product deleted successfully");

        // Reload DataTable after deletion
        if (productTable) {
            productTable.ajax.reload(null, false); // Keep the current page
        }
    } catch (error) {
        handleFetchError(error);
    }
}

function updatePricePlaceholder() {
    const isWeightBased = productUnitWeight.checked;
    priceLabel.textContent = isWeightBased ? "Price Per 100g:" : "Price Per Piece:";
    priceInput.placeholder = isWeightBased ? "Enter price per 100g" : "Enter price per piece";
}


async function initializeProductModal() {
    resetProductModal();
    await populateCategorySelect();
}

function resetProductModal() {
    clearForm(productForm);
    productForm.dataset.method = "POST";
    delete productForm.dataset.productUuid;

    productUnitWeight.checked = true;
    updatePricePlaceholder();

    productInstockStatus.checked = true;

}

function validateProductName() {
    return validateField(
        productName,
        [
            value => value.trim() !== "",
            value => value.length >= 3,
            value => value.length <= 150,
            value => /^[A-Za-z0-9\-'/()& ]+$/.test(value)
        ],
        [
            "Product Name is required.",
            "Product Name must be at least 3 character long.",
            "Product Name must be at most 150 characters long.",
            "Product Name can only contain letters, numbers, hyphens (-), apostrophes ('), slashes (/), parentheses (()), and ampersands (&)."
        ]
    );
}

function validateProductDescription() {
    return validateField(
        productDescription,
        [
            value => value.length <= 500
        ],
        [
            "Description must be at most 500 characters long."
        ]
    );
}

function validateProductCategory() {
    return validateField(
        productCategory,
        [
            value => value.trim() !== "none"
        ],
        [
            "Category is required."
        ]
    );
}

function validateProductImage() {
    const file = productImage.files[0]; // Get the selected file
    const maxSize = 2 * 1024 * 1024; // 2MB in bytes

    return validateField(
        productImage,
        [
            () => file !== undefined,
            () => file && file.size <= maxSize
        ],
        [
            "Product Image is required.",
            "File size must not exceed 2MB."
        ]
    );
}


function validateProductPrice() {
    return validateField(
        priceInput,
        [
            value => value.trim() !== "",
            value => !isNaN(value) && parseFloat(value) > 0
        ],
        [
            "Price is required.",
            "Price must be a positive number."
        ]
    );
}

async function handleProductFormSubmit() {
    const isValidName = validateProductName();
    const isValidDescription = validateProductDescription();
    const isValidCategory = validateProductCategory();
    const isValidPrice = validateProductPrice();

    const isEdit = productForm.dataset.method === "PUT";
    const isValidImage = isEdit ? true : validateProductImage(); // Image validation only for create

    if (!isValidName || !isValidDescription || !isValidCategory || !isValidPrice || !isValidImage) return;

    const requestData = {
        name: productName.value.trim(),
        description: productDescription.value.trim(),
        categoryId: productCategory.value,
        price: parseFloat(priceInput.value),
        unitType: productUnitWeight.checked ? "WEIGHT" : "PIECE",
        inStock: productInstockStatus.checked
    };

    try {
        setButtonLoadingState(productFormSubmitBtn, true);

        let productUuid;
        if (isEdit) {
            // **UPDATE PRODUCT**
            productUuid = productForm.dataset.productUuid;
            await ProductAPI.updateProduct(productUuid, requestData);
        } else {
            // **CREATE PRODUCT**
            const createdProduct = await ProductAPI.createProduct(requestData);
            if (!createdProduct || !createdProduct.uuid) throw new Error("Failed to create product");
            productUuid = createdProduct.uuid;
        }

        // **UPLOAD IMAGE ONLY IF A NEW IMAGE IS SELECTED**
        const imageFile = productImage.files[0];
        if (imageFile) {
            await ProductAPI.uploadProductImage(productUuid, imageFile);
        }

        Toast.success(`Product ${isEdit ? "updated" : "created"} successfully`);
        productModalInstance.hide();

        if (productTable) {
            productTable.ajax.reload(null, false);
        }
    } catch (error) {
        handleFetchError(error);
    } finally {
        setButtonLoadingState(productFormSubmitBtn, false);
    }
}


async function populateCategorySelect() {
    try {
        const categories = await CategoryAPI.getAllCategories();

        productCategory.length = 0;

        setupDefaultOption(productCategory, "Select a category");

        categories.forEach(category => {
            createSelectOption(productCategory, category.name, category.uuid, "text-capitalize");
        });

    } catch (error) {
        handleFetchError(error);
    }
}

export { addProductBtn, handleProductFormSubmit, initializeProductModal, initializeProductTable, priceInput, productCategory, productDescription, productForm, productImage, productName, productUnitPiece, productUnitWeight, updatePricePlaceholder, validateProductCategory, validateProductDescription, validateProductImage, validateProductName, validateProductPrice };
