package com.unibank.UnitechAppApplication.controller;

import com.unibank.UnitechAppApplication.service.CurrencyService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/currency")
@Api(tags = "Currency")
public class CurrencyController {
    private final CurrencyService currencyService;

    @GetMapping("/{from}/{to}")
    public ResponseEntity<Double> convertCurrency(
        @PathVariable("from") String fromCurrency,
        @PathVariable("to") String toCurrency
    ) {
        String currencyPair = fromCurrency + "/" + toCurrency;
        return ResponseEntity.ok(currencyService.getCurrencyRate(currencyPair));
    }

}
