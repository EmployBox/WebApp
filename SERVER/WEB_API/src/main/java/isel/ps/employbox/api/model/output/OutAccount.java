package isel.ps.employbox.api.model.output;

public class OutAccount {
    private final long accountID;
    private final String email;
    private final float rating;

    public OutAccount(long accountID, String email, float rating) {
        this.accountID = accountID;
        this.email = email;
        this.rating = rating;
    }

    public long getAccountID() {
        return accountID;
    }


    public String getEmail() {
        return email;
    }


    public float getRating() {
        return rating;
    }

}
