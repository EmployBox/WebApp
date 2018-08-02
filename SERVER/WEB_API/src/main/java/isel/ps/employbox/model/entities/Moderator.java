package isel.ps.employbox.model.entities;


import com.github.jayield.rapper.annotations.ColumnName;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class Moderator extends Account {
    @ColumnName(foreignName = "moderatorId")
    private final Supplier<CompletableFuture<List<Rating>>> ratingsModerated;

    public Moderator(){
        ratingsModerated = null;
    }

    public Moderator(
            long accountID,
            String name,
            String email,
            String password,
            float rating,
            long version,
            List<Comment> comments,
            List<Chat> chats,
            List<Rating> ratings,
            List<Rating> ratingsModerated
    ) {
        super(accountID, name, email, password, "MOD", rating, version, null, comments, chats, ratings, null, null);
        this.ratingsModerated = ()-> CompletableFuture.completedFuture(ratingsModerated);
    }

    public Supplier<CompletableFuture<List<Rating>>> getRatingsModerated() {
        return ratingsModerated;
    }
}
