package isel.ps.EmployBox.dal.domainModel;

import isel.ps.EmployBox.dal.util.Streamable;

import java.sql.Date;
import java.util.stream.Stream;

public class Job extends DomainObject<Long> {
    @ID(isIdentity = true)
    private long jobId;
    private final long accountID;
    private final String address;
    private final int wage;
    private final String description;
    private final String schedule;
    private final Date offerBeginDate;
    private final Date offerEndDate;
    private final String offerType; //TODO change to enum

    private final Streamable<Application> applications;
    private final Streamable<JobExperience> experiences;

    public Job(
            long id,
            long accountID,
            String address,
            int wage,
            String description,
            String schedule,
            Date offerBeginDate,
            Date offerEndDate,
            String offerType,
            long version,
            Streamable<Application> applications,
            Streamable<JobExperience> experiences
    ) {
        super(id, version);
        this.jobId = id;
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

    public static Job create(
            long accountID,
            String address,
            int wage,
            String description,
            String schedule,
            Date offerBeginDate,
            Date offerEndDate,
            String offerType,
            Streamable<Application> applications,
            Streamable<JobExperience> experiences
    ) {
        Job job = new Job(defaultKey, accountID, address, wage, description, schedule, offerBeginDate, offerEndDate, offerType, 0, applications, experiences);
        job.markNew();
        return job;
    }

    public static Job load(
            long id,
            long accountID,
            String address,
            int wage,
            String description,
            String schedule,
            Date offerBeginDate,
            Date offerEndDate,
            String offerType,
            long version,
            Streamable<Application> applications,
            Streamable<JobExperience> experiences
    ) {
        Job job = new Job(id, accountID, address, wage, description, schedule, offerBeginDate, offerEndDate, offerType, version, applications, experiences);
        job.markClean();
        return job;
    }

    public static Job update(
            Job job,
            long accountID,
            String address,
            int wage,
            String description,
            String schedule,
            Date offerBeginDate,
            Date offerEndDate,
            String offerType,
            Streamable<Application> applications,
            Streamable<JobExperience> experiences
    ){
        job.markToBeDirty();
        Job newJob = new Job(job.getAccountID(), accountID, address, wage, description, schedule, offerBeginDate, offerEndDate, offerType, job.getNextVersion(), applications, experiences);
        newJob.markDirty();
        return newJob;
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

    public Stream<Application> getApplications() {
        return applications.get().stream();
    }

    public Stream<JobExperience> getExperiences() {
        return experiences.get().stream();
    }
}
