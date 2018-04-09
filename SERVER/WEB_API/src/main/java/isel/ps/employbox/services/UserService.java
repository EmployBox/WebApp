package isel.ps.employbox.services;

import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.RapperRepository;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.exceptions.UnauthorizedException;
import isel.ps.employbox.model.entities.Application;
import isel.ps.employbox.model.entities.Curriculum;
import isel.ps.employbox.model.entities.User;
import javafx.util.Pair;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class UserService {

    private final RapperRepository<User, Long> userRepo;
    private final JobService jobService;
    private final RapperRepository<Curriculum, Long> curriculumRepo;
    private final RapperRepository<Application, Pair<Long,Long>> applicationRepo;

    public UserService(RapperRepository<User, Long> userRepo, JobService jobService, RapperRepository<Curriculum, Long> curriculumRepo, RapperRepository<Application, Pair<Long, Long>> applicationRepo) {
        this.userRepo = userRepo;
        this.jobService = jobService;
        this.curriculumRepo = curriculumRepo;
        this.applicationRepo = applicationRepo;
    }

    public Stream<User> getAllUsers(Map<String, String> queryString) {
        String name = queryString.get("name");
        String academicInstitution = queryString.get("academicInstitution");
        String job = queryString.get("job");
        String experienceComp = queryString.get("experienceComp");
        String experienceYears = queryString.get("experienceYears");
        String page = queryString.get("page");
        //TODO filters...
        return null;//userMapper.getAll().join();
    }

    public User getUser(long id) {
        Optional<User> ouser = userRepo.findById(id);
        if(!ouser.isPresent())
            throw new ResourceNotFoundException(ErrorMessages.resourceNotfound_user);
        return ouser.get();
    }

    public Application getApplication(long userId, long jobId) {
        getUser(userId);
        jobService.getJob(jobId);
        Optional<Application> oaplication = applicationRepo.findById(new Pair(userId, jobId));
        if(!oaplication.isPresent())
            throw new ResourceNotFoundException(ErrorMessages.resourceNotfound_application);

        Application application = oaplication.get();
        if(application.getUserId() != userId)
            throw new UnauthorizedException( ErrorMessages.unAuthorized_application );

        return application;
    }


    public Stream<Application> getAllApplications(long userId)/**), Map<String, String> queryString) {*/
    {
        return StreamSupport.stream(applicationRepo.findAll().spliterator(), false)
                .filter(curr-> curr.getUserId() == userId);
    }


    public Stream<Curriculum> getAllCurriculums(long userId ) /**, Map<String, String> queryString) {*/
    {
        return StreamSupport.stream(curriculumRepo.findAll().spliterator(), false)
                .filter(curr-> curr.getUserId() == userId);
    }

    public Curriculum getCurriculum(long userId, long cid) {
        getUser(userId);
        Optional<Curriculum> ocurriculum = curriculumRepo.findById(cid);

        if(!ocurriculum.isPresent())
            throw new ResourceNotFoundException(ErrorMessages.resourceNotfound_curriculum);
        Curriculum curriculum = ocurriculum.get();

        if(curriculum.getUserId()!= userId)
            throw new UnauthorizedException(ErrorMessages.unAuthorized_curriculum);

        return curriculum;
    }

    public void updateUser(User user) {
        getUser(user.getAccountID());
        userRepo.update(user);
    }

    public void updateApplication(Application application) {
        getApplication(application.getUserId(), application. getJobId());
        applicationRepo.update(application);
    }

    public void updateCurriculum(Curriculum curriculum) {
        getCurriculum(curriculum.getUserId(), curriculum.getCurriculumId() );
        curriculumRepo.update(curriculum);
    }

    //todo authorization code
    public void createUser(User user) {
        userRepo.create(user);
    }

    public void createApplication(long userId, Application application) {
        if(application.getUserId() != userId)
            throw new BadRequestException( ErrorMessages.badRequest_IdsMismatch);

        applicationRepo.create(application);
    }

    public void createCurriculum(long userId, Curriculum curriculum) {
        if(curriculum.getUserId() != userId)
            throw new BadRequestException( ErrorMessages.badRequest_IdsMismatch);

        curriculumRepo.create(curriculum);
    }

    public void deleteUser(long id) {
        userRepo.delete(getUser(id));
    }

    public void deleteApplication(long userId, long jobId) {
        applicationRepo.delete( getApplication(userId,jobId));
    }

    public void deleteCurriculum(long userId, long cid) {
        curriculumRepo.delete( getCurriculum(userId, cid) );
    }
}