package model;

import javafx.util.Pair;
import util.Streamable;

import java.sql.Date;

public class Job extends DomainObject<Long> {
    private final long accountID;
    private final String address;
    private final int wage;
    private final String description;
    private final String schedule;
    private final Date offerBeginDate;
    private final Date offerEndDate;
    private final String offerType; //TODO change to enum

    private final Iterable<Experience> experiences;
    private final Iterable<Pair<User, Curriculum>> applications;

    private Job(long id,
                long accountID,
                String address,
                int wage,
                String description,
                String schedule,
                Date offerBeginDate,
                Date offerEndDate,
                String offerType,
                long version,
                Iterable<Experience> experiences,
                Iterable<Pair<User, Curriculum>> applications)
    {
        super(id, version);
        this.accountID = accountID;
        this.address = address;
        this.wage = wage;
        this.description = description;
        this.schedule = schedule;
        this.offerBeginDate = offerBeginDate;
        this.offerEndDate = offerEndDate;
        this.offerType = offerType;
        this.experiences = experiences;
        this.applications = applications;
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
            long version,
            Iterable<Experience> experiences,
            Iterable<Pair<User, Curriculum>> applications)
    {
        Job job = new Job(-1, accountID, address, wage, description, schedule, offerBeginDate, offerEndDate, offerType, version, experiences, applications);
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
            Iterable<Experience> experiences,
            Iterable<Pair<User, Curriculum>> applications)
    {
        Job job = new Job(id, accountID, address, wage, description, schedule, offerBeginDate, offerEndDate, offerType, version, experiences, applications);
        job.markClean();
        return job;
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

    public Iterable<Experience> getExperiences() {
        return experiences;
    }

    public Iterable<Pair<User, Curriculum>> getApplications() {
        return applications;
    }
}
