package org.example.springsecurity.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

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

    /**
     * Test Example
     */
    public static void main(String[] args) {
        String userName = "LÂM TRUNG HIẾU";
        System.out.println("Is the valid ?: " + validate(REGEX_USERNAME, userName));
    }
}
