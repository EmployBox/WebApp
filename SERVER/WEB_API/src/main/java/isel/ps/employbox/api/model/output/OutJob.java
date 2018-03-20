package isel.ps.employbox.api.model.output;

import java.sql.Date;
import java.util.List;

public class OutJob {
    private long accountID;
    private List<Experience> experiences;
    private String address;
    private double wage;
    private String description;
    private String schedule;
    private Date offerBeginDate;
    private Date offerEndDate;
    private String offerType;
    private String applications_url;

    public long getAccountID() {
        return accountID;
    }

    public void setAccountID(long accountID) {
        this.accountID = accountID;
    }

    public List<Experience> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<Experience> experiences) {
        this.experiences = experiences;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getWage() {
        return wage;
    }

    public void setWage(double wage) {
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

    public Date getOfferBeginDate() {
        return offerBeginDate;
    }

    public void setOfferBeginDate(Date offerBeginDate) {
        this.offerBeginDate = offerBeginDate;
    }

    public Date getOfferEndDate() {
        return offerEndDate;
    }

    public void setOfferEndDate(Date offerEndDate) {
        this.offerEndDate = offerEndDate;
    }

    public String getOfferType() {
        return offerType;
    }

    public void setOfferType(String offerType) {
        this.offerType = offerType;
    }

    public String getApplications_url() {
        return applications_url;
    }

    public void setApplications_url(String applications_url) {
        this.applications_url = applications_url;
    }

    private class Experience {
        private String competence;
        private int years;

        public String getCompetence() {
            return competence;
        }

        public void setCompetence(String competence) {
            this.competence = competence;
        }

        public int getYears() {
            return years;
        }

        public void setYears(int years) {
            this.years = years;
        }
    }
}
