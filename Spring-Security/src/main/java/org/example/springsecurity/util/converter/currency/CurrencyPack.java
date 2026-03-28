package org.example.springsecurity.util.converter.currency;

/**
 * Thông tin tiền tệ, tách riêng khỏi ngôn ngữ.
 * Cùng ngôn ngữ có thể dùng nhiều loại tiền (EN + USD, EN + GBP, EN + EUR).
 */
public interface CurrencyPack {
    /**
     * Mã tiền tệ, ví dụ "VND", "USD", "EUR"
     */
    String code();

    /**
     * Tên đơn vị chính: "đồng", "Dollars", "Euros"
     */
    String mainUnit();

    /**
     * Tên đơn vị phụ: "xu", "Cents", "Centimes"
     */
    String subUnit();

    /**
     * Số chữ số thập phân cho đơn vị phụ (VND: 2, BHD: 3...)
     */
    default int subUnitDecimals() {
        return 2;
    }
}
