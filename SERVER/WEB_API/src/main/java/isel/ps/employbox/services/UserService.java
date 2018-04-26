package isel.ps.employbox.services;

import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.exceptions.UnauthorizedException;
import isel.ps.employbox.model.entities.Account;
import isel.ps.employbox.model.entities.Application;
import isel.ps.employbox.model.entities.Curriculum;
import isel.ps.employbox.model.entities.User;
import javafx.util.Pair;
import org.github.isel.rapper.DataRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static isel.ps.employbox.ErrorMessages.resourceNotfound_user;

@Service
public class UserService implements UserDetailsService {

    private final DataRepository<Account, Long> accRepo;
    private final DataRepository<User, Long> userRepo;
    private final DataRepository<Curriculum, Long> curriculumRepo;
    private final DataRepository<Application, Application.ApplicationKeys> applicationRepo;

    public UserService(
            DataRepository<Account, Long> accRepo, DataRepository<User, Long> userRepo,
            DataRepository<Curriculum, Long> curriculumRepo,
            DataRepository<Application, Application.ApplicationKeys> applicationRepo)
    {
        this.accRepo = accRepo;
        this.userRepo = userRepo;
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

        List<Application> aplications = getUser(userId).getApplications().get();

        Optional<Application> oret;
        if(aplications.isEmpty() ||(oret = aplications.parallelStream().filter(curr-> curr.getUserId() == userId && curr.getJobId() == jobId).findFirst()).isPresent())
            throw new ResourceNotFoundException(ErrorMessages.resourceNotfound_application);

        return oret.get();
    }


    public Stream<Application> getAllApplications(long accountId)/**), Map<String, String> queryString) {*/
    {
        List<User> luser = userRepo.findWhere(new Pair<>("accountId",accountId)).join();
        if(luser.isEmpty())
            return Stream.empty();

        return luser
                .get(0)
                .getApplications()
                .get()
                .stream();
    }


    public Stream<Curriculum> getCurricula(long userId, String email ) /**, Map<String, String> queryString) {*/
    {
        return StreamSupport.stream( getUser(userId, email).getCurricula().get().spliterator(), false)
                .filter(curr-> curr.getUserId() == userId);
    }

    public Curriculum getCurriculum(long userId, long cid, String email) {
        List<Curriculum> curricula = getUser(userId, email).getCurricula().get();

        Optional<Curriculum> oret;
        if(curricula.isEmpty() || ! (oret = curricula.parallelStream().filter(curr-> curr.getIdentityKey() == cid).findFirst()).isPresent())
            throw new ResourceNotFoundException(ErrorMessages.resourceNotfound_curriculum);

        return oret.get();
    }

    public void updateUser(User user, String email) {
        getUser(user.getIdentityKey(), email);
        if(!userRepo.update(user).join())
            throw new ResourceNotFoundException("something went wrong updating this item") ;
    }

    public void updateApplication(Application application, String email) {
        getUser(application.getUserId(), email);
        getApplication(application.getUserId(), application. getJobId());
        applicationRepo.update(application);
    }

    public void updateCurriculum(Curriculum curriculum, String email) {
        getUser(curriculum.getUserId(), email);
        getCurriculum(curriculum.getUserId(), curriculum.getIdentityKey(), email);
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
        Optional<User> ouser = userRepo
                .findAll()
                .join()
                .stream()
                .filter(curr-> curr.getEmail().equals(username))
                .findFirst();

        if( !ouser.isPresent())
            throw new UsernameNotFoundException( resourceNotfound_user );

        return ouser.get();
    }
}