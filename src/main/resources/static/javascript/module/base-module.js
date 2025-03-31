
const logoutBtn = document.getElementById("logout_btn");
const searchForm = document.getElementById("search_form");
const searchInput = document.getElementById("search_input");

function handleLogout(e) {
    e.preventDefault();
    document.logout_form.submit();
}

// Enable toast globally
function enableToastGlobally() {
    const toastLive = document.getElementById("toast");
    if (toastLive) {
        bootstrap.Toast.getOrCreateInstance(toastLive).show();
    }
}

export { enableToastGlobally, handleLogout, logoutBtn, searchForm, searchInput };
