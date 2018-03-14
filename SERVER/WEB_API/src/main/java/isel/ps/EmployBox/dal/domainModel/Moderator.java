package isel.ps.EmployBox.dal.domainModel;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Moderator extends Account {
    private final Supplier<List<Rating>> ratingsModerated;

    public Moderator(
            long accountID,
            String email,
            String password,
            double rating,
            long version,
            Supplier<List<Comment>> comments,
            Supplier<List<Chat>> chats,
            Supplier<List<Rating>> ratings,
            Supplier<List<Rating>> ratingsModerated
    ) {
        super(accountID, email, password, rating, version, null, comments, chats, ratings, null);
        this.ratingsModerated = ratingsModerated;
    }

    public static Moderator create(
            String email,
            String password,
            double rating,
            long version,
            Supplier<List<Comment>> comments,
            Supplier<List<Chat>> chats,
            Supplier<List<Rating>> ratings,
            Supplier<List<Rating>> ratingsModerated
    ){
        Moderator moderator = new Moderator(
                defaultKey,
                email,
                password,
                rating,
                version,
                comments,
                chats,
                ratings,
                ratingsModerated
        );
        moderator.markNew();
        return moderator;
    }

    public static Moderator load(
            long accountId,
            String email,
            String password,
            double rating,
            long version,
            Supplier<List<Comment>> comments,
            Supplier<List<Chat>> chats,
            Supplier<List<Rating>> ratings,
            Supplier<List<Rating>> ratingsModerated
    ){
        Moderator moderator = new Moderator(
                accountId,
                email,
                password,
                rating,
                version,
                comments,
                chats,
                ratings,
                ratingsModerated
        );
        moderator.markClean();
        return moderator;
    }

    public Stream<Rating> getRatingsModerated() {
        return ratingsModerated.get().stream();
    }
}
