package isel.ps.employbox.controllers;

import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.binder.CurriculumBinder;
import isel.ps.employbox.model.entities.Curriculum;
import isel.ps.employbox.model.input.InCurriculum;
import isel.ps.employbox.model.output.HalCollection;
import isel.ps.employbox.model.output.OutCurriculum;
import isel.ps.employbox.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static isel.ps.employbox.ErrorMessages.badRequest_IdsMismatch;

@RestController
@RequestMapping("/accounts/users/{id}/curricula")
public class CurriculumController {

    private UserService userService;
    private CurriculumBinder curriculumBinder;

    public CurriculumController( UserService userService, CurriculumBinder curriculumBinder){
        this.userService = userService;
        this.curriculumBinder = curriculumBinder;
    }

    @GetMapping
    public Mono<HalCollection> getCurricula(@PathVariable long id, Authentication authentication){
        return curriculumBinder.bindOutput(
                userService.getCurricula(id, authentication.getName()),
                this.getClass()
        );
    }

    @GetMapping("/{cid}")
    public Mono<OutCurriculum> getCurriculum(@PathVariable long id, @PathVariable long cid,  Authentication authentication){
        return curriculumBinder.bindOutput(
                userService.getCurriculum(id, cid, authentication.getName()));
    }

    @PutMapping("/{cid}")
    public Mono<Void> updateCurriculum(
            @PathVariable long id,
            @PathVariable long cid,
            @RequestBody InCurriculum inCurriculum,
            Authentication authentication
    ){
        if(inCurriculum.getAccountID() != id || inCurriculum.getId() != cid) throw new BadRequestException(badRequest_IdsMismatch);
        Curriculum curriculum = curriculumBinder.bindInput(inCurriculum);
        return userService.updateCurriculum(curriculum,authentication.getName() );
    }

    @PostMapping
    public Mono<OutCurriculum> createCurriculum( @PathVariable long id, @RequestBody InCurriculum inCurriculum, Authentication authentication){
        return curriculumBinder.bindOutput(
                userService.createCurriculum(id, curriculumBinder.bindInput(inCurriculum), authentication.getName())
        );
    }

    @DeleteMapping("/{cid}")
    public Mono<Void> deleteCurriculum( @PathVariable long id, @PathVariable long cid, Authentication authentication){
        return userService.deleteCurriculum(id, cid, authentication.getName());
    }
}
