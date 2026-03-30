document.addEventListener("DOMContentLoaded", () => {
    const rawEvents = Array.isArray(window.allEvents) ? window.allEvents : [];

    const events = rawEvents
        .filter(e => e && e.status === "PUBLISHED" && e.startTime)
        .map(e => ({
            ...e,
            start: new Date(e.startTime),
            end: e.endTime ? new Date(e.endTime) : null
        }))
        .sort((a, b) => a.start - b.start);

    const highlightsGridEl = document.getElementById("highlights-grid");
    const highlightsPrevBtn = document.getElementById("highlights-prev");
    const highlightsNextBtn = document.getElementById("highlights-next");
    const highlightsGrid = document.getElementById("highlights-grid");
    const highlightsEmpty = document.getElementById("highlights-empty");
    const savedEventsGrid = document.getElementById("saved-events-grid");
    const savedEventsEmpty = document.getElementById("saved-events-empty");
    const calendarGrid = document.getElementById("calendar-grid");
    const calendarMonthLabel = document.getElementById("calendar-month-label");
    const monthEventsList = document.getElementById("month-events-list");
    const monthListEmpty = document.getElementById("month-list-empty");

    const prevMonthBtn = document.getElementById("prev-month");
    const nextMonthBtn = document.getElementById("next-month");

    const modal = document.getElementById("event-modal");
    const closeModal = document.getElementById("close-modal");
    const modalImage = document.getElementById("modal-image");
    const modalTitle = document.getElementById("modal-title");
    const modalDate = document.getElementById("modal-date");
    const modalLocation = document.getElementById("modal-location");
    const modalOrganizer = document.getElementById("modal-organizer");
    const modalDescription = document.getElementById("modal-description");

    const today = new Date();
    let currentYear = today.getFullYear();
    let currentMonth = today.getMonth();

    function formatMonthYear(date) {
        return date.toLocaleDateString("en-US", {
            month: "long",
            year: "numeric"
        });
    }

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

    function getUpcomingEvents(limit = 9) {
    const now = new Date();
    return events
        .filter(e => e.start >= now)
        .sort((a, b) => a.start - b.start)
        .slice(0, limit);
    }

    function getMonthEvents(year, month) {
        return events.filter(event =>
            event.start.getFullYear() === year &&
            event.start.getMonth() === month
        );
    }

    function openModal(event) {
        modalTitle.textContent = event.title;
        modalDate.textContent = `${formatDate(event.start)} • ${formatTimeRange(event.start, event.end)}`;
        modalLocation.textContent = `Location: ${event.location || "TBA"}`;
        modalOrganizer.textContent = `Organizer: ${event.organizerName || "Unknown"}`;
        modalDescription.textContent = event.description || "No description available.";

        if (event.imageUrl) {
            modalImage.src = event.imageUrl;
        } else {
            modalImage.src = "/images/events/campus-event-default.png";
        }

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

    function renderUpcomingEvents() {
        if (!highlightsGrid || !highlightsEmpty) return;

        highlightsGrid.innerHTML = "";
        const upcomingEvents = getUpcomingEvents();

        if (upcomingEvents.length === 0) {
            highlightsEmpty.hidden = false;
            return;
        }

        highlightsEmpty.hidden = true;
        upcomingEvents.forEach(event => {
            highlightsGrid.appendChild(createEventCard(event));
        });
    }

    function renderSavedEvents() {
        if (!savedEventsGrid || !savedEventsEmpty) return;

        savedEventsGrid.innerHTML = "";
        savedEventsEmpty.hidden = false;
    }

    function renderCalendar(year, month, monthEvents) {
        if (!calendarGrid) return;

        calendarGrid.innerHTML = "";
        if (calendarMonthLabel) {
            calendarMonthLabel.textContent = formatMonthYear(new Date(year, month, 1));
        }

        const weekdays = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];

        weekdays.forEach(day => {
            const el = document.createElement("div");
            el.className = "calendar-weekday";
            el.textContent = day;
            calendarGrid.appendChild(el);
        });

        const firstDay = new Date(year, month, 1).getDay();
        const totalDays = new Date(year, month + 1, 0).getDate();

        for (let i = 0; i < firstDay; i++) {
            const empty = document.createElement("div");
            empty.className = "calendar-day is-empty";
            calendarGrid.appendChild(empty);
        }

        for (let day = 1; day <= totalDays; day++) {
            const cellDate = new Date(year, month, day);

            const dayEvents = monthEvents.filter(e => e.start.getDate() === day);

            const cell = document.createElement("div");
            cell.className = "calendar-day";

            if (
                cellDate.getDate() === today.getDate() &&
                cellDate.getMonth() === today.getMonth() &&
                cellDate.getFullYear() === today.getFullYear()
            ) {
                cell.classList.add("is-today");
            }

            const number = document.createElement("div");
            number.className = "day-number";
            number.textContent = day;
            cell.appendChild(number);

            if (dayEvents.length > 0) {
                cell.classList.add("has-event");

                const dot = document.createElement("div");
                dot.className = "event-dot";
                cell.appendChild(dot);

                if (dayEvents.length > 1) {
                    const count = document.createElement("div");
                    count.className = "event-count";
                    count.textContent = `+${dayEvents.length}`;
                    cell.appendChild(count);
                }

                cell.style.cursor = "pointer";
                cell.addEventListener("click", () => openModal(dayEvents[0]));
            }

            calendarGrid.appendChild(cell);
        }
    }

    function renderMonthEventsList(monthEvents) {
        if (!monthEventsList || !monthListEmpty) return;

        monthEventsList.innerHTML = "";

        if (monthEvents.length === 0) {
            monthListEmpty.hidden = false;
            return;
        }

        monthListEmpty.hidden = true;

        monthEvents.forEach(event => {
            const li = document.createElement("li");
            li.className = "clickable-card";

            li.innerHTML = `
                <span class="list-date">${formatDate(event.start)}</span>
                <div class="list-title">${escapeHtml(event.title)}</div>
                <div class="list-meta">${formatTimeRange(event.start, event.end)} • ${escapeHtml(event.location || "TBA")}</div>
            `;

            li.addEventListener("click", () => openModal(event));
            monthEventsList.appendChild(li);
        });
    }

    function getCardScrollAmount(container) {
        const card = container?.querySelector(".event-card");
        if (!card) return 320;

        const cardWidth = card.offsetWidth;
        const style = window.getComputedStyle(container);
        const gap = parseInt(style.gap || "16");

        return cardWidth + gap;
    }

    highlightsPrevBtn?.addEventListener("click", () => {
        highlightsGridEl?.scrollBy({
            left: -getCardScrollAmount(highlightsGridEl),
            behavior: "smooth"
        });
    });

    highlightsNextBtn?.addEventListener("click", () => {
        highlightsGridEl?.scrollBy({
            left: getCardScrollAmount(highlightsGridEl),
            behavior: "smooth"
        });
    });

    highlightsGridEl?.addEventListener("wheel", (e) => {
        if (Math.abs(e.deltaY) > Math.abs(e.deltaX)) {
            e.preventDefault();
            highlightsGridEl.scrollBy({
                left: e.deltaY,
                behavior: "auto"
            });
        }
    }, { passive: false });

    function renderAll() {
        const monthEvents = getMonthEvents(currentYear, currentMonth);
        renderUpcomingEvents();
        renderSavedEvents();
        renderCalendar(currentYear, currentMonth, monthEvents);
        renderMonthEventsList(monthEvents);
    }

    prevMonthBtn?.addEventListener("click", () => {
        currentMonth--;
        if (currentMonth < 0) {
            currentMonth = 11;
            currentYear--;
        }
        renderAll();
    });

    nextMonthBtn?.addEventListener("click", () => {
        currentMonth++;
        if (currentMonth > 11) {
            currentMonth = 0;
            currentYear++;
        }
        renderAll();
    });

    closeModal?.addEventListener("click", closeEventModal);

    modal?.addEventListener("click", (e) => {
        if (e.target === modal) {
            closeEventModal();
        }
    });

    renderAll();
});