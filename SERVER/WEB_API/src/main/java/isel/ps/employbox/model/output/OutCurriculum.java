package isel.ps.employbox.model.output;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import isel.ps.employbox.controllers.curricula.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class OutCurriculum implements OutputDto {

    private final long userId;

    @JsonProperty
    private final long curriculumId;

    @JsonProperty
    private final String title;

    @JsonProperty
    private final _Links _links;

    public OutCurriculum(
            long userId,
            long curriculumId,
            String title
    ){
        this.userId = userId;
        this.curriculumId = curriculumId;
        this.title = title;
        this._links = new _Links();
    }

    @JsonIgnore
    @Override
    public CurriculumItemOutput getCollectionItemOutput() {
        return new CurriculumItemOutput(title);
    }

    class CurriculumItemOutput {
        @JsonProperty
        private String title;

        @JsonProperty
        private final _Links _links = new _Links();

        private CurriculumItemOutput(String title){
            this.title = title;
        }

        class _Links {
            @JsonProperty
            private Self self = new Self();

            private class Self{
                @JsonProperty
                final String href = HOSTNAME + linkTo (CurriculumController.class, userId).slash(curriculumId).withSelfRel().getHref();
            }
        }
    }

    private class _Links{
        @JsonProperty
        private Self self = new Self();

        @JsonProperty
        private Projects projects = new Projects();

        @JsonProperty
        private AcademicBackgrounds academicBackgrounds = new AcademicBackgrounds();

        @JsonProperty
        private PreviousJobs applications = new PreviousJobs();

        @JsonProperty
        private CurriculumExperiences chats = new CurriculumExperiences();


        private class Self{
            @JsonProperty
            final String href = HOSTNAME + linkTo (CurriculumController.class, userId).slash(curriculumId).withSelfRel().getHref();
        }

        private class Projects {
            @JsonProperty
            final String href = HOSTNAME + linkTo ( methodOn(ProjectsController.class).getProjects(userId,curriculumId, 0)).withRel("projects").getHref();
        }

        private class AcademicBackgrounds {
            @JsonProperty
            final String href = HOSTNAME + linkTo ( methodOn(AcademicBackgroundController.class).getAcademicBackground(userId,curriculumId, 0)).withRel("academicBackground").getHref();
        }

        private class PreviousJobs {
            @JsonProperty
            final String href = HOSTNAME +  linkTo ( methodOn(PreviousJobsController.class).getPreviousJobs(userId,curriculumId, 0,0)).withRel("previousJobs").getHref();
        }

        private class CurriculumExperiences {
            @JsonProperty
            final String href = HOSTNAME +  linkTo ( methodOn(CurriculumExperienceController.class).getCurriculumExperiences(userId,curriculumId, 0,0)).withRel("curriculumExperience").getHref();
        }
    }
}
