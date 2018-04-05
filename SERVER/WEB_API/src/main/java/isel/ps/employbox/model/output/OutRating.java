package isel.ps.employbox.model.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import isel.ps.employbox.controllers.RatingController;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class OutRating extends ResourceSupport{
    @JsonProperty
    private final long accountIDFrom;

    @JsonProperty
    private final long accountIDTo;

    @JsonProperty
    private final float workLoad;

    @JsonProperty
    private final float wage;

    @JsonProperty
    private final float workEnvironment;

    @JsonProperty
    private final float competence;

    @JsonProperty
    private final float pontuality;

    @JsonProperty
    private final float assiduity;

    @JsonProperty
    private final float demeanor;


    public OutRating(
            long accountIDFrom,
            long accountIDTo,
            float workLoad,
            float wage,
            float workEnvironment,
            float competence,
            float pontuality,
            float assiduity,
            float demeanor)
    {
        this.accountIDFrom = accountIDFrom;
        this.accountIDTo = accountIDTo;
        this.workLoad = workLoad;
        this.wage = wage;
        this.workEnvironment = workEnvironment;
        this.competence = competence;
        this.pontuality = pontuality;
        this.assiduity = assiduity;
        this.demeanor = demeanor;
        this.add(linkTo(RatingController.class, accountIDFrom).withSelfRel());
    }
}
