package isel.ps.employbox.model.entities.jobs;

import com.github.jayield.rapper.DomainObject;
import com.github.jayield.rapper.annotations.ColumnName;
import com.github.jayield.rapper.annotations.Id;
import com.github.jayield.rapper.annotations.Version;
import com.github.jayield.rapper.mapper.externals.Foreign;
import com.github.jayield.rapper.unitofwork.UnitOfWork;
import isel.ps.employbox.exceptions.ResourceNotFoundException;

import java.sql.Timestamp;
import java.time.Instant;

import static com.github.jayield.rapper.mapper.MapperRegistry.getMapper;

public class Schedule implements DomainObject<Long> {
    @Id(isIdentity = true)
    private final long scheduleId;
    private final Instant date;
    private final Instant startHour;
    private final Instant endHour;
    private final String repeats;

    @ColumnName(name = "jobId")
    private Foreign<Job, Long> job;

    @Version
    private final long version;

    public Schedule(){
        this.scheduleId = 0;
        this.startHour = null;
        this.endHour = null;
        this.job = null;
        this.version = 0;
        this.date = null;
        this.repeats = null;
    }

    public Schedule(long scheduleId,
                    long jobId,
                    Timestamp date,
                    Timestamp startHour,
                    Timestamp endHour,
                    String repeats,
                    int version)
    {
        this.repeats = repeats;
        UnitOfWork unitOfWork = new UnitOfWork();
        this.job = new Foreign<>(jobId, unit -> getMapper(Job.class, unitOfWork).findById( jobId)
                .thenCompose( res -> unitOfWork.commit().thenApply(aVoid -> res))
                .thenApply(job1 -> job1.orElseThrow(() -> new ResourceNotFoundException("Job not Found"))));

        this.scheduleId = scheduleId;
        if(date != null)
            this.date = date.toInstant();
        else
            this.date = null;
        if(startHour != null)
            this.startHour = startHour.toInstant();
        else
            this.startHour = null;
        if(endHour != null)
            this.endHour = endHour.toInstant();
        else
            this.endHour = null;
        this.version = version;
    }

    @Override
    public Long getIdentityKey() {
        return scheduleId;
    }

    @Override
    public long getVersion() {
        return version;
    }

    public Instant getStartHour() {
        return startHour;
    }

    public Instant getEndHour() {
        return endHour;
    }

    public Foreign<Job, Long> getJob() {
        return job;
    }

    public void setJob(Foreign<Job, Long> job) {
        this.job = job;
    }

    public Instant getDate() {
        return this.date;
    }

    public String getRepeats() {
        return repeats;
    }
}
