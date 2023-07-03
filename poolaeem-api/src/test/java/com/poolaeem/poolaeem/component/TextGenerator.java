package com.poolaeem.poolaeem.component;

public class TextGenerator {
    public static String generate(int length) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            builder.append("갉");
        }

        return builder.toString();
    }
}
