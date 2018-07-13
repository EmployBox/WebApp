package isel.ps.employbox.model.input.curricula.childs;

import isel.ps.employbox.model.entities.curricula.childs.AcademicBackground;
import isel.ps.employbox.model.entities.curricula.childs.CurriculumExperience;
import isel.ps.employbox.model.entities.curricula.childs.PreviousJobs;
import isel.ps.employbox.model.entities.curricula.childs.Project;

import java.util.Collections;
import java.util.List;

public class InCurriculum {
    private long accountId;
    private long curriculumId;
    private String title;
    private List<CurriculumExperience> experiences = Collections.emptyList();
    private List<PreviousJobs> previousJobs = Collections.emptyList();
    private List<Project> projects = Collections.emptyList();
    private List<AcademicBackground> academicBackground = Collections.emptyList();
    private long version;

    public long getAccountId() {
        return accountId;
    }

    public long getCurriculumId() {
        return curriculumId;
    }

    public String getTitle() {
        return title;
    }

    public List<CurriculumExperience> getExperiences() {
        return experiences;
    }

    public List<PreviousJobs> getPreviousJobs() {
        return previousJobs;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public List<AcademicBackground> getAcademicBackground() {
        return academicBackground;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public void setCurriculumId(long curriculumId) {
        this.curriculumId = curriculumId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setExperiences(List<CurriculumExperience> experiences) {
        this.experiences = experiences;
    }

    public void setPreviousJobs(List<PreviousJobs> previousJobs) {
        this.previousJobs = previousJobs;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public void setAcademicBackground(List<AcademicBackground> academicBackground) {
        this.academicBackground = academicBackground;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}