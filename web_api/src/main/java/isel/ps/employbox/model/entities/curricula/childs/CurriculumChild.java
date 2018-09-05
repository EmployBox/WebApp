package isel.ps.employbox.model.entities.curricula.childs;

import org.springframework.stereotype.Component;

@Component
public abstract class CurriculumChild{
    public abstract long getCurriculumId();
    public abstract long getAccountId();
    public abstract void setAccountId(long l);
    public abstract void setCurriculumId(long l);
}
