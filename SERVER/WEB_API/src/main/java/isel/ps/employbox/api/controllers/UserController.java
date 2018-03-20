package isel.ps.employbox.api.controllers;

import isel.ps.employbox.api.model.output.Application;
import isel.ps.employbox.api.model.output.Curriculum;
import isel.ps.employbox.api.model.output.User;
import isel.ps.employbox.api.services.APIService;
import isel.ps.employbox.api.services.ModelBinder;
import isel.ps.employbox.api.services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class UserController implements ModelBinder<isel.ps.employbox.dal.model.User, User, isel.ps.employbox.api.model.input.User, Long> {

    private final UserService userService;
    private final APIService apiService;

    public UserController(UserService userService, APIService apiService) {
        this.userService = userService;
        this.apiService = apiService;
    }

    @Override
    public List<User> bindOutput(List<isel.ps.employbox.dal.model.User> list) {
        return list
                .stream()
                .map(user -> new User(user.getIdentityKey(), user.getName(), user.getEmail(), user.getPhotoUrl(), user.getSummary(), user.getRating()))
                .collect(Collectors.toList());
    }

    @Override
    public List<isel.ps.employbox.dal.model.User> bindInput(List<isel.ps.employbox.api.model.input.User> list) {
        return list
                .stream()
                .map(user -> new isel.ps.employbox.dal.model.User(-1, user.getEmail(), user.getPassword(), 0, 0, user.getName(), user.getSummary(), user.getPhoto_url(),
                        Collections::emptyList, Collections::emptyList, Collections::emptyList, Collections::emptyList, Collections::emptyList,
                        Collections::emptyList, Collections::emptyList))
                .collect(Collectors.toList());
    }

    @GetMapping("/account/user")
    public List<User> getAllUsers(@RequestParam Map<String,String> queryString){
        return bindOutput(userService.getAllUsers(queryString));
    }

    @GetMapping("/account/user/{id}")
    public Optional<User> getUser(@PathVariable long id){
        return bindOutput(Collections.singletonList(userService.getUser(id)))
                .stream()
                .findFirst();
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
        apiService.validateAPIKey(apiKey);
        userService.updateUser(id, user);
    }

    @PutMapping("/account/user/{id}/apply/{jid}")
    public void updateApplication(
            @RequestHeader("apiKey") String apiKey,
            @PathVariable long id,
            @PathVariable long jid,
            @RequestBody isel.ps.employbox.api.model.input.Application application
    ){
        apiService.validateAPIKey(apiKey);
        userService.updateApplication(id, jid, application);
    }

    @PutMapping("/account/user/{id}/curriculum/{cid}")
    public void updateCurriculum(
            @RequestHeader("apiKey") String apiKey,
            @PathVariable long id,
            @PathVariable long cid,
            @RequestBody isel.ps.employbox.api.model.input.Curriculum curriculum
    ){
        apiService.validateAPIKey(apiKey);
        userService.updateCurriculum(id, cid, curriculum);
    }

    @PostMapping("/account/user/")
    public void createUser(
            @RequestHeader("apiKey") String apiKey,
            @RequestBody isel.ps.employbox.api.model.input.User user
    ){
        apiService.validateAPIKey(apiKey);
        userService.createUser(user);
    }

    @PostMapping("/account/user/{id}/apply/{jid}")
    public void createApplication(
            @RequestHeader("apiKey") String apiKey,
            @RequestBody isel.ps.employbox.api.model.input.Application application
    ){
        apiService.validateAPIKey(apiKey);
        userService.createApplication(application);
    }

    @PostMapping("/account/user/{id}/curriculum/")
    public void createCurriculum(@RequestHeader("apiKey") String apiKey, @PathVariable long id, @RequestBody isel.ps.employbox.api.model.input.Curriculum curriculum){
        apiService.validateAPIKey(apiKey);
        userService.createCurriculum(id, curriculum);
    }

    @DeleteMapping("/account/user/{id}")
    public void deleteUser(@RequestHeader("apiKey") String apiKey, @PathVariable long id){
        apiService.validateAPIKey(apiKey);
        userService.deleteUser(id);
    }

    @DeleteMapping("/account/user/{id}/application/{jid}")
    public void deleteApplication(@RequestHeader("apiKey") String apiKey, @PathVariable long id, @PathVariable long jid){
        apiService.validateAPIKey(apiKey);
        userService.deleteApplication(id, jid);
    }

    @DeleteMapping("/account/user/{id}/curriculum/{cid}")
    public void deleteCurriculum(@RequestHeader("apiKey") String apiKey, @PathVariable long id, @PathVariable long cid){
        apiService.validateAPIKey(apiKey);
        userService.deleteCurriculum(id, cid);
    }
}