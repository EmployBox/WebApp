package isel.ps.employbox.controllers;

import com.github.jayield.rapper.DataRepository;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.binder.CurriclumBinders.*;
import isel.ps.employbox.model.entities.*;
import isel.ps.employbox.model.entities.CurriculumChilds.AcademicBackground;
import isel.ps.employbox.model.entities.CurriculumChilds.CurriculumExperience;
import isel.ps.employbox.model.entities.CurriculumChilds.PreviousJobs;
import isel.ps.employbox.model.entities.CurriculumChilds.Project;
import isel.ps.employbox.model.input.InCurriculum;
import isel.ps.employbox.model.output.*;
import isel.ps.employbox.services.curricula.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static isel.ps.employbox.ErrorMessages.BAD_REQUEST_IDS_MISMATCH;

@RestController
@RequestMapping("/accounts/users/{id}/curricula")
public class CurriculumController {

    private final DataRepository <Project, Long> projectRepo;
    private final DataRepository <AcademicBackground, Long> backgroundRepo;
    private final DataRepository <PreviousJobs, Long> prevJobRepo;
    private final DataRepository <CurriculumExperience, Long> currExpRepo;


    private final CurriculumBinder curriculumBinder;
    private final AcademicBackgroundBinder academicBackgroundBinder;
    private final PreviousJobsBinder previousJobsBinder;
    private final CurriculumExperienceBinder curriculumExperienceBinder;
    private final ProjectBinder projectBinder;
    private final PreviousJobService previousJobService;
    private final CurriculumService curriculumService;
    private final AcademicBackgroundService academicBackgroundService;
    private final ProjectService projectService;
    private final CurriculumExperienceService curriculumExperienceService;

    public CurriculumController(
            DataRepository<Project, Long> projectRepository,
            DataRepository<AcademicBackground, Long> academicBackgroundLongDataRepository,
            DataRepository<PreviousJobs, Long> previousJobLongDataRepository,
            DataRepository<CurriculumExperience, Long> curriculumExperienceLongDataRepository,
            CurriculumService userService,
            CurriculumBinder curriculumBinder,
            AcademicBackgroundBinder academicBackgroundBinder,
            PreviousJobsBinder previousJobsBinder,
            CurriculumExperienceBinder curriculumExperienceBinder,
            ProjectBinder projectBinder, PreviousJobService previousJobService,
            AcademicBackgroundService academicBackgroundService,
            ProjectService projectService,
            CurriculumExperienceService curriculumExperienceService)
    {
        this.projectRepo = projectRepository;
        this.backgroundRepo = academicBackgroundLongDataRepository;
        this.prevJobRepo = previousJobLongDataRepository;
        this.currExpRepo = curriculumExperienceLongDataRepository;
        this.curriculumService = userService;
        this.curriculumBinder = curriculumBinder;
        this.academicBackgroundBinder = academicBackgroundBinder;
        this.previousJobsBinder = previousJobsBinder;
        this.curriculumExperienceBinder = curriculumExperienceBinder;
        this.projectBinder = projectBinder;
        this.previousJobService = previousJobService;
        this.academicBackgroundService = academicBackgroundService;
        this.projectService = projectService;
        this.curriculumExperienceService = curriculumExperienceService;
    }

    @GetMapping
    public Mono<HalCollectionPage> getCurricula(@PathVariable long id, @RequestParam(defaultValue = "0") int page, Authentication authentication){
        return curriculumBinder.bindOutput(
                curriculumService.getCurricula(id, authentication.getName(), page),
                this.getClass(),
                id
        );
    }

    @GetMapping("/{cid}")
    public Mono<OutCurriculum> getCurriculum(@PathVariable long id, @PathVariable long cid,  Authentication authentication){
        return curriculumBinder.bindOutput(
                curriculumService.getCurriculum(id, cid, authentication.getName()));
    }

    @GetMapping("/{cid}/academic/{academicId}")
    public Mono<OutAcademicBackground> getAcademicBackground(
            @PathVariable long id,
            @PathVariable long cid,
            @PathVariable long academicId){
        return academicBackgroundBinder.bindOutput(curriculumService.getCurriculumChild(backgroundRepo, id, cid, academicId));
    }

    @GetMapping("/{cid}/academic")
    public Mono<HalCollectionPage> getAcademicBackground(@PathVariable long id, @PathVariable long cid, @RequestParam(defaultValue = "0") int page){
        return academicBackgroundBinder.bindOutput(
                academicBackgroundService.getCurriculumAcademicBackgrounds( cid, page ),
                this.getClass(),
                id,
                cid
        );
    }

    @GetMapping("/{cid}/projects/{projectId}")
    public Mono<OutProject> getProjects (
            @PathVariable long id,
            @PathVariable long cid,
            @PathVariable long projectId){
        return projectBinder.bindOutput(curriculumService.getCurriculumChild(projectRepo, id, cid, projectId));
    }

    @GetMapping("/{cid}/projects")
    public Mono<HalCollectionPage> getProjects(
            @PathVariable long id,
            @PathVariable long cid,
            @RequestParam(defaultValue = "0") int page
    ) {
        return projectBinder.bindOutput(
                projectService.getCurriculumProjects(cid, page),
                this.getClass(),
                id,
                cid
        );
    }

    @GetMapping("/{cid}/previousJobs/{prvJbId}")
    public Mono<OutPreviousJobs> getPreviousJob (
            @PathVariable long id,
            @PathVariable long cid,
            @PathVariable long prvJbId){
        return previousJobsBinder.bindOutput(curriculumService.getCurriculumChild(prevJobRepo, id, cid, prvJbId));
    }

    @GetMapping("/{cid}/previousJobs")
    public Mono<HalCollectionPage> getPreviousJobs(
            @PathVariable long id,
            @PathVariable long cid,
            @RequestParam(defaultValue = "0") int page
    ){
        return previousJobsBinder.bindOutput(
                previousJobService.getCurriculumPreviousJobs( cid, page ),
                this.getClass(),
                id,
                cid );
    }

    @GetMapping("/{cid}/experiences/{expId}")
    public Mono<OutCurriculumExperience> getCurriculumExperience (
            @PathVariable long id,
            @PathVariable long cid,
            @PathVariable long expId){
        return curriculumExperienceBinder.bindOutput(curriculumService.getCurriculumChild(currExpRepo, id, cid, expId));
    }

    @GetMapping("/{cid}/experiences")
    public Mono<HalCollectionPage> getCurriculumExperiences (
            @PathVariable long id,
            @PathVariable long cid,
            @RequestParam(defaultValue = "0") int page
    ){
        return curriculumExperienceBinder.bindOutput(
                curriculumExperienceService.getCurriculumExperiences(cid, page),
                this.getClass(),
                id
        );
    }

    @PostMapping
    public Mono<OutCurriculum> createCurriculum( @PathVariable long id, @RequestBody InCurriculum inCurriculum, Authentication authentication){
        return curriculumBinder.bindOutput(curriculumService.createCurriculum(id, curriculumBinder.bindInput(inCurriculum), authentication.getName()));
    }

    @PutMapping("/{cid}")
    public Mono<Void> updateCurriculum(
            @PathVariable long id,
            @PathVariable long cid,
            @RequestBody InCurriculum inCurriculum,
            Authentication authentication
    ){
        if(inCurriculum.getAccountId() != id || inCurriculum.getCurriculumId() != cid) throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
        Curriculum curriculum = curriculumBinder.bindInput(inCurriculum);
        return curriculumService.updateCurriculum(curriculum,authentication.getName() );
    }

    @DeleteMapping("/{cid}")
    public Mono<Void> deleteCurriculum( @PathVariable long id, @PathVariable long cid, Authentication authentication){
        return curriculumService.deleteCurriculum(id, cid, authentication.getName());
    }

    @PostMapping("/{cid}/curriculumExp")
    public Mono<CurriculumExperience> addCurriculumExperienceToJob(
            @PathVariable long id,
            @PathVariable long cid,
            @RequestBody CurriculumExperience curriculumExperience,
            Authentication authentication)
    {
        return Mono.fromFuture( curriculumExperienceService.addCurriculumExperience(id,cid, curriculumExperience,authentication.getName()));
    }

    @PutMapping("/{cid}/curriculumExp/{ceId}")
    public Mono<Void> updateCurriculumExperience(
            @PathVariable long id,
            @PathVariable long cid,
            @PathVariable long jeId,
            @RequestBody CurriculumExperience curriculumExperience,
            Authentication authentication
    ){
        if(curriculumExperience.getAccountId() != id || curriculumExperience.getCurriculumId() != cid || curriculumExperience.getIdentityKey() != jeId)
            throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
        return curriculumExperienceService.updateCurriculumExperience(id, cid, curriculumExperience,authentication.getName() );
    }

    @DeleteMapping("/{cid}/curriculumExp/{ceId}")
    public Mono<Void> deleteCurriculumExperience(
            @PathVariable long ceId,
            @PathVariable long id,
            @PathVariable long cid,
            Authentication authentication)
    {
        return curriculumExperienceService.deleteCurriculumExperience(ceId, id, cid, authentication.getName());
    }

    @PostMapping("/{cid}/background/{abkId}")
    public Mono<AcademicBackground> addAcademicBackground (
            @PathVariable long abkId,
            @PathVariable long id,
            @PathVariable long cid,
            @RequestBody AcademicBackground academicBackground,
            Authentication authentication)
    {
        return Mono.fromFuture( academicBackgroundService.addAcademicBackgroundToCurriculum(abkId, id,cid, academicBackground,authentication.getName()));
    }

    @PutMapping("/{cid}/background/{abkId}")
    public Mono<Void> updateAcademicBackground(
            @PathVariable long id,
            @PathVariable long cid,
            @RequestBody AcademicBackground academicBackground,
            Authentication authentication)
    {
        if(academicBackground.getAccountId() != id || academicBackground.getCurriculumId() != cid)
            throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
        return academicBackgroundService.updateAcademicBackground(id, cid, academicBackground, authentication.getName() );
    }

    @DeleteMapping("/{cid}/background/{abkId}")
    public Mono<Void> deleteAcademicBackground(
            @PathVariable long abkId,
            @PathVariable long id,
            @PathVariable long cid,
            Authentication authentication)
    {
        return academicBackgroundService.deleteAcademicBackground(abkId, id, cid, authentication.getName());
    }

    @PostMapping("/{cid}/project")
    public Mono<Project> addProject(
            @PathVariable long id,
            @PathVariable long cid,
            @RequestBody Project project,
            Authentication authentication)
    {
        return Mono.fromFuture( projectService.addProjectToCurriculum(id,cid, project,authentication.getName()));
    }

    @PutMapping("/{cid}/project/{ceId}")
    public Mono<Void> updateProject(
            @PathVariable long ceId,
            @PathVariable long id,
            @PathVariable long cid,
            @RequestBody Project project,
            Authentication authentication
    ){
        if(project.getAccountId() != id || project.getCurriculumId() != cid)
            throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
        return projectService.updateProject(ceId, id, cid, project,authentication.getName() );
    }

    @DeleteMapping("/{cid}/project/{pjId}")
    public Mono<Void> deleteProject(
            @PathVariable long pjId,
            @PathVariable long id,
            @PathVariable long cid,
            Authentication authentication)
    {
        return projectService.deleteProject(pjId, id, cid, authentication.getName());
    }

    @PostMapping("/{cid}/previousJob")
    public Mono<PreviousJobs> addPreviousJob (
            @PathVariable long id,
            @PathVariable long cid,
            @RequestBody PreviousJobs previousJobs,
            Authentication authentication)
    {
        return Mono.fromFuture( previousJobService.addPreviousJobToCurriculum(id,cid, previousJobs,authentication.getName()));
    }

    @PutMapping("/{cid}/previousJob/{pvjId}")
    public Mono<Void> updatePreviousJob(
            @PathVariable long pvjId,
            @PathVariable long id,
            @PathVariable long cid,
            @RequestBody PreviousJobs previousJobs,
            Authentication authentication)
    {
        if(previousJobs.getAccountId() != id || previousJobs.getCurriculumId() != cid)
            throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
        return previousJobService.updatePreviousJob(pvjId, id, cid, previousJobs,authentication.getName() );
    }

    @DeleteMapping("/{cid}/previousJob/{pvjId}")
    public Mono<Void> deletePreviousJob(
            @PathVariable long pvjId,
            @PathVariable long id,
            @PathVariable long cid,
            Authentication authentication)
    {
        return previousJobService.deletePreviousJob(pvjId, id, cid, authentication.getName());
    }
}
