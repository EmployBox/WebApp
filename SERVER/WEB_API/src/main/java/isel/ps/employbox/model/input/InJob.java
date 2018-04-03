package isel.ps.employbox.model.input;

import java.sql.Date;
import java.util.List;

public class InJob {
    private long jobID;
    private String title;
    private long accountID;
    private List<InExperience> experiences;
    private String address;
    private int wage;
    private String description;
    private String schedule;
    private Date offerBeginDate;
    private Date offerEndDate;
    private String offerType;

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

    public long getAccountID() {
        return accountID;
    }

    public void setAccountID(long accountID) {
        this.accountID = accountID;
    }

    public List<InExperience> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<InExperience> experiences) {
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

    public Date getOfferBeginDate() {
        return offerBeginDate;
    }

    public void setOfferBeginDate(Date offerBeginDate) {
        this.offerBeginDate = offerBeginDate;
    }

    public Date getOfferEndDate() {return offerEndDate;}

    public void setOfferEndDate(Date offerEndDate) {
        this.offerEndDate = offerEndDate;
    }

    public String getOfferType() {
        return offerType;
    }

    public void setOfferType(String offerType) {
        this.offerType = offerType;
    }

    public class InExperience {
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
