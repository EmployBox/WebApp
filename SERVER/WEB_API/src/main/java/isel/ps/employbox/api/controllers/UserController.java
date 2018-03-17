package isel.ps.employbox.api.controllers;

import isel.ps.employbox.api.model.output.Application;
import isel.ps.employbox.api.model.output.Curriculum;
import isel.ps.employbox.api.model.output.User;
import isel.ps.employbox.api.services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public User getUser(@PathVariable long id){
        return userService.getUser(id);
    }

    @GetMapping("/account/user/{id}/apply")
    public List<Application> getApplications(@PathVariable long id, @RequestParam Map<String,String> queryString){
        return userService.getAllApplications(id, queryString);
    }

    @GetMapping("/account/user/{id}/curriculums")
    public List<Curriculum> getCurriculums(@PathVariable long id, @RequestParam Map<String,String> queryString){
        return userService.getAllCurriculums(id, queryString);
    }

    @PutMapping("/account/user/{id}")
    public void updateUser(
            @RequestHeader("apiKey") String apiKey,
            @PathVariable long id,
            @RequestBody isel.ps.employbox.api.model.input.User user
    ){
        //TODO validate apiKey
        //TODO update user
        //TODO return unauthorized access
    }
}
