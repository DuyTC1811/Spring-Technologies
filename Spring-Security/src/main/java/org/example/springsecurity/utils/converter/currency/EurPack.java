package org.example.springsecurity.utils.converter.currency;

public final class EurPack implements CurrencyPack {
    @Override
    public String code() {
        return "EUR";
    }

    @Override
    public String mainUnit() {
        return "Euros";
    }

    @Override
    public String subUnit() {
        return "Centimes";
    }
}
