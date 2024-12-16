package org.example;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class TimeZoneConverter {
    private static final Scanner scanner = new Scanner(System.in);
    private static boolean isMilitaryTime = false;  // Default to 12-hour format
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final FavoriteZones favoriteZones = new FavoriteZones();
    private static boolean showMainMenu = true;

    public static void main(String[] args) {
        while (true) {
            if (showMainMenu) {
                displayMainMenu();
                showMainMenu = false;
            }
            handleUserInput();
        }
    }

    // Display main menu
    private static void displayMainMenu() {
        System.out.println("****************************************");
        System.out.println("*          TIME ZONE CONVERTER         *");
        System.out.println("****************************************");
        System.out.println("Press (X) for All Commands");
    }

    // Handle user input
    private static void handleUserInput() {
        String input = scanner.nextLine();
        switch (input.toUpperCase()) {
            case "X" -> displayCommands();
            case "O" -> displayInstructions();
            case "L" -> displayTimeZones();
            case "F" -> addToFavorites();
            case "D" -> removeFromFavorites();
            case "V" -> favoriteZones.listFavorites();
            case "S" -> displaySettingsMenu();
            case "B" -> showMainMenu = true; // Set flag to true to show main menu again
            case "Q" -> {
                System.out.println("Goodbye!");
                System.exit(0);
            }
            case "T" -> runTests(); // Add a case for running tests
            default -> System.out.println("Invalid input. Please try again.");
        }
    }

    // Remove timezone from favorites
    private static void removeFromFavorites() {
        System.out.println("Enter the timezone you want to remove from favorites:");
        String zoneId = scanner.nextLine();
        favoriteZones.removeFavorite(zoneId);
    }

    // Display available commands
    private static void displayCommands() {
        System.out.println("COMMANDS:");
        System.out.println("(O) Instructions");
        System.out.println("(L) List All Timezones");
        System.out.println("(F) Add to Favorites");
        System.out.println("(V) View Favorites");
        System.out.println("(D) Delete from Favorites");
        System.out.println("(S) Settings");
        System.out.println("(B) Back");
        System.out.println("(Q) Quit");
        System.out.println("(T) Run Tests"); // Add a command for running tests
        System.out.println("\n----------------------------------------------------------------------\n");
    }

    // Display instructions
    private static void displayInstructions() {
        System.out.println("----------------------------------------------------------------------");
        System.out.println("INSTRUCTIONS: ");
        System.out.println("1. Enter your timezone: \t\t(Example: Europe/Berlin)");
        System.out.println("2. Enter the time to convert: \t(Example: 2024-04-16 15:30)");
        System.out.println("3. Enter your target timezone: \t(Example: America/Los_Angeles) ");
        System.out.println("Press Enter to convert.\n");
        System.out.println("OUTPUT MAY LOOK LIKE THIS");
        System.out.println("--------------------------------------------");
        System.out.println("Converted Time: 2024-04-16 06:30 PST");
        System.out.println("--------------------------------------------\n");
        System.out.println("\n");
        System.out.println("TIMEZONE CONVERTER COMMANDS:");
        System.out.println("(C) Start TimeZone Converter");
        System.out.println("(B) Back");
        System.out.println("(Q) Quit");
        System.out.println("\n----------------------------------------------------------------------\n");
        String input = scanner.nextLine();
        switch (input.toUpperCase()) {
            case "C" -> convertTime();
            case "B" -> showMainMenu = true;
            case "Q" -> {
                System.out.println("Goodbye!");
                System.exit(0);
            }
            default -> System.out.println("Invalid input. Please try again.");
        }
    }

    // Convert time
    private static void convertTime() {
        System.out.println("Enter your timezone: ");
        String sourceZoneId = scanner.nextLine();
        ZoneId sourceZone;
        try {
            sourceZone = ZoneId.of(sourceZoneId);
        } catch (DateTimeException e) {
            System.out.println("Invalid timezone. Please try again.");
            return;
        }

        LocalDateTime timeToConvert = null;
        while (timeToConvert == null) {
            System.out.println("Enter the time to convert (Format: yyyy-MM-dd HH:mm): ");
            String inputTime = scanner.nextLine();
            try {
                timeToConvert = LocalDateTime.parse(inputTime, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid time format. Please try again.");
            }
        }

        System.out.println("Enter your target timezone: ");
        String targetZoneId = scanner.nextLine();
        ZoneId targetZone;
        try {
            targetZone = ZoneId.of(targetZoneId);
        } catch (DateTimeException e) {
            System.out.println("Invalid timezone. Please try again.");
            return;
        }

        ZonedDateTime zonedDateTime = ZonedDateTime.of(timeToConvert, sourceZone);
        ZonedDateTime convertedZonedDateTime = zonedDateTime.withZoneSameInstant(targetZone);

        System.out.println("\nConverted Time: " + formatTime(convertedZonedDateTime) + " " + targetZoneId);
        System.out.println("--------------------------------------------\n");
    }

    // Display timezones
    private static void displayTimeZones() {
        System.out.println("List of all timezones:");
        System.out.println("--------------------------------------------");
        System.out.println("(AF) Africa");
        System.out.println("(AM) America");
        System.out.println("(AA) Asia");
        System.out.println("(EU) Europe");
        System.out.println("(OZ) Oceania");
        System.out.println("(ALL) All");
        System.out.println("--------------------------------------------");
        String input = scanner.nextLine();
        String[] zoneIds;
        switch (input.toUpperCase()) {
            case "ALL" -> zoneIds = ZoneId.getAvailableZoneIds().toArray(new String[0]);
            case "AF" -> zoneIds = filterZonesByContinent("Africa");
            case "AM" -> zoneIds = filterZonesByContinent("America");
            case "AA" -> zoneIds = filterZonesByContinent("Asia");
            case "EU" -> zoneIds = filterZonesByContinent("Europe");
            case "OZ" -> zoneIds = filterZonesByContinent("Oceania");
            default -> {
                System.out.println("Invalid input. Please try again.");
                return;
            }
        }
        for (String zoneId : zoneIds) {
            System.out.println(zoneId);
        }
        System.out.println("--------------------------------------------\n");
    }

    // Filter timezones by continent
    private static String[] filterZonesByContinent(String continent) {
        return ZoneId.getAvailableZoneIds().stream()
                .filter(zoneId -> zoneId.startsWith(continent))
                .toArray(String[]::new);
    }

    // Add timezone to favorites
    private static void addToFavorites() {
        System.out.println("Enter the timezone you want to add to favorites:");
        String zoneId = scanner.nextLine();
        favoriteZones.addFavorite(zoneId);
    }

    // Display settings menu
    private static void displaySettingsMenu() {
        System.out.println("****************************************");
        System.out.println("*             Settings                 *");
        System.out.println("****************************************");
        System.out.println("COMMANDS:");
        System.out.println("(Z) Time Output");
        System.out.println("(B) Back ");
        System.out.println("(Q) Quit");
        System.out.println();

        String input = scanner.nextLine();
        switch (input.toUpperCase()) {
            case "Z" -> displayTimeOutputSettings();
            case "B" -> {
            }
            case "Q" -> {
                System.out.println("Goodbye!");
                System.exit(0);
            }
            default -> System.out.println("Invalid input. Please try again.");
        }
    }

    // Display time output settings
    private static void displayTimeOutputSettings() {
        System.out.println("****************************************");
        System.out.println("*         Time Output Settings         *");
        System.out.println("****************************************");
        System.out.println("Choose the time format:");
        System.out.println("(M) Military Time (24H)");
        System.out.println("(T) Standard Time (12H)");
        System.out.println("(B) Back to Settings Menu");
        System.out.println();

        String input = scanner.nextLine();
        switch (input.toUpperCase()) {
            case "M" -> {
                isMilitaryTime = true;
                System.out.println("Military Time (24H) enabled.");
            }
            case "T" -> {
                isMilitaryTime = false;
                System.out.println("Standard Time (12H) enabled.");
            }
            case "B" -> {
            }
            default -> System.out.println("Invalid input. Please try again.");
        }
    }

    // Format time
    private static String formatTime(ZonedDateTime time) {
        DateTimeFormatter formatter;
        if (isMilitaryTime) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        } else {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
        }
        return time.format(formatter);
    }

    // Run tests
    private static void runTests() {
        System.out.println("Running tests...");
        // Add your test commands here
        // Example test: Convert time from Europe/Berlin to America/Los_Angeles
        String sourceZoneId = "Europe/Berlin";
        String targetZoneId = "America/Los_Angeles";
        String inputTime = "2024-04-16 15:30";
        try {
            ZoneId sourceZone = ZoneId.of(sourceZoneId);
            ZoneId targetZone = ZoneId.of(targetZoneId);
            LocalDateTime timeToConvert = LocalDateTime.parse(inputTime, formatter);
            ZonedDateTime zonedDateTime = ZonedDateTime.of(timeToConvert, sourceZone);
            ZonedDateTime convertedZonedDateTime = zonedDateTime.withZoneSameInstant(targetZone);
            System.out.println("Test Passed: " + formatTime(convertedZonedDateTime) + " " + targetZoneId);
        } catch (DateTimeException e) {
            System.out.println("Test Failed: " + e.getMessage());
        }
    }
}
