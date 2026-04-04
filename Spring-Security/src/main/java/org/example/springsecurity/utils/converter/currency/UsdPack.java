package org.example.springsecurity.utils.converter.currency;

public final class UsdPack implements CurrencyPack {
    @Override
    public String code() {
        return "USD";
    }

    @Override
    public String mainUnit() {
        return "Dollars";
    }

    @Override
    public String subUnit() {
        return "Cents";
    }

}
