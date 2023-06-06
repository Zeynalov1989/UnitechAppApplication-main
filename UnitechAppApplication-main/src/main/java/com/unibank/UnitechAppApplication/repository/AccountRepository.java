package com.unibank.UnitechAppApplication.repository;

import com.unibank.UnitechAppApplication.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface AccountRepository extends JpaRepository<Account,Long> {

    List<Account> findByUserIdAndStatus(long user_id, Account.Status status);
}