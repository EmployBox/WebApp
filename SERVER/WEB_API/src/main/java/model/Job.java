package model;

import javafx.util.Pair;

import java.sql.Date;
import java.util.stream.Stream;

public class Job extends DomainObject {
    private long accountID;
    private String address;
    private int wage;
    private String description;
    private String schedule;
    private Date offerBeginDate;
    private Date offerEndDate;
    private String offerType; //TODO change to enum

    private Stream<Experience> experiences;
    private Stream<Pair<User, Curriculum>> applications;

    private Job(long id,
                long accountID,
                String address,
                int wage,
                String description,
                String schedule,
                Date offerBeginDate,
                Date offerEndDate,
                String offerType,
                long version)
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
            long version)
    {
        Job job = new Job(-1, accountID, address, wage, description, schedule, offerBeginDate, offerEndDate, offerType, version);
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
            long version)
    {
        Job job = new Job(id, accountID, address, wage, description, schedule, offerBeginDate, offerEndDate, offerType, version);
        job.markClean();
        return job;
    }

    public long getAccountID() {
        return accountID;
    }

    public void setAccountID(long accountID) {
        markToBeDirty();
        this.accountID = accountID;
        markDirty();
    }

    public int getWage() {
        return wage;
    }

    public void setWage(int wage) {
        markToBeDirty();
        this.wage = wage;
        markDirty();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        markToBeDirty();
        this.description = description;
        markDirty();
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        markToBeDirty();
        this.schedule = schedule;
        markDirty();
    }

    public Date getOfferBeginDate() {
        return offerBeginDate;
    }

    public void setOfferBeginDate(Date offerBeginDate) {
        markToBeDirty();
        this.offerBeginDate = offerBeginDate;
        markDirty();
    }

    public Date getOfferEndDate() {
        return offerEndDate;
    }

    public void setOfferEndDate(Date offerEndDate) {
        markToBeDirty();
        this.offerEndDate = offerEndDate;
        markDirty();
    }

    public String getOfferType() {
        return offerType;
    }

    public void setOfferType(String offerType) {
        markToBeDirty();
        this.offerType = offerType;
        markDirty();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Stream<Experience> getExperiences() {
        return experiences;
    }

    public void setExperiences(Stream<Experience> experiences) {
        this.experiences = experiences;
    }

    public Stream<Pair<User, Curriculum>> getApplications() {
        return applications;
    }

    public void setApplications(Stream<Pair<User, Curriculum>> applications) {
        this.applications = applications;
    }
}
