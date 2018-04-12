package isel.ps.employbox.model.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

public class OutAccount extends ResourceSupport {

    @JsonProperty
    private final long accountID;

    @JsonProperty
    private final String email;

    @JsonProperty
    private final float rating;

    public OutAccount(long accountID, String email, float rating) {
        this.accountID = accountID;
        this.email = email;
        this.rating = rating;
    }
}
