package org.example.springsecurity.util.converter;

import lombok.experimental.UtilityClass;
import org.example.springsecurity.util.converter.currency.CurrencyPack;
import org.example.springsecurity.util.converter.currency.EurPack;
import org.example.springsecurity.util.converter.currency.UsdPack;
import org.example.springsecurity.util.converter.currency.VndPack;
import org.example.springsecurity.util.converter.lang.EnglishPack;
import org.example.springsecurity.util.converter.lang.FrenchPack;
import org.example.springsecurity.util.converter.lang.LanguagePack;
import org.example.springsecurity.util.converter.lang.VietnamesePack;

import java.util.LinkedHashMap;
import java.util.Map;

@UtilityClass
public class PackRegistry {
    private static final Map<String, LanguagePack> LANGUAGES = new LinkedHashMap<>();
    private static final Map<String, CurrencyPack> CURRENCIES = new LinkedHashMap<>();

    static {
        // Ngôn ngữ mặc định
        registerLanguage(new VietnamesePack());
        registerLanguage(new EnglishPack());
        registerLanguage(new FrenchPack());

        // Tiền tệ mặc định
        registerCurrency(new VndPack());
        registerCurrency(new UsdPack());
        registerCurrency(new EurPack());
    }

    public static void registerLanguage(LanguagePack pack) {
        LANGUAGES.put(pack.code().toLowerCase(), pack);
    }

    public static LanguagePack getLanguage(String code) {
        LanguagePack pack = LANGUAGES.get(code.toLowerCase());
        if (pack == null) {
            throw new IllegalArgumentException("Unsupported language: " + code + ". Available: " + LANGUAGES.keySet());
        }
        return pack;
    }

    public static void registerCurrency(CurrencyPack pack) {
        CURRENCIES.put(pack.code().toUpperCase(), pack);
    }

    public static CurrencyPack getCurrency(String code) {
        CurrencyPack pack = CURRENCIES.get(code.toUpperCase());
        if (pack == null) {
            throw new IllegalArgumentException("Unsupported currency: " + code + ". Available: " + CURRENCIES.keySet());
        }
        return pack;
    }
}
