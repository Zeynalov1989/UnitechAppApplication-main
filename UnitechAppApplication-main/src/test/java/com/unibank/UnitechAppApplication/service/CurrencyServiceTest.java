package com.unibank.UnitechAppApplication.service;

import com.unibank.UnitechAppApplication.model.Currency;
import com.unibank.UnitechAppApplication.repository.CurrencyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {
    @Mock
    private RatesMockService ratesService;

    @Mock
    private CurrencyRepository currencyRepository;

    @InjectMocks
    private CurrencyService currencyService;

    @Test
    void testGetCurrencyRate_CurrencyExistsInRepository() {
        String currencyPair = "USD/AZN";
        Currency currency = Currency.builder()
            .pair(currencyPair)
            .rate(1.7)
            .build();
        when(currencyRepository.findByPair(currencyPair)).thenReturn(java.util.Optional.of(currency));

        Double rate = currencyService.getCurrencyRate(currencyPair);

        assertEquals(currency.getRate(), rate);
        verify(currencyRepository, times(1)).findByPair(currencyPair);
        verify(ratesService, never()).getExchangeRate(currencyPair);
        verify(currencyRepository, never()).save(any(Currency.class));
    }

    @Test
    void testGetCurrencyRate_CurrencyDoesNotExistInRepository() {
        String currencyPair = "USD/EUR";
        when(currencyRepository.findByPair(currencyPair)).thenReturn(java.util.Optional.empty());
        when(ratesService.getExchangeRate(currencyPair)).thenReturn(1.7);

        Double rate = currencyService.getCurrencyRate(currencyPair);

        assertEquals(1.7, rate);
        verify(currencyRepository, times(1)).findByPair(currencyPair);
        verify(ratesService, times(1)).getExchangeRate(currencyPair);
        verify(currencyRepository, times(1)).save(any(Currency.class));
    }
}