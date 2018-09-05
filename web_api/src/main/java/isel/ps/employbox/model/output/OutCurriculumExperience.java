package isel.ps.employbox.model.output;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import isel.ps.employbox.controllers.curricula.CurriculumExperienceController;

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
        return new CurriculumExperienceItemOutput(curriculumId, competence, year);
    }

    class CurriculumExperienceItemOutput {
        @JsonProperty
        private final long curriculumId;

        @JsonProperty
        private final String competence;

        @JsonProperty
        private final int year;

        @JsonProperty
        private final _Links _links;

        @JsonIgnore
        CurriculumExperienceItemOutput(long curriculumId, String competence, int year) {
            this.curriculumId = curriculumId;
            this.competence = competence;
            this.year = year;
            _links = new _Links();
        }

        private class _Links {
            @JsonProperty
            private Self self = new Self();

            private class Self {
                @JsonProperty
                final String href = HOSTNAME + linkTo( methodOn(CurriculumExperienceController.class).getCurriculumExperience(accountId, curriculumId, curriculumExperienceId)).withSelfRel().getHref();
            }
        }
    }

    private class _Links {
        @JsonProperty
        private Self self = new _Links.Self();

        private class Self {
            @JsonProperty
            final String href = HOSTNAME + linkTo( methodOn(CurriculumExperienceController.class).getCurriculumExperience(accountId, curriculumId, curriculumExperienceId)).withSelfRel().getHref();
        }
    }
}