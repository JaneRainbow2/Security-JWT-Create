package com.softserve.itacademy.todolist.repository;

import com.softserve.itacademy.todolist.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query(value = "select r from Role r where r.name = ?1")
    Role findByName(String name);
}
