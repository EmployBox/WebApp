package isel.ps.employbox.model.input;

import isel.ps.employbox.model.entities.Application;
import isel.ps.employbox.model.entities.JobExperience;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

public class InJob {
    private long jobID;
    private String title;
    private long accountId;
    private List<JobExperience> experiences = Collections.emptyList();
    private List<Application> applications = Collections.emptyList();
    private String address;
    private int wage;
    private String description;
    private String schedule;
    private Timestamp offerBeginDate;
    private Timestamp offerEndDate;
    private String offerType;
    private String country;
    private String district;
    private double longitude;
    private double latitude;
    private long version;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getJobID() {
        return jobID;
    }

    public void setJobID(long jobID) {
        this.jobID = jobID;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public List<JobExperience> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<JobExperience> experiences) {
        this.experiences = experiences;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getWage() {
        return wage;
    }

    public void setWage(int wage) {
        this.wage = wage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public Timestamp getOfferBeginDate() {
        return offerBeginDate;
    }

    public void setOfferBeginDate(Timestamp offerBeginDate) {
        this.offerBeginDate = offerBeginDate;
    }

    public Timestamp getOfferEndDate() {return offerEndDate;}

    public void setOfferEndDate(Timestamp offerEndDate) {
        this.offerEndDate = offerEndDate;
    }

    public String getOfferType() {
        return offerType;
    }

    public void setOfferType(String offerType) {
        this.offerType = offerType;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }
}
