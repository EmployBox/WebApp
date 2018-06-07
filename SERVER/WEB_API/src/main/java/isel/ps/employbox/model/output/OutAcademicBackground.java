package isel.ps.employbox.model.output;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import isel.ps.employbox.controllers.CurriculumController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class OutAcademicBackground implements OutputDto {

    private final long academicBackgroundId;

    private final long accountId;

    protected final long curriculumId;

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

    @JsonProperty
    private final _Links link = new _Links();

    public OutAcademicBackground(
            long academicBackgroundId,
            long accountId,
            long curriculumId,
            String institution,
            String degree,
            String studyArea,
            String beginDate,
            String endDate)
    {
        this.academicBackgroundId = academicBackgroundId;
        this.institution = institution;
        this.degree = degree;
        this.studyArea = studyArea;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.accountId = accountId;
        this.curriculumId = curriculumId;
    }

    @JsonIgnore
    @Override
    public Object getCollectionItemOutput() {
        return new AcademicBackgroundItemOutput(institution, degree);
    }

    class AcademicBackgroundItemOutput {
        @JsonProperty
        private final String institution;

        @JsonProperty
        private final String degree;

        @JsonProperty
        private _Links _links = new _Links();

        AcademicBackgroundItemOutput(String institution, String degree) {
            this.institution = institution;
            this.degree = degree;
        }

        private class _Links {
            @JsonProperty
            private _Links.Self self = new _Links.Self();

            private class Self {
                @JsonProperty
                final String href = HOSTNAME +  linkTo( methodOn(CurriculumController.class).getAcademicBackground(accountId, curriculumId, 0)).slash(academicBackgroundId).withSelfRel().getHref();
            }
        }
    }

    private class _Links{
        @JsonProperty
        private Self self = new Self();

        private class Self{
            String href = linkTo( methodOn(CurriculumController.class).getAcademicBackground(accountId, curriculumId, 0)).slash(academicBackgroundId).withSelfRel().getHref();
        }
    }
}