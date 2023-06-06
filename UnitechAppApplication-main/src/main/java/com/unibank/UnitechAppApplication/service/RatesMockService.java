package com.unibank.UnitechAppApplication.service;

import com.unibank.UnitechAppApplication.model.Currency;
import com.unibank.UnitechAppApplication.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RatesMockService {
    private final Map<String, Double> rates; // Rates in third party server
    private final CurrencyRepository currencyRepository;

    @Scheduled(fixedRate = 30000)
    public void updateRates() {
        List<Currency> currencies = new ArrayList<>();
        for (String currencyPair : rates.keySet()) {
            double currentRate = rates.get(currencyPair);
            double newRate = currentRate * (1 + Math.random() * 0.01 - 0.005);
            rates.put(currencyPair, newRate);
            Currency currency = currencyRepository.findByPair(currencyPair).orElse(Currency.builder()
                .pair(currencyPair)
                .build());
            currency.setRate(newRate);
            currencies.add(currency);
        }
        currencyRepository.saveAll(currencies);
    }

    public double getExchangeRate(String currencyPair) {
        if (!rates.containsKey(currencyPair)) {
            double defaultRate = generateDefaultRate();
            rates.put(currencyPair, defaultRate);
        }
        return rates.get(currencyPair);
    }

    private double generateDefaultRate() {
        double minRate = 0.5;
        double maxRate = 5.5;
        return minRate + Math.random() * (maxRate - minRate);
    }
}
