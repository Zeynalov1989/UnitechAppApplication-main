package com.unibank.UnitechAppApplication.service;

import com.unibank.UnitechAppApplication.model.Currency;
import com.unibank.UnitechAppApplication.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final RatesMockService ratesService;
    private final CurrencyRepository currencyRepository;

    public Double getCurrencyRate(String currencyPair) {
        Currency currency = currencyRepository.findByPair(currencyPair).orElse(null);
        if (currency == null) {
            double rate = ratesService.getExchangeRate(currencyPair);
            currencyRepository.save(Currency.builder()
                .pair(currencyPair)
                .rate(rate)
                .build());
            return rate;
        }
        return currency.getRate();
    }

}





