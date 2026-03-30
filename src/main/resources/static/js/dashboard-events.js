document.addEventListener("DOMContentLoaded", () => {
    const rawEvents = Array.isArray(window.dashboardEvents) ? window.dashboardEvents : [];

    const events = rawEvents
        .filter(e => e && e.startTime)
        .map(e => ({
            ...e,
            start: new Date(e.startTime),
            end: e.endTime ? new Date(e.endTime) : null
        }))
        .sort((a, b) => a.start - b.start);
    
    const dashboardEventsGrid = document.getElementById("dashboard-events-grid");
    const dashboardEventsPrevBtn = document.getElementById("dashboard-events-prev");
    const dashboardEventsNextBtn = document.getElementById("dashboard-events-next");
    const eventsGrid = document.getElementById("dashboard-events-grid");
    const eventsEmpty = document.getElementById("dashboard-events-empty");

    const modal = document.getElementById("dashboard-event-modal");
    const closeModal = document.getElementById("dashboard-close-modal");
    const modalImage = document.getElementById("dashboard-modal-image");
    const modalTitle = document.getElementById("dashboard-modal-title");
    const modalDate = document.getElementById("dashboard-modal-date");
    const modalLocation = document.getElementById("dashboard-modal-location");
    const modalOrganizer = document.getElementById("dashboard-modal-organizer");
    const modalDescription = document.getElementById("dashboard-modal-description");

    function formatDate(date) {
        return date.toLocaleDateString("en-US", {
            month: "short",
            day: "numeric",
            year: "numeric"
        });
    }

    function formatTime(date) {
        return date.toLocaleTimeString("en-US", {
            hour: "numeric",
            minute: "2-digit"
        });
    }

    function formatTimeRange(start, end) {
        if (!end) return formatTime(start);
        return `${formatTime(start)} - ${formatTime(end)}`;
    }

    function escapeHtml(value) {
        if (value == null) return "";
        return String(value)
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;")
            .replace(/'/g, "&#039;");
    }

    function openModal(event) {
        modalTitle.textContent = event.title;
        modalDate.textContent = `${formatDate(event.start)} • ${formatTimeRange(event.start, event.end)}`;
        modalLocation.textContent = `Location: ${event.location || "TBA"}`;
        modalOrganizer.textContent = `Organizer: ${event.organizerName || "Unknown"}`;
        modalDescription.textContent = event.description || "No description available.";
        modalImage.src = event.imageUrl || "/images/events/campus-event-default.png";
        modal.classList.remove("hidden");
    }

    function closeEventModal() {
        modal.classList.add("hidden");
    }

    function createEventCard(event) {
        const card = document.createElement("article");
        card.className = "event-card clickable-card";

        card.innerHTML = `
            <img src="${escapeHtml(event.imageUrl || "/images/events/campus-event-default.png")}" class="event-thumb" alt="${escapeHtml(event.title)}">
            <h3>${escapeHtml(event.title)}</h3>
            <p class="meta">${formatDate(event.start)} • ${formatTimeRange(event.start, event.end)}</p>
            <p class="meta">${escapeHtml(event.location || "TBA")}</p>
            <p class="description">${escapeHtml(event.description || "No description available.")}</p>
        `;

        card.addEventListener("click", () => openModal(event));
        return card;
    }

    function getCardScrollAmount(container) {
        const card = container?.querySelector(".event-card");
        if (!card) return 320;

        const cardWidth = card.offsetWidth;
        const style = window.getComputedStyle(container);
        const gap = parseInt(style.gap || "16");

        return cardWidth + gap;
    }

    dashboardEventsPrevBtn?.addEventListener("click", () => {
        dashboardEventsGrid?.scrollBy({
            left: -getCardScrollAmount(dashboardEventsGrid),
            behavior: "smooth"
        });
    });

    dashboardEventsNextBtn?.addEventListener("click", () => {
        dashboardEventsGrid?.scrollBy({
            left: getCardScrollAmount(dashboardEventsGrid),
            behavior: "smooth"
        });
    });

    dashboardEventsGrid?.addEventListener("wheel", (e) => {
        if (Math.abs(e.deltaY) > Math.abs(e.deltaX)) {
            e.preventDefault();
            dashboardEventsGrid.scrollBy({
                left: e.deltaY,
                behavior: "auto"
            });
        }
    }, { passive: false });

    function renderEvents() {
        if (!eventsGrid || !eventsEmpty) return;

        eventsGrid.innerHTML = "";

        if (events.length === 0) {
            eventsEmpty.hidden = false;
            return;
        }

        eventsEmpty.hidden = true;
        events.forEach(event => {
            eventsGrid.appendChild(createEventCard(event));
        });
    }

    closeModal?.addEventListener("click", closeEventModal);

    modal?.addEventListener("click", (e) => {
        if (e.target === modal) {
            closeEventModal();
        }
    });

    renderEvents();
});