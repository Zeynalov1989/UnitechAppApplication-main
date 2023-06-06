package com.unibank.UnitechAppApplication.controller;

import com.unibank.UnitechAppApplication.service.CurrencyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurrencyControllerTest {
    @Mock
    private CurrencyService currencyService;

    @InjectMocks
    private CurrencyController currencyController;

    @Test
    void testConvertCurrency() {
        String fromCurrency = "USD";
        String toCurrency = "AZN";
        String currencyPair = fromCurrency + "/" + toCurrency;
        Double expectedRate = 1.7;
        when(currencyService.getCurrencyRate(currencyPair)).thenReturn(expectedRate);

        ResponseEntity<Double> response = currencyController.convertCurrency(fromCurrency, toCurrency);

        assertEquals(expectedRate, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(currencyService, times(1)).getCurrencyRate(currencyPair);
    }
}
