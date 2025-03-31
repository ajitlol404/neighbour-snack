import Toast from "../helper/toast.js";

// Retrieve CSRF token information from meta tags
const csrfHeaderMetaKey = document.querySelector("meta[name=_csrf_header]").content;
const csrfHeaderMetaValue = document.querySelector("meta[name=_csrf]").content;

async function fetchRequest(url, method, requestBody = null) {
    const headers = {
        "Accept": "application/json",
        "Content-Type": "application/json",
        [csrfHeaderMetaKey]: csrfHeaderMetaValue
    };

    const options = {
        method,
        headers
    };

    if (requestBody) {
        options.body = JSON.stringify(requestBody);
    }

    const response = await fetch(url, options);
    let body = await response.text();
    if (response.headers.get("Content-Type")?.includes("json"))
        body = JSON.parse(body);

    if (!response.ok) {
        return Promise.reject({
            status: response.status,
            body
        });
    }
    return body;
}

export async function getRequest(url) {
    return fetchRequest(url, "GET");
}

export async function postRequest(url, requestBody) {
    return fetchRequest(url, "POST", requestBody);
}

export async function patchRequest(url, requestBody) {
    return fetchRequest(url, "PATCH", requestBody);
}

export async function putRequest(url, requestBody) {
    return fetchRequest(url, "PUT", requestBody);
}

export async function deleteRequest(url) {
    return fetchRequest(url, "DELETE");
}

export async function postUploadFile(url, file, fieldName = "file") {
    return uploadFileRequest(url, "POST", file, fieldName);
}

export async function putUploadFile(url, file, fieldName = "file") {
    return uploadFileRequest(url, "PUT", file, fieldName);
}

export async function patchUploadFile(url, file, fieldName = "file") {
    return uploadFileRequest(url, "PATCH", file, fieldName);
}

export async function uploadFileRequest(url, method, file, fieldName = "file") {

    const formData = new FormData();
    formData.append(fieldName, file);

    const headers = {
        [csrfHeaderMetaKey]: csrfHeaderMetaValue,
    };

    const options = {
        method,
        headers,
        body: formData,
    };

    const response = await fetch(url, options);

    if (!response.ok) {
        const body = await response.json();
        return Promise.reject({
            status: response.status,
            body,
        });
    }

    return response.json();
}


export function handleFetchError(e) {
    console.error(e);

    if (e.status === 400 && e.body?.errors) {
        // Show a separate toast for each error
        // Object.values(e.body.errors).forEach((errorMessage) => {
        //     Toast.error(errorMessage);
        // });
        e.body.errors.forEach((error) => {
            Toast.error(error.message);
        });
    } else {
        let errorMessage = "Something went wrong";

        if (e.body?.detail) {
            errorMessage = e.body.detail;
        }

        switch (e.status) {
            case 400:
                Toast.error(errorMessage);
                break;
            case 404:
                Toast.warning(errorMessage || "Not found");
                break;
            case 403:
                Toast.warning(errorMessage || "Access denied");
                break;
            default:
                Toast.error(errorMessage);
                break;
        }
    }
}
