package isel.ps.employbox.api.controllers;

import isel.ps.employbox.api.model.input.InCurriculum;
import isel.ps.employbox.api.model.output.OutCurriculum;
import isel.ps.employbox.api.services.ModelBinder;
import isel.ps.employbox.api.services.UserService;
import isel.ps.employbox.dal.model.Curriculum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class CurriculumController implements ModelBinder<Curriculum,OutCurriculum,InCurriculum,Long> {

    @Override
    public List<OutCurriculum> bindOutput(List<Curriculum> list) {
        return null;
    }

    @Override
    public List<Curriculum> bindInput(List<InCurriculum> list) {
        return null;
    }

    @Override
    public OutCurriculum bindOutput(Curriculum object) {
        return null;
    }

    @Override
    public Curriculum bindInput(InCurriculum object) {
        return null;
    }

    @Autowired
    private UserService userService;


    @GetMapping("/account/user/{id}/curriculums")
    public List<OutCurriculum> getAllCurriculums(@PathVariable long id, @RequestParam Map<String,String> queryString){
        return bindOutput(
                userService.getAllCurriculums(id, queryString)
        );
    }

    @GetMapping("/account/user/{id}/curriculum/{cid}")
    public OutCurriculum getCurriculum(@PathVariable long id, @PathVariable long cid){
        return bindOutput(
                userService.getCurriculum(id, cid));
    }

}
