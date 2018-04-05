package isel.ps.employbox.services;

import isel.ps.employbox.model.entities.Job;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Stream;

@Service
public class JobService {

    public Stream<Job> getAccountOffers(long id) {
        return null;
    }

    public Stream<Job> getAllJobs(Map<String, String> queryString) {
        return null;
    }

    public Job getJob(long id) {
        return null;
    }

    public void updateJob(Job job) {
    }

    public void createJob(Job job) {
    }

    public void deleteJob(long id) {
    }
}
