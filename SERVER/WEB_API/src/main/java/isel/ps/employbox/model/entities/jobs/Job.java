package isel.ps.employbox.model.entities.jobs;

import com.github.jayield.rapper.DomainObject;
import com.github.jayield.rapper.annotations.ColumnName;
import com.github.jayield.rapper.annotations.Id;
import com.github.jayield.rapper.annotations.Version;
import com.github.jayield.rapper.mapper.externals.Foreign;
import com.github.jayield.rapper.unitofwork.UnitOfWork;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.model.binders.jobs.ApplicationBinder;
import isel.ps.employbox.model.binders.jobs.JobExperienceBinder;
import isel.ps.employbox.model.binders.jobs.ScheduleBinder;
import isel.ps.employbox.model.entities.Account;
import isel.ps.employbox.model.input.InApplication;
import isel.ps.employbox.model.input.InJobExperience;
import isel.ps.employbox.model.input.InSchedule;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    private final String type;

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
        type = null;
    }

    public Job(
            long id,
            String title,
            String address,
            int wage,
            String description,
            Timestamp offerBeginDate,
            Timestamp offerEndDate,
            String offerType,
            String type, long version
    ) {
        this.jobId = id;
        this.title = title;
        this.address = address;
        this.wage = wage;
        this.description = description;
        this.offerBeginDate = offerBeginDate;
        this.offerEndDate = offerEndDate;
        this.offerType = offerType;
        this.type = type;
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
            String type,
            List<InApplication> applications,
            List<InJobExperience> experiences,
            List<InSchedule> schedules,
            long version
    ) {
        this.type = type;
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
        this.applications = (__)-> {
            ApplicationBinder applicationBinder = new ApplicationBinder();

            List<Application> list = applicationBinder.bindInput(applications.stream().peek(inApplication -> inApplication.setJobId(this.jobId))).collect(Collectors.toList());
            return CompletableFuture.completedFuture(list);
        };
        this.experiences = (__)-> {
            JobExperienceBinder jobExperienceBinder = new JobExperienceBinder();

            List<JobExperience> list = jobExperienceBinder.bindInput(experiences.stream().peek(inJobExperience -> inJobExperience.setJobId(this.jobId))).collect(Collectors.toList());
            return CompletableFuture.completedFuture(list);
        };;
        this.schedules = (__)-> {
            ScheduleBinder scheduleBinder = new ScheduleBinder();

            List<Schedule> list = scheduleBinder.bindInput(schedules.stream().peek(inSchedule -> inSchedule.setJobId(this.jobId))).collect(Collectors.toList());
            return CompletableFuture.completedFuture(list);
        };;
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

    public String getType() {
        return type;
    }
}
