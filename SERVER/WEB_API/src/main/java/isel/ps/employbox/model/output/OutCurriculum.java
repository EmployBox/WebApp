package isel.ps.employbox.model.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import isel.ps.employbox.controllers.CurriculumController;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class OutCurriculum extends ResourceSupport {

    @JsonProperty
    private final long id;

    @JsonProperty
    private final String title;

    @JsonProperty
    private final List<OutExperience> experiences;

    @JsonProperty
    private final List<OutPreviousJobs> previousJobs;

    @JsonProperty
    private final List<OutProject> projects;

    @JsonProperty
    private final List<OutAcademicBackground> academicBackground;

    public OutCurriculum(
            long userId,
            long id,
            String title,
            List<OutExperience> experiences,
            List<OutPreviousJobs> previousJobs,
            List<OutProject> projects,
            List<OutAcademicBackground> academicBackground
    ){
        this.id = id;
        this.title = title;
        this.experiences = experiences;
        this.previousJobs = previousJobs;
        this.projects = projects;
        this.academicBackground = academicBackground;
        this.add( linkTo (CurriculumController.class, userId).slash(id).withSelfRel());
    }

    public static class OutExperience {
        @JsonProperty
        private final String competence;

        @JsonProperty
        private final int year;

        public OutExperience(String competence, int year){

            this.competence = competence;
            this.year = year;
        }
    }

    public static class OutPreviousJobs {
        @JsonProperty
        private final String companyName;

        @JsonProperty
        private final String beginDate;

        @JsonProperty
        private final String endDate;

        @JsonProperty
        private final String workload;

        @JsonProperty
        private final String role;

        public OutPreviousJobs(String companyName, String beginDate, String endDate, String workload, String role){
            this.companyName = companyName;
            this.beginDate = beginDate;
            this.endDate = endDate;
            this.workload = workload;
            this.role = role;
        }
    }

    public static class OutProject {
        @JsonProperty
        private final String name;

        @JsonProperty
        private final String description;

        public OutProject(String name, String description){
            this.name = name;
            this.description = description;
        }
    }

    public static class OutAcademicBackground {
        @JsonProperty
        private final String institution;

        @JsonProperty
        private final String degree;

        @JsonProperty
        private final String studyArea;

        @JsonProperty
        private final String beginDate;

        @JsonProperty
        private final String endDate;

        public OutAcademicBackground(String institution, String degree, String studyArea, String beginDate, String endDate){
            this.institution = institution;
            this.degree = degree;
            this.studyArea = studyArea;
            this.beginDate = beginDate;
            this.endDate = endDate;
        }
    }
}
