import { } from "./helper/utility.js";
import {
    addCategoryBtn, categoryDescription, categoryForm, categoryName, handleCategoryFormSubmit,
    initializeCategoryModal, initializeCategoryTable, validateCategoryDescription, validateCategoryName
} from "./module/category-module.js";

addCategoryBtn.addEventListener("click", () => initializeCategoryModal());

categoryName.addEventListener("input", validateCategoryName);
categoryDescription.addEventListener("input", validateCategoryDescription);

categoryForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    await handleCategoryFormSubmit();
});

initializeCategoryTable();