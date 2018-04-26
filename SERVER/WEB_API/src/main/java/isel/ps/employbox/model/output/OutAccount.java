package isel.ps.employbox.model.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

public class OutAccount extends ResourceSupport {

    @JsonProperty
    private final long accountID;

    @JsonProperty
    private final String email;

    @JsonProperty
    private final double rating;

    @JsonProperty
    private final long version;

    public OutAccount(long accountID, String email, double rating, long version) {
        this.accountID = accountID;
        this.email = email;
        this.rating = rating;
        this.version = version;
    }
}
