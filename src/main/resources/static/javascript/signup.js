import {
    handleSignupFormSubmit, initializeSignupModal, signupEmail, signupForm, signupModal,
    signupName, signupPassword, validateSignupEmail, validateSignupName, validateSignupPassword
} from "./module/signup-module.js";

signupModal.addEventListener("show.bs.modal", () => {
    initializeSignupModal();
});

signupName.addEventListener("input", validateSignupName);
signupEmail.addEventListener("input", validateSignupEmail);
signupPassword.addEventListener("input", validateSignupPassword);

signupForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    await handleSignupFormSubmit();
});