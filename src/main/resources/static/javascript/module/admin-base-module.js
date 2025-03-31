
// Enable toast globally
function enableToastGlobally() {
    const toastLive = document.getElementById("toast");
    if (toastLive) {
        bootstrap.Toast.getOrCreateInstance(toastLive).show();
    }
}

export {
    enableToastGlobally
};
