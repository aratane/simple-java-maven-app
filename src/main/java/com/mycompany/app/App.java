package com.mycompany.app;

import java.util.Scanner;

/**
 * An enhanced version of the Java application with an improved console interface.
 */
public class EnhancedApp {

    private static final String GREETING_MESSAGE = "Hello, World!";
    private static final String ABOUT_MESSAGE = "This is a simple Java console application.";

    public static void main(String[] args) {
        displayWelcomeScreen();
        displayMenu();
    }

    private static void displayWelcomeScreen() {
        System.out.println("=== Welcome to EnhancedApp ===");
        System.out.println(GREETING_MESSAGE);
        System.out.println(ABOUT_MESSAGE);
        System.out.println();
    }

    private static void displayMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("=== Menu ===");
            System.out.println("1. Print Greeting");
            System.out.println("2. About");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = getChoice(scanner);

            switch (choice) {
                case 1:
                    printGreeting();
                    break;
                case 2:
                    displayAbout();
                    break;
                case 3:
                    exitApplication(scanner);
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }

    private static int getChoice(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next(); // Consume invalid input
        }
        return scanner.nextInt();
    }

    private static void printGreeting() {
        System.out.println("=== Greeting ===");
        System.out.println(GREETING_MESSAGE);
        System.out.println();
    }

    private static void displayAbout() {
        System.out.println("=== About ===");
        System.out.println(ABOUT_MESSAGE);
        System.out.println();
    }

    private static void exitApplication(Scanner scanner) {
        System.out.println("Exiting the application. Goodbye!");
        scanner.close();
        System.exit(0);
    }
}
