package org.example.springsecurity.utils;

import org.example.springsecurity.utils.converter.NumberToWords;

import java.math.BigDecimal;

public class NumberUtil {
    public static void main(String[] args) {
        BigDecimal big = new BigDecimal("542780009700056345");
        BigDecimal withSub = new BigDecimal("1234567.50");
        BigDecimal rate = new BigDecimal("9876.125");
        BigDecimal neg = new BigDecimal("-500000.75");
        BigDecimal zero = BigDecimal.ZERO;

        System.out.println("============ [TIẾNG VIỆT + VND] ============");
        System.out.println(NumberToWords.convert(big, "vi", "VND"));
        System.out.println(NumberToWords.convert(withSub, "vi", "VND"));
        System.out.println(NumberToWords.convert(neg, "vi", "VND"));
        System.out.println(NumberToWords.convert(zero, "vi", "VND"));

        System.out.println();
        System.out.println("============ [TIẾNG VIỆT (không tiền tệ)] ============");
        System.out.println(NumberToWords.convert(rate, "vi"));

        System.out.println();
        System.out.println("============ [ENGLISH + USD] ============");
        System.out.println(NumberToWords.convert(big, "en", "USD"));
        System.out.println(NumberToWords.convert(withSub, "en", "USD"));
        System.out.println(NumberToWords.convert(neg, "en", "USD"));

        System.out.println();
        System.out.println("============ [ENGLISH (no currency)] ============");
        System.out.println(NumberToWords.convert(rate, "en"));

        System.out.println();
        System.out.println("============ [FRANÇAIS + EUR] ============");
        System.out.println(NumberToWords.convert(big, "fr", "EUR"));
        System.out.println(NumberToWords.convert(withSub, "fr", "EUR"));
        System.out.println(NumberToWords.convert(new BigDecimal("75"), "fr", "EUR"));
        System.out.println(NumberToWords.convert(new BigDecimal("91.30"), "fr", "EUR"));

        System.out.println();
        System.out.println("============ [MIX: ENGLISH + EUR] ============");
        System.out.println(NumberToWords.convert(withSub, "en", "EUR"));

        System.out.println();
        System.out.println("============ [MIX: FRANÇAIS + USD] ============");
        System.out.println(NumberToWords.convert(withSub, "fr", "USD"));
    }
}
