(function () {
    const rawEvents = Array.isArray(window.allEvents) ? window.allEvents : [];

    const events = rawEvents
        .map((event) => {
            const date = event.eventDate ? new Date(`${event.eventDate}T00:00:00`) : null;
            return { ...event, parsedDate: date };
        })
        .filter((event) => event.parsedDate && !Number.isNaN(event.parsedDate.getTime()))
        .sort((a, b) => a.parsedDate - b.parsedDate);

    const now = new Date();
    let selectedYear = now.getFullYear();
    let selectedMonth = now.getMonth();

    if (events.length > 0) {
        selectedYear = events[0].parsedDate.getFullYear();
        selectedMonth = events[0].parsedDate.getMonth();

        const thisMonthHasEvents = events.some(
            (event) =>
                event.parsedDate.getFullYear() === now.getFullYear() &&
                event.parsedDate.getMonth() === now.getMonth()
        );

        if (thisMonthHasEvents) {
            selectedYear = now.getFullYear();
            selectedMonth = now.getMonth();
        }
    }

    const monthLabel = document.getElementById('month-label');
    const highlightsGrid = document.getElementById('highlights-grid');
    const highlightsEmpty = document.getElementById('highlights-empty');
    const calendarGrid = document.getElementById('calendar-grid');
    const monthEventsList = document.getElementById('month-events-list');
    const monthListEmpty = document.getElementById('month-list-empty');

    const monthFormatter = new Intl.DateTimeFormat('en-US', { month: 'long', year: 'numeric' });
    const dateFormatter = new Intl.DateTimeFormat('en-US', { month: 'short', day: 'numeric', year: 'numeric' });

    function currentMonthEvents() {
        return events.filter(
            (event) =>
                event.parsedDate.getFullYear() === selectedYear &&
                event.parsedDate.getMonth() === selectedMonth
        );
    }

    function renderHighlights(monthEvents) {
        highlightsGrid.innerHTML = '';

        if (monthEvents.length === 0) {
            highlightsEmpty.hidden = false;
            return;
        }

        highlightsEmpty.hidden = true;

        monthEvents.forEach((event) => {
            const card = document.createElement('article');
            card.className = 'event-card';

            const description = event.description || 'No description yet.';
            const time = event.eventTime && event.eventTime.trim() ? ` • ${event.eventTime}` : '';

            card.innerHTML = `
                <h3>${event.title}</h3>
                <p class="meta">${dateFormatter.format(event.parsedDate)}${time} • ${event.location || 'TBA'}</p>
                <div class="description-wrap">
                    <p class="description">${description}</p>
                    <div class="description-popup">${description}</div>
                </div>
            `;

            highlightsGrid.appendChild(card);
        });

    }

    function renderCalendar(monthEvents) {
        const weekdays = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
        const eventCountsByDay = monthEvents.reduce((acc, event) => {
            const day = event.parsedDate.getDate();
            acc[day] = (acc[day] || 0) + 1;
            return acc;
        }, {});

        calendarGrid.innerHTML = '';
        weekdays.forEach((label) => {
            const weekdayEl = document.createElement('div');
            weekdayEl.className = 'calendar-weekday';
            weekdayEl.textContent = label;
            calendarGrid.appendChild(weekdayEl);
        });

        const firstDay = new Date(selectedYear, selectedMonth, 1);
        const daysInMonth = new Date(selectedYear, selectedMonth + 1, 0).getDate();
        const leadingEmpty = firstDay.getDay();

        for (let i = 0; i < leadingEmpty; i += 1) {
            const emptyCell = document.createElement('div');
            emptyCell.className = 'calendar-day is-empty';
            calendarGrid.appendChild(emptyCell);
        }

        for (let day = 1; day <= daysInMonth; day += 1) {
            const dayCell = document.createElement('div');
            const dayEvents = eventCountsByDay[day] || 0;
            const isToday =
                day === now.getDate() &&
                selectedMonth === now.getMonth() &&
                selectedYear === now.getFullYear();

            dayCell.className = `calendar-day${dayEvents ? ' has-event' : ''}${isToday ? ' is-today' : ''}`;
            dayCell.innerHTML = `<span class="day-number">${day}</span>`;

            if (dayEvents > 0) {
                dayCell.innerHTML += '<span class="event-dot" aria-hidden="true"></span>';
                dayCell.innerHTML += `<span class="event-count">${dayEvents}</span>`;
            }

            calendarGrid.appendChild(dayCell);
        }
    }

    function renderList(monthEvents) {
        monthEventsList.innerHTML = '';

        if (monthEvents.length === 0) {
            monthListEmpty.hidden = false;
            return;
        }

        monthListEmpty.hidden = true;

        monthEvents.forEach((event) => {
            const item = document.createElement('li');
            item.innerHTML = `
                <span class="list-date">${dateFormatter.format(event.parsedDate)}</span>
                <p class="list-title">${event.title}</p>
                <p class="list-meta">${event.eventTime || 'Time TBD'} • ${event.location || 'Location TBD'}</p>
            `;
            monthEventsList.appendChild(item);
        });
    }

    function render() {
        const monthDate = new Date(selectedYear, selectedMonth, 1);
        const monthEvents = currentMonthEvents();
        monthLabel.textContent = `${monthFormatter.format(monthDate)} • ${monthEvents.length} event${monthEvents.length === 1 ? '' : 's'}`;

        renderHighlights(monthEvents);
        renderCalendar(monthEvents);
        renderList(monthEvents);
    }

    document.getElementById('prev-month').addEventListener('click', () => {
        selectedMonth -= 1;
        if (selectedMonth < 0) {
            selectedMonth = 11;
            selectedYear -= 1;
        }
        render();
    });

    document.getElementById('next-month').addEventListener('click', () => {
        selectedMonth += 1;
        if (selectedMonth > 11) {
            selectedMonth = 0;
            selectedYear += 1;
        }
        render();
    });

    render();
})();
