package isel.ps.employbox.api.services;

import isel.ps.employbox.api.model.output.Application;
import isel.ps.employbox.api.model.output.Curriculum;
import isel.ps.employbox.dal.Mapper;
import isel.ps.employbox.dal.model.User;
import isel.ps.employbox.dal.util.MapperRegistry;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private final Mapper<isel.ps.employbox.dal.model.User, Long> userMapper = MapperRegistry.getMapper(isel.ps.employbox.dal.model.User.class).get();
    private final Mapper<isel.ps.employbox.dal.model.Curriculum, String> curriculumMapper = MapperRegistry.getMapper(isel.ps.employbox.dal.model.Curriculum.class).get();
    private final Mapper<isel.ps.employbox.dal.model.Application, String> applicationMapper = MapperRegistry.getMapper(isel.ps.employbox.dal.model.Application.class).get();

    public List<User> getAllUsers(Map<String, String> queryString) {
        String name = queryString.get("name");
        String academicInstitution = queryString.get("academicInstitution");
        String job = queryString.get("job");
        String experienceComp = queryString.get("experienceComp");
        String experienceYears = queryString.get("experienceYears");
        String page = queryString.get("page");
        //TODO filters...
        return userMapper.getAll().join();
    }

    public User getUser(long id) {
        return userMapper.getById(id).join();
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
        userMapper.getById(id).join().markRemoved();
    }

    public void deleteApplication(long id, long jid) {

    }

    public void deleteCurriculum(long id, long cid) {
        
    }
}