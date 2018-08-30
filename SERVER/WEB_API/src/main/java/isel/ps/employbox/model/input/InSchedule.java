package isel.ps.employbox.model.input;

import java.time.Instant;

public class InSchedule{
    private long scheduleId;
    private long jobId;
    private long accountId;
    private Instant date;
    private Instant startHour;
    private Instant endHour;
    private String repeats;
    private int version;


    public long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Instant getStartHour() {
        return startHour;
    }

    public void setStartHour(Instant startHour) {
        this.startHour = startHour;
    }

    public Instant getEndHour() {
        return endHour;
    }

    public void setEndHour(Instant endHour) {
        this.endHour = endHour;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public long getJobId() {
        return jobId;
    }

    public void setJobId(long jobId) {
        this.jobId = jobId;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public String getRepeats() {
        return this.repeats;
    }

    public void setRepeats(String repeats) {
        this.repeats = repeats;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }
}