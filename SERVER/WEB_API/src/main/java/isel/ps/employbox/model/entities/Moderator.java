package isel.ps.employbox.model.entities;

import com.github.jayield.rapper.ColumnName;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Moderator extends Account {
    @ColumnName(foreignName = "moderatorId")
    private final CompletableFuture<List<Rating>> ratingsModerated;

    public Moderator(){
        ratingsModerated = null;
    }

    public Moderator(
            long accountID,
            String email,
            String password,
            float rating,
            long version,
            CompletableFuture<List<Comment>> comments,
            CompletableFuture<List<Chat>> chats,
            CompletableFuture<List<Rating>> ratings,
            CompletableFuture<List<Rating>> ratingsModerated
    ) {
        super(accountID, email, password, rating, version, null, comments, chats, ratings, null, null);
        this.ratingsModerated = ratingsModerated;
    }

    public CompletableFuture<List<Rating>> getRatingsModerated() {
        return ratingsModerated;
    }
}
