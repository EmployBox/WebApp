package isel.ps.employbox.api.services;

import isel.ps.employbox.api.model.output.Application;
import isel.ps.employbox.api.model.output.Curriculum;
import isel.ps.employbox.api.model.output.User;
import isel.ps.employbox.dal.Mapper;
import isel.ps.employbox.dal.util.MapperRegistry;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService implements ModelBinder<isel.ps.employbox.dal.model.User, User, isel.ps.employbox.api.model.input.User, Long> {

    private final Mapper<isel.ps.employbox.dal.model.User, Long> userMapper = MapperRegistry.getMapper(isel.ps.employbox.dal.model.User.class);

    @Override
    public List<User> bindOutput(List<isel.ps.employbox.dal.model.User> list) {
        return null;
    }

    @Override
    public List<isel.ps.employbox.dal.model.User> bindInput(List<isel.ps.employbox.api.model.input.User> list) {
        return null;
    }

    public List<User> getAllUsers(Map<String, String> queryString) {
        String name = queryString.get("name");
        String academicInstitution = queryString.get("academicInstitution");
        String job = queryString.get("job");
        String experienceComp = queryString.get("experienceComp");
        String experienceYears = queryString.get("experienceYears");
        String page = queryString.get("page");
        //TODO filters...
        return userMapper.getAll()
                .thenApply(this::bindOutput)
                .join();
    }

    public Optional<User> getUser(long id) {
        return bindOutput(Collections.singletonList(userMapper.getById(id).join()))
                .stream()
                .findFirst();
    }

    public List<Application> getAllApplications(long id, Map<String, String> queryString) {
        return null;
    }

    public List<Curriculum> getAllCurriculums(long id, Map<String, String> queryString) {
        return null;
    }

    public Curriculum getCurriculum(long id, long cid) {
        return null;
    }

    public void updateUser(long id, isel.ps.employbox.api.model.input.User user) {

    }

    public void updateApplication(long id, long jid, isel.ps.employbox.api.model.input.Application application) {

    }

    public void updateCurriculum(long id, long cid, isel.ps.employbox.api.model.input.Curriculum curriculum) {

    }

    public void createUser(isel.ps.employbox.api.model.input.User user) {

    }

    public void createApplication(isel.ps.employbox.api.model.input.Application application) {

    }

    public void createCurriculum(long id, isel.ps.employbox.api.model.input.Curriculum curriculum) {

    }

    public void deleteUser(long id) {

    }

    public void deleteApplication(long id, long jid) {

    }

    public void deleteCurriculum(long id, long cid) {
        
    }
}