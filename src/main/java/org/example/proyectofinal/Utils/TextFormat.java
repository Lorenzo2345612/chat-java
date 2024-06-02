package org.example.proyectofinal.Utils;

public class TextFormat {
    public static String toFixedLengthString(String string, int length) {
        return String.format("%-" + length + "."+length+"s", string);
    }
}
