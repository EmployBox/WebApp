package isel.ps.employbox.api.controllers;

import isel.ps.employbox.api.model.input.InApplication;
import isel.ps.employbox.api.model.input.InCurriculum;
import isel.ps.employbox.api.model.input.InUser;
import isel.ps.employbox.api.model.output.OutApplication;
import isel.ps.employbox.api.model.output.OutCurriculum;
import isel.ps.employbox.api.model.output.OutUser;
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
public class UserController implements ModelBinder<isel.ps.employbox.dal.model.User, OutUser, InUser, Long> {

    private final UserService userService;
    private final APIService apiService;

    public UserController(UserService userService, APIService apiService) {
        this.userService = userService;
        this.apiService = apiService;
    }

    @Override
    public List<OutUser> bindOutput(List<isel.ps.employbox.dal.model.User> list) {
        return list
                .stream()
                .map(user -> new OutUser(user.getIdentityKey(), user.getName(), user.getEmail(), user.getPhotoUrl(), user.getSummary(), user.getRating()))
                .collect(Collectors.toList());
    }

    @Override
    public List<isel.ps.employbox.dal.model.User> bindInput(List<InUser> list) {
        return list
                .stream()
                .map(inUser -> new isel.ps.employbox.dal.model.User(inUser.getEmail(), inUser.getPassword(), 0, inUser.getName(), inUser.getSummary(), inUser.getPhoto_url()))
                .collect(Collectors.toList());
    }

    @GetMapping("/account/user")
    public List<OutUser> getAllUsers(@RequestParam Map<String,String> queryString){
        return bindOutput(userService.getAllUsers(queryString));
    }

    @GetMapping("/account/user/{id}")
    public Optional<OutUser> getUser(@PathVariable long id){
        return bindOutput(Collections.singletonList(userService.getUser(id)))
                .stream()
                .findFirst();
    }

    @GetMapping("/account/user/{id}/apply")
    public List<OutApplication> getAllApplications(@PathVariable long id, @RequestParam Map<String,String> queryString){
        return userService.getAllApplications(id, queryString);
    }

    @GetMapping("/account/user/{id}/curriculums")
    public List<OutCurriculum> getAllCurriculums(@PathVariable long id, @RequestParam Map<String,String> queryString){
        return userService.getAllCurriculums(id, queryString);
    }

    @GetMapping("/account/user/{id}/curriculum/{cid}")
    public OutCurriculum getCurriculum(@PathVariable long id, @PathVariable long cid){
        return userService.getCurriculum(id, cid);
    }

    @PutMapping("/account/user/{id}")
    public void updateUser(
            @RequestHeader("apiKey") String apiKey,
            @PathVariable long id,
            @RequestBody InUser inUser
    ){
        apiService.validateAPIKey(apiKey);
        userService.updateUser(id, inUser);
    }

    @PutMapping("/account/user/{id}/apply/{jid}")
    public void updateApplication(
            @RequestHeader("apiKey") String apiKey,
            @PathVariable long id,
            @PathVariable long jid,
            @RequestBody InApplication inApplication
    ){
        apiService.validateAPIKey(apiKey);
        userService.updateApplication(id, jid, inApplication);
    }

    @PutMapping("/account/user/{id}/curriculum/{cid}")
    public void updateCurriculum(
            @RequestHeader("apiKey") String apiKey,
            @PathVariable long id,
            @PathVariable long cid,
            @RequestBody InCurriculum inCurriculum
    ){
        apiService.validateAPIKey(apiKey);
        userService.updateCurriculum(id, cid, inCurriculum);
    }

    @PostMapping("/account/user/")
    public void createUser(
            @RequestHeader("apiKey") String apiKey,
            @RequestBody InUser inUser
    ){
        apiService.validateAPIKey(apiKey);
        userService.createUser(inUser);
    }

    @PostMapping("/account/user/{id}/apply/{jid}")
    public void createApplication(
            @RequestHeader("apiKey") String apiKey,
            @RequestBody InApplication inApplication
    ){
        apiService.validateAPIKey(apiKey);
        userService.createApplication(inApplication);
    }

    @PostMapping("/account/user/{id}/curriculum/")
    public void createCurriculum(@RequestHeader("apiKey") String apiKey, @PathVariable long id, @RequestBody InCurriculum inCurriculum){
        apiService.validateAPIKey(apiKey);
        userService.createCurriculum(id, inCurriculum);
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