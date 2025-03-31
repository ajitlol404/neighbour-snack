import { } from "./helper/utility.js";
import {
    addProductBtn, handleProductFormSubmit, initializeProductModal, initializeProductTable, priceInput,
    productCategory, productDescription, productForm, productImage, productName, productUnitPiece, productUnitWeight,
    updatePricePlaceholder, validateProductCategory, validateProductDescription, validateProductImage, validateProductName,
    validateProductPrice
} from "./module/product-module.js";


initializeProductTable();

addProductBtn.addEventListener("click", async () => await initializeProductModal());

productUnitWeight.addEventListener("change", updatePricePlaceholder);
productUnitPiece.addEventListener("change", updatePricePlaceholder);

productName.addEventListener("input", validateProductName);
productDescription.addEventListener("input", validateProductDescription);
productCategory.addEventListener("change", validateProductCategory);
priceInput.addEventListener("input", validateProductPrice);
productImage.addEventListener("change", validateProductImage);

productForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    await handleProductFormSubmit();
});