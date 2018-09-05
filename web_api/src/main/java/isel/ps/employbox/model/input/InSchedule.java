package isel.ps.employbox.model.input;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

public class InSchedule{
    private long scheduleId;
    private long jobId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private Timestamp date;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private Timestamp startHour;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private Timestamp endHour;

    private String repeats;
    private int version;


    public long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Timestamp getStartHour() {
        return startHour;
    }

    public void setStartHour(Timestamp startHour) {
        this.startHour = startHour;
    }

    public Timestamp getEndHour() {
        return endHour;
    }

    public void setEndHour(Timestamp endHour) {
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

    public String getRepeats() {
        return this.repeats;
    }

    public void setRepeats(String repeats) {
        this.repeats = repeats;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}