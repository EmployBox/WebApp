package isel.ps.employbox.model.entities.curricula.childs;


import com.github.jayield.rapper.DomainObject;
import com.github.jayield.rapper.Id;

import java.security.InvalidParameterException;
import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.List;

public class Schedule implements DomainObject<Long> {
    public static final String PARTIAL = "PARTIAL";
    public static final String FULLTIME = "FULLTIME";

    @Id(isIdentity = true)
    private final long scheduleId;

    private List<WorkingDay> workingDays = null;
    private final String avaiability;
    private final int version;

    public Schedule(long scheduleId, List<WorkingDay> workingDays, String avaiability,  int version) {
        this.scheduleId = scheduleId;
        this.avaiability = avaiability;
        this.version = version;
        if(avaiability == PARTIAL)
            this.workingDays = workingDays;
    }

    @Override
    public Long getIdentityKey() {
        return scheduleId;
    }

    @Override
    public long getVersion() {
        return version;
    }

    class WorkingDay {
        final List WEEK_DAYS = Arrays.asList(new DateFormatSymbols().getWeekdays());
        final String dayName;
        final List<AvaiabilityInterval> avaiabilityIntervals;

        WorkingDay(String dayName, List<AvaiabilityInterval> avaiabilityIntervals) {
            if(!WEEK_DAYS.contains(dayName.toUpperCase()))
                throw new InvalidParameterException("WRONG WEEK NAME");

            this.dayName = dayName;
            this.avaiabilityIntervals = avaiabilityIntervals;
        }
    }

    class AvaiabilityInterval{
        final int hourFrom;
        final int minutesFrom;
        final int hourTo;
        final int minutesTo;

        AvaiabilityInterval(int hourFrom, int minutesFrom, int hourTo, int minutesTo) {
            if(hourFrom < 0 || hourFrom > 23 || hourTo < hourFrom || hourTo > 23 || hourTo == hourFrom && minutesFrom >= minutesTo)
                throw new InvalidParameterException("Invalid time interval");
            this.hourFrom = hourFrom;
            this.minutesFrom = minutesFrom;
            this.hourTo = hourTo;
            this.minutesTo = minutesTo;
        }

        @Override
        public String toString(){
            return String.format("", hourFrom,":", minutesFrom, "  -  ", hourTo,":", minutesTo);
        }
    }
}