package isel.ps.employbox.model.output;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import isel.ps.employbox.controllers.account.RatingController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class OutRating implements OutputDto {
    @JsonProperty
    private final long accountIDFrom;

    @JsonProperty
    private final long accountIDTo;

    @JsonProperty
    private final double workLoad;

    @JsonProperty
    private final double wage;

    @JsonProperty
    private final double workEnvironment;

    @JsonProperty
    private final double competence;

    @JsonProperty
    private final double pontuality;

    @JsonProperty
    private final double assiduity;

    @JsonProperty
    private final double demeanor;

    @JsonProperty
    private final long version;

    @JsonProperty
    private final _Links _links;

    public OutRating(
            long accountIDFrom,
            long accountIDTo,
            double workLoad,
            double wage,
            double workEnvironment,
            double competence,
            double pontuality,
            double assiduity,
            double demeanor,
            long version)
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
        this.version = version;
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
        private final _Links _links;


        RatingsItemOutput(long accountIDFrom, long accountIDTo) {
            this.accountIDFrom = accountIDFrom;
            this.accountIDTo = accountIDTo;
            this._links = new _Links();
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
