package isel.ps.employbox.model.input;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class InCurriculum {
    private long accountId;
    private long curriculumId;
    private String title;
    private List<Experience> experiences;
    private List<PreviousJobs> previousJobs;
    private List<Project> projects;
    private List<AcademicBackground> academicBackground;

    public long getAccountId() {
        return accountId;
    }

    public long getCurriculumId() {
        return curriculumId;
    }

    public String getTitle() {
        return title;
    }

    public List<Experience> getExperiences() {
        return experiences;
    }

    public List<PreviousJobs> getPreviousJobs() {
        return previousJobs;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public List<AcademicBackground> getAcademicBackground() {
        return academicBackground;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public void setCurriculumId(long curriculumId) {
        this.curriculumId = curriculumId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setExperiences(List<Experience> experiences) {
        this.experiences = experiences;
    }

    public void setPreviousJobs(List<PreviousJobs> previousJobs) {
        this.previousJobs = previousJobs;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public void setAcademicBackground(List<AcademicBackground> academicBackground) {
        this.academicBackground = academicBackground;
    }

    private class Experience {
        private final String competence;
        private final int year;

        public Experience(String competence, int year){

            this.competence = competence;
            this.year = year;
        }

        public String getCompetence() {
            return competence;
        }

        public int getYear() {
            return year;
        }
    }

    private class PreviousJobs {
        private final String companyName;
        private final String beginDate;
        private final String endDate;
        private final String workload;
        private final String role;

        public PreviousJobs(String companyName, String beginDate, String endDate, String workload, String role){
            this.companyName = companyName;
            this.beginDate = beginDate;
            this.endDate = endDate;
            this.workload = workload;
            this.role = role;
        }

        @JsonProperty("company-name")
        public String getCompanyName() {
            return companyName;
        }

        @JsonProperty("begin-date")
        public String getBeginDate() {
            return beginDate;
        }

        @JsonProperty("end-date")
        public String getEndDate() {
            return endDate;
        }

        public String getWorkload() {
            return workload;
        }

        public String getRole() {
            return role;
        }
    }

    private class Project {
        private final String name;
        private final String description;

        public Project(String name, String description){
            this.name = name;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }
    }

    private class AcademicBackground {
        private final String institution;
        private final String degree;
        private final String studyArea;
        private final String beginDate;
        private final String endDate;

        public AcademicBackground(String institution, String degree, String studyArea, String beginDate, String endDate){

            this.institution = institution;
            this.degree = degree;
            this.studyArea = studyArea;
            this.beginDate = beginDate;
            this.endDate = endDate;
        }

        public String getInstitution() {
            return institution;
        }

        public String getDegree() {
            return degree;
        }

        public String getStudyArea() {
            return studyArea;
        }

        @JsonProperty("begin-date")
        public String getBeginDate() {
            return beginDate;
        }

        @JsonProperty("end-date")
        public String getEndDate() {
            return endDate;
        }
    }
}
