function showToast(type, message) {
    let container = document.getElementById("toast-container");

    // If the container doesn't exist, create it and append to the body
    if (!container) {
        container = document.createElement("div");
        container.id = "toast-container";
        container.className = "toast-container z-2000 p-3 top-0 start-50 translate-middle-x position-fixed";
        document.body.appendChild(container);
    }

    // Get background class from the map (defaults to "danger" if type is invalid)
    const backgroundClass = `text-bg-${backgroundMap.get(type) || "danger"}`;

    // Create a new toast element
    const toast = document.createElement("div");
    toast.className = `toast align-items-center border-0 ${backgroundClass} my-2`;
    toast.setAttribute("role", "alert");
    toast.setAttribute("aria-live", "assertive");
    toast.setAttribute("aria-atomic", "true");
    toast.setAttribute("data-bs-delay", "5000");

    // Create the toast body with icon and message
    const toastBody = getToastBody(message, type);
    toast.appendChild(toastBody);

    // Append the toast to the container
    container.appendChild(toast);

    // Initialize and show the toast using Bootstrap
    const toastInstance = new bootstrap.Toast(toast);
    toastInstance.show();

    // Remove the toast from the DOM after it hides
    toast.addEventListener("hidden.bs.toast", () => {
        toast.remove();

        // If the container is empty, remove it from the DOM
        if (container.children.length === 0) {
            container.remove();
        }
    });
}

/**
 * Create the body of the toast with an icon and a message.
 * @param {string} message - The message to display in the toast.
 * @param {string} type - The type of the toast ('success', 'warning', 'error').
 * @returns {HTMLElement} The toast body element.
 */
function getToastBody(message, type) {
    const div = document.createElement("div");
    div.classList.add("d-flex", "justify-content-center");

    const innerDiv = document.createElement("div");
    innerDiv.classList.add("toast-body");

    // Append icon and message
    innerDiv.appendChild(getIcon(type));
    innerDiv.appendChild(document.createTextNode(" " + message));

    div.appendChild(innerDiv);
    return div;
}

/**
 * Get the appropriate icon for the toast type.
 * @param {string} type - The type of the toast ('success', 'warning', 'error').
 * @returns {HTMLElement} The icon element.
 */
function getIcon(type) {
    const icon = document.createElement("i");

    // Get icon class from the map (defaults to "fa-circle-xmark" for unknown types)
    const iconClass = iconMap.get(type) || "fa-circle-xmark";
    icon.classList.add("fa-solid", iconClass);

    return icon;
}

/**
 * Toast notification object with methods for different types of toasts.
 * @namespace Toast
 */
const Toast = {
    success: (message) => showToast("success", message),
    warning: (message) => showToast("warning", message),
    error: (message) => showToast("error", message)
};

/**
 * Map for background colors corresponding to different toast types.
 * @type {Map<string, string>}
 */
const backgroundMap = new Map(
    [
        ["success", "success"],
        ["warning", "warning"],
        ["error", "danger"]
    ]
);

/**
 * Map for icons corresponding to different toast types.
 * @type {Map<string, string>}
 */
const iconMap = new Map(
    [
        ["success", "fa-circle-check"],
        ["warning", "fa-circle-exclamation"],
        ["error", "fa-circle-xmark"]
    ]
);

export default Toast;
