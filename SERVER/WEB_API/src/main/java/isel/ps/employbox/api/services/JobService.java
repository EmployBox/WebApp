package isel.ps.employbox.api.services;

import isel.ps.employbox.dal.model.Job;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JobService {

    public Optional<Job> getJob(long id, long jid) {
        return null;
    }

    public void deleteJob(long id, long jid) {

    }

    public void updateJob(Job job) {
    }

    public void createJob(Job job) {
    }
}
