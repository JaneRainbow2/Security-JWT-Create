package com.softserve.itacademy.todolist.repository;

import com.softserve.itacademy.todolist.model.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StateRepository extends JpaRepository<State, Long> {
    State findByName(String name);
    List<State> findByOrderByIdAsc();
    @Query(value = "select max(id) + 1 from states", nativeQuery = true)
    long getNextId();
}
