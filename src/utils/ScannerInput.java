package utils;

import java.util.Scanner;

/**
 * This class provides methods for the robust handling of I/O using Scanner.
 * It creates a new Scanner object for each read from the user, eliminating
 * the Scanner bug where buffers don't flush correctly after an int read.
 * The methods also parse numeric data to ensure it is correct. If it isn't,
 * the user is prompted to enter it again.
 *
 * @author Siobhan Drohan, Mairead Meagher
 * @version 1.0
 */
public class ScannerInput {

    /**
     * Read an int from the user. If the entered data isn't an int, prompt again.
     *
     * @param prompt The message shown to the user.
     * @return The verified int entered by the user.
     */
    public static int readNextInt(String prompt) {
        do {
            var scanner = new Scanner(System.in);
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.next());
            } catch (NumberFormatException e) {
                System.err.println("\tEnter a number please.");
            }
        } while (true);
    }

    /**
     * Read a double from the user. If the entered data isn't a double, prompt again.
     *
     * @param prompt The message shown to the user.
     * @return The verified double entered by the user.
     */
    public static double readNextDouble(String prompt) {
        do {
            var scanner = new Scanner(System.in);
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.next());
            } catch (NumberFormatException e) {
                System.err.println("\tEnter a number please.");
            }
        } while (true);
    }

    /**
     * Read a line of text from the user. No validation is performed.
     *
     * @param prompt The message shown to the user.
     * @return The String entered by the user.
     */
    public static String readNextLine(String prompt) {
        Scanner input = new Scanner(System.in);
        System.out.print(prompt);
        return input.nextLine();
    }

    /**
     * Read a single character from the user. No validation is performed.
     *
     * @param prompt The message shown to the user.
     * @return The first character entered by the user.
     */
    public static char readNextChar(String prompt) {
        Scanner input = new Scanner(System.in);
        System.out.print(prompt);
        return input.next().charAt(0);
    }
}
