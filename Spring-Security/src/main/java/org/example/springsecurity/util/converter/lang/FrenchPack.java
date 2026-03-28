package org.example.springsecurity.util.converter.lang;

public final class FrenchPack implements LanguagePack {
    private static final String[] BELOW_20 = {
            "", "Un", "Deux", "Trois", "Quatre", "Cinq", "Six", "Sept", "Huit", "Neuf",
            "Dix", "Onze", "Douze", "Treize", "Quatorze", "Quinze",
            "Seize", "Dix-sept", "Dix-huit", "Dix-neuf"
    };

    private static final String[] TENS = {
            "", "", "Vingt", "Trente", "Quarante", "Cinquante",
            "Soixante", "Soixante", "Quatre-vingts", "Quatre-vingt"
            // index 7 = Soixante (dùng cho 70–79: Soixante-dix, Soixante et onze...)
            // index 8 = Quatre-vingts (80 đúng, 81–89 bỏ 's')
            // index 9 = Quatre-vingt (dùng cho 90–99: Quatre-vingt-dix...)
    };

    private static final String[] SCALES = {
            "", "Mille", "Million", "Milliard", "Billion", "Billiard", "Trillion"
    };

    @Override
    public String code() {
        return "fr";
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
        return "Zéro";
    }

    @Override
    public String hundred() {
        return "Cent";
    }

    @Override
    public String negative() {
        return "Négatif";
    }

    @Override
    public String decimalSeparator() {
        return "virgule";
    }

    @Override
    public String tensUnitSeparator() {
        return "-";
    }

    /**
     * Tiếng Pháp có quy tắc đặc biệt:
     * 70 = Soixante-dix, 71 = Soixante et onze, 72 = Soixante-douze ...
     * 80 = Quatre-vingts, 81 = Quatre-vingt-un ...
     * 90 = Quatre-vingt-dix, 91 = Quatre-vingt-onze ...
     */
    @Override
    public void formatThreeDigits(int num, StringBuilder out) {
        if (num == 0) return;

        if (num >= 100) {
            ThreeDigitFormatter.appendSpace(out);
            int h = num / 100;
            if (h == 1) {
                out.append("Cent");
            } else {
                out.append(BELOW_20[h]);
                // Cent prend un 's' si multiple et rien ne suit
                int remainder = num % 100;
                out.append(remainder == 0 ? " Cents" : " Cent");
            }
            num %= 100;
            if (num == 0) return;
        }

        ThreeDigitFormatter.appendSpace(out);

        if (num < 20) {
            out.append(BELOW_20[num]);
        } else if (num < 70) {
            // 20–69: règle standard
            out.append(TENS[num / 10]);
            int unit = num % 10;
            if (unit == 1) {
                out.append(" et ").append(BELOW_20[unit]);  // Vingt et un
            } else if (unit > 0) {
                out.append('-').append(BELOW_20[unit]);
            }
        } else if (num < 80) {
            // 70–79: Soixante + (10–19)
            out.append("Soixante");
            int sub = num - 60; // 10–19
            if (sub == 11) {
                out.append(" et ").append(BELOW_20[sub]); // Soixante et onze
            } else {
                out.append('-').append(BELOW_20[sub]);
            }
        } else if (num == 80) {
            out.append("Quatre-vingts"); // avec 's'
        } else {
            // 81–99: Quatre-vingt + (1–19)
            out.append("Quatre-vingt");
            int sub = num - 80; // 1–19
            out.append('-').append(BELOW_20[sub]);
        }
    }
}
