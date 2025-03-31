import {
    forgotPasswordModal, forgotPasswordForm, forgotPasswordEmail,
    initializeForgotPasswordModal, validateForgotPasswordEmail, handleForgotPasswordFormSubmit
} from "./module/forgot-password-module.js";

forgotPasswordModal.addEventListener("show.bs.modal", () => {
    initializeForgotPasswordModal();
});

forgotPasswordEmail.addEventListener("input", validateForgotPasswordEmail);

forgotPasswordForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    await handleForgotPasswordFormSubmit();
});