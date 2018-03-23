package isel.ps.employbox.api.model.binder;

import isel.ps.employbox.api.model.input.InUser;
import isel.ps.employbox.api.model.output.OutUser;
import isel.ps.employbox.api.model.ModelBinder;
import isel.ps.employbox.dal.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserBinder implements ModelBinder<User, OutUser, InUser, Long> {
    @Override
    public List<OutUser> bindOutput(List<User> list) {
        return list
                .stream()
                .map(user -> new OutUser(user.getIdentityKey(), user.getName(), user.getEmail(), user.getPhotoUrl(), user.getSummary(), user.getRating()))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> bindInput(List<InUser> list) {
        return list
                .stream()
                .map(inUser -> new User(inUser.getEmail(), inUser.getPassword(), 0, inUser.getName(), inUser.getSummary(), inUser.getPhoto_url()))
                .collect(Collectors.toList());
    }

    @Override
    public OutUser bindOutput(User user) {
        return new OutUser(user.getIdentityKey(), user.getName(), user.getEmail(), user.getPhotoUrl(), user.getSummary(), user.getRating());
    }

    @Override
    public User bindInput(InUser inUser) {
        return new User(inUser.getEmail(), inUser.getPassword(), 0, inUser.getName(), inUser.getSummary(), inUser.getPhoto_url());
    }
}
