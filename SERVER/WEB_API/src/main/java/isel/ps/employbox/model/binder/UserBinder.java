package isel.ps.employbox.model.binder;

import isel.ps.employbox.model.entities.User;
import isel.ps.employbox.model.input.InUser;
import isel.ps.employbox.model.output.OutUser;
import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class UserBinder extends ModelBinder<User, OutUser, InUser> {

    @Override
    Stream<User> bindInput(Stream<InUser> list) {
        return null;
    }

    @Override
    public Resource<OutUser> bindOutput(User user) {
        return new Resource( new OutUser(user.getAccountID(), user.getName(), user.getEmail(), user.getPhotoUrl(), user.getSummary(), user.getRating()));
    }

    @Override
    public User bindInput(InUser inUser) {
        return new User(inUser.getEmail(), inUser.getPassword(), 0, inUser.getName(), inUser.getSummary(), inUser.getPhoto_url());
    }
}
