package com.vktech.universalbuzz.repository;

import com.vktech.universalbuzz.model.Event;
import com.vktech.universalbuzz.model.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByStatusOrderByStartTimeAsc(EventStatus status);

    List<Event> findByStatusAndStartTimeGreaterThanEqualOrderByStartTimeAsc(
            EventStatus status, LocalDateTime startTime
    );
}