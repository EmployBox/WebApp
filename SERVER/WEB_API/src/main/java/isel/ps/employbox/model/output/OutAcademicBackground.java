package isel.ps.employbox.model.output;


import com.fasterxml.jackson.annotation.JsonProperty;
import isel.ps.employbox.controllers.CurriculumController;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class OutAcademicBackground extends ResourceSupport {

    private final long academicBackgroundId;

    private final long accountId;

    private final long curriculumId;

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
        this.add(linkTo( methodOn(CurriculumController.class).getAcademicBackground(this.accountId, this.curriculumId)).slash(academicBackgroundId).withSelfRel());
    }
}