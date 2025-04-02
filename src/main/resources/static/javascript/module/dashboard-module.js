import { CategoryAPI } from "../helper/api-helper.js";
import { handleFetchError } from "../helper/fetch-helper.js";
import Toast from "../helper/toast.js";
import { createSpinnerElement, DATATABLE_DEFAULT_OPTIONS } from "../helper/utility.js";

const categoryList = document.getElementById("category_list");
const sortOptions = document.getElementById('sort_options')
const searchInput = document.getElementById("search_input");
let productTable;
const CART_KEY = "cart_items";

async function initializeDashboardData() {
    // 1. Show spinner immediately when loading starts
    categoryList.textContent = "";
    categoryList.appendChild(
        createSpinnerElement({
            containerClass: "list-group-item",
            alignment: "center"
        })
    );

    try {
        const [categories] = await Promise.all([
            CategoryAPI.getAllPublicCategories()
        ]);

        // 2. Clear spinner after data is loaded
        categoryList.textContent = '';

        if (!categories.length) {
            const noCategoryMsg = document.createElement("div");
            noCategoryMsg.classList.add("list-group-item", "text-muted", "text-center");
            noCategoryMsg.textContent = "No categories available";
            categoryList.appendChild(noCategoryMsg);
            return;
        }

        // Add "All Categories" option
        const allCategoriesLink = document.createElement("a");
        allCategoriesLink.href = "#";
        allCategoriesLink.classList.add("list-group-item", "text-capitalize", "category-filter", "active");
        allCategoriesLink.dataset.category = "";
        allCategoriesLink.textContent = "All Categories";
        categoryList.appendChild(allCategoriesLink);

        // Add individual categories
        categories.forEach(category => {
            const link = document.createElement("a");
            link.href = `#${category.normalizedName}`;
            link.classList.add("list-group-item", "text-capitalize", "category-filter");
            link.dataset.category = category.name; // Use the actual category name for filtering
            link.textContent = category.name;
            categoryList.appendChild(link);
        });

    } catch (error) {
        handleFetchError(error);
    }
}

function initializeProductTable() {
    productTable = new DataTable("#product_table", {
        ...DATATABLE_DEFAULT_OPTIONS,
        "dom": 'rt<"d-flex align-items-center justify-content-between"ip>',
        "ajax": {
            ...DATATABLE_DEFAULT_OPTIONS.ajax,
            "url": "/products",
            "error": function (xhr, error, thrown) {
                Toast.error("Product datatables ajax error");
                if (xhr.status >= 400 && xhr.status < 600) {
                    const response = xhr.responseJSON;
                    const message = response ? response.message : "An error occurred";
                    Toast.error(message);
                }
            }
        },
        "columns": [
            { data: "price", visible: false },
            { data: "name", visible: false },
            { data: "categoryName", visible: false },
            {
                data: null,
                orderable: false,
                render: function (data, type, row) {
                    // Create the main card container
                    const card = document.createElement("div");
                    card.className = "card h-100";

                    // Create card body
                    const cardBody = document.createElement("div");
                    cardBody.className = "card-body";
                    card.appendChild(cardBody);

                    // Create row container
                    const rowDiv = document.createElement("div");
                    rowDiv.className = "row g-2 align-items-center";
                    cardBody.appendChild(rowDiv);

                    // Create image column
                    const imgCol = document.createElement("div");
                    imgCol.className = "col-2";
                    rowDiv.appendChild(imgCol);

                    const img = document.createElement("img");
                    img.src = `/products/${row.uuid}/images`;
                    img.className = "img-fluid rounded";
                    img.alt = "Product";
                    imgCol.appendChild(img);

                    // Create content column
                    const contentCol = document.createElement("div");
                    contentCol.className = "col-10 position-relative"; // Added position-relative
                    rowDiv.appendChild(contentCol);

                    // Create product name container
                    const nameContainer = document.createElement("div");
                    nameContainer.className = "d-flex justify-content-between align-items-center mb-1";
                    contentCol.appendChild(nameContainer);

                    // Create product name
                    const name = document.createElement("h4");
                    name.className = "text-capitalize fw-bold mb-0";
                    name.textContent = row.name;
                    nameContainer.appendChild(name);

                    // Create stock status badge (pill-shaped, aligned with product name)
                    const stockBadge = document.createElement("span");
                    stockBadge.className = row.inStock ? "badge rounded-pill bg-success" : "badge rounded-pill bg-danger";
                    stockBadge.textContent = row.inStock ? "In Stock" : "Out of Stock";
                    nameContainer.appendChild(stockBadge);

                    // Create price
                    const price = document.createElement("p");
                    price.className = "text-danger fw-bold";
                    price.textContent = `₹${row.price.toFixed(2)}`;
                    contentCol.appendChild(price);

                    // Create description
                    const desc = document.createElement("p");
                    desc.className = "small text-muted";
                    desc.textContent = row.description || "No description available";
                    contentCol.appendChild(desc);

                    // Create rating container
                    const ratingContainer = document.createElement("div");
                    contentCol.appendChild(ratingContainer);

                    // Add rating stars (assuming generateRatingStars returns HTML string)
                    ratingContainer.appendChild(generateRatingStars(row.rating || 0));

                    // Add review count
                    const reviewCount = document.createElement("span");
                    reviewCount.className = "small text-muted";
                    reviewCount.textContent = `(${row.reviewCount || 0})`;
                    ratingContainer.appendChild(reviewCount);

                    // Create button container
                    const btnContainer = document.createElement("div");
                    btnContainer.className = "mt-2 d-flex align-items-center";
                    contentCol.appendChild(btnContainer);

                    // Create Add to Cart button (outline with icon)
                    const addToCartBtn = document.createElement("button");
                    addToCartBtn.className = "btn btn-outline-primary btn-sm";
                    addToCartBtn.disabled = !row.inStock;

                    // Create cart icon
                    const cartIcon = document.createElement("i");
                    cartIcon.className = "fas fa-shopping-cart me-1";
                    addToCartBtn.appendChild(cartIcon);

                    // Add text
                    const btnText = document.createTextNode("Add to Cart");
                    addToCartBtn.appendChild(btnText);

                    btnContainer.appendChild(addToCartBtn);

                    // Create Wishlist button
                    const wishlistBtn = document.createElement("button");
                    wishlistBtn.className = "btn btn-outline-danger btn-sm ms-1";
                    const heartIcon = document.createElement("i");
                    heartIcon.className = "fas fa-heart";
                    wishlistBtn.appendChild(heartIcon);
                    btnContainer.appendChild(wishlistBtn);

                    // Return the outer container
                    return card;
                }
            }
        ],
        columnDefs: [
            { targets: [0, 1, 2], searchable: true }, // Make hidden columns searchable
            { targets: 3, searchable: true, orderable: false } // Card column - searchable but not sortable
        ]
    });

    return productTable;
}

function handleProductSorting() {
    if (!productTable) return;

    const [colIndex, dir] = this.value.split('-');
    productTable.order([parseInt(colIndex), dir]).draw();

    // Reset to the first page after sorting
    productTable.page("first").draw("page");
}

function handleProductSearch() {
    if (!productTable) return;

    const searchTerm = searchInput.value.trim();
    productTable.search(searchTerm).draw();
}

function handleCategoryFilter(e) {
    if (e.target.classList.contains("category-filter")) {
        e.preventDefault();
        const category = e.target.dataset.category;

        // Remove active class from all items
        document.querySelectorAll("#category_list .category-filter").forEach(item => {
            item.classList.remove("active");
        });

        // Add active class to clicked item
        e.target.classList.add("active");

        // Filter the table
        productTable.column(2).search(category).draw();
    }
}

function generateRatingStars(rating) {
    // Ensure rating is an integer between 0 and 5
    const validatedRating = Math.max(0, Math.min(5, Math.floor(rating)));

    // Create a container element
    const starContainer = document.createElement("span");

    // Add full stars
    for (let i = 0; i < validatedRating; i++) {
        const star = document.createElement("i");
        star.classList.add("fas", "fa-star", "text-warning"); // Full star
        starContainer.appendChild(star);
    }

    // Add empty stars
    for (let i = 0; i < 5 - validatedRating; i++) {
        const star = document.createElement("i");
        star.classList.add("far", "fa-star", "text-warning"); // Empty star
        starContainer.appendChild(star);
    }
    return starContainer;
}

export {
    handleProductSearch, handleProductSorting, initializeDashboardData, initializeProductTable, handleCategoryFilter,
    searchInput, sortOptions, categoryList
};

