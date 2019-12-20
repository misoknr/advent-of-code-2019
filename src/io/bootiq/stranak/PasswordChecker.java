package io.bootiq.stranak;

public class PasswordChecker {

    private boolean digitsCheck(String password) {
        int lastDigit = password.charAt(0);
        boolean sameAdjacentDigits = false;

        for (int i = 1; i < password.length(); i++) {
            int digit = password.charAt(i);

            if (digit < lastDigit) {
                return false;
            }

            if (lastDigit == digit) {
                sameAdjacentDigits = true;
            }

            lastDigit = digit;
        }

        return sameAdjacentDigits;
    }

    public boolean isPossiblePassword(int password) {
        String passString = String.valueOf(password);

        if (digitsCheck(passString)) {
            return true;
        }

        return false;
    }


    public int countPossiblePasswords(int[] range) {
        int count = 0;

        for (int p = range[0]; p <= range[1]; p++) {
            if (isPossiblePassword(p)) count++;
        }

        return count;
    }

    public static void main(String[] args) {
        int[] passwordRange = new int[]{240920, 789857};

        PasswordChecker passwordChecker = new PasswordChecker();
        int possiblePasswordCount = passwordChecker.countPossiblePasswords(passwordRange);

        System.out.println(String.format("Number of possible passwords: %d", possiblePasswordCount));
    }

}
