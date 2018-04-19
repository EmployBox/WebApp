package isel.ps.employbox.model.entities;

import org.github.isel.rapper.DomainObject;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class Curriculum implements DomainObject<Long> {

    private final long userId;
    private long curriculumId;
    private final String title;
    private final long version;
    private final Supplier<List<PreviousJobs>> previousJobs;
    private final Supplier<List<AcademicBackground>> academicBackground;
    private final Supplier<List<Project>> projects;
    private final Supplier<List<CurriculumExperience>> experiences;

    public Curriculum(long userId, long curriculumId, String title, long version, Supplier<List<PreviousJobs>> previousJobs, Supplier<List<AcademicBackground>> academicBackground,
                      Supplier<List<Project>> projects, Supplier<List<CurriculumExperience>> experiences) {
        this.userId = userId;
        this.curriculumId = curriculumId;
        this.title = title;
        this.version = version;
        this.previousJobs =  previousJobs;
        this.academicBackground = academicBackground;
        this.projects = projects;
        this.experiences = experiences;
    }

    public Curriculum(long id, String title) {
        this.userId = id;
        this.title= title;
        this.previousJobs = Collections::emptyList;
        this.academicBackground = Collections::emptyList;
        this.projects = Collections::emptyList;
        this.experiences = Collections::emptyList;
        this.version = -1;
    }

    @Override
    public Long getIdentityKey() {
        return curriculumId;
    }

    public long getVersion() {
        return version;
    }

    public long getUserId() {
        return userId;
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
