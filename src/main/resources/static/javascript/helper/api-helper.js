import { deleteRequest, getRequest, patchRequest, patchUploadFile, postRequest, putRequest } from './fetch-helper.js';


const BASE_URLS = {
    SIGN_UP: "/auth/signup",
    CATEGORY: "/admin/categories",
    PRODUCT: "/admin/products",
    SMTP: "/admin/smtp"
};

// Authentication
const AuthAPI = {
    signUp: (body) => postRequest(BASE_URLS.SIGN_UP, body),
};

// Categories
const CategoryAPI = {
    createCategory: (body) => postRequest(BASE_URLS.CATEGORY, body),
    getAllCategories: () => getRequest(BASE_URLS.CATEGORY),
    getAllPublicCategories: () => getRequest("/categories"),
    getCategoryByUuid: (uuid) => getRequest(`${BASE_URLS.CATEGORY}/${uuid}`),
    updateCategoryByUuid: (uuid, body) => putRequest(`${BASE_URLS.CATEGORY}/${uuid}`, body),
    deleteCategoryByUuid: (uuid) => deleteRequest(`${BASE_URLS.CATEGORY}/${uuid}`),
};

// Products
const ProductAPI = {
    createProduct: (body) => postRequest(BASE_URLS.PRODUCT, body),
    updateProduct: (uuid, body) => putRequest(`${BASE_URLS.PRODUCT}/${uuid}`, body),
    uploadProductImage: (productUuid, imageFile) => patchUploadFile(`${BASE_URLS.PRODUCT}/${productUuid}/images`, imageFile, "image"),
    getProductByUuid: (uuid) => getRequest(`${BASE_URLS.PRODUCT}/${uuid}`),
    getAllProducts: () => getRequest(BASE_URLS.PRODUCT),
    getProductImage: (uuid) => getRequest(`${BASE_URLS.PRODUCT}/${uuid}/images`),
    deleteProductByUuid: (uuid) => deleteRequest(`${BASE_URLS.PRODUCT}/${uuid}`)
};

const SmtpAPI = {
    getSmtp: () => getRequest(BASE_URLS.SMTP),
    updateSmtp: (body) => putRequest(BASE_URLS.SMTP, body)
};

export {
    BASE_URLS, AuthAPI, CategoryAPI, ProductAPI, SmtpAPI
}