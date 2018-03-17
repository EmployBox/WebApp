package isel.ps.employbox.api.controllers;

import isel.ps.employbox.api.model.output.Application;
import isel.ps.employbox.api.model.output.Curriculum;
import isel.ps.employbox.api.model.output.User;
import isel.ps.employbox.api.services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/account/user")
    public List<User> getAllUsers(@RequestParam Map<String,String> queryString){
        return userService.getAllUsers(queryString);
    }

    @GetMapping("/account/user/{id}")
    public Optional<User> getUser(@PathVariable long id){
        return userService.getUser(id);
    }

    @GetMapping("/account/user/{id}/apply")
    public List<Application> getAllApplications(@PathVariable long id, @RequestParam Map<String,String> queryString){
        return userService.getAllApplications(id, queryString);
    }

    @GetMapping("/account/user/{id}/curriculums")
    public List<Curriculum> getAllCurriculums(@PathVariable long id, @RequestParam Map<String,String> queryString){
        return userService.getAllCurriculums(id, queryString);
    }

    @GetMapping("/account/user/{id}/curriculum/{cid}")
    public Curriculum getCurriculum(@PathVariable long id, @PathVariable long cid){
        return userService.getCurriculum(id, cid);
    }

    @PutMapping("/account/user/{id}")
    public void updateUser(
            @RequestHeader("apiKey") String apiKey,
            @PathVariable long id,
            @RequestBody isel.ps.employbox.api.model.input.User user
    ){
        //TODO validate apiKey
        userService.updateUser(id, user);
        //TODO return unauthorized access
    }

    @PutMapping("/account/user/{id}/apply/{jid}")
    public void updateApplication(
            @RequestHeader("apiKey") String apiKey,
            @PathVariable long id,
            @PathVariable long jid,
            @RequestBody isel.ps.employbox.api.model.input.Application application
    ){
        //TODO validate apiKey
        userService.updateApplication(id, jid, application);
        //TODO return unauthorized access
    }

    @PutMapping("/account/user/{id}/curriculum/{cid}")
    public void updateCurriculum(
            @RequestHeader("apiKey") String apiKey,
            @PathVariable long id,
            @PathVariable long cid,
            @RequestBody isel.ps.employbox.api.model.input.Curriculum curriculum
    ){
        //TODO validate apiKey
        userService.updateCurriculum(id, cid, curriculum);
        //TODO return unauthorized access
    }

    @PostMapping("/account/user/")
    public void createUser(
            @RequestHeader("apiKey") String apiKey,
            @RequestBody isel.ps.employbox.api.model.input.User user
    ){
        //TODO validate apiKey
        userService.createUser(user);
        //TODO return unauthorized access
    }

    @PostMapping("/account/user/{id}/apply/{jid}")
    public void createApplication(
            @RequestHeader("apiKey") String apiKey,
            @RequestBody isel.ps.employbox.api.model.input.Application application
    ){
        //TODO validate apiKey
        userService.createApplication(application);
        //TODO return unauthorized access
    }

    @PostMapping("/account/user/{id}/curriculum/")
    public void createCurriculum(@RequestHeader("apiKey") String apiKey, @PathVariable long id, @RequestBody isel.ps.employbox.api.model.input.Curriculum curriculum){
        //TODO validate apiKey
        userService.createCurriculum(id, curriculum);
        //TODO return unauthorized access
    }

    @DeleteMapping("/account/user/{id}")
    public void deleteUser(@RequestHeader("apiKey") String apiKey, @PathVariable long id){
        //TODO validate apiKey
        userService.deleteUser(id);
        //TODO return unauthorized access
    }

    @DeleteMapping("/account/user/{id}/application/{jid}")
    public void deleteApplication(@RequestHeader("apiKey") String apiKey, @PathVariable long id, @PathVariable long jid){
        //TODO validate apiKey
        userService.deleteApplication(id, jid);
        //TODO return unauthorized access
    }

    @DeleteMapping("/account/user/{id}/curriculum/{cid}")
    public void deleteCurriculum(@RequestHeader("apiKey") String apiKey, @PathVariable long id, @PathVariable long cid){
        //TODO validate apiKey
        userService.deleteCurriculum(id, cid);
        //TODO return unauthorized access
    }
}