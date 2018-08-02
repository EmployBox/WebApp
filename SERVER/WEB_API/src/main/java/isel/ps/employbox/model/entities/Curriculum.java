package isel.ps.employbox.model.entities;

import com.github.jayield.rapper.DomainObject;
import com.github.jayield.rapper.annotations.ColumnName;
import com.github.jayield.rapper.annotations.Id;
import com.github.jayield.rapper.annotations.Version;
import isel.ps.employbox.model.entities.curricula.childs.AcademicBackground;
import isel.ps.employbox.model.entities.curricula.childs.CurriculumExperience;
import isel.ps.employbox.model.entities.curricula.childs.PreviousJobs;
import isel.ps.employbox.model.entities.curricula.childs.Project;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class Curriculum implements DomainObject<Long> {
    @Id(isIdentity = true)
    private final long curriculumId;
    private final long accountId;
    private final String title;
    @Version
    private final long version;
    @ColumnName(foreignName = "curriculumId" )
    private final Supplier<CompletableFuture<List<PreviousJobs>>> previousJobs;
    @ColumnName(foreignName = "curriculumId" )
    private final Supplier<CompletableFuture<List<AcademicBackground>>> academicBackground;
    @ColumnName(foreignName = "curriculumId" )
    private final Supplier<CompletableFuture<List<Project>>> projects;
    @ColumnName(foreignName = "curriculumId" )
    private final Supplier<CompletableFuture<List<CurriculumExperience>>> experiences;

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

    public Curriculum(
            long accountId,
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
        this.previousJobs =  () -> CompletableFuture.completedFuture(previousJobsList);
        this.academicBackground = () -> CompletableFuture.completedFuture(academicBackgroundList);
        this.experiences =  () -> CompletableFuture.completedFuture(experiencesList);
        this.projects =  () -> CompletableFuture.completedFuture(projectsList);
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

    public Supplier<CompletableFuture<List<PreviousJobs>>> getPreviousJobs() {
        return previousJobs;
    }

    public Supplier<CompletableFuture<List<AcademicBackground>>> getAcademicBackground() {
        return academicBackground;
    }

    public Supplier<CompletableFuture<List<Project>>> getProjects() {
        return projects;
    }

    public Supplier<CompletableFuture<List<CurriculumExperience>>> getExperiences() {
        return experiences;
    }

}
