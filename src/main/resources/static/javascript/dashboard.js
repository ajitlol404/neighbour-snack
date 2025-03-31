import { handleCategoryFilter, handleProductSearch, categoryList, handleProductSorting, initializeDashboardData, initializeProductTable, searchInput, sortOptions } from "./module/dashboard-module.js";

initializeDashboardData();

initializeProductTable();

sortOptions.addEventListener("change", handleProductSorting);

searchInput.addEventListener("input", handleProductSearch);

categoryList.addEventListener('click', handleCategoryFilter);
