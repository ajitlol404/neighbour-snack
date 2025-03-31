import { postRequest, handleFetchError } from "../helper/fetch-helper.js";

import {
    validateField, clearForm, EMAIL_REGEX, setButtonLoadingState, NAME_REGEX,
    PASSWORD_REGEX
} from "../helper/utility.js";

const signupModal = document.getElementById("signup_modal");
const signupModalInstance = bootstrap.Modal.getOrCreateInstance(signupModal);
const signupForm = document.getElementById("signup_form");
const signupName = document.getElementById("signup_name");
const signupEmail = document.getElementById("signup_email");
const signupPassword = document.getElementById("signup_password");
const signupFormSubmitBtn = document.getElementById("signup_form_submit_btn");

function initializeSignupModal() {
    resetSignupModal();
}

function resetSignupModal() {
    clearForm(signupForm);
}

function validateSignupName() {
    return validateField(
        signupName,
        [
            value => value.trim() !== '',
            value => value.length >= 3 && value.length <= 50,
            value => NAME_REGEX.test(value)
        ],
        [
            "Full Name is required.",
            "Full Name must be between 3 and 50 characters long.",
            "Full Name can only contain letters, numbers, and a single space between words."
        ]
    );
}

function validateSignupEmail() {
    return validateField(
        signupEmail,
        [
            value => value !== '',
            value => value.length <= 320,
            value => EMAIL_REGEX.test(value)
        ],
        [
            "Email is required.",
            "Email must be less than or equal to 320 characters long.",
            "Email must be a valid email format.",
        ]
    );
}

function validateSignupPassword() {
    return validateField(
        signupPassword,
        [
            value => value !== '',
            value => PASSWORD_REGEX.test(value)
        ],
        [
            "Password is required.",
            "Password must be 8-32 characters long, include at least one uppercase letter, one lowercase letter, one digit, and one special character. No spaces allowed."
        ]
    );
}

async function handleSignupFormSubmit() {

    const isSignupNameValid = validateSignupName();
    const isSignupEmailValid = validateSignupEmail();
    const isSignupPasswordValid = validateSignupPassword();

    if (!isSignupNameValid || !isSignupEmailValid || !isSignupPasswordValid) {
        return;
    }

    const signupData = {
        fullName: signupName.value.trim(),
        email: signupEmail.value.trim(),
        password: signupPassword.value.trim()
    };


    try {
        setButtonLoadingState(signupFormSubmitBtn, true);

        const response = await postRequest("/api/v1/signup", signupData);
        console.log("Signup successful:", response);

        signupModalInstance.hide();

    } catch (e) {
        handleFetchError(e);
    } finally {
        setButtonLoadingState(signupFormSubmitBtn, false);
    }

}

export {
    handleSignupFormSubmit, initializeSignupModal, signupEmail, signupForm, signupModal,
    signupName, signupPassword, validateSignupEmail, validateSignupName, validateSignupPassword
}