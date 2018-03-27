package isel.ps.employbox.api.model.output;

import java.sql.Date;
import java.util.List;

public class OutJob {
    private final long accountID;
    private final String title;
    private final List<OutExperience> experiences;
    private final String address;
    private final double wage;
    private final String description;
    private final String schedule;
    private final Date offerBeginDate;
    private final Date offerEndDate;
    private final String offerType;
    private final String applications_url;

    public OutJob(long accountID, String title, List<OutExperience> experiences, String address, double wage, String description, String schedule, Date offerBeginDate, Date offerEndDate, String offerType) {
        this.accountID = accountID;
        this.title = title;
        this.experiences = experiences;
        this.address = address;
        this.wage = wage;
        this.description = description;
        this.schedule = schedule;
        this.offerBeginDate = offerBeginDate;
        this.offerEndDate = offerEndDate;
        this.offerType = offerType;
        this.applications_url = String.format("/account/user/%d/applications", accountID);
    }

    public long getAccountID() {
        return accountID;
    }

    public List<OutExperience> getExperiences() {
        return experiences;
    }

    public String getAddress() {
        return address;
    }

    public double getWage() {
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

    public String getApplications_url() {
        return applications_url;
    }

    public String getTitle() {
        return title;
    }

    public class OutExperience {
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
