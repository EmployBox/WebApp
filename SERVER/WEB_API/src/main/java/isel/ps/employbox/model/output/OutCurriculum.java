package isel.ps.employbox.model.output;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class OutCurriculum {

    private final long id;
    private final String title;
    private final List<Experience> experiences;
    private final List<PreviousJobs> previousJobs;
    private final List<Project> projects;
    private final List<AcademicBackground> academicBackground;

    public OutCurriculum(long id, String title, List<Experience> experiences, List<PreviousJobs> previousJobs, List<Project> projects, List<AcademicBackground> academicBackground){
        this.id = id;
        this.title = title;
        this.experiences = experiences;
        this.previousJobs = previousJobs;
        this.projects = projects;
        this.academicBackground = academicBackground;
    }

    public long getId() {
        return id;
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
