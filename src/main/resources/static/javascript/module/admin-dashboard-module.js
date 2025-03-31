import { CategoryAPI, ProductAPI } from "../helper/api-helper.js";
import { handleFetchError } from "../helper/fetch-helper.js";

const totalCategories = document.getElementById("totalCategories");
const activeCategories = document.getElementById("activeCategories");
const inactiveCategories = document.getElementById("inactiveCategories");
const totalProducts = document.getElementById("totalProducts");
const inStockProducts = document.getElementById("inStockProducts");
const outOfStockProducts = document.getElementById("outOfStockProducts");


async function initializeDashboardData() {
    try {
        // Fetch categories and products simultaneously
        const [categories, products] = await Promise.all([
            CategoryAPI.getAllCategories(),
            ProductAPI.getAllProducts()
        ]);

        totalCategories.textContent = categories.length;
        activeCategories.textContent = categories.filter(cat => cat.isActive).length;
        inactiveCategories.textContent = categories.length - categories.filter(cat => cat.isActive).length;

        totalProducts.textContent = products.length;
        inStockProducts.textContent = products.filter(prod => prod.inStock).length;
        outOfStockProducts.textContent = products.length - products.filter(prod => prod.inStock).length;
    } catch (error) {
        handleFetchError(error);
    }
}

export {
    initializeDashboardData
}