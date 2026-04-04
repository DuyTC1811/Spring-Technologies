package org.example.springsecurity.utils.converter.lang;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ThreeDigitFormatter {
    public static void appendSpace(StringBuilder sb) {
        if (!sb.isEmpty() && sb.charAt(sb.length() - 1) != ' ') {
            sb.append(' ');
        }
    }

    public static void format(int num, StringBuilder out, LanguagePack lp) {
        if (num == 0) return;

        String[] below20 = lp.below20();
        String[] tens = lp.tens();

        if (num >= 100) {
            appendSpace(out);
            out.append(below20[num / 100]).append(' ').append(lp.hundred());
            num %= 100;
            if (num > 0 && !lp.afterHundred().isEmpty()) {
                out.append(' ').append(lp.afterHundred());
            }
        }

        if (num >= 20) {
            appendSpace(out);
            out.append(tens[num / 10]);
            num %= 10;
            if (num > 0) {
                out.append(lp.tensUnitSeparator()).append(below20[num]);
                return;
            }
        }

        if (num > 0) {
            appendSpace(out);
            out.append(below20[num]);
        }
    }
}
