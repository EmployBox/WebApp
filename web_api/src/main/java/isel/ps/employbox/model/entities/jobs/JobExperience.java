package isel.ps.employbox.model.entities.jobs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.jayield.rapper.DomainObject;
import com.github.jayield.rapper.annotations.ColumnName;
import com.github.jayield.rapper.annotations.Id;
import com.github.jayield.rapper.annotations.Version;
import com.github.jayield.rapper.mapper.externals.Foreign;
import com.github.jayield.rapper.unitofwork.UnitOfWork;
import isel.ps.employbox.exceptions.ResourceNotFoundException;

import static com.github.jayield.rapper.mapper.MapperRegistry.getMapper;

public class JobExperience implements DomainObject<Long> {
    @Id( isIdentity = true)
    private final long jobExperienceId;

    private final String competence;
    private final int years;
    @Version
    private final long version;

    @ColumnName(name = "jobId")
    private final Foreign<Job, Long> job;

    public JobExperience(){
        job = null;
        years = 0;
        jobExperienceId = 0;
        competence = null;
        version = 0;
    }

    public JobExperience(long jobExperienceId, long jobId, String competences,  short years, long version){
        UnitOfWork unitOfWork = new UnitOfWork();
        this.job = new Foreign<>(jobId, unit -> getMapper(Job.class, unitOfWork).findById( jobId)
                .thenCompose( res -> unitOfWork.commit().thenApply( __-> res))
                .thenApply(job1 -> job1.orElseThrow(() -> new ResourceNotFoundException("Job not Found"))));

        this.jobExperienceId = jobExperienceId;
        this.competence = competences;
        this.years = years;
        this.version = version;
    }


    @JsonIgnore
    @Override
    public Long getIdentityKey() {
        return jobExperienceId;
    }

    @Override
    public long getVersion() {
        return version;
    }

    public int getYears() {
        return years;
    }

    public String getCompetence() {
        return competence;
    }

    public Foreign<Job, Long> getJob() {
        return job;
    }
}
