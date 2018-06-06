package isel.ps.employbox.model.output;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import isel.ps.employbox.controllers.CurriculumController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class OutProject implements OutputDto {

    @JsonProperty
    private final long projectId;

    @JsonProperty
    private final long accountId;

    @JsonProperty
    private final long curriculumId;

    @JsonProperty
    private final String name;

    @JsonProperty
    private final String description;

    @JsonProperty
    private final _Links _links;

    public OutProject(long projectId, long accountId, long curriculumId, String name, String description){
        this.projectId = projectId;
        this.name = name;
        this.description = description;
        this.accountId = accountId;
        this.curriculumId = curriculumId;
        this._links = new _Links();
    }

    @JsonIgnore
    @Override
    public Object getCollectionItemOutput() {
        return new ProjectsItemOutput(name, description);
    }

    class ProjectsItemOutput {
        @JsonProperty
        private final String name;

        @JsonProperty
        private final String description;


        ProjectsItemOutput(String name, String description) {
            this.name = name;
            this.description = description;
        }

        private class _Links {
            @JsonProperty
            private Self self = new Self();

            private class Self {
                @JsonProperty
                final String href = HOSTNAME +  linkTo( methodOn(CurriculumController.class).getProjects(accountId, curriculumId, 0)).slash(projectId).withSelfRel().getHref();
            }
        }
    }

    private class _Links {
        @JsonProperty
        private Self self = new Self();

        private class Self {
            @JsonProperty
            final String href = HOSTNAME +  linkTo( methodOn(CurriculumController.class).getProjects(accountId, curriculumId, 0)).slash(projectId).withSelfRel().getHref();
        }
    }
}