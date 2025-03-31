import { } from "./helper/utility.js";
import { handleLogout, logoutBtn } from "./module/base-module.js";

// Enable tooltip globally
const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]')
const tooltipList = [...tooltipTriggerList].map(tooltipTriggerEl => new bootstrap.Tooltip(tooltipTriggerEl))

// Logout
logoutBtn?.addEventListener("click", handleLogout);