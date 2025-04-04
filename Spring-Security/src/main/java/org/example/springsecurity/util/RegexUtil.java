package org.example.springsecurity.util;

import lombok.Getter;
import lombok.Setter;

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

    /**
     * Test Example
     */
    public static void main(String[] args) {
        String userName = "LÂM TRUNG HIẾU";
        System.out.println("Is the valid ?: " + validate(REGEX_USERNAME, userName));

        System.out.println("Remove sign: " + removeSign(userName));
    }
}
