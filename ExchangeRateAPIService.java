package com.alura.challenge;

import com.google.gson.JsonObject;

public record ExchangeRateAPIService() {

    public JsonObject getConversionData(String fromCurrency, String toCurrency, double amount) {

        return new JsonObject();
    }
}
