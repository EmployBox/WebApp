package isel.ps.employbox.model.input;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class InSchedule{
    private long scheduleId;
    private long jobId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date date;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private Date startHour;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private Date endHour;
    private String repeats;
    private int version;


    public long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Instant getStartHour() {
        return startHour.toInstant();
    }

    public void setStartHour(Date startHour) {
        this.startHour = startHour;
    }

    public Instant getEndHour() {
        return endHour.toInstant();
    }

    public void setEndHour(Date endHour) {
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

    public Instant getDate() {
        return date.toInstant();
    }

    public void setDate(Date date) {
        this.date = date;
    }
}