package com.unibank.UnitechAppApplication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unibank.UnitechAppApplication.model.User;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByPin(String pin);

}
