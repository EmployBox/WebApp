package model;

import util.Streamable;

import java.util.stream.Stream;

public class Account extends AutoGeneratedIdentity {
    protected final long accountID;
    protected final String email;
    protected final String password;
    protected final double rating;
       
    protected final Streamable<Job> offeredJobs;
    protected final Streamable<Comment> comments;
    protected final Streamable<Chat> chats;
    protected final Streamable<Rating> ratings;

    protected Account(long accountID,
                      String email,
                      String password,
                      double rating,
                      long version,
                      Streamable<Job> offeredJobs,
                      Streamable<Comment> comments,
                      Streamable<Chat> chats,
                      Streamable<Rating> ratings
    ){
        super(accountID, version);
        this.accountID = accountID;
        this.email = email;
        this.password = password;
        this.rating = rating;
        this.offeredJobs = offeredJobs;
        this.chats = chats;
        this.ratings = ratings;
        this.comments = comments;
    }

    /**
     * Since we're creating programmatically this object, we don't know the primaryKey value, so we set it to defaultKey
     * @param email
     * @param password
     * @param rating
     * @return
     */
    public static Account create(String email,
                                 String password,
                                 double rating,
                                 Streamable<Job> offeredJobs,
                                 Streamable<Comment> comments,
                                 Streamable<Chat> chats,
                                 Streamable<Rating> ratings
    ){

        Account account = new Account(defaultKey, email, password, rating, 0, offeredJobs,comments,chats,ratings);
        account.markNew();
        return account;
    }

    public static Account load(long accountID,
                               String email,
                               String password,
                               double rating,
                               long version,
                               Streamable<Job> offeredJobs,
                               Streamable<Comment> comments,
                               Streamable<Chat> chats,
                               Streamable<Rating> ratings
    ){
        Account account = new Account(accountID, email, password, rating, version, offeredJobs,comments,chats,ratings);
        account.markClean();
        return account;
    }

    public static Account update(Account acc,
                                 String email,
                                 String password,
                                 double rating,
                                 Streamable<Job> offeredJobs,
                                 Streamable<Comment> comments,
                                 Streamable<Chat> chats,
                                 Streamable<Rating> ratings){
        acc.markToBeDirty();
        Account account = new Account(acc.getIdentityKey(), email, password, rating, acc.getNextVersion(), offeredJobs, comments, chats, ratings );
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

    public Stream<Job> getOfferedJobs() {
        return offeredJobs.get();
    }
}
