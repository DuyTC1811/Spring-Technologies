package org.example.springsecurity.util.converter.currency;

public final class VndPack implements CurrencyPack {
    @Override
    public String code() {
        return "VND";
    }

    @Override
    public String mainUnit() {
        return "đồng";
    }

    @Override
    public String subUnit() {
        return "xu";
    }

}
