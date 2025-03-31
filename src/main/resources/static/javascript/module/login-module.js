import {
    validateField, EMAIL_REGEX, setButtonLoadingState
} from "../helper/utility.js";


const loginForm = document.getElementById("login_form");
const loginEmail = document.getElementById("login_email");
const loginPassword = document.getElementById("login_password");
const loginFormSubmitBtn = document.getElementById("login_form_submit_btn");

function validateLoginEmail() {
    return validateField(
        loginEmail,
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

function validateLoginPassword() {
    return validateField(
        loginPassword,
        [
            value => value !== '',
        ],
        [
            "Password is required.",
        ]
    );
}

async function handleLoginFormSubmit() {
    const isLoginEmailValid = validateLoginEmail();
    const isLoginPasswordValid = validateLoginPassword();

    if (!isLoginEmailValid || !isLoginPasswordValid) {
        return;
    }

    try {
        setButtonLoadingState(loginFormSubmitBtn, true);
        loginForm.submit();
    } catch (error) {
        console.error('Error during login:', error);
    } finally {
        setButtonLoadingState(loginFormSubmitBtn, false);
    }
}

export {
    handleLoginFormSubmit, loginEmail, loginForm, loginPassword, validateLoginEmail, validateLoginPassword
}