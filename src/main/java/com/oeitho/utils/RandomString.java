package com.oeitho.utils;

import java.security.SecureRandom;

public class RandomString {

    public static final String UPPER_CASE_ALPHANUMERIC = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private SecureRandom random;
    
    public RandomString() {
        random = new SecureRandom();
    }

    public String randomString(String characterSet, int length) {
        if (characterSet.length() < 1) {
            throw new IllegalArgumentException("Provided character may not contain less than 1 character!");
        }
        if (length < 0) {
            throw new IllegalArgumentException("Length may not be less than 0!");
        }

        StringBuilder stringBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int characterIndex = random.nextInt(characterSet.length());
            stringBuilder.append(characterSet.charAt(characterIndex));
        }
        return stringBuilder.toString();
    }

}
