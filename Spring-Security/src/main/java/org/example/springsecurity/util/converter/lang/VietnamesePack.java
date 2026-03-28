package org.example.springsecurity.util.converter.lang;

public final class VietnamesePack implements LanguagePack {
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

    @Override
    public String code() {
        return "vi";
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
        return "Không";
    }

    @Override
    public String hundred() {
        return "Trăm";
    }

    @Override
    public String negative() {
        return "Âm";
    }

    @Override
    public String decimalSeparator() {
        return "phẩy";
    }

}
