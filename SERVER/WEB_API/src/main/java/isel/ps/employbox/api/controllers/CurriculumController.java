package isel.ps.employbox.api.controllers;

import isel.ps.employbox.api.exceptions.BadRequestException;
import isel.ps.employbox.api.model.binder.CurriculumBinder;
import isel.ps.employbox.api.model.input.InCurriculum;
import isel.ps.employbox.api.model.output.OutCurriculum;
import isel.ps.employbox.api.services.APIService;
import isel.ps.employbox.api.services.UserService;
import isel.ps.employbox.dal.model.Curriculum;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static isel.ps.employbox.api.ErrorMessages.BAD_REQUEST_IDS_MISMATCH;

@RestController
public class CurriculumController {

    private APIService apiService;
    private UserService userService;
    private CurriculumBinder curriculumBinder;

    public CurriculumController(APIService apiService, UserService userService, CurriculumBinder curriculumBinder){
        this.apiService = apiService;
        this.userService = userService;
        this.curriculumBinder = curriculumBinder;
    }

    @GetMapping("/account/user/{id}/curriculums")
    public List<OutCurriculum> getAllCurriculums(@PathVariable long id, @RequestParam Map<String,String> queryString){
        return curriculumBinder.bindOutput(
                userService.getAllCurriculums(id, queryString)
        );
    }

    @GetMapping("/account/user/{id}/curriculum/{cid}")
    public OutCurriculum getCurriculum(@PathVariable long id, @PathVariable long cid){
        return curriculumBinder.bindOutput(
                userService.getCurriculum(id, cid));
    }

    @PutMapping("/account/user/{id}/curriculum/{cid}")
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

    @PostMapping("/account/user/{id}/curriculum")
    public void createCurriculum(@RequestHeader("apiKey") String apiKey, @PathVariable long id, @RequestBody InCurriculum inCurriculum){
        apiService.validateAPIKey(apiKey);
        userService.createCurriculum(curriculumBinder.bindInput(inCurriculum));
    }

    @DeleteMapping("/account/user/{id}/curriculum/{cid}")
    public void deleteCurriculum(@RequestHeader("apiKey") String apiKey, @PathVariable long id, @PathVariable long cid){
        apiService.validateAPIKey(apiKey);
        userService.deleteCurriculum(id, cid);
    }
}
