package isel.ps.employbox.model.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import isel.ps.employbox.controllers.CurriculumController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class OutCurriculumExperience implements OutputDto {

    @JsonProperty
    private final long curriculumExperienceId;

    @JsonProperty
    private final long accountId;

    @JsonProperty
    private final long curriculumId;

    @JsonProperty
    private final String competence;

    @JsonProperty
    private final int year;

    @JsonProperty
    private final _Links _links;

    public OutCurriculumExperience(long curriculumExperienceId, long accountId, long curriculumId, String competence, int year){
        this.curriculumExperienceId = curriculumExperienceId;
        this.competence = competence;
        this.year = year;
        this.accountId = accountId;
        this.curriculumId = curriculumId;
        this._links = new _Links();
    }

    @Override
    public Object getCollectionItemOutput() {
        return new CurriculumExperienceItemOutput(curriculumId);
    }

    class CurriculumExperienceItemOutput {
        @JsonProperty
        private final long curriculumId;

        CurriculumExperienceItemOutput(long curriculumId) {
            this.curriculumId = curriculumId;
        }

        private class _Links {
            @JsonProperty
            private Self self = new Self();

            private class Self {
                @JsonProperty
                final String href = HOSTNAME + linkTo( methodOn(CurriculumController.class).getCurriculumExperience(accountId, curriculumId, curriculumExperienceId)).withSelfRel().getHref();
            }
        }
    }

    private class _Links {
        @JsonProperty
        private Self self = new _Links.Self();

        private class Self {
            @JsonProperty
            final String href = HOSTNAME + linkTo( methodOn(CurriculumController.class).getCurriculumExperience(accountId, curriculumId, curriculumExperienceId)).withSelfRel().getHref();
        }
    }
}