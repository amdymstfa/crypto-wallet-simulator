package app ;

import util.ConsolePrinter;  
import java.util.Scanner;


public class InputReader {

    private final Scanner scanner ;

    public InputReader(Scanner scanner){
        this.scanner = scanner;
    }

    // read an integer 
   public int readInt(String prompt) {
        System.out.print(prompt);
        try {
            String input = scanner.nextLine().trim();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            ConsolePrinter.printError("Please enter a valid number");
            return -1;
        }
    }

    // read Double 
   public double readDouble(String prompt) {
        System.out.print(prompt);
        try {
            String input = scanner.nextLine().trim();
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            ConsolePrinter.printError("Please enter a valid decimal number");
            return -1.0;
        }
    }

    // read String 
    public String readString(String prompt){
        System.out.println(prompt);
        return scanner.nextLine().trim();
    }

    // read confirmation
     public boolean readConfirmation(String prompt) {
        System.out.print(prompt + " (y/n): ");
        String input = scanner.nextLine().trim().toLowerCase();
        return input.equals("y") || input.equals("yes");
    }

    public void waitForEnter() {
        ConsolePrinter.waitForEnter();
    }
}