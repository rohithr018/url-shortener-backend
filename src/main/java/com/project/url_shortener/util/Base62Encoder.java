package com.project.url_shortener.util;

import org.springframework.stereotype.Component;

@Component
public class Base62Encoder {
    private static final String Base62="0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public String encode(long num){
        StringBuilder sb= new StringBuilder();
        while(num>0){
            sb.append(Base62.charAt((int)(num%62)));
            num/=62;
        }
        while(sb.length()<6)sb.append('0');
        return sb.reverse().toString();
    }
}
