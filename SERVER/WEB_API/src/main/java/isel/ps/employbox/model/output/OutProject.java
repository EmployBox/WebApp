package isel.ps.employbox.model.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import isel.ps.employbox.controllers.CurriculumController;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class OutProject extends ResourceSupport {

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


    public OutProject(long projectId, long accountId, long curriculumId, String name, String description){
        this.projectId = projectId;
        this.name = name;
        this.description = description;
        this.accountId = accountId;
        this.curriculumId = curriculumId;
        this.add( linkTo( methodOn(CurriculumController.class).getProjects(this.accountId, this.curriculumId)).slash(projectId).withSelfRel());
    }
}