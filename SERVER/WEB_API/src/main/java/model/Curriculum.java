package model;

import java.util.stream.Stream;

public class Curriculum extends DomainObject<String>{

    private final long accountId;
    private final long curriculumId;

    private final Stream<PreviousJobs> previousJobs;
    private final Stream<AcademicBackground> academicBackground;
    private final Stream<Project> projects;

    private Curriculum(long accountId,
                       long curriculumId,
                       Stream<PreviousJobs> previousJobs,
                       Stream<AcademicBackground> academicBackground,
                       Stream<Project> projects) {
        super(String.format("%d%n-%d%n", accountId, curriculumId));
        this.accountId = accountId;
        this.curriculumId = curriculumId;
        this.previousJobs = previousJobs;
        this.academicBackground = academicBackground;
        this.projects = projects;
    }

    public static Curriculum create(long accountId,
                                    Stream<PreviousJobs> previousJobs,
                                    Stream<AcademicBackground> academicBackground,
                                    Stream<Project> projects)
    {
        Curriculum curriculum = new Curriculum(accountId, -1, previousJobs, academicBackground, projects);
        curriculum.markNew();
        return curriculum;
    }

    public static Curriculum load(long accountId,
                                  long curriculumId,
                                  Stream<PreviousJobs> previousJobs,
                                  Stream<AcademicBackground> academicBackground,
                                  Stream<Project> projects)
    {
        Curriculum curriculum = new Curriculum(accountId, curriculumId, previousJobs, academicBackground, projects);
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
        return previousJobs;
    }

    public Stream<AcademicBackground> getAcademicBackground() {
        return academicBackground;
    }
}
