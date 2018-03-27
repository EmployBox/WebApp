package isel.ps.employbox.api.services;

import isel.ps.employbox.dal.model.Job;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class JobService {

    public List<Job> getAccountOffers(long id) {
        return null;
    }

    public List<Job> getAllJobs(Map<String, String> queryString) {
        return null;
    }

    public Optional<Job> getJob(long id, long jid) {
        return null;
    }

    public void updateJob(Job job) {
    }

    public void createJob(Job job) {
    }

    public void deleteJob(long id, long jid) {
    }
}
