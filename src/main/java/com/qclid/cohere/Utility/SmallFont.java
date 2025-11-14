package com.qclid.cohere.Utility;

import java.util.HashMap;
import java.util.Map;

public class SmallFont {

    private static final Map<Character, Character> fontMap = new HashMap<>();

    static {
        fontMap.put('a', 'ᴀ');
        fontMap.put('b', 'ʙ');
        fontMap.put('c', 'ᴄ');
        fontMap.put('d', 'ᴅ');
        fontMap.put('e', 'ᴇ');
        fontMap.put('f', 'ꜰ');
        fontMap.put('g', 'ɢ');
        fontMap.put('h', 'ʜ');
        fontMap.put('i', 'ɪ');
        fontMap.put('j', 'ᴊ');
        fontMap.put('k', 'ᴋ');
        fontMap.put('l', 'ʟ');
        fontMap.put('m', 'ᴍ');
        fontMap.put('n', 'ɴ');
        fontMap.put('o', 'ᴏ');
        fontMap.put('p', 'ᴘ');
        fontMap.put('q', 'ǫ');
        fontMap.put('r', 'ʀ');
        fontMap.put('s', 'ѕ');
        fontMap.put('t', 'ᴛ');
        fontMap.put('u', 'ᴜ');
        fontMap.put('v', 'ᴠ');
        fontMap.put('w', 'ᴡ');
        fontMap.put('x', 'х');
        fontMap.put('y', 'ʏ');
        fontMap.put('z', 'ᴢ');
    }

    public static String toSmallFont(String text) {
        if (text == null) {
            return null;
        }
        StringBuilder smallFontText = new StringBuilder();
        for (char c : text.toLowerCase().toCharArray()) {
            smallFontText.append(fontMap.getOrDefault(c, c));
        }
        return smallFontText.toString();
    }
}
