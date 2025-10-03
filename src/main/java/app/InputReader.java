package app;

import util.ConsolePrinter;
import java.util.Scanner;

public class InputReader {
    
    private final Scanner scanner;
    
    public InputReader(Scanner scanner) {
        this.scanner = scanner;
    }
    
    /**
     * Reads an integer with validation and retry
     */
    public int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    ConsolePrinter.printError("Input cannot be empty");
                    continue;
                }
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                ConsolePrinter.printError("Please enter a valid number");
            }
        }
    }
    
    /**
     * Reads an integer within a range
     */
    public int readIntInRange(String prompt, int min, int max) {
        while (true) {
            int value = readInt(prompt);
            if (value >= min && value <= max) {
                return value;
            }
            ConsolePrinter.printError("Please enter a number between " + min + " and " + max);
        }
    }
    
    /**
     * Reads a double with validation and retry
     */
    public double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    ConsolePrinter.printError("Input cannot be empty");
                    continue;
                }
                double value = Double.parseDouble(input);
                if (value <= 0) {
                    ConsolePrinter.printError("Amount must be positive");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                ConsolePrinter.printError("Please enter a valid decimal number");
            }
        }
    }
    
    /**
     * Reads a non-empty string
     */
    public String readString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            ConsolePrinter.printError("Input cannot be empty");
        }
    }
    
    /**
     * Reads yes/no confirmation
     */
    public boolean readConfirmation(String prompt) {
        while (true) {
            System.out.print(prompt + " (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y") || input.equals("yes")) {
                return true;
            } else if (input.equals("n") || input.equals("no")) {
                return false;
            }
            ConsolePrinter.printError("Please enter 'y' or 'n'");
        }
    }
    
    public void waitForEnter() {
        ConsolePrinter.waitForEnter();
    }
}