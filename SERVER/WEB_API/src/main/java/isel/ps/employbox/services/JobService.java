package isel.ps.employbox.services;

import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.RapperRepository;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.model.entities.Job;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class JobService {
    private final RapperRepository<Job, Long> jobRepo;
    private final AccountService accountService;

    public JobService(RapperRepository<Job, Long> jobRepo, AccountService accountService) {
        this.jobRepo = jobRepo;
        this.accountService = accountService;
    }

    //todo we want a query string?
    public Stream<Job> getAllJobs(Map<String, String> queryString) {
        return StreamSupport.stream( jobRepo.findAll().spliterator(), false);
    }

    public Job getJob(long jid) {
        Optional<Job> ojob= jobRepo.findById(jid);
        if(!ojob.isPresent())
            throw new ResourceNotFoundException(ErrorMessages.resourceNotfound_job );
        return ojob.get();
    }

    public void updateJob(Job job) {
        getJob(job.getJobId());
        jobRepo.update(job);
    }

    public void createJob(Job job) {
        jobRepo.create(job);
    }

    public void deleteJob(long id) {
        jobRepo.delete(getJob(id));
    }
}
