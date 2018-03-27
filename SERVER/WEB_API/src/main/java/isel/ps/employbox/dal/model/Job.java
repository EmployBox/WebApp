package isel.ps.employbox.dal.model;

import java.sql.Date;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class Job extends DomainObject<Long> {
    @ID(isIdentity = true)
    private long jobId;
    private final String title;
    private final long accountID;
    private final String address;
    private final int wage;
    private final String description;
    private final String schedule;
    private final Date offerBeginDate;
    private final Date offerEndDate;
    private final String offerType; //TODO change to enum

    private final Supplier<List<Application>> applications;
    private final Supplier<List<JobExperience>> experiences;

    public Job(
            long id,
            String title,
            long accountID,
            String address,
            int wage,
            String description,
            String schedule,
            Date offerBeginDate,
            Date offerEndDate,
            String offerType,
            long version,
            Supplier<List<Application>> applications,
            Supplier<List<JobExperience>> experiences
    ) {
        super(id, version);
        this.jobId = id;
        this.title = title;
        this.accountID = accountID;
        this.address = address;
        this.wage = wage;
        this.description = description;
        this.schedule = schedule;
        this.offerBeginDate = offerBeginDate;
        this.offerEndDate = offerEndDate;
        this.offerType = offerType;
        this.applications = applications;
        this.experiences = experiences;
    }

    public Job(
            long accountID,
            long jobId,
            String title,
            String address,
            int wage,
            String description,
            String schedule,
            Date offerBeginDate,
            Date offerEndDate,
            String offerType
    ){
        super((long) -1, -1);
        this.jobId = jobId;
        this.accountID = accountID;
        this.title = title;
        this.address = address;
        this.wage = wage;
        this.description = description;
        this.schedule = schedule;
        this.offerBeginDate = offerBeginDate;
        this.offerEndDate = offerEndDate;
        this.offerType = offerType;
        this.applications = Collections::emptyList;
        this.experiences = Collections::emptyList;
    }

    public long getAccountID() {
        return accountID;
    }

    public int getWage() {
        return wage;
    }

    public String getDescription() {
        return description;
    }

    public String getSchedule() {
        return schedule;
    }

    public Date getOfferBeginDate() {
        return offerBeginDate;
    }

    public Date getOfferEndDate() {
        return offerEndDate;
    }

    public String getOfferType() {
        return offerType;
    }

    public String getAddress() {
        return address;
    }

    public Supplier<List<Application>> getApplications() {
        return applications;
    }

    public Supplier<List<JobExperience>> getExperiences() {
        return experiences;
    }

    public long getJobId() {
        return jobId;
    }

    public String getTitle() {
        return title;
    }
}
