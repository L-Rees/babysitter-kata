package babysitter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Babysitter {
		
	private static final LocalTime LATEST_END_TIME = LocalTime.parse("04:01");
	private static final LocalTime EARLIEST_START_TIME = LocalTime.parse("17:00");
	


	public int compute(String family, LocalDateTime start, LocalDateTime end) throws InvalidTimesException {
		LocalTime endTime = end.toLocalTime();
		LocalTime startTime = start.toLocalTime(); // maybe start times always get rounded down and end times always get
													// rounded up?
		
		int pay = 0;

		validateTimes(start, end, endTime, startTime);
		long firstElapsedMinutes = 0;
		long secondElapsedMinutes = 0;
		long thirdElapsedMinutes = 0;

		if (family.equalsIgnoreCase("A") || family.equalsIgnoreCase("C")) {
			// Family A pays $15 per hour before 11pm, and $20 per hour the rest of the night
			// Family C pays $21 per hour before 9pm, then $15 the rest of the night
			
			LocalTime rateChangeTime = null;

			if (family.equalsIgnoreCase("A")) {
				rateChangeTime = LocalTime.parse("23:00");
			} else if (family.equalsIgnoreCase("C")) {
				rateChangeTime = LocalTime.parse("21:00");
			}

			if ((endTime.isBefore(rateChangeTime) || endTime.equals(rateChangeTime)) && endTime.isAfter(EARLIEST_START_TIME)) {
				firstElapsedMinutes = Duration.between(start, end).toMinutes() + 1;
			} else if (startTime.isAfter(rateChangeTime) || startTime.equals(rateChangeTime) || startTime.isBefore(LATEST_END_TIME)) {
				secondElapsedMinutes = Duration.between(start, end).toMinutes() + 1;
			} else if (startTime.isBefore(rateChangeTime) && (endTime.isAfter(rateChangeTime) || endTime.isBefore(LATEST_END_TIME))) {
				firstElapsedMinutes = Duration.between(startTime, rateChangeTime).toMinutes() + 1;
				if (endTime.isBefore(LATEST_END_TIME)) {
					secondElapsedMinutes = Duration.between(rateChangeTime, LocalTime.MAX).toMinutes() + 1;
					secondElapsedMinutes += Duration.between(LocalTime.MIDNIGHT, endTime).toMinutes() + 1;
				} else if (endTime.isBefore(LocalTime.MAX)) {
					secondElapsedMinutes = Duration.between(rateChangeTime, endTime).toMinutes() + 1;
				}
			}
			if (family.equalsIgnoreCase("A")) {
				pay = (int) ((firstElapsedMinutes / 60 * 15) + (secondElapsedMinutes / 60 * 20));
			} else if (family.equalsIgnoreCase("C")) {
				pay = (int) ((firstElapsedMinutes / 60 * 21) + (secondElapsedMinutes / 60 * 15));
			}

		} else if (family.equalsIgnoreCase("B")) {
			// Family B pays $12 per hour before 10pm, $8 between 10 and 12, and $16 the
			// rest of the night
			LocalTime rateChangeTime1 = LocalTime.parse("22:00");
			LocalTime rateChangeTime2 = LocalTime.MIDNIGHT;

			if ((endTime.isBefore(rateChangeTime1) || endTime.equals(rateChangeTime1)) && endTime.isAfter(EARLIEST_START_TIME)) {
				firstElapsedMinutes = Duration.between(start, end).toMinutes() + 1;

			} else if (startTime.isBefore(LATEST_END_TIME)) {
				thirdElapsedMinutes = Duration.between(start, end).toMinutes() + 1;

			} else if (startTime.isAfter(rateChangeTime1) || startTime.equals(rateChangeTime1)) {
				if (endTime.isBefore(LocalTime.MAX) && endTime.isAfter(rateChangeTime1)) {
					secondElapsedMinutes = Duration.between(start, end).toMinutes() + 1;
				} else if (endTime.equals(rateChangeTime2)) {
					secondElapsedMinutes = Duration.between(startTime, LocalTime.MAX).toMinutes() + 1;
				} else if (endTime.isBefore(LATEST_END_TIME)) {
					secondElapsedMinutes = Duration.between(startTime, LocalTime.MAX).toMinutes() + 1;
					thirdElapsedMinutes = Duration.between(LocalTime.MIDNIGHT, endTime).toMinutes() + 1;
				}

			} else if (startTime.isBefore(rateChangeTime1)) {

				if ((endTime.isAfter(rateChangeTime1))) {
					firstElapsedMinutes = Duration.between(startTime, rateChangeTime1).toMinutes() + 1;
					secondElapsedMinutes = Duration.between(rateChangeTime1, endTime).toMinutes() + 1;
				} else if (endTime.equals(rateChangeTime2)) {
					firstElapsedMinutes = Duration.between(startTime, rateChangeTime1).toMinutes() + 1;
					secondElapsedMinutes = Duration.between(rateChangeTime1, LocalTime.MAX).toMinutes() + 1;
				} else if (endTime.isBefore(LATEST_END_TIME) || endTime.equals(LATEST_END_TIME)) {
					firstElapsedMinutes = Duration.between(startTime, rateChangeTime1).toMinutes() + 1;
					secondElapsedMinutes = Duration.between(rateChangeTime1, LocalTime.MAX).toMinutes() + 1;
					thirdElapsedMinutes += Duration.between(LocalTime.MIDNIGHT, endTime).toMinutes() + 1;
				} else if (endTime.isBefore(LocalTime.MAX)) {
					firstElapsedMinutes = Duration.between(startTime, rateChangeTime1).toMinutes() + 1;
					secondElapsedMinutes = Duration.between(rateChangeTime1, endTime).toMinutes() + 1;
				}
			}
			pay = (int) ((firstElapsedMinutes / 60 * 12) + (thirdElapsedMinutes / 60 * 16)
					+ (secondElapsedMinutes / 60 * 8));
		}
		return pay;
	}



	private void validateTimes(LocalDateTime start, LocalDateTime end, LocalTime endTime, LocalTime startTime)
			throws InvalidTimesException {
		if (end.isBefore(start) || (endTime.isAfter(LATEST_END_TIME) && endTime.isBefore(EARLIEST_START_TIME))
				|| (startTime.isBefore(EARLIEST_START_TIME) && startTime.isAfter(LATEST_END_TIME))
				|| (Duration.between(start, end).toHours() > 11)) {
			throw new InvalidTimesException();
		}
	}

}
