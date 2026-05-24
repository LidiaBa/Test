package org.example.type;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Url {
    public static Long getId(String url) {  // no usages
        String[] urls = url.split("/");
        String last = urls[urls.length - 1];
        if (last.isEmpty()) last = urls[urls.length - 2];
        try {
            return Long.parseLong(last);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
