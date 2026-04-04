package org.example.springsecurity.utils;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

@UtilityClass
public class CurrencyUtils {
    /**
     * Lấy scale theo ISO 4217 từ JDK Currency
     * VND → 0, JPY → 0, USD → 2, EUR → 2, KWD → 3, ...
     */
    public static int getScaleForCurrency(String currency) {
        return Currency.getInstance(currency.toUpperCase()).getDefaultFractionDigits();
    }

    /**
     * Làm tròn theo business rule:
     * - scale = 0 (VND, JPY, ...): HALF_UP
     * - scale = 2 (USD, EUR, ...): nếu chữ số thứ (scale+1) > 0 → luôn tăng 1 đơn vị
     * - scale = 3 (KWD, BHD, ...): tương tự, nếu chữ số thứ 4 > 0 → tăng 1
     */
    public static BigDecimal roundByCurrency(BigDecimal amount, String currency) {
        if (amount == null) return BigDecimal.ZERO;

        int scale = getScaleForCurrency(currency);

        if (scale == 0) {
            // VND, JPY, ... → làm tròn kế toán HALF_UP
            return amount.setScale(0, RoundingMode.HALF_UP);
        }

        // USD, EUR, KWD, ... → cắt tại đúng scale, nếu còn dư → tăng 1 đơn vị nhỏ nhất
        BigDecimal truncated = amount.setScale(scale, RoundingMode.DOWN);
        if (amount.compareTo(truncated) > 0) {
            // Còn phần dư sau vị trí scale → tăng 1 đơn vị tại vị trí cuối
            BigDecimal oneUnit = BigDecimal.ONE.scaleByPowerOfTen(-scale); // 0.01 với scale=2; 0.001 với scale=3
            return truncated.add(oneUnit);
        }
        return truncated;
    }
}
