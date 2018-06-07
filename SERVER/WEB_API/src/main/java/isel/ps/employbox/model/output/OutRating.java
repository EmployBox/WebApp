package isel.ps.employbox.model.output;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import isel.ps.employbox.controllers.RatingController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class OutRating implements OutputDto {
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

    @JsonProperty
    private final _Links _links;

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
        this._links = new _Links();
    }

    @JsonIgnore
    @Override
    public Object getCollectionItemOutput() {
        return new RatingsItemOutput(accountIDFrom, accountIDTo);
    }

    class RatingsItemOutput {
        @JsonProperty
        private final long accountIDFrom;

        @JsonProperty
        private final long accountIDTo;

        @JsonProperty
        private final _Links _links = new _Links();


        RatingsItemOutput(long accountIDFrom, long accountIDTo) {
            this.accountIDFrom = accountIDFrom;
            this.accountIDTo = accountIDTo;
        }

        private class _Links {
            @JsonProperty
            private _Links.Self self = new _Links.Self();

            private class Self {
                @JsonProperty
                final String href = HOSTNAME + linkTo(RatingController.class, accountIDFrom).withSelfRel().getHref();
            }
        }
    }

    private class _Links {
        @JsonProperty
        private Self self = new Self();

        private class Self {
            @JsonProperty
            final String href = HOSTNAME + linkTo(RatingController.class, accountIDFrom).withSelfRel().getHref();
        }
    }
}
