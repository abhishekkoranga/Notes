package com.speer.assessmet.notes.repository;

import com.speer.assessmet.notes.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUsersRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
}
