# Babysitter Kata

Simulates a babysitter getting paid for a night's work. It calculates the babysitter's pay.

I built it using *Gradle* and Java's LocalTime and LocalDateTime apis. 


Babysitter.compute(String family, LocalDateTime start, LocalDateTime end)

family is either a, b, or c (case insensitive)
times are only calculated to the minute (no need for milliseconds)

## The babysitter:

starts no earlier than 5:00PM
leaves no later than 4:00AM
only babysits for one family per night
gets paid for full hours (no fractional hours)
should be prevented from mistakes when entering times (e.g. end time before start time, or outside of allowable work hours)


## The job:

Pays different rates for each family (based on bedtimes, kids and pets, etc...)
Family A pays $15 per hour before 11pm, and $20 per hour the rest of the night
Family B pays $12 per hour before 10pm, $8 between 10 and 12, and $16 the rest of the night
Family C pays $21 per hour before 9pm, then $15 the rest of the night
The time ranges are the same as the babysitter (5pm through 4am)