package model;

public class User extends Account {
    private String name;
    private String summary;
    private String photoUrl;

    public User(long accountID,
                String email,
                String password,
                double rating,
                long version,
                String name,
                String summary,
                String photoUrl
    ){
        super(accountID,email,password,rating,version);
        this.name = name;
        this.summary = summary;
        this.photoUrl = photoUrl;
    }


    public static User create(long accountID,
                            String email,
                            String password,
                            double rating,
                            long version,
                            String name,
                            String summary,
                            String photoUrl
    ){
        User user = new User(accountID,email,password,rating,version,name,summary,photoUrl);
        user.markNew();
        return user;
    }

    public static User load(long accountID,
                            String email,
                            String password,
                            double rating,
                            long version,
                            String name,
                            String summary,
                            String photoUrl
    ){
        User user = new User(accountID,email,password,rating,version,name,summary,photoUrl);
        user.markClean();
        return user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        markToBeDirty();
        this.name = name;
        markDirty();
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary){
        markToBeDirty();
        this.summary = summary;
        markDirty();
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl){
        this.photoUrl = photoUrl;
    }
}
