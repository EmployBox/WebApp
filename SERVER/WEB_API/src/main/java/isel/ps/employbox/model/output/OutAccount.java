package isel.ps.employbox.model.output;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OutAccount implements OutputDto {

    @JsonProperty
    private final long accountId;

    @JsonProperty
    private final String email;

    @JsonProperty
    private final double rating;

    @JsonProperty
    private final long version;

    public OutAccount(long accountID, String email, double rating, long version) {
        this.accountId = accountID;
        this.email = email;
        this.rating = rating;
        this.version = version;
    }

    public long getAccountId() {
        return accountId;
    }

    public String getEmail() {
        return email;
    }

    public double getRating() {
        return rating;
    }

    public long getVersion() {
        return version;
    }

    @JsonIgnore
    @Override
    public Object getCollectionItemOutput() {
        return null;
    }
}
