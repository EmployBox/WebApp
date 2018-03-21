package isel.ps.employbox.api.services;

import isel.ps.employbox.dal.RapperRepository;
import isel.ps.employbox.dal.model.Application;
import isel.ps.employbox.dal.model.Curriculum;
import isel.ps.employbox.dal.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private RapperRepository<User, Long> userRepo;
    private RapperRepository<Curriculum, Long> curriculumRepo;
    private RapperRepository<User, Long> applicationRepo;

    public List<User> getAllUsers(Map<String, String> queryString) {
        String name = queryString.get("name");
        String academicInstitution = queryString.get("academicInstitution");
        String job = queryString.get("job");
        String experienceComp = queryString.get("experienceComp");
        String experienceYears = queryString.get("experienceYears");
        String page = queryString.get("page");
        //TODO filters...
        return null;//userMapper.getAll().join();
    }

    public Optional<User> getUser(long id) {
        return userRepo.findById(id);
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

    public void updateUser(long id, User inUser) {

    }

    public void updateApplication(long id, long jid, Application inApplication) {

    }

    public void updateCurriculum(long id, long cid, Curriculum inCurriculum) {

    }

    public void createUser(User user) {
        userRepo.create(user);
    }

    public void createApplication(Application inApplication) {

    }

    public void createCurriculum(long id, Curriculum inCurriculum) {

    }

    public void deleteUser(long id) {
        userRepo.deleteById(id);
    }

    public void deleteApplication(long id, long jid) {

    }

    public void deleteCurriculum(long id, long cid) {
        
    }
}