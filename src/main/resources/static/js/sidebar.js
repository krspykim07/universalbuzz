document.addEventListener("DOMContentLoaded", () => {
    const sidebar = document.getElementById("sidebar");
    const toggleBtn = document.getElementById("sidebarToggle");

    if (!sidebar || !toggleBtn) return;

    const savedState = localStorage.getItem("sidebar-collapsed");
    if (savedState === "true") {
        sidebar.classList.add("collapsed");
    }

    toggleBtn.addEventListener("click", () => {
        sidebar.classList.toggle("collapsed");
        localStorage.setItem(
            "sidebar-collapsed",
            sidebar.classList.contains("collapsed")
        );
    });

    if (window.lucide) {
        lucide.createIcons();
    }
});