import { } from "./helper/utility.js";
import {
    handleSmtpFormSubmit,
    initializeSmtpDetails,
    smtpForm,
    smtpHost,
    smtpPass,
    smtpPort,
    smtpUser,
    validateSmtpHost,
    validateSmtpPass,
    validateSmtpPort,
    validateSmtpUser
} from "./module/setting-module.js";

initializeSmtpDetails();

smtpHost.addEventListener("input", validateSmtpHost);
smtpPort.addEventListener("input", validateSmtpPort);
smtpUser.addEventListener("input", validateSmtpUser);
smtpPass.addEventListener("input", validateSmtpPass);

smtpForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    await handleSmtpFormSubmit();
});