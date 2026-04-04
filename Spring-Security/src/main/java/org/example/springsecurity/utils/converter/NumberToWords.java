package org.example.springsecurity.utils.converter;

import org.example.springsecurity.utils.converter.currency.CurrencyPack;
import org.example.springsecurity.utils.converter.lang.LanguagePack;
import org.example.springsecurity.utils.converter.lang.ThreeDigitFormatter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * Chuyển số thành chữ, hỗ trợ đa ngôn ngữ và đa loại tiền tệ.
 *
 * <pre>
 * // Cơ bản
 * NumberToWords.convert(new BigDecimal("1234"), "vi");
 *
 * // Tiền tệ
 * NumberToWords.convert(new BigDecimal("1234.50"), "vi", "VND");
 *
 * // Không tiền tệ, đọc thập phân
 * NumberToWords.convert(new BigDecimal("3.14"), "en");
 *
 * // Thêm ngôn ngữ / tiền tệ runtime
 * PackRegistry.registerLanguage(new JapanesePack());
 * PackRegistry.registerCurrency(new JpyPack());
 * </pre>
 */
public final class NumberToWords {

    private static final BigInteger THOUSAND = BigInteger.valueOf(1000);

    private NumberToWords() {
    }

    // PHẦN NGUYÊN
    private static String integerPartToWords(BigInteger n, LanguagePack lp) {
        if (n.signum() == 0) return lp.zero();

        String[] scales = lp.scales();
        StringBuilder res = new StringBuilder();
        int scaleIndex = 0;

        while (n.signum() > 0 && scaleIndex < scales.length) {
            BigInteger[] divRem = n.divideAndRemainder(THOUSAND);
            int chunk = divRem[1].intValue();

            if (chunk != 0) {
                StringBuilder part = new StringBuilder();
                lp.formatThreeDigits(chunk, part);

                if (!scales[scaleIndex].isEmpty()) {
                    ThreeDigitFormatter.appendSpace(part);
                    part.append(scales[scaleIndex]);
                }

                if (!res.isEmpty()) {
                    part.append(' ').append(res);
                }
                res = part;
            }

            n = divRem[0];
            scaleIndex++;
        }

        return res.toString();
    }

    // PHẦN THẬP PHÂN (từng chữ số)
    private static String decimalDigitsToWords(String digits, LanguagePack lp) {
        String[] below20 = lp.below20();
        StringBuilder sb = new StringBuilder();
        for (char c : digits.toCharArray()) {
            if (!sb.isEmpty()) sb.append(' ');
            sb.append(below20[c - '0']);
        }
        return sb.toString();
    }

    // PUBLIC API

    /**
     * Chuyển số thành chữ với tiền tệ.
     *
     * @param value        số cần chuyển
     * @param langCode     mã ngôn ngữ ("vi", "en", "fr"...)
     * @param currencyCode mã tiền tệ ("VND", "USD", "EUR"...)
     * @return chuỗi kết quả
     */
    public static String convert(BigDecimal value, String langCode, String currencyCode) {
        LanguagePack lp = PackRegistry.getLanguage(langCode);
        CurrencyPack cp = PackRegistry.getCurrency(currencyCode);

        boolean negative = value.signum() < 0;
        BigDecimal abs = value.abs();
        BigInteger intPart = abs.toBigInteger();
        BigDecimal fracPart = abs.subtract(new BigDecimal(intPart));

        StringBuilder result = new StringBuilder();

        if (negative) {
            result.append(lp.negative()).append(' ');
        }

        result.append(integerPartToWords(intPart, lp));

        if (fracPart.signum() > 0) {
            int subUnits = fracPart.movePointRight(cp.subUnitDecimals())
                    .setScale(0, RoundingMode.HALF_UP)
                    .intValue();

            if (subUnits > 0) {
                result.append(' ').append(cp.mainUnit()).append(' ');
                StringBuilder subPart = new StringBuilder();
                lp.formatThreeDigits(subUnits, subPart);
                result.append(subPart).append(' ').append(cp.subUnit());
            } else {
                result.append(' ').append(cp.mainUnit());
            }
        } else {
            result.append(' ').append(cp.mainUnit());
        }

        return result.toString();
    }

    /**
     * Chuyển số thành chữ không dùng tiền tệ.
     * Phần thập phân đọc từng chữ số sau dấu phẩy/point.
     *
     * @param value    số cần chuyển
     * @param langCode mã ngôn ngữ
     * @return chuỗi kết quả
     */
    public static String convert(BigDecimal value, String langCode) {
        LanguagePack lp = PackRegistry.getLanguage(langCode);

        boolean negative = value.signum() < 0;
        BigDecimal abs = value.abs();
        BigInteger intPart = abs.toBigInteger();
        BigDecimal fracPart = abs.subtract(new BigDecimal(intPart));

        StringBuilder result = new StringBuilder();

        if (negative) {
            result.append(lp.negative()).append(' ');
        }

        result.append(integerPartToWords(intPart, lp));

        if (fracPart.signum() > 0) {
            String fracStr = abs.toPlainString();
            String digits = fracStr.substring(fracStr.indexOf('.') + 1);
            result.
                    append(' ')
                    .append(lp.decimalSeparator())
                    .append(' ')
                    .append(decimalDigitsToWords(digits, lp));
        }

        return result.toString();
    }
}
