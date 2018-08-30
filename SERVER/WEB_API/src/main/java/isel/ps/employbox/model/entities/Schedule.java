package isel.ps.employbox.model.entities;

import com.github.jayield.rapper.DomainObject;
import com.github.jayield.rapper.annotations.ColumnName;
import com.github.jayield.rapper.annotations.Id;
import com.github.jayield.rapper.annotations.Version;
import com.github.jayield.rapper.mapper.externals.Foreign;
import com.github.jayield.rapper.unitofwork.UnitOfWork;
import isel.ps.employbox.exceptions.ResourceNotFoundException;

import java.time.Instant;

import static com.github.jayield.rapper.mapper.MapperRegistry.getMapper;

public class Schedule implements DomainObject<Long> {

    @Id(isIdentity = true)
    private final long scheduleId;

    private final Instant startDate;
    private final Instant endDate;
    private final Instant startHour;
    private final Instant endHour;
    private final String scheduleType;

    @ColumnName(name = "accountId")
    private Foreign<Account,Long> account;

    @ColumnName(name = "jobId")
    private Foreign<Job, Long> job;

    @Version
    private final long version;

    public Schedule(){
        this.scheduleId = 0;
        this.startDate = null;
        this.endDate = null;
        this.startHour = null;
        this.endHour = null;
        this.scheduleType = null;
        this.account = null;
        this.job = null;
        this.version = 0;
    }

    public Schedule(long scheduleId,
                    long accountId,
                    long jobId,
                    Instant startDate,
                    Instant endDate,
                    Instant startHour,
                    Instant endHour,
                    String scheduleType,
                    int version)
    {
        UnitOfWork unitOfWork = new UnitOfWork();
        this.account = new Foreign(accountId, unit -> getMapper(Account.class, unitOfWork).findById( accountId)
                .thenCompose( res -> unitOfWork.commit().thenApply( __-> res))
                .thenApply(account1 -> account1.orElseThrow(() -> new ResourceNotFoundException("Account not Found"))));

        this.job = new Foreign(accountId, unit -> getMapper(Job.class, unitOfWork).findById( jobId)
                .thenCompose( res -> unitOfWork.commit().thenApply( __-> res))
                .thenApply(job1 -> job1.orElseThrow(() -> new ResourceNotFoundException("Job not Found"))));

        this.scheduleId = scheduleId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startHour = startHour;
        this.endHour = endHour;
        this.scheduleType = scheduleType;
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

    public Instant getStartDate() {
        return startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public Instant getStartHour() {
        return startHour;
    }

    public Instant getEndHour() {
        return endHour;
    }

    public String getScheduleType() {
        return scheduleType;
    }

    public Foreign<Account, Long> getAccount() {
        return account;
    }

    public Foreign<Job, Long> getJob() {
        return job;
    }

    public void setJob(Foreign<Job, Long> job) {
        this.job = job;
    }

    public void setAccount(Foreign<Account, Long> account) {
        this.account = account;
    }
}
