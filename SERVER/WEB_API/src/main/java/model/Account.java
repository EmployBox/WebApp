package model;

public class Account extends AutoGeneratedIdentity {
    private final long accountID;
    private final String email;
    private final String password;
    private final double rating;

    protected Account(long accountID, String email, String password, double rating, long version){
        super(accountID, version);
        this.accountID = accountID;
        this.email = email;
        this.password = password;
        this.rating = rating;
    }

    /**
     * Since we're creating programmatically this object, we don't know the primaryKey value, so we set it to defaultKey
     * @param email
     * @param password
     * @param rating
     * @return
     */
    public static Account create(String email, String password, double rating){
        Account account = new Account(defaultKey, email, password, rating, 0);
        account.markNew();
        return account;
    }

    public static Account load(long accountID, String email, String password, double rating, long version){
        Account account = new Account(accountID, email, password, rating, version);
        account.markClean();
        return account;
    }

    public static Account update(Account acc, String email, String password, double rating){
        acc.markToBeDirty();
        Account account = new Account(acc.getIdentityKey(), email, password, rating, acc.getNextVersion());
        account.markDirty();
        return account;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public double getRating() {
        return rating;
    }
}
