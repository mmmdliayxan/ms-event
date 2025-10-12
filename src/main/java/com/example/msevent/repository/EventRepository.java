package com.example.msevent.repository;

import com.example.msevent.model.Category;
import com.example.msevent.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByCategory(Category category);
    List<Event> findByTitle(String title);
}