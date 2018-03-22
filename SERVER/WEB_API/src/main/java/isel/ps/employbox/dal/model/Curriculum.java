package isel.ps.employbox.dal.model;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class Curriculum extends DomainObject<String>{

    @ID
    private final long accountId;
    @ID
    private long curriculumId;
    private final String title;

    private final Supplier<List<PreviousJobs>> previousJobs;
    private final Supplier<List<AcademicBackground>> academicBackground;
    private final Supplier<List<Project>> projects;
    private final Supplier<List<CurriculumExperience>> experiences;

    public Curriculum(
            long accountId,
            long curriculumId,
            String title,
            long version,
            Supplier<List<PreviousJobs>> previousJobs,
            Supplier<List<AcademicBackground>> academicBackground,
            Supplier<List<Project>> projects,
            Supplier<List<CurriculumExperience>> experiences
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

    public Curriculum(long id, String title) {
        super(Long.toString(id),-1);
        this.accountId = id;
        this.title= title;
        this.previousJobs = Collections::emptyList;
        this.academicBackground = Collections::emptyList;
        this.projects = Collections::emptyList;
        this.experiences = Collections::emptyList;
    }

    public static Curriculum create(
            long accountId,
            String title,
            long version,
            Supplier<List<PreviousJobs>> previousJobs,
            Supplier<List<AcademicBackground>> academicBackground,
            Supplier<List<Project>> projects,
            Supplier<List<CurriculumExperience>> experiences
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
            Supplier<List<PreviousJobs>> previousJobs,
            Supplier<List<AcademicBackground>> academicBackground,
            Supplier<List<Project>> projects,
            Supplier<List<CurriculumExperience>> experiences
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

    public Supplier<List<PreviousJobs>> getPreviousJobs() {
        return previousJobs;
    }

    public Supplier<List<AcademicBackground>> getAcademicBackground() {
        return academicBackground;
    }

    public Supplier<List<Project>> getProjects() {
        return projects;
    }

    public Supplier<List<CurriculumExperience>> getExperiences() {
        return experiences;
    }
}
