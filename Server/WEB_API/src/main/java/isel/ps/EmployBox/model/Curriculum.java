package isel.ps.EmployBox.model;

import isel.ps.EmployBox.util.Streamable;

import java.util.stream.Stream;

public class Curriculum extends DomainObject<String>{

    @ID
    private final long accountId;
    @ID
    private long curriculumId;
    private final String title;

    private final Streamable<PreviousJobs> previousJobs;
    private final Streamable<AcademicBackground> academicBackground;
    private final Streamable<Project> projects;
    private final Streamable<CurriculumExperience> experiences;

    private Curriculum(
            long accountId,
            long curriculumId,
            String title,
            long version,
            Streamable<PreviousJobs> previousJobs,
            Streamable<AcademicBackground> academicBackground,
            Streamable<Project> projects,
            Streamable<CurriculumExperience> experiences
    ) {
        super(String.format("%d%n-%d%n", accountId, curriculumId), version);
        this.accountId = accountId;
        this.curriculumId = curriculumId;
        this.title = title;
        this.previousJobs =  previousJobs;
        this.academicBackground = academicBackground;
        this.projects = projects;
        this.experiences = experiences;
    }

    public static Curriculum create(
            long accountId,
            String title,
            long version,
            Streamable<PreviousJobs> previousJobs,
            Streamable<AcademicBackground> academicBackground,
            Streamable<Project> projects,
            Streamable<CurriculumExperience> experiences
    ) {
        Curriculum curriculum = new Curriculum(accountId, defaultKey, title, version, previousJobs, academicBackground, projects, experiences);
        curriculum.markNew();
        return curriculum;
    }

    public static Curriculum load(
            long accountId,
            long curriculumId,
            String title,
            long version,
            Streamable<PreviousJobs> previousJobs,
            Streamable<AcademicBackground> academicBackground,
            Streamable<Project> projects,
            Streamable<CurriculumExperience> experiences
    ) {
        Curriculum curriculum = new Curriculum(accountId, curriculumId, title, version, previousJobs, academicBackground, projects, experiences);
        curriculum.markClean();
        return curriculum;
    }

    public long getAccountId() {
        return accountId;
    }

    public long getCurriculumId() {
        return curriculumId;
    }

    public String getTitle() {
        return title;
    }

    public Stream<PreviousJobs> getPreviousJobs() {
        return previousJobs.get();
    }

    public Stream<AcademicBackground> getAcademicBackground() {
        return academicBackground.get();
    }

    public Stream<Project> getProjects() {
        return projects.get();
    }

    public Stream<CurriculumExperience> getExperiences() {
        return experiences.get();
    }
}
