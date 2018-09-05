package isel.ps.employbox.controllers.curricula;

import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.binders.curricula.CurriculumBinder;
import isel.ps.employbox.model.entities.Curriculum;
import isel.ps.employbox.model.input.curricula.childs.InCurriculum;
import isel.ps.employbox.model.output.Collections.HalCollectionPage;
import isel.ps.employbox.model.output.OutCurriculum;
import isel.ps.employbox.services.curricula.CurriculumService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

import static isel.ps.employbox.ErrorMessages.BAD_REQUEST_IDS_MISMATCH;

@RestController
@RequestMapping("/accounts/users/{id}/curricula")
public class CurriculumController {
    private final CurriculumBinder curriculumBinder;
    private final CurriculumService curriculumService;

    public CurriculumController(
            CurriculumService userService,
            CurriculumBinder curriculumBinder
    ) {
        this.curriculumService = userService;
        this.curriculumBinder = curriculumBinder;
    }

    @GetMapping
    public Mono<HalCollectionPage<Curriculum>> getCurricula(
            @PathVariable long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String orderColumn,
            @RequestParam(required = false, defaultValue = "ASC") String orderClause

    ){
        CompletableFuture<HalCollectionPage<Curriculum>> future = curriculumService.getCurricula(id, page, pageSize, orderColumn, orderClause)
                .thenCompose(curriculumCollectionPage -> curriculumBinder.bindOutput(curriculumCollectionPage, this.getClass(), id));
        return Mono.fromFuture(future);
    }

    @GetMapping("/{cid}")
    public Mono<OutCurriculum> getCurriculum(@PathVariable long id, @PathVariable long cid){
        CompletableFuture<OutCurriculum> future = curriculumService.getCurriculum(id, cid)
                .thenCompose(curriculumBinder::bindOutput);
        return Mono.fromFuture(future);
    }

    @PostMapping
    public Mono<OutCurriculum> createCurriculum( @PathVariable long id, @RequestBody InCurriculum inCurriculum, Authentication authentication){
        CompletableFuture<OutCurriculum> future = curriculumService.createCurriculum(id, curriculumBinder.bindInput(inCurriculum), authentication.getName())
                .thenCompose(curriculumBinder::bindOutput);
        return Mono.fromFuture(future);
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
        return curriculumService.deleteCurriculum(id, cid,null, authentication.getName());
    }
}
