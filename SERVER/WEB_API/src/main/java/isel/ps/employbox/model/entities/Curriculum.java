package isel.ps.employbox.model.entities;

import com.github.jayield.rapper.ColumnName;
import com.github.jayield.rapper.DomainObject;
import com.github.jayield.rapper.Id;
import com.github.jayield.rapper.Version;
import isel.ps.employbox.model.entities.CurriculumChilds.AcademicBackground;
import isel.ps.employbox.model.entities.CurriculumChilds.CurriculumExperience;
import isel.ps.employbox.model.entities.CurriculumChilds.PreviousJobs;
import isel.ps.employbox.model.entities.CurriculumChilds.Project;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Curriculum implements DomainObject<Long> {
    @Id(isIdentity = true)
    private final long curriculumId;
    private final long accountId;
    private final String title;
    @Version
    private final long version;
    @ColumnName(foreignName = "curriculumId" )
    private final CompletableFuture<List<PreviousJobs>> previousJobs;
    @ColumnName(foreignName = "curriculumId" )
    private final CompletableFuture<List<AcademicBackground>> academicBackground;
    @ColumnName(foreignName = "curriculumId" )
    private final CompletableFuture<List<Project>> projects;
    @ColumnName(foreignName = "curriculumId" )
    private final CompletableFuture<List<CurriculumExperience>> experiences;

    public Curriculum(){
        accountId = 0;
        title = null;
        version = 0;
        previousJobs = null;
        academicBackground = null;
        projects = null;
        experiences = null;
        curriculumId = 0;
    }

    public Curriculum(long accountId, long curriculumId, String title, long version) {
        this.accountId = accountId;
        this.curriculumId = curriculumId;
        this.title= title;
        this.previousJobs = null;
        this.academicBackground = null;
        this.projects = null;
        this.experiences = null;
        this.version = version;
    }

    public Curriculum(long accountId,
                      long curriculumId,
                      String title,
                      long version,
                      List<PreviousJobs> previousJobsList,
                      List<AcademicBackground> academicBackgroundList,
                      List<CurriculumExperience> experiencesList,
                      List<Project> projectsList
    ) {
        this.accountId = accountId;
        this.curriculumId = curriculumId;
        this.title = title;
        this.previousJobs =  CompletableFuture.completedFuture(previousJobsList);
        this.academicBackground = CompletableFuture.completedFuture( academicBackgroundList);
        this.experiences = CompletableFuture.completedFuture(experiencesList);
        this.projects = CompletableFuture.completedFuture(projectsList);
        this.version = version;
    }

    @Override
    public Long getIdentityKey() {
        return curriculumId;
    }

    public long getVersion() {
        return version;
    }

    public long getAccountId() {
        return accountId;
    }

    public String getTitle() {
        return title;
    }

    public CompletableFuture<List<PreviousJobs>> getPreviousJobs() {
        return previousJobs;
    }

    public CompletableFuture<List<AcademicBackground>> getAcademicBackground() {
        return academicBackground;
    }

    public CompletableFuture<List<Project>> getProjects() {
        return projects;
    }

    public CompletableFuture<List<CurriculumExperience>> getExperiences() {
        return experiences;
    }


}
