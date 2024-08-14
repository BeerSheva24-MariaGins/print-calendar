package telran.time;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

record MonthYear(int month, int year) {
}

public class Main {

    public static void main(String[] args) {
        try {
            MonthYear monthYear = getMonthYear(args); 
            printCalendar(monthYear);
        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void printCalendar(MonthYear monthYear) {
        System.out.println();
        printTitle(monthYear);
        System.out.println();
        printWeekDays();
        printDates(monthYear);
        System.out.println();
    }

    private static void printDates(MonthYear monthYear) {
        LocalDate date = LocalDate.of(monthYear.year(), monthYear.month(), 1);
        int firstDayOfWeek = date.getDayOfWeek().getValue();
        int offset = getOffset(firstDayOfWeek);
        int lastDay = date.lengthOfMonth();

        for (int i = 0; i < offset; i++) {
            System.out.print("     ");
        }

        for (int day = 1; day <= lastDay; day++) {
            System.out.printf("%5d", day);
            if ((day + offset) % 7 == 0) {
                System.out.println();
            }
        }
        System.out.println();
    }

    private static void printWeekDays() {
        StringBuilder weekDays = new StringBuilder();
        for (DayOfWeek day : DayOfWeek.values()) {
            weekDays.append(String.format(" %4s", day.getDisplayName(TextStyle.SHORT, Locale.US)));
        }
        System.out.println(centerText(weekDays.toString()));
        //System.out.println("  ----------------------------------");
        System.out.println(" ...................................");
    }

    private static void printTitle(MonthYear monthYear) {
        String title = String.format("    %s %d", LocalDate.of(monthYear.year(), monthYear.month(), 1).getMonth().getDisplayName(TextStyle.FULL, Locale.US), monthYear.year());
        System.out.println(centerText(title));
    }

    private static MonthYear getMonthYear(String[] args) throws Exception {
        LocalDate now = LocalDate.now();
        int month = now.getMonthValue();
        int year = now.getYear();

        if (args.length == 2) {
            try {
                month = Integer.parseInt(args[0]);
                year = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                throw new Exception("Invalid month or year format. Please provide valid integers.");
            }
        }

        return new MonthYear(month, year);
    }

    private static int getOffset(int firstDayOfWeek) {
        return firstDayOfWeek - DayOfWeek.MONDAY.getValue();
    }

    private static String centerText(String text) {
        int width = 36; 
        int padding = (width - text.length()) / 2;
        StringBuilder paddedText = new StringBuilder();
        for (int i = 0; i < padding; i++) {
            paddedText.append(" ");
        }
        paddedText.append(text);
        return paddedText.toString();
    }
}