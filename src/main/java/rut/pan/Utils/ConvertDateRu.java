package rut.pan.Utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ConvertDateRu {
    public String toMonthString(Integer i) {
        if (i == null) {
            throw new NullPointerException("can't convert null to month");
        }
        return switch (i) {
            case 1 -> "Январь";
            case 2 -> "Февраль";
            case 3 -> "Март";
            case 4 -> "Апрель";
            case 5 -> "Май";
            case 6 -> "Июнь";
            case 7 -> "Июль";
            case 8 -> "Август";
            case 9 -> "Сентябрь";
            case 10 -> "Октябрь";
            case 11 -> "Ноябрь";
            case 12 -> "Декабрь";
            default -> throw new IllegalArgumentException("can't convert " + i + " to month");
        };
    }

    public String toMonthString(String month) {
        if (month == null) {
            throw new NullPointerException("can't convert null to month");
        }
        return switch (month.toLowerCase()) {
            case "january" -> "Январь";
            case "february" -> "Февраль";
            case "march" -> "Март";
            case "april" -> "Апрель";
            case "may" -> "Май";
            case "june" -> "Июнь";
            case "july" -> "Июль";
            case "august" -> "Август";
            case "september" -> "Сентябрь";
            case "october" -> "Октябрь";
            case "november" -> "Ноябрь";
            case "december" -> "Декабрь";
            default -> throw new IllegalArgumentException("can't convert " + month + " to month");
        };
    }
}
