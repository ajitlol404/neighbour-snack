import { enableToastGlobally } from "./module/base-module.js";
import { handleLoginFormSubmit, loginEmail, loginForm, loginPassword, validateLoginEmail, validateLoginPassword } from "./module/login-module.js";

enableToastGlobally();

loginEmail.addEventListener("input", validateLoginEmail);
loginPassword.addEventListener("input", validateLoginPassword);

loginForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    await handleLoginFormSubmit();
});