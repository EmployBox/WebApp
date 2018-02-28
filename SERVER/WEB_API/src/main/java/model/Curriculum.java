package model;

public class Curriculum extends DomainObject{

    private final long accountId;
    private final long curriculumId;

    private Curriculum(long accountId, long curriculumId) {
        super(String.format("%d%n-%d%n", accountId, curriculumId));
        this.accountId = accountId;
        this.curriculumId = curriculumId;
    }

    public static Curriculum create(long accountId){
        Curriculum curriculum = new Curriculum(accountId, -1);
        curriculum.markNew();
        return curriculum;
    }

    public static Curriculum load(long accountId, long curriculumId){
        Curriculum curriculum = new Curriculum(accountId, curriculumId);
        curriculum.markClean();
        return curriculum;
    }

    public long getAccountId() {
        return accountId;
    }

    public long getCurriculumId() {
        return curriculumId;
    }
}
