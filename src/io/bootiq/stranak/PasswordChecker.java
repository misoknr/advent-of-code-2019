package io.bootiq.stranak;

import java.util.HashMap;
import java.util.Map;

public class PasswordChecker {

    private String adjustToNearestPossiblePassword(String password, int breakingIndex) {
        String adjustedPassword = password.substring(0, breakingIndex);
        int lastGoodDigit = Integer.valueOf(new String(new char[]{password.charAt(breakingIndex - 1)}));

        for (int i = breakingIndex; i < password.length(); i++) {
            adjustedPassword = adjustedPassword.concat(String.valueOf(lastGoodDigit));
        }

        return adjustedPassword;
    }

    private boolean isPossiblePassword(String password, boolean sameDigitsInPair) {
        int lastDigit = password.charAt(0);
        Map<Integer, Integer> sameDigits = new HashMap<>();

        for (int i = 1; i < password.length(); i++) {
            int digit = password.charAt(i);

            if (digit < lastDigit) {
                return false;
            }

            if (lastDigit == digit) {
                Integer sameDigitGroup = sameDigits.get(digit);
                sameDigits.put(digit, sameDigitGroup == null ? 2 : sameDigitGroup + 1);
            }

            lastDigit = digit;
        }

        if (!sameDigitsInPair) {
            return sameDigits.size() > 0;
        }

        boolean sameDigitPairExists = false;

        for (Integer sameDigitCount : sameDigits.values()) {
            if (sameDigitCount == 2) {
                sameDigitPairExists = true;
                break;
            }
        }

        return sameDigitPairExists;
    }

    public int countPossiblePasswords(int[] range, boolean sameDigitsInPair) {
        int count = 0;
//        int passwordStart = Integer.parseInt(adjustToNearestPossiblePassword(String.valueOf(range[0]), 1));
        int passwordStart = range[0];

        for (int p = passwordStart; p <= range[1]; p++) {
            if (isPossiblePassword(String.valueOf(p), sameDigitsInPair)) {
                count++;
            }
        }

        return count;
    }

    public static void main(String[] args) {
        int[] passwordRange = new int[]{240920, 789857};

        PasswordChecker passwordChecker = new PasswordChecker();

        int possiblePasswordCount = passwordChecker.countPossiblePasswords(passwordRange, true);
        System.out.println(String.format("Number of possible passwords: %d", possiblePasswordCount));

//        System.out.println(passwordChecker.adjustToNearestPossiblePassword(String.valueOf(240920), 2));

//        String password = "111122";
//        System.out.println(String.format("Password %s may be possible: %s", password, passwordChecker.isPossiblePassword(password, true)));
    }

}
