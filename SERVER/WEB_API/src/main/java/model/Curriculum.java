package model;

import util.Streamable;

import java.util.stream.Stream;

public class Curriculum extends DomainObject<String>{

    private final long accountId;
    private final long curriculumId;

    private final Streamable<PreviousJobs> previousJobs;
    private final Streamable<AcademicBackground> academicBackground;
    private final Streamable<Project> projects;

    private Curriculum(long accountId,
                       long curriculumId,
                       Streamable<PreviousJobs> previousJobs,
                       Streamable<AcademicBackground> academicBackground,
                       Streamable<Project> projects,
                       long version) {
        super(String.format("%d%n-%d%n", accountId, curriculumId), version);
        this.accountId = accountId;
        this.curriculumId = curriculumId;
        this.previousJobs =  previousJobs;
        this.academicBackground = academicBackground;
        this.projects = projects;
    }

    public static Curriculum create(long accountId,
                                    Streamable<PreviousJobs> previousJobs,
                                    Streamable<AcademicBackground> academicBackground,
                                    Streamable<Project> projects,
                                    long version)
    {
        Curriculum curriculum = new Curriculum(accountId, -1,  previousJobs, academicBackground, projects,version);
        curriculum.markNew();
        return curriculum;
    }

    public static Curriculum load(long accountId,
                                  long curriculumId,
                                  long version,
                                  Streamable<PreviousJobs> previousJobs,
                                  Streamable<AcademicBackground> academicBackground,
                                  Streamable<Project> projects)
    {
        Curriculum curriculum = new Curriculum(accountId, curriculumId,  previousJobs, academicBackground, projects, version);
        curriculum.markClean();
        return curriculum;
    }

    public long getAccountId() {
        return accountId;
    }

    public long getCurriculumId() {
        return curriculumId;
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
}
