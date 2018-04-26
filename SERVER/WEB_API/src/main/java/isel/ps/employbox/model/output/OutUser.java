package isel.ps.employbox.model.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import isel.ps.employbox.controllers.*;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class OutUser extends ResourceSupport {

    @JsonProperty
    private final long id;

    @JsonProperty
    private final String name;

    @JsonProperty
    private final String email;

    @JsonProperty
    private final String photo_url;

    @JsonProperty
    private final String summary;

    @JsonProperty
    private final double rating;

    public OutUser(long id, String name, String email, String photo_url, String summary, double rating) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.photo_url = photo_url;
        this.summary = summary;
        this.rating = rating;
        this.add( linkTo ( UserController.class).slash(id).withSelfRel());
        this.add( linkTo ( JobController.class,id).withRel("offered_jobs"));
        this.add( linkTo ( CurriculumController.class,id).withRel("curricula"));
        this.add( linkTo ( methodOn(UserController.class).getAllApplications(id)).withRel("applications"));
        this.add( linkTo ( ChatController.class,id).withRel("chats"));
        this.add( linkTo ( CommentController.class,id).withRel("comments"));
        this.add( linkTo ( RatingController.class,id).withRel("ratings"));
        this.add( linkTo( methodOn(FollowsController.class,id).getFollowers(id) ).withRel("followers"));
        this.add( linkTo( methodOn(FollowsController.class,id).getFollowing(id, null) ).withRel("following"));
    }
}
