package com.project.Splitwise.controller;

import com.project.Splitwise.model.Currency;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class CurrencyController {

    @GetMapping("/availableCurrencies")
    public ResponseEntity<List<String>> retrieveSupportedCurrencies(){
        List<String> currencyList = new ArrayList<>();
        Currency[] currencies = Currency.values();

        for (Currency currency : currencies) {
            currencyList.add(currency.name());
        }
        return ResponseEntity.ok(currencyList);
    }

}
