package isel.ps.employbox.model.entities.jobs;

import com.github.jayield.rapper.DomainObject;
import com.github.jayield.rapper.annotations.ColumnName;
import com.github.jayield.rapper.annotations.Id;
import com.github.jayield.rapper.annotations.Version;
import com.github.jayield.rapper.mapper.externals.Foreign;
import com.github.jayield.rapper.unitofwork.UnitOfWork;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.model.entities.Account;
import isel.ps.employbox.model.entities.Curriculum;

import java.sql.Timestamp;
import java.time.Instant;

import static com.github.jayield.rapper.mapper.MapperRegistry.getMapper;

public class Application implements DomainObject<Long> {

    @Id(isIdentity = true)
    private final long applicationId;
    private final Instant datetime;

    @Version
    private final long version;

    @ColumnName(name = "jobId")
    private final Foreign<Job, Long> job;

    @ColumnName(name = "accountId")
    private final Foreign<Account,Long> account;

    @ColumnName(name = "curriculumId")
    private final Foreign<Curriculum,Long> curriculum;

    public Application(){
        account = null;
        job = null;
        curriculum = null;
        datetime = null;
        version = 0;
        applicationId = 0;
    }

    public Application(
            long applicationId,
            long accountId,
            long jobId,
            long curriculumId,
            Timestamp datetime,
            long version) {
            UnitOfWork unitOfWork = new UnitOfWork();

        this.applicationId = applicationId;
        if(datetime != null)
            this.datetime = datetime.toInstant();
        else this.datetime = null;

        this.version = version;

        this.account = new Foreign<>(accountId, unit -> getMapper(Account.class, unitOfWork).findById( accountId)
                .thenCompose( res -> unitOfWork.commit().thenApply( __-> res))
                .thenApply(account1 -> account1.orElseThrow(() -> new ResourceNotFoundException("Account not Found"))));

        this.job = new Foreign<>(jobId, unit -> getMapper(Job.class, unitOfWork).findById( jobId)
                .thenCompose( res -> unitOfWork.commit().thenApply( __-> res))
                .thenApply(job1 -> job1.orElseThrow(() -> new ResourceNotFoundException("Job not Found"))));


        this.curriculum = new Foreign<>(curriculumId, unit -> getMapper(Curriculum.class, unitOfWork).findById(curriculumId)
                .thenCompose(res -> unitOfWork.commit().thenApply(__ -> res))
                .thenApply(curriculum -> curriculum.orElseThrow(() -> new ResourceNotFoundException("Curriculum not Found"))));
    }

    @Override
    public Long getIdentityKey() {
        return applicationId;
    }

    public long getVersion() {
        return version;
    }

    public Instant getDatetime() {
        return datetime;
    }

    public Foreign<Job, Long> getJob() {
        return job;
    }

    public Foreign<Account, Long> getAccount() {
        return account;
    }

    public Foreign<Curriculum, Long> getCurriculum() {
        return curriculum;
    }
}
