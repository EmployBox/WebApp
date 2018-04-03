package isel.ps.employbox.model.binder;

import isel.ps.employbox.model.input.InUser;
import isel.ps.employbox.model.output.OutUser;
import isel.ps.employbox.model.ModelBinder;
import isel.ps.employbox.model.entities.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserBinder implements ModelBinder<User, OutUser, InUser, Long> {
    @Override
    public List<OutUser> bindOutput(List<User> list) {
        return list
                .stream()
                .map(this::bindOutput)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> bindInput(List<InUser> list) {
        return list
                .stream()
                .map(this::bindInput)
                .collect(Collectors.toList());
    }

    @Override
    public OutUser bindOutput(User user) {
        return new OutUser(user.getAccountID(), user.getName(), user.getEmail(), user.getPhotoUrl(), user.getSummary(), user.getRating());
    }

    @Override
    public User bindInput(InUser inUser) {
        return new User(inUser.getEmail(), inUser.getPassword(), 0, inUser.getName(), inUser.getSummary(), inUser.getPhoto_url());
    }
}
