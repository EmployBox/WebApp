package isel.ps.employbox.model.input.curricula.childs;

import java.util.Collections;
import java.util.List;

public class InCurriculum {
    private long accountId;
    private long curriculumId;
    private String title;
    private List<InCurriculumExperience> experiences = Collections.emptyList();
    private List<InPreviousJobs> previousJobs = Collections.emptyList();
    private List<InProject> projects = Collections.emptyList();
    private List<InAcademicBackground> academicBackground = Collections.emptyList();
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

    public List<InCurriculumExperience> getExperiences() {
        return experiences;
    }

    public List<InPreviousJobs> getPreviousJobs() {
        return previousJobs;
    }

    public List<InProject> getProjects() {
        return projects;
    }

    public List<InAcademicBackground> getAcademicBackground() {
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

    public void setExperiences(List<InCurriculumExperience> experiences) {
        this.experiences = experiences;
    }

    public void setPreviousJobs(List<InPreviousJobs> previousJobs) {
        this.previousJobs = previousJobs;
    }

    public void setProjects(List<InProject> projects) {
        this.projects = projects;
    }

    public void setAcademicBackground(List<InAcademicBackground> academicBackground) {
        this.academicBackground = academicBackground;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}
