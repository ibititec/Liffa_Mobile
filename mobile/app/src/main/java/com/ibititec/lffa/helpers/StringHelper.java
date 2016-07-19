package com.ibititec.lffa.helpers;

import java.util.List;

/**
 * Created by pedro on 11/02/16.
 */
public class StringHelper {
    public static String join(List<String> items, String separator) {
        if (items.size() == 0)
            return "";

        StringBuilder sb = new StringBuilder(items.get(0));

        for (int i = 1; i < items.size(); i++) {
            sb.append(separator);
            sb.append(items.get(i));
        }

        return sb.toString();
    }
}