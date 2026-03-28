package org.example.springsecurity.util;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

@Getter
@Setter
public class RegexUtil {
    public static final String REGEX_PHONE_NUMBER = "^(84|0)(3[2-9]|5[682]|7[06789]|8[1-58]|9[0-9])\\d{7}$";
    public static final String REGEX_USERNAME = "^[A-Za-zÀÁẠẢÃÂẦẤẬẨẪĂẰẮẶẲẴÈÉẸẺẼÊỀẾỆỂỄÌÍỊỈĨÒÓỌỎÕÔỒỐỘỔỖƠỜỚỢỞỠÙÚỤỦŨƯỪỨỰỬỮỲÝỴỶỸĐàáạảãâầấậẩẫăằắặẳẵèéẹẻẽêềếệểễìíịỉĩòóọỏõôồốộổỗơờớợởỡùúụủũưừứựửữỳýỵỷỹđ]*(?:\\s[A-Za-zÀÁẠẢÃÂẦẤẬẨẪĂẰẮẶẲẴÈÉẸẺẼÊỀẾỆỂỄÌÍỊỈĨÒÓỌỎÕÔỒỐỘỔỖƠỜỚỢỞỠÙÚỤỦŨƯỪỨỰỬỮỲÝỴỶỸĐàáạảãâầấậẩẫăằắặẳẵèéẹẻẽêềếệểễìíịỉĩòóọỏõôồốộổỗơờớợởỡùúụủũưừứựửữỳýỵỷỹđ]*)*$";
    public static final String REGEX_IDENTIFICATION = "^\\d{9}$|^\\d{12}$";
    public static final String REGEX_DATE_DD_MM_YYYY = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$";
    public static final String REGEX_DATE_YYYY_MM_DD = "^\\d{4}-\\d{2}-\\d{2}$";
    public static final String REGEX_NO_SPECIAL_CHARACTERS = "^[^~!@#$%^&*(),<.>?‘’/:;“”+=_ ]*$";
    public static final String REGEX_PASSPORT = "^[A-Z][0-9]{7}$";
    public static final String REGEX_CCCD = "^(0[0-9]{2}|[1-8][0-9]|9[0-6])[0-9][0-9]{2}[0-9]{6}$";
    public static final String REGEX_MILITARY_ID = "^\\d{8,12}$";
    public static final String REGEX_CMND = "^\\d{9}$|^\\d{12}$";
    public static final String REGEX_EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    public static boolean validate(String pattern, String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }

        if (pattern == null || pattern.isEmpty()) {
            return false;
        }

        return value.matches(pattern);
    }

    public static String removeSign(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        StringBuilder result = new StringBuilder();
        for (char c : str.toCharArray()) {
            String replacement = NO_SIGN_MAP.get(String.valueOf(c));
            if (replacement != null) {
                result.append(replacement);
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }


    private static final Map<String, String> NO_SIGN_MAP = Map.<String, String>ofEntries(
            Map.entry("Á", "A"), Map.entry("À", "A"), Map.entry("Ả", "A"), Map.entry("Ã", "A"),
            Map.entry("Ạ", "A"), Map.entry("á", "a"), Map.entry("à", "a"), Map.entry("ả", "a"),
            Map.entry("ã", "a"), Map.entry("ạ", "a"), Map.entry("Ó", "O"), Map.entry("Ò", "O"),
            Map.entry("Ỏ", "O"), Map.entry("Õ", "O"), Map.entry("Ọ", "O"), Map.entry("ó", "o"),
            Map.entry("ò", "o"), Map.entry("ỏ", "o"), Map.entry("õ", "o"), Map.entry("ọ", "o"),
            Map.entry("Ă", "A"), Map.entry("Ắ", "A"), Map.entry("Ằ", "A"), Map.entry("Ẳ", "A"),
            Map.entry("Ẵ", "A"), Map.entry("Ặ", "A"), Map.entry("ă", "a"), Map.entry("ắ", "a"),
            Map.entry("ằ", "a"), Map.entry("ẳ", "a"), Map.entry("ẵ", "a"), Map.entry("ặ", "a"),
            Map.entry("Ô", "O"), Map.entry("Ố", "O"), Map.entry("Ồ", "O"), Map.entry("Ổ", "O"),
            Map.entry("Ỗ", "O"), Map.entry("Ộ", "O"), Map.entry("ô", "o"), Map.entry("ố", "o"),
            Map.entry("ồ", "o"), Map.entry("ổ", "o"), Map.entry("ỗ", "o"), Map.entry("ộ", "o"),
            Map.entry("Â", "A"), Map.entry("Ấ", "A"), Map.entry("Ầ", "A"), Map.entry("Ẩ", "A"),
            Map.entry("Ẫ", "A"), Map.entry("Ậ", "A"), Map.entry("â", "a"), Map.entry("ấ", "a"),
            Map.entry("ầ", "a"), Map.entry("ẩ", "a"), Map.entry("ẫ", "a"), Map.entry("ậ", "a"),
            Map.entry("Ơ", "O"), Map.entry("Ớ", "O"), Map.entry("Ờ", "O"), Map.entry("Ở", "O"),
            Map.entry("Ỡ", "O"), Map.entry("Ợ", "O"), Map.entry("ơ", "o"), Map.entry("ớ", "o"),
            Map.entry("ờ", "o"), Map.entry("ở", "o"), Map.entry("ỡ", "o"), Map.entry("ợ", "o"),
            Map.entry("É", "E"), Map.entry("È", "E"), Map.entry("Ẻ", "E"), Map.entry("Ẽ", "E"),
            Map.entry("Ẹ", "E"), Map.entry("é", "e"), Map.entry("è", "e"), Map.entry("ẻ", "e"),
            Map.entry("ẽ", "e"), Map.entry("ẹ", "e"), Map.entry("Ú", "U"), Map.entry("Ù", "U"),
            Map.entry("Ủ", "U"), Map.entry("Ũ", "U"), Map.entry("Ụ", "U"), Map.entry("ú", "u"),
            Map.entry("ù", "u"), Map.entry("ủ", "u"), Map.entry("ũ", "u"), Map.entry("ụ", "u"),
            Map.entry("Ê", "E"), Map.entry("Ế", "E"), Map.entry("Ề", "E"), Map.entry("Ể", "E"),
            Map.entry("Ễ", "E"), Map.entry("Ệ", "E"), Map.entry("ê", "e"), Map.entry("ế", "e"),
            Map.entry("ề", "e"), Map.entry("ể", "e"), Map.entry("ễ", "e"), Map.entry("ệ", "e"),
            Map.entry("Ư", "U"), Map.entry("Ứ", "U"), Map.entry("Ừ", "U"), Map.entry("Ử", "U"),
            Map.entry("Ữ", "U"), Map.entry("Ự", "U"), Map.entry("ư", "u"), Map.entry("ứ", "u"),
            Map.entry("ừ", "u"), Map.entry("ử", "u"), Map.entry("ữ", "u"), Map.entry("ự", "u"),
            Map.entry("Í", "I"), Map.entry("Ì", "I"), Map.entry("Ỉ", "I"), Map.entry("Ĩ", "I"),
            Map.entry("Ị", "I"), Map.entry("í", "i"), Map.entry("ì", "i"), Map.entry("ỉ", "i"),
            Map.entry("ĩ", "i"), Map.entry("ị", "i"), Map.entry("Ý", "Y"), Map.entry("Ỳ", "Y"),
            Map.entry("Ỷ", "Y"), Map.entry("Ỹ", "Y"), Map.entry("Ỵ", "Y"), Map.entry("Đ", "D"),
            Map.entry("ý", "y"), Map.entry("ỳ", "y"), Map.entry("ỷ", "y"), Map.entry("ỹ", "y"),
            Map.entry("ỵ", "y"), Map.entry("đ", "d")
    );

    private static final String[] BELOW_20 = {
            "", "Một", "Hai", "Ba", "Bốn", "Năm", "Sáu", "Bảy", "Tám", "Chín",
            "Mười", "Mười một", "Mười hai", "Mười ba", "Mười bốn", "Mười lăm",
            "Mười sáu", "Mười bảy", "Mười tám", "Mười chín"
    };

    private static final String[] TENS = {
            "", "", "Hai mươi", "Ba mươi", "Bốn mươi", "Năm mươi",
            "Sáu mươi", "Bảy mươi", "Tám mươi", "Chín mươi"
    };

    private static final String[] SCALES = {
            "", "Ngàn", "Triệu", "Tỷ", "Nghìn tỷ", "Một nghìn tỷ", "Tỷ tỷ"
    };

    private static final BigInteger THOUSAND = BigInteger.valueOf(1000);

    private static void threeDigitsToWords(int num, StringBuilder out) {
        if (num == 0) return;

        if (num >= 100) {
            if (!out.isEmpty() && out.charAt(out.length() - 1) != ' ')
                out.append(' ');
            out.append(BELOW_20[num / 100]).append(" Trăm");
            num %= 100;
        }

        if (num >= 20) {
            if (!out.isEmpty() && out.charAt(out.length() - 1) != ' ')
                out.append(' ');
            out.append(TENS[num / 10]);
            num %= 10;
        }

        if (num > 0) {
            if (!out.isEmpty() && out.charAt(out.length() - 1) != ' ')
                out.append(' ');
            out.append(BELOW_20[num]);
        }
    }

    private static String integerPartToWords(BigInteger n) {
        if (n.signum() == 0) return "Không";

        StringBuilder res = new StringBuilder();
        int scaleIndex = 0;

        while (n.signum() > 0 && scaleIndex < SCALES.length) {
            BigInteger[] divRem = n.divideAndRemainder(THOUSAND);
            int chunk = divRem[1].intValue();

            if (chunk != 0) {
                StringBuilder part = new StringBuilder();
                threeDigitsToWords(chunk, part);

                if (!SCALES[scaleIndex].isEmpty()) {
                    if (!part.isEmpty() && part.charAt(part.length() - 1) != ' ')
                        part.append(' ');
                    part.append(SCALES[scaleIndex]);
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

    /**
     * Đọc phần thập phân theo từng chữ số.
     * Ví dụ: ".50" → "Năm Không", ".125" → "Một Hai Năm"
     */
    private static String decimalPartToWords(String decimalDigits) {
        StringBuilder sb = new StringBuilder();
        for (char c : decimalDigits.toCharArray()) {
            if (!sb.isEmpty()) sb.append(' ');
            sb.append(BELOW_20[c - '0']);
        }
        return sb.toString();
    }

    /**
     * Chuyển BigDecimal thành chữ tiếng Việt.
     * <p>
     * Ví dụ:
     * <ul>
     *   <li>1234567       → "Một Triệu Hai Trăm Ba Mươi Bốn Ngàn Năm Trăm Sáu Mươi Bảy đồng"</li>
     *   <li>1234567.50    → "Một Triệu ... Bảy đồng Năm Mươi xu"</li>
     *   <li>1234567.125   → "Một Triệu ... Bảy phẩy Một Hai Năm"</li>
     * </ul>
     *
     * @param value    số cần chuyển
     * @param currency true = dùng "đồng/xu" (2 chữ số thập phân),
     *                 false = dùng "phẩy" rồi đọc từng chữ số
     */
    public static String numberToWords(BigDecimal value, boolean currency) {
        boolean negative = value.signum() < 0;
        BigDecimal abs = value.abs();

        // Tách phần nguyên & thập phân
        BigInteger intPart = abs.toBigInteger();
        BigDecimal fracPart = abs.subtract(new BigDecimal(intPart)); // 0.xxx

        StringBuilder result = new StringBuilder();

        if (negative) result.append("Âm ");

        // Phần nguyên
        result.append(integerPartToWords(intPart));

        // Phần thập phân
        if (fracPart.signum() > 0) {
            if (currency) {
                // Làm tròn 2 chữ số → xu (0.50 → 50 xu)
                int xu = fracPart.movePointRight(2)
                        .setScale(0, java.math.RoundingMode.HALF_UP)
                        .intValue();
                if (xu > 0) {
                    result.append(" đồng ");
                    // Đọc xu như số bình thường (1–99)
                    StringBuilder xuPart = new StringBuilder();
                    threeDigitsToWords(xu, xuPart);
                    result.append(xuPart).append(" xu");
                } else {
                    result.append(" đồng");
                }
            } else {
                // Đọc từng chữ số sau dấu phẩy
                String fracStr = abs.toPlainString();
                String decimalDigits = fracStr.substring(fracStr.indexOf('.') + 1);
                result.append(" phẩy ").append(decimalPartToWords(decimalDigits));
            }
        } else if (currency) {
            result.append(" đồng");
        }

        return result.toString();
    }

    // Overload mặc định: không dùng tiền tệ
    public static String numberToWords(BigDecimal value) {
        return numberToWords(value, false);
    }


    /**
     * Test Example
     */
    public static void main(String[] args) {
        String userName = "LÂM TRUNG HIẾU";
        System.out.println("Is the valid ?: " + validate(REGEX_USERNAME, userName));
        System.out.println("Remove sign: " + removeSign(userName));
        // Số nguyên lớn
        System.out.println(numberToWords(new BigDecimal("542780009700056345"), true));
        // → ... đồng

        // Có xu
        System.out.println(numberToWords(new BigDecimal("1234567.50"), true));
        // → Một Triệu Hai Trăm Ba Mươi Bốn Ngàn Năm Trăm Sáu Mươi Bảy đồng Năm Mươi xu

        // Không dùng tiền tệ, đọc từng chữ số
        System.out.println(numberToWords(new BigDecimal("9876.125")));
        // → Chín Ngàn Tám Trăm Bảy Mươi Sáu phẩy Một Hai Năm

        // Số âm
        System.out.println(numberToWords(new BigDecimal("-500000.75"), true));
        // → Âm Năm Trăm Ngàn đồng Bảy Mươi Năm xu
    }
}
