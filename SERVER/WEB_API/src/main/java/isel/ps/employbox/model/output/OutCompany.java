package isel.ps.employbox.model.output;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import isel.ps.employbox.controllers.CompanyController;
import isel.ps.employbox.controllers.account.AccountJobController;
import isel.ps.employbox.controllers.account.CommentController;
import isel.ps.employbox.controllers.account.FollowsControllers.FollowedController;
import isel.ps.employbox.controllers.account.FollowsControllers.FollowingController;
import isel.ps.employbox.controllers.account.RatingController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class OutCompany implements OutputDto {

    @JsonProperty
    private final Long accountId;

    @JsonProperty
    private final String email;

    @JsonProperty
    private final double rating;

    @JsonProperty
    private final String name;

    @JsonProperty
    private final String specialization;

    @JsonProperty
    private final Integer yearFounded;

    @JsonProperty
    private final String logoUrl;

    @JsonProperty
    private final String webpageUrl;

    @JsonProperty
    private final String description;

    @JsonProperty
    private final _Links _links;

    public OutCompany(long accountId, String email, double rating, String name, String specialization, Integer yearFounded, String logoUrl, String webpageUrl, String description) {
        this.accountId = accountId;
        this.email = email;
        this.rating = rating;
        this.name = name;
        this.specialization = specialization;
        this.yearFounded = yearFounded;
        this.logoUrl = logoUrl;
        this.webpageUrl = webpageUrl;
        this.description = description;
        this._links = new _Links();
    }

    @JsonIgnore
    @Override
    public Object getCollectionItemOutput() {
        return new CompanyItemOutput(accountId, rating, name, specialization, yearFounded);
    }

    class CompanyItemOutput {
        @JsonProperty
        private final Long accountId;
        @JsonProperty
        private final String name;
        @JsonProperty
        private final double rating;
        @JsonProperty
        private final String specialization;
        @JsonProperty
        private final Integer yearFounded;
        @JsonProperty
        private final _Links _links;

        private CompanyItemOutput(Long accountId, double rating, String name, String specialization, Integer yearFounded){
            this.accountId = accountId;
            this.rating = rating;
            this.name = name;
            this.specialization = specialization;
            this.yearFounded = yearFounded;
            _links = new _Links();
        }
    }

    private class _Links {
        @JsonProperty
        private Self self = new Self();

        @JsonProperty
        private Comments comments = new Comments();

        @JsonProperty
        private Ratings ratings = new Ratings();

        @JsonProperty
        private Followers followers = new Followers();

        @JsonProperty
        private Following following = new Following();

        @JsonProperty
        private Offered_jobs offered_jobs = new Offered_jobs();

        private class Self {
            @JsonProperty
            final String href = HOSTNAME + linkTo(CompanyController.class).slash(accountId).withSelfRel().getHref();
        }

        private class Comments {
            @JsonProperty
            final String href = HOSTNAME + linkTo( methodOn(CommentController.class, accountId).getAllComments(accountId, 0,5,null, null)).withRel("comments").expand().getHref();
        }

        private class Ratings {
            @JsonProperty
            final String href = HOSTNAME + linkTo ( methodOn(RatingController.class, accountId).getRatings(accountId,0,5,null, null)).withRel("ratings").expand().getHref();
        }

        private class Followers {
            @JsonProperty
            final String href = HOSTNAME + linkTo( methodOn(FollowedController.class, accountId).getTheAccountsWichThisAccountIsFollowed(accountId, 0, 5, null, null, null)).withRel("followers").expand().getHref();
        }
        
        private class Following {
            @JsonProperty
            final String href = HOSTNAME + linkTo( methodOn(FollowingController.class, accountId).getTheAccountsWichThisAccountIsFollower(accountId, 0, 5,null,null,null) ).withRel("following").expand().getHref();
        }

        private class Offered_jobs{
            @JsonProperty
            final String href = HOSTNAME + linkTo(methodOn( AccountJobController.class, accountId).getOfferedJobs(accountId, 0, 5, null, null)).withRel("offered_jobs").expand().getHref();
        }
    }
}
