package dev.gether.getmetin.utils;

public class NumberChecker {

    public static boolean isDouble(String text) {
        if (text == null) {
            return false;
        }
        try {
            Double.parseDouble(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
