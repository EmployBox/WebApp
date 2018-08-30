package isel.ps.employbox.model.entities;


import com.github.jayield.rapper.DomainObject;
import com.github.jayield.rapper.annotations.ColumnName;
import com.github.jayield.rapper.annotations.Id;
import com.github.jayield.rapper.annotations.Version;
import com.github.jayield.rapper.mapper.externals.Foreign;
import com.github.jayield.rapper.unitofwork.UnitOfWork;
import isel.ps.employbox.exceptions.ResourceNotFoundException;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static com.github.jayield.rapper.mapper.MapperRegistry.getMapper;


public class Job implements DomainObject<Long> {

    @Id(isIdentity =  true)
    private final long jobId;
    private final String title;
    private final String address;
    private final int wage;
    private final String description;
    private final Timestamp offerBeginDate;
    private final Timestamp offerEndDate;
    private final String offerType;
    @Version
    private final long version;
    @ColumnName(name = "accountId")
    private Foreign<Account,Long> account;
    @ColumnName(foreignName = "jobId")
    private Function<UnitOfWork, CompletableFuture<List<Application>>> applications;
    @ColumnName(foreignName = "jobId")
    private Function<UnitOfWork, CompletableFuture<List<JobExperience>>> experiences;
    @ColumnName(foreignName = "jobId")
    private Function<UnitOfWork, CompletableFuture<List<Schedule>>> schedules;

    public Job(){
        title = null;
        account = null;
        address = null;
        wage = 0;
        description = null;
        offerBeginDate = null;
        offerEndDate = null;
        offerType = null;
        applications = null;
        experiences = null;
        version = 0;
        jobId = 0;
    }

    public Job(
            long id,
            String title,
            String address,
            int wage,
            String description,
            String schedule,
            Timestamp offerBeginDate,
            Timestamp offerEndDate,
            String offerType,
            long version
    ) {
        this.jobId = id;
        this.title = title;
        this.address = address;
        this.wage = wage;
        this.description = description;
        this.offerBeginDate = offerBeginDate;
        this.offerEndDate = offerEndDate;
        this.offerType = offerType;
        this.version = version;
    }

    public Job(
            long accountId,
            long jobId,
            String title,
            String address,
            int wage,
            String description,
            Timestamp offerBeginDate,
            Timestamp offerEndDate,
            String offerType,
            List<Application> applications,
            List<JobExperience> experiences,
            List<Schedule> schedules,
            long version
    ) {
        UnitOfWork unitOfWork = new UnitOfWork();
        this.account = new Foreign(accountId, unit -> getMapper(Account.class, unitOfWork).findById( accountId)
                .thenCompose( res -> unitOfWork.commit().thenApply( __-> res))
                .thenApply(account1 -> account1.orElseThrow(() -> new ResourceNotFoundException("Account not Found"))));
        this.jobId = jobId;
        this.title = title;
        this.address = address;
        this.wage = wage;
        this.description = description;
        this.offerBeginDate = offerBeginDate;
        this.offerEndDate = offerEndDate;
        this.offerType = offerType;
        this.applications = (__)-> CompletableFuture.completedFuture(applications);
        this.experiences = (__)-> CompletableFuture.completedFuture(experiences);
        this.schedules = (__)-> CompletableFuture.completedFuture(schedules);
        this.version = version;
    }

    @Override
    public Long getIdentityKey() {
        return jobId;
    }

    public long getVersion() {
        return version;
    }

    public int getWage() {
        return wage;
    }

    public String getDescription() {
        return description;
    }

    public Timestamp getOfferBeginDate() {
        return offerBeginDate;
    }

    public Timestamp getOfferEndDate() {
        return offerEndDate;
    }

    public String getOfferType() {
        return offerType;
    }

    public String getAddress() {
        return address;
    }

    public String getTitle() {
        return title;
    }

    public Function<UnitOfWork, CompletableFuture<List<JobExperience>>> getExperiences() {
        return experiences;
    }

    public Function<UnitOfWork, CompletableFuture<List<Application>>> getApplications() {
        return applications;
    }

    public Foreign<Account, Long> getAccount() {
        return account;
    }

    public Function<UnitOfWork, CompletableFuture<List<Schedule>>> getSchedules() {
        return schedules;
    }
}
