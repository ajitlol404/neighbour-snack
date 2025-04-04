import { SmtpAPI } from "../helper/api-helper.js";
import { handleFetchError } from "../helper/fetch-helper.js";
import Toast from "../helper/toast.js";
import { setButtonLoadingState, validateField } from "../helper/utility.js";

const smtpForm = document.getElementById("smtp_form");
const smtpHost = document.getElementById("smtp_host");
const smtpPort = document.getElementById("smtp_port");
const smtpUser = document.getElementById("smtp_user");
const smtpPass = document.getElementById("smtp_pass");
const smtpSsl = document.getElementById("smtp_ssl");
const smtpFormSubmitBtn = document.getElementById("smtp_form_submit_btn");

async function initializeSmtpDetails() {
    try {
        const { host, port, isSsl, username } = await SmtpAPI.getSmtp();
        smtpHost.value = host;
        smtpPort.value = port;
        smtpUser.value = username;
        smtpSsl.checked = isSsl;
    } catch (error) {
        handleFetchError(error);
    }
}

function validateSmtpHost() {
    return validateField(
        smtpHost,
        [
            value => value.trim() !== "", // Not blank
            value => value.length <= 255 // Max length 255
        ],
        [
            "Host cannot be blank.",
            "Host must not exceed 255 characters."
        ]
    );
}

function validateSmtpPort() {
    return validateField(
        smtpPort,
        [
            value => value.trim() !== "", // Not null
            value => /^\d+$/.test(value), // Must be a number
            value => parseInt(value, 10) >= 1, // Min 1
            value => parseInt(value, 10) <= 65535 // Max 65535
        ],
        [
            "Port cannot be blank.",
            "Port must be a valid number.",
            "Port must be at least 1.",
            "Port cannot exceed 65535."
        ]
    );
}

function validateSmtpUser() {
    return validateField(
        smtpUser,
        [
            value => value.trim() !== "", // Not blank
            value => value.length <= 255 // Max length 255
        ],
        [
            "Username cannot be blank.",
            "Username must not exceed 255 characters."
        ]
    );
}

function validateSmtpPass() {
    return validateField(
        smtpPass,
        [
            value => value.trim() !== "", // Not blank
            value => value.length <= 255 // Max length 255
        ],
        [
            "Password cannot be blank.",
            "Password must not exceed 255 characters."
        ]
    );
}

async function handleSmtpFormSubmit() {

    const isHostValid = validateSmtpHost();
    const isPortValid = validateSmtpPort();
    const isUserValid = validateSmtpUser();
    const isPassValid = validateSmtpPass();

    // Stop if any field is invalid
    if (!isHostValid || !isPortValid || !isUserValid || !isPassValid) return;

    const requestData = {
        host: smtpHost.value.trim(),
        port: smtpPort.value.trim(),
        isSsl: smtpSsl.checked,
        username: smtpUser.value.trim(),
        password: smtpPass.value.trim()
    };

    try {
        setButtonLoadingState(smtpFormSubmitBtn, true);

        await SmtpAPI.updateSmtp(requestData);

        Toast.success("SMTP configured successfully");


    } catch (error) {
        handleFetchError(error);
    } finally {
        setButtonLoadingState(smtpFormSubmitBtn, false);
    }

}

export {
    handleSmtpFormSubmit, initializeSmtpDetails,
    smtpForm, smtpFormSubmitBtn, smtpHost, smtpPass, smtpPort, smtpSsl, smtpUser, validateSmtpHost, validateSmtpPass, validateSmtpPort, validateSmtpUser
};

