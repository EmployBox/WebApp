package isel.ps.employbox.model.entities.composedKeys;

public class UserCurriculumKey {
    private final long userId;
    private final long curriculumId;

    public UserCurriculumKey(long userId, long curriculumId) {
        this.userId = userId;
        this.curriculumId = curriculumId;
    }

    public long getUserId() {
        return userId;
    }

    public long getCurriculumId() {
        return curriculumId;
    }
}
