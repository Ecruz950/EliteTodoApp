const body = document.querySelector("body")

const sidebar = document.querySelector(".sidebar")

const sidebarToggle = document.querySelector(".sidebar-toggle")

const searchButton = document.querySelector(".search-box")

const darkModeSwitch = document.querySelector(".dark-mode-toggle-switch")

const darkModeText = document.querySelector(".mode-text")

sidebarToggle.addEventListener("click", () => {
    sidebar.classList.toggle("close")
})

searchButton.addEventListener("click", () => {
    sidebar.classList.remove("close")
})

darkModeSwitch.addEventListener("click", () => {
    const isDark = body.classList.toggle("dark");
    darkModeText.innerText = isDark ? "Light Mode" : "Dark Mode";
});