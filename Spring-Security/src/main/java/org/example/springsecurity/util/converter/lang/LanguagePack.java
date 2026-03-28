package org.example.springsecurity.util.converter.lang;

/**
 * Định nghĩa toàn bộ từ vựng và quy tắc format cho một ngôn ngữ.
 * Mỗi ngôn ngữ implement interface này.
 */
public interface LanguagePack {

    /**
     * Mã ngôn ngữ, ví dụ "vi", "en", "fr"
     */
    String code();

    /**
     * Từ cho 0–19 (mảng 20 phần tử, index 0 = "")
     */
    String[] below20();

    /**
     * Từ cho bội số 10 (mảng 10 phần tử, index 0,1 = "")
     */
    String[] tens();

    /**
     * Tên bậc: "", Thousand, Million, ...
     */
    String[] scales();

    String zero();

    String hundred();

    String negative();

    String decimalSeparator();

    /**
     * Hook format nhóm 3 chữ số (0–999).
     * Override khi ngôn ngữ có quy tắc đặc biệt (Pháp: 70-99, Nhật: hệ vạn...).
     */
    default void formatThreeDigits(int num, StringBuilder out) {
        ThreeDigitFormatter.format(num, out, this);
    }

    /**
     * Từ chèn sau "Hundred" và trước phần còn lại.
     * Tiếng Anh: "and" → One Hundred AND Five.
     */
    default String afterHundred() {
        return "";
    }

    /**
     * Ký tự nối hàng chục với hàng đơn vị.
     * Tiếng Anh: "-" → Twenty-One. Tiếng Việt: " " → Hai mươi Một.
     */
    default String tensUnitSeparator() {
        return " ";
    }
}
