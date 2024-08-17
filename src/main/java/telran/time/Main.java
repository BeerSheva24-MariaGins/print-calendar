package telran.time;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.util.Locale;

record MonthYear(int month, int year) {}

public class Main {

	private static final int TITLE_OFFSET = 5;
	private static final int COLUMN_WIDTH = 4;
	private static DayOfWeek[] weekDays = DayOfWeek.values();
	public static void main(String[] args)  {
		try {
			MonthYear monthYear = getMonthYear(args);
			weekDays = getWeekDays(args);
			printCalendar(monthYear);
		} catch (RuntimeException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private static  MonthYear getMonthYear(String[] args) throws Exception{
		int monthNumber = getMonth(args);
		int year = getYear(args);
		return new MonthYear(monthNumber, year);
	}

	private static int getYear(String[] args) throws Exception {
		int year = args.length < 2 ? getCurrentYear() : getYear(args[1]);
		return year;
	}

	private static int getYear(String yearStr) throws Exception {
		try {
			int res = Integer.parseInt(yearStr);
			return res;
		} catch (NumberFormatException e) {
			throw new Exception("year must be an integer number");
		}
		
	}

	private static int getCurrentYear() {
		
		return LocalDate.now().getYear();
	}

	private static int getMonth(String[] args) throws Exception{
		int month = args.length == 0 ? getCurrentMonth() : getMonthNumber(args[0]);
		return month;
	}

	private static int getMonthNumber(String monthStr)throws Exception {
		try {
			int result = Integer.parseInt(monthStr);
			if (result < 1) {
				throw new Exception("Month cannot be less than 1");
			}
			if(result > 12) {
				throw new Exception("Month cannot be greater than 12");
			}
			return result;
		} catch (NumberFormatException e) {
			throw new Exception("Month must be a number");
		}
	}

	private static int getCurrentMonth() {
		
		return LocalDate.now().get(ChronoField.MONTH_OF_YEAR);
	}

	private static void printCalendar(MonthYear monthYear) {
		printTitle(monthYear);
		printWeekDays();
		printDates(monthYear);
		
		
	}

	private static void printDates(MonthYear monthYear) {
		int lastDayOfMonth = getLastDayOfMonth(monthYear);
		int currentWeekDay = getFirstDayOfWeek(monthYear);
		int firstOffset = getOffset(currentWeekDay);
		System.out.printf("%s", " ".repeat(firstOffset));
		for(int day = 1; day <= lastDayOfMonth; day++) {
			System.out.printf("%" + COLUMN_WIDTH +"d", day);
			if(currentWeekDay == weekDays.length) {
				currentWeekDay = 0;
				System.out.println();
			}
			currentWeekDay++;
		}
		
	}

	private static int getOffset(int currentWeekDay) {
		
		return COLUMN_WIDTH * (currentWeekDay - 1);
	}

	private static int getFirstDayOfWeek(MonthYear monthYear) {
		LocalDate ld = LocalDate.of(monthYear.year(), monthYear.month(),
				1);
		int delta = getStartDaysDelta(weekDays[0], DayOfWeek.values()[0]);
		int res = ld.getDayOfWeek().plus(delta).getValue();
		return res;
	}

	private static int getStartDaysDelta(DayOfWeek customStartDay, DayOfWeek originalStartDay) {
		
		return 7 - (customStartDay.getValue() - originalStartDay.getValue());
	}

	private static int getLastDayOfMonth(MonthYear monthYear) {
		YearMonth ym = YearMonth.of(monthYear.year(), monthYear.month());
		return ym.lengthOfMonth();
	}

	private static void printWeekDays() {
		System.out.printf("%s", " ".repeat(1));
		for(DayOfWeek weekday: weekDays) {
			System.out.printf("%" + COLUMN_WIDTH +"s",weekday.getDisplayName(TextStyle.SHORT,
					Locale.forLanguageTag("en")));
			
		}
		System.out.println();
		
	}

	private static void printTitle(MonthYear monthYear) {
		String monthName = Month.of(monthYear.month())
		.getDisplayName(TextStyle.FULL, Locale.getDefault());
System.out.printf("\n%s%s %d\n"," ".repeat(TITLE_OFFSET), monthName, monthYear.year());
	}
    private static DayOfWeek[] getWeekDays(String[] args) throws Exception {
		DayOfWeek startWeekDay = args.length < 3 ? DayOfWeek.of(1) : getStartWeekDay(args[2]);
		return startWeekDay.getValue() == 1 ? DayOfWeek.values() : getWeekDays(startWeekDay);
	}

	private static DayOfWeek[] getWeekDays(DayOfWeek startWeekDay) {
		int nWeekDays = DayOfWeek.values().length;
		DayOfWeek [] res = new DayOfWeek[nWeekDays];
		res[0] = startWeekDay;
		for(int i = 0; i < nWeekDays; i++) {
			res[i] = startWeekDay.plus(i);
		}
		return res;
	}
	private static DayOfWeek getStartWeekDay(String dayStr) throws Exception{
		try {
			DayOfWeek res = DayOfWeek.valueOf(dayStr.toUpperCase());
			return res;
		} catch (Exception e) {
			throw new Exception("start week day must be an English full week day name");
		}
		
	}
    
}