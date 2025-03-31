async function sleep(ms) {
    if (typeof ms === "number") {
        return new Promise(resolve => setTimeout(resolve, ms));
    } else {
        throw new Error("ms is not a 'number'");
    }
}

async function hideInstanceWithDelay(instance, delay = 160) {
    while (instance._isTransitioning) {
        await sleep(delay);
    }
    instance.hide();
}

function getLoader() {
    const span = document.createElement("span");
    span.className = "spinner-border spinner-border-sm ms-2";
    span.ariaHidden = true;
    return span;
}


function setButtonLoadingState(button, isLoading) {
    if (isLoading) {
        // Save original content only if not already saved
        if (!button.dataset.originalText) {
            button.dataset.originalText = button.textContent.trim();

            // Save the original icon (whether it's an <i> or SVG)
            const originalIcon = button.querySelector("svg") || button.querySelector("i");
            if (originalIcon) {
                button.dataset.originalIcon = originalIcon.outerHTML; // Save the full SVG/I tag structure
            }
        }

        // Disable the button
        button.disabled = true;

        // Clear the button's content
        button.textContent = "";

        // Create and append spinner icon
        const spinnerIcon = document.createElement("i");
        spinnerIcon.className = 'fa-solid fa-spinner fa-spin';
        button.appendChild(spinnerIcon);

        // Add loading text
        const loadingText = document.createTextNode('\u00A0Loading...');
        button.appendChild(loadingText);
    } else {
        // Re-enable the button
        button.disabled = false;

        // Clear current content
        button.textContent = '';

        // Restore the original icon (if it existed)
        if (button.dataset.originalIcon) {
            const template = document.createElement('template');
            template.innerHTML = button.dataset.originalIcon.trim();
            button.appendChild(template.content.firstChild);
        }

        // Restore the original text
        const originalText = document.createTextNode(`\u00A0${button.dataset.originalText}`);
        button.appendChild(originalText);
    }
}


function getIcon(className) {
    const icon = document.createElement("i");
    icon.className = `${className} cursor-pointer`;
    return icon;
}

const getEyeSlashIcon = () => getIcon("fa-solid fa-eye-slash");
const getEyeIcon = () => getIcon("fa-solid fa-eye");
const getTrashIcon = () => getIcon("fa-solid fa-trash");
const getPenIcon = () => getIcon("fa-solid fa-pen");
const getSendIcon = () => getIcon("fa-solid fa-paper-plane");
const getPdfIcon = () => getIcon("fa-solid fa-file-pdf");
const getExcelIcon = () => getIcon("fa-solid fa-file-excel");

function formatDateTime(timestamp, use12HourFormat = true) {
    const pad = (value) => value.toString().padStart(2, '0');
    const date = new Date(timestamp);
    const year = date.getFullYear();
    const month = pad(date.getMonth() + 1);
    const day = pad(date.getDate());
    let hours = date.getHours();
    const minutes = pad(date.getMinutes());

    if (use12HourFormat) {
        const ampm = hours >= 12 ? 'PM' : 'AM';
        hours = hours % 12 || 12; // Convert to 12-hour format
        return `${day}/${month}/${year} ${hours}:${minutes} ${ampm}`;
    }

    return `${day}/${month}/${year} ${pad(hours)}:${minutes}`;
}

function disableOrEnableForm(form, submitButton, isEnabled) {
    Array.from(form.querySelectorAll('*')).forEach(field => field.disabled = isEnabled);
    submitButton.disabled = isEnabled;
}

function clearForm(form) {
    if (form) {
        // Clear all input fields
        form.querySelectorAll("input, textarea, select").forEach(field => {
            if (field.tagName === "SELECT") {
                field.length = 0;
            } else {
                field.value = '';
            }
            field.classList.remove("is-invalid");
        });

        // Clear all error messages
        form.querySelectorAll("small").forEach(small => small.textContent = '');
    }
}

function validateField(input, validationLogic, errorMessages, errorMsgElement = null) {
    const { value, id } = input;

    errorMsgElement = errorMsgElement || document.getElementById(id + "_err_msg");

    errorMsgElement.textContent = ''; // Clear any previous error messages
    input.classList.remove("is-invalid");

    for (let i = 0; i < validationLogic.length; i++) {
        if (!validationLogic[i](value)) {
            input.classList.add("is-invalid");
            errorMsgElement.textContent = errorMessages[i];
            return false;
        }
    }

    return true;
}


function createSelectOption(selectElement, text, value, className = "") {
    const option = new Option(text, value);
    if (className) option.classList.add(className);

    selectElement.add(option);
    return option;
}


function setupDefaultOption(selectElement, text) {
    const option = createSelectOption(selectElement, text, "none");
    option.hidden = true;
    option.selected = true;
    return option;
};

const EMAIL_REGEX = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Z]{2,}$/i;
const NAME_REGEX = /^[a-zA-Z0-9]+( [a-zA-Z0-9]+)*$/;
const PASSWORD_REGEX = /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[!@#$%^&*(),.?":{}|<>])[A-Za-z\d!@#$%^&*(),.?":{}|<>]{8,32}$/;

const DATATABLE_DEFAULT_OPTIONS = {
    ajax: {
        dataSrc: "",
        type: "GET"
    },
    processing: true,
    searching: true,
    pageLength: 10,
    ordering: true,
    lengthChange: true,
    emptyTable: "No data available in table",
    info: true,
    bStateSave: false,
    language: {
        search: "", // Removes the 'Search:' label
        searchPlaceholder: "Search...", // Custom search placeholder
        lengthMenu: "_MENU_", // Hides 'entries per page' text, keeps the dropdown
        paginate: {
            next: "<i class='fa-solid fa-arrow-right'></i>",
            previous: "<i class='fa-solid fa-arrow-left'></i>"
        }
    }
};

/**
 * Creates a customizable spinner element
 * @param {Object} options - Configuration options for the spinner
 * @param {string} [options.size=''] - Spinner size ('sm' for small)
 * @param {string} [options.color='text-primary'] - Bootstrap text color class
 * @param {string} [options.alignment='center'] - Text alignment ('left', 'center', 'right')
 * @param {string} [options.containerClass=''] - Additional classes for the container
 * @param {string} [options.spinnerClass=''] - Additional classes for the spinner
 * @param {string} [options.text='Loading...'] - Accessibility text
 * @param {boolean} [options.showText=false] - Whether to show text next to spinner
 * @returns {HTMLElement} The spinner element
 */
function createSpinnerElement(options = {}) {
    // Default options
    const config = {
        size: '',
        color: 'text-primary',
        alignment: 'center',
        containerClass: '',
        spinnerClass: '',
        text: 'Loading...',
        showText: false,
        ...options
    };

    // Create container
    const container = document.createElement('div');
    container.className = `text-${config.alignment} ${config.containerClass}`.trim();

    // Create spinner
    const spinner = document.createElement('div');
    spinner.className = `spinner-border ${config.size ? 'spinner-border-' + config.size : ''} ${config.color} ${config.spinnerClass}`.trim();
    spinner.setAttribute('role', 'status');

    // Create accessibility text
    const spinnerText = document.createElement('span');
    spinnerText.className = 'visually-hidden';
    spinnerText.textContent = config.text;

    // Assemble spinner
    spinner.appendChild(spinnerText);
    container.appendChild(spinner);

    // Add visible text if requested
    if (config.showText) {
        const visibleText = document.createElement('span');
        visibleText.className = 'ms-2';
        visibleText.textContent = config.text;
        container.appendChild(visibleText);
    }

    return container;
}

export {
    sleep, hideInstanceWithDelay, getLoader, setButtonLoadingState, getIcon, getEyeSlashIcon,
    getEyeIcon, getTrashIcon, getPenIcon, getSendIcon, getPdfIcon, getExcelIcon, formatDateTime,
    disableOrEnableForm, clearForm, validateField, createSelectOption, setupDefaultOption,
    EMAIL_REGEX, NAME_REGEX, PASSWORD_REGEX, DATATABLE_DEFAULT_OPTIONS, createSpinnerElement
};

