package com.unibank.UnitechAppApplication.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unibank.UnitechAppApplication.model.Currency;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency,Long> {

       List<Currency> findCurrencyByCreatedDateLessThan(LocalDateTime localDateTime);

       Optional<Currency> findByPair(String pair);
}
