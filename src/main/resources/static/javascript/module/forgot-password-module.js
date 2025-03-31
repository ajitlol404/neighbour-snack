import {
    validateField, clearForm, EMAIL_REGEX, setButtonLoadingState
} from "../helper/utility.js";

const forgotPasswordModal = document.getElementById("forgot_password_modal");
const forgotPasswordModalInstance = bootstrap.Modal.getOrCreateInstance(forgotPasswordModal);
const forgotPasswordForm = document.getElementById("forgot_password_form");
const forgotPasswordEmail = document.getElementById("forgot_password_email");
const forgotPasswordSubmitBtn = document.getElementById("forgot_password_submit_btn");

function initializeForgotPasswordModal() {
    resetForgotPasswordModal();
}

function resetForgotPasswordModal() {
    clearForm(forgotPasswordForm);
}

function validateForgotPasswordEmail() {
    return validateField(
        forgotPasswordEmail,
        [
            value => value !== '',
            value => value.length <= 256,
            value => EMAIL_REGEX.test(value)
        ],
        [
            "Email is required.",
            "Email must be less than or equal to 256 characters long.",
            "Email must be a valid email format.",
        ]
    );
}

async function handleForgotPasswordFormSubmit() {

    const isForgotPasswordEmailValid = validateForgotPasswordEmail();

    if (!isForgotPasswordEmailValid) {
        return;
    }

    try {
        setButtonLoadingState(forgotPasswordSubmitBtn, true);

        forgotPasswordModalInstance.hide();

    } catch (e) {
        console.log(e);
    } finally {
        setButtonLoadingState(forgotPasswordSubmitBtn, false);
    }

}

export {
    forgotPasswordModal, forgotPasswordModalInstance, forgotPasswordForm, forgotPasswordEmail, forgotPasswordSubmitBtn,
    initializeForgotPasswordModal, validateForgotPasswordEmail, handleForgotPasswordFormSubmit
}