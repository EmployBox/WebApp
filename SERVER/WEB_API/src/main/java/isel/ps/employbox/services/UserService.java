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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static isel.ps.employbox.ErrorMessages.resourceNotfound_user;

@Service
public class UserService implements UserDetailsService {

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

    public Stream<User> getAllUsers() {
        return StreamSupport.stream(userRepo.findAll().join().spliterator(), false);
    }

    public User getUser(long id, String... email) {
        if(email.length > 1)
            throw new InvalidParameterException("Only 1 or 2 parameters are allowed for this method");

        Optional<User> ouser = userRepo.findById(id).join();
        if(!ouser.isPresent())
            throw new ResourceNotFoundException(resourceNotfound_user);
        User user = ouser.get();

        if(email.length == 1 && !user.getEmail().equals(email[0]))
            throw new UnauthorizedException(ErrorMessages.unAuthorized_IdAndEmailMismatch);

        return user;
    }

    public Application getApplication(long userId, long jobId) {
        getUser(userId);
        jobService.getJob(jobId);
        Optional<Application> oaplication = ( Optional<Application>)applicationRepo.findById(new Pair(userId, jobId)).join();
        if(!oaplication.isPresent())
            throw new ResourceNotFoundException(ErrorMessages.resourceNotfound_application);

        Application application = oaplication.get();
        if(application.getUserId() != userId)
            throw new UnauthorizedException( ErrorMessages.unAuthorized_application );

        return application;
    }


    public Stream<Application> getAllApplications(long userId)/**), Map<String, String> queryString) {*/
    {
        return StreamSupport.stream(applicationRepo.findAll().join().spliterator(), false)
                .filter(curr-> curr.getUserId() == userId);
    }


    public Stream<Curriculum> getCurricula(long userId ) /**, Map<String, String> queryString) {*/
    {
        return StreamSupport.stream(curriculumRepo.findAll().join().spliterator(), false)
                .filter(curr-> curr.getUserId() == userId);
    }

    public Curriculum getCurriculum(long userId, long cid, String email) {
        getUser(userId, email);

        Optional<Curriculum> ocurriculum = curriculumRepo.findById(cid).join();

        if(!ocurriculum.isPresent())
            throw new ResourceNotFoundException(ErrorMessages.resourceNotfound_curriculum);
        Curriculum curriculum = ocurriculum.get();

        if(curriculum.getUserId()!= userId)
            throw new UnauthorizedException(ErrorMessages.unAuthorized_curriculum);

        return curriculum;
    }

    public void updateUser(User user, String email) {
        getUser(user.getAccountID(), email);
        userRepo.update(user);
    }

    public void updateApplication(Application application, String email) {
        getUser(application.getUserId(), email);
        getApplication(application.getUserId(), application. getJobId());
        applicationRepo.update(application);
    }

    public void updateCurriculum(Curriculum curriculum, String email) {
        getUser(curriculum.getUserId(), email);
        getCurriculum(curriculum.getUserId(), curriculum.getCurriculumId(), email);
        curriculumRepo.update(curriculum);
    }


    public void createUser(User user) {
        userRepo.create(user);
    }

    public void createApplication(long userId, Application application, String email) {
        getUser(userId, email);

        if(application.getUserId() != userId)
            throw new BadRequestException( ErrorMessages.badRequest_IdsMismatch);

        applicationRepo.create(application);
    }

    public void createCurriculum(long userId, Curriculum curriculum, String email) {
        getUser(userId, email);

        if(curriculum.getUserId() != userId)
            throw new BadRequestException( ErrorMessages.badRequest_IdsMismatch);

        curriculumRepo.create(curriculum);
    }

    public void deleteUser(long id, String email) {
        userRepo.delete(getUser(id,email));
    }

    public void deleteApplication(long userId, long jobId, String email) {
        getUser(userId, email);
        applicationRepo.delete( getApplication(userId,jobId));
    }

    public void deleteCurriculum(long userId, long cid, String name) {
        curriculumRepo.delete( getCurriculum(userId, cid, name) );
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> ouser =  StreamSupport.stream(
                userRepo.findAll().join().spliterator(), false)
                .filter(curr-> curr.getEmail().equals(username))
                .findFirst();

        if( !ouser.isPresent())
            throw new UsernameNotFoundException( resourceNotfound_user );

        return ouser.get();
    }
}