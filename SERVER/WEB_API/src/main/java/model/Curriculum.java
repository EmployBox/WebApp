package model;

import java.util.stream.Stream;

public class Curriculum extends DomainObject{

    private final long accountId;
    private final long curriculumId;

    private final Stream<PreviousJobs> previousJobs;
    private final Stream<AcademicBackground> academicBackground;

    private Curriculum(long accountId, long curriculumId, Stream<PreviousJobs> previousJobs, Stream<AcademicBackground> academicBackground) {
        super(String.format("%d%n-%d%n", accountId, curriculumId));
        this.accountId = accountId;
        this.curriculumId = curriculumId;
        this.previousJobs = previousJobs;
        this.academicBackground = academicBackground;
    }

    public static Curriculum create(long accountId, Stream<PreviousJobs> previousJobs, Stream<AcademicBackground> academicBackground){
        Curriculum curriculum = new Curriculum(accountId, -1, previousJobs, academicBackground);
        curriculum.markNew();
        return curriculum;
    }

    public static Curriculum load(long accountId, long curriculumId, Stream<PreviousJobs> previousJobs, Stream<AcademicBackground> academicBackground){
        Curriculum curriculum = new Curriculum(accountId, curriculumId, previousJobs, academicBackground);
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
