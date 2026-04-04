package org.example.springsecurity.utils.converter.lang;

public final class EnglishPack implements LanguagePack {
    private static final String[] BELOW_20 = {
            "", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine",
            "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen",
            "Sixteen", "Seventeen", "Eighteen", "Nineteen"
    };

    private static final String[] TENS = {
            "", "", "Twenty", "Thirty", "Forty", "Fifty",
            "Sixty", "Seventy", "Eighty", "Ninety"
    };

    private static final String[] SCALES = {
            "", "Thousand", "Million", "Billion", "Trillion", "Quadrillion", "Quintillion"
    };

    @Override
    public String code() {
        return "en";
    }

    @Override
    public String[] below20() {
        return BELOW_20;
    }

    @Override
    public String[] tens() {
        return TENS;
    }

    @Override
    public String[] scales() {
        return SCALES;
    }

    @Override
    public String zero() {
        return "Zero";
    }

    @Override
    public String hundred() {
        return "Hundred";
    }

    @Override
    public String negative() {
        return "Negative";
    }

    @Override
    public String decimalSeparator() {
        return "Point";
    }

    @Override
    public String afterHundred() {
        return "and";
    }

    @Override
    public String tensUnitSeparator() {
        return "-";
    }
}
