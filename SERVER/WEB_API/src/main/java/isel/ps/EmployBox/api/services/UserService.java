package isel.ps.EmployBox.api.services;

import isel.ps.EmployBox.api.model.output.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserService {

    public List<User> getAllUsers(Map<String, String> queryString) {
        String name = queryString.get("name");
        String academicInstitution = queryString.get("academicInstitution");
        String job = queryString.get("job");
        String experienceComp = queryString.get("experienceComp");
        String experienceYears = queryString.get("experienceYears");
        String page = queryString.get("page");
        return null;
    }

    public User getUser(long id) {
        return null;
    }
}
