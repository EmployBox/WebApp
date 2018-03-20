package isel.ps.employbox.api.services;

import isel.ps.employbox.api.model.input.InApplication;
import isel.ps.employbox.api.model.input.InCurriculum;
import isel.ps.employbox.api.model.input.InUser;
import isel.ps.employbox.api.model.output.OutApplication;
import isel.ps.employbox.api.model.output.OutCurriculum;
import isel.ps.employbox.dal.Mapper;
import isel.ps.employbox.dal.model.User;
import isel.ps.employbox.dal.util.MapperRegistry;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private final Mapper<isel.ps.employbox.dal.model.User, Long> userMapper = MapperRegistry.getMapper(isel.ps.employbox.dal.model.User.class).orElse(null);
    private final Mapper<isel.ps.employbox.dal.model.Curriculum, String> curriculumMapper = MapperRegistry.getMapper(isel.ps.employbox.dal.model.Curriculum.class).orElse(null);
    private final Mapper<isel.ps.employbox.dal.model.Application, String> applicationMapper = MapperRegistry.getMapper(isel.ps.employbox.dal.model.Application.class).orElse(null);

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

    public List<OutApplication> getAllApplications(long id, Map<String, String> queryString) {
        return null;
    }

    public List<OutCurriculum> getAllCurriculums(long id, Map<String, String> queryString) {
        return null;
    }

    public OutCurriculum getCurriculum(long id, long cid) {
        return null;
    }

    public void updateUser(long id, InUser inUser) {

    }

    public void updateApplication(long id, long jid, InApplication inApplication) {

    }

    public void updateCurriculum(long id, long cid, InCurriculum inCurriculum) {

    }

    public void createUser(InUser inUser) {

    }

    public void createApplication(InApplication inApplication) {

    }

    public void createCurriculum(long id, InCurriculum inCurriculum) {

    }

    public void deleteUser(long id) {
        userMapper.getById(id).join().markRemoved();
    }

    public void deleteApplication(long id, long jid) {

    }

    public void deleteCurriculum(long id, long cid) {
        
    }
}