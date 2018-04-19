package isel.ps.employbox.model.entities.composedKeys;

public class AcademicBackgroundKey {
    private Long userId;
    private Long CurriculumId;

    public AcademicBackgroundKey(Long userId, Long curriculumId) {
        this.userId = userId;
        CurriculumId = curriculumId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getCurriculumId() {
        return CurriculumId;
    }
}
