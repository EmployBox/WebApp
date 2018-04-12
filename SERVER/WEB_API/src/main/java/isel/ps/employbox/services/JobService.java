package isel.ps.employbox.services;

import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.RapperRepository;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.model.entities.Job;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class JobService {
    private final RapperRepository<Job, Long> jobRepo;
    private final UserService userService;

    public JobService(RapperRepository<Job, Long> jobRepo, UserService userService) {
        this.jobRepo = jobRepo;
        this.userService = userService;
    }

    public Stream<Job> getAllJobs() {
        return StreamSupport.stream( jobRepo.findAll().join().spliterator(), false);
    }

    public Job getJob(long jid) {
        Optional<Job> ojob= jobRepo.findById(jid).join();
        if(!ojob.isPresent())
            throw new ResourceNotFoundException(ErrorMessages.resourceNotfound_job );
        return ojob.get();
    }

    public void updateJob(Job job, String email) {
        getJob(job.getJobId());
        userService.getUser(job.getAccountID(), email);
        jobRepo.update(job);
    }

    public void createJob(Job job, String email) {
        userService.getUser(job.getAccountID(), email);
        jobRepo.create(job);
    }

    public void deleteJob(long id, String email) {
        userService.getUser(id, email);
        jobRepo.delete(getJob(id));
    }
}
