package isel.ps.employbox.model.input;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

public class InJob {
    private long jobID;
    private String title;
    private long accountId;
    private List<InJobExperience> experiences = Collections.emptyList();
    private List<InApplication> applications = Collections.emptyList();
    private List<InSchedule> schedules = Collections.emptyList();
    private String address;
    private int wage;
    private String description;
    private Timestamp offerBeginDate;
    private Timestamp offerEndDate;
    private String offerType;
    private String country;
    private String district;
    private String city;
    private double longitude;
    private double latitude;
    private String type;
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

    public List<InJobExperience> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<InJobExperience> experiences) {
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

    public List<InApplication> getApplications() {
        return applications;
    }

    public void setApplications(List<InApplication> applications) {
        this.applications = applications;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public List<InSchedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<InSchedule> schedules) {
        this.schedules = schedules;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
