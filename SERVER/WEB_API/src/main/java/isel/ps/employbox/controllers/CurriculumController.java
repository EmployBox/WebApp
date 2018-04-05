package isel.ps.employbox.controllers;

import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.binder.CurriculumBinder;
import isel.ps.employbox.model.entities.Curriculum;
import isel.ps.employbox.model.input.InCurriculum;
import isel.ps.employbox.model.output.HalCollection;
import isel.ps.employbox.model.output.OutCurriculum;
import isel.ps.employbox.services.APIService;
import isel.ps.employbox.services.UserService;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static isel.ps.employbox.ErrorMessages.BAD_REQUEST_IDS_MISMATCH;

@RestController
@RequestMapping("/accounts/users/{id}/curriculum")
public class CurriculumController {

    private APIService apiService;
    private UserService userService;
    private CurriculumBinder curriculumBinder;

    public CurriculumController(APIService apiService, UserService userService, CurriculumBinder curriculumBinder){
        this.apiService = apiService;
        this.userService = userService;
        this.curriculumBinder = curriculumBinder;
    }

    @GetMapping
    public Resource<HalCollection> getAllCurriculums(@PathVariable long id, @RequestParam Map<String,String> queryString){
        return curriculumBinder.bindOutput(
                userService.getAllCurriculums(id, queryString),
                this.getClass(),
                id
        );
    }

    @GetMapping("/{cid}")
    public Resource<OutCurriculum> getCurriculum(@PathVariable long id, @PathVariable long cid){
        return curriculumBinder.bindOutput(
                userService.getCurriculum(id, cid));
    }

    @PutMapping("/{cid}")
    public void updateCurriculum(
            @RequestHeader("apiKey") String apiKey,
            @PathVariable long id,
            @PathVariable long cid,
            @RequestBody InCurriculum inCurriculum
    ){
        apiService.validateAPIKey(apiKey);
        if(inCurriculum.getAccountID() != id || inCurriculum.getId() != cid) throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
        Curriculum curriculum = curriculumBinder.bindInput(inCurriculum);
        userService.updateCurriculum(curriculum);
    }

    @PostMapping
    public void createCurriculum(@RequestHeader("apiKey") String apiKey, @PathVariable long id, @RequestBody InCurriculum inCurriculum){
        apiService.validateAPIKey(apiKey);
        userService.createCurriculum(curriculumBinder.bindInput(inCurriculum));
    }

    @DeleteMapping("/{cid}")
    public void deleteCurriculum(@RequestHeader("apiKey") String apiKey, @PathVariable long id, @PathVariable long cid){
        apiService.validateAPIKey(apiKey);
        userService.deleteCurriculum(id, cid);
    }
}
