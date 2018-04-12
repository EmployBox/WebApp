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
    public HalCollection getCurricula(@PathVariable long id,  Authentication authentication){
        return curriculumBinder.bindOutput(
                userService.getCurricula(id),
                this.getClass(),
                id,
                authentication.getName()
        );
    }

    @GetMapping("/{cid}")
    public OutCurriculum getCurriculum(@PathVariable long id, @PathVariable long cid,  Authentication authentication){
        return curriculumBinder.bindOutput(
                userService.getCurriculum(id, cid, authentication.getName()));
    }

    @PutMapping("/{cid}")
    public void updateCurriculum(
            @PathVariable long id,
            @PathVariable long cid,
            @RequestBody InCurriculum inCurriculum,
            Authentication authentication
    ){
        if(inCurriculum.getAccountID() != id || inCurriculum.getId() != cid) throw new BadRequestException(badRequest_IdsMismatch);
        Curriculum curriculum = curriculumBinder.bindInput(inCurriculum);
        userService.updateCurriculum(curriculum,authentication.getName() );
    }

    @PostMapping
    public void createCurriculum( @PathVariable long id, @RequestBody InCurriculum inCurriculum, Authentication authentication){
        userService.createCurriculum(id, curriculumBinder.bindInput(inCurriculum), authentication.getName());
    }

    @DeleteMapping("/{cid}")
    public void deleteCurriculum( @PathVariable long id, @PathVariable long cid, Authentication authentication){
        userService.deleteCurriculum(id, cid, authentication.getName());
    }
}
