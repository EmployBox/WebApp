package model;

public class Curriculum extends DomainObject<String>{

    private final long accountId;
    private final long curriculumId;

    private final Iterable<PreviousJobs> previousJobs;
    private final Iterable<AcademicBackground> academicBackground;
    private final Iterable<Project> projects;

    private Curriculum(long accountId,
                       long curriculumId,
                       Iterable<PreviousJobs> previousJobs,
                       Iterable<AcademicBackground> academicBackground,
                       Iterable<Project> projects,
                       long version) {
        super(String.format("%d%n-%d%n", accountId, curriculumId), "", version);
        this.accountId = accountId;
        this.curriculumId = curriculumId;
        this.previousJobs =  previousJobs;
        this.academicBackground = academicBackground;
        this.projects = projects;
    }

    public static Curriculum create(long accountId,
                                    Iterable<PreviousJobs> previousJobs,
                                    Iterable<AcademicBackground> academicBackground,
                                    Iterable<Project> projects,
                                    long version)
    {
        Curriculum curriculum = new Curriculum(accountId, -1,  previousJobs, academicBackground, projects,version);
        curriculum.markNew();
        return curriculum;
    }

    public static Curriculum load(long accountId,
                                  long curriculumId,
                                  long version,
                                  Iterable<PreviousJobs> previousJobs,
                                  Iterable<AcademicBackground> academicBackground,
                                  Iterable<Project> projects)
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

    public Iterable<PreviousJobs> getPreviousJobs() {
        return previousJobs;
    }

    public Iterable<AcademicBackground> getAcademicBackground() {
        return academicBackground;
    }

    public Iterable<Project> getProjects() {
        return projects;
    }
}
