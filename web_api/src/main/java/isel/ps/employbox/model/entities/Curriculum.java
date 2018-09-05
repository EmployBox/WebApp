package isel.ps.employbox.model.entities;

import com.github.jayield.rapper.DomainObject;
import com.github.jayield.rapper.annotations.ColumnName;
import com.github.jayield.rapper.annotations.Id;
import com.github.jayield.rapper.annotations.Version;
import com.github.jayield.rapper.unitofwork.UnitOfWork;
import isel.ps.employbox.model.binders.curricula.AcademicBackgroundBinder;
import isel.ps.employbox.model.binders.curricula.CurriculumExperienceBinder;
import isel.ps.employbox.model.binders.curricula.PreviousJobsBinder;
import isel.ps.employbox.model.binders.curricula.ProjectBinder;
import isel.ps.employbox.model.entities.curricula.childs.AcademicBackground;
import isel.ps.employbox.model.entities.curricula.childs.CurriculumExperience;
import isel.ps.employbox.model.entities.curricula.childs.PreviousJobs;
import isel.ps.employbox.model.entities.curricula.childs.Project;
import isel.ps.employbox.model.entities.jobs.Application;
import isel.ps.employbox.model.input.curricula.childs.InAcademicBackground;
import isel.ps.employbox.model.input.curricula.childs.InCurriculumExperience;
import isel.ps.employbox.model.input.curricula.childs.InPreviousJobs;
import isel.ps.employbox.model.input.curricula.childs.InProject;
import org.springframework.boot.logging.LoggingSystemProperties;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Curriculum implements DomainObject<Long> {
    @Id(isIdentity = true)
    private final long curriculumId;
    private final long accountId;
    private final String title;
    @Version
    private final long version;
    @ColumnName(foreignName = "curriculumId" )
    private final Function<UnitOfWork, CompletableFuture<List<PreviousJobs>>> previousJobs;
    @ColumnName(foreignName = "curriculumId" )
    private final Function<UnitOfWork, CompletableFuture<List<AcademicBackground>>> academicBackground;
    @ColumnName(foreignName = "curriculumId" )
    private final Function<UnitOfWork, CompletableFuture<List<Project>>> projects;
    @ColumnName(foreignName = "curriculumId" )
    private final Function<UnitOfWork, CompletableFuture<List<CurriculumExperience>>> experiences;
    @ColumnName(foreignName = "curriculumId" )
    private final Function<UnitOfWork, CompletableFuture<List<Application>>> applications;

    public Curriculum(){
        accountId = 0;
        title = null;
        version = 0;
        previousJobs = null;
        academicBackground = null;
        applications = null;
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
        this.applications = null;
        this.projects = null;
        this.experiences = null;
        this.version = version;
    }

    public Curriculum(
            long accountId,
            long curriculumId,
            String title,
            long version,
            List<InPreviousJobs> previousJobs,
            List<InAcademicBackground> academicBackgroundList,
            List<InCurriculumExperience> experiences,
            List<InProject> projects
    ) {
        this.accountId = accountId;
        this.curriculumId = curriculumId;
        this.title = title;
        this.previousJobs = ignored -> {
            PreviousJobsBinder previousJobsBinder = new PreviousJobsBinder();

            List<PreviousJobs> list = previousJobsBinder.bindInput(previousJobs.stream()
                    .peek(inPrevious -> {
                        inPrevious.setCurriculumId(this.curriculumId);
                        inPrevious.setAccountId(accountId);
                    })).collect(Collectors.toList());
            return CompletableFuture.completedFuture(list);
        };

        this.experiences = ignored -> {
            CurriculumExperienceBinder curriculumExperienceBinder = new CurriculumExperienceBinder();

            List<CurriculumExperience> list = curriculumExperienceBinder.bindInput(experiences.stream()
                    .peek(inJobExperience -> {
                                inJobExperience.setCurriculumId(this.curriculumId);
                                inJobExperience.setAccountId(accountId);
                            }
                    )).collect(Collectors.toList());
            return CompletableFuture.completedFuture(list);
        };
        this.projects = ignored -> {
            ProjectBinder projectBinder = new ProjectBinder();

            List<Project> list = projectBinder.bindInput(projects.stream()
                    .peek(inProject -> {
                        inProject.setCurriculumId(this.curriculumId);
                        inProject.setAccountId(accountId);
                    })
            ).collect(Collectors.toList());
            return CompletableFuture.completedFuture(list);
        };
        this.academicBackground = ignored -> {
            AcademicBackgroundBinder academicBackgroundBinder = new AcademicBackgroundBinder();

            List<AcademicBackground> list = academicBackgroundBinder
                    .bindInput(academicBackgroundList.stream()
                            .peek(inAcademicBackground -> {
                                inAcademicBackground.setCurriculumId(this.curriculumId);
                                inAcademicBackground.setAccountId(this.accountId);
                            })
                    ).collect(Collectors.toList());
            return CompletableFuture.completedFuture(list);
        };
        this.applications = null;
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

    public Function<UnitOfWork, CompletableFuture<List<PreviousJobs>>> getPreviousJobs() {
        return previousJobs;
    }

    public Function<UnitOfWork, CompletableFuture<List<AcademicBackground>>> getAcademicBackground() {
        return academicBackground;
    }

    public Function<UnitOfWork, CompletableFuture<List<Project>>> getProjects() {
        return projects;
    }

    public Function<UnitOfWork, CompletableFuture<List<CurriculumExperience>>> getExperiences() {
        return experiences;
    }

    public Function<UnitOfWork, CompletableFuture<List<Application>>> getApplications() {
        return applications;
    }
}
