package isel.ps.employbox.services;

import isel.ps.employbox.RapperRepository;
import isel.ps.employbox.model.entities.Application;
import isel.ps.employbox.model.entities.Curriculum;
import isel.ps.employbox.model.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private RapperRepository<User, Long> userRepo;
    private RapperRepository<Curriculum, Long> curriculumRepo;
    private RapperRepository<Application, Long> applicationRepo;

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

    public void updateUser(User user) {
        userRepo.update(user);
    }

    public void updateApplication(Application application) {
        applicationRepo.update(application);
    }

    public void updateCurriculum(Curriculum curriculum) {
        curriculumRepo.update(curriculum);
    }

    public void createUser(User user) {
        userRepo.create(user);
    }

    public void createApplication(Application application) {
        applicationRepo.create(application);
    }

    public void createCurriculum(Curriculum curriculum) {
        curriculumRepo.create(curriculum);
    }

    public void deleteUser(long id) {
        userRepo.deleteById(id);
    }

    public void deleteApplication(long aid, long jid) {
    }

    public void deleteCurriculum(long id, long cid) {
        
    }
}