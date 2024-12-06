package se.lisau.project.util;

import java.util.Scanner;

public class ScannerUtil {
    private static final Scanner sc = new Scanner(System.in);

    // läser strängar
    public static String getUserInput() {
        return sc.nextLine().toLowerCase();
    }

    // läser double
    public static double readDouble() {
        try {
            return sc.nextDouble();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // läser int
    public static int readInt() {
        return sc.nextInt();
    }

    // stänger scanner
    public static void closeScanner() {
        sc.close();
    }
}
