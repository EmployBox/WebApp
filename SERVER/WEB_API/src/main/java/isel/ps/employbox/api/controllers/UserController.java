package isel.ps.employbox.api.controllers;

import isel.ps.employbox.api.model.input.InApplication;
import isel.ps.employbox.api.model.input.InCurriculum;
import isel.ps.employbox.api.model.input.InUser;
import isel.ps.employbox.api.model.output.OutUser;
import isel.ps.employbox.api.services.APIService;
import isel.ps.employbox.api.services.ModelBinder;
import isel.ps.employbox.api.services.UserService;
import isel.ps.employbox.dal.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class UserController implements ModelBinder<User, OutUser, InUser, Long> {

    @Autowired
    private  UserService userService;
    @Autowired
    private  APIService apiService;

    @Override
    public List<OutUser> bindOutput(List<User> list) {
        return list
                .stream()
                .map(user -> new OutUser(user.getIdentityKey(), user.getName(), user.getEmail(), user.getPhotoUrl(), user.getSummary(), user.getRating()))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> bindInput(List<InUser> list) {
        return list
                .stream()
                .map(inUser -> new User(inUser.getEmail(), inUser.getPassword(), 0, inUser.getName(), inUser.getSummary(), inUser.getPhoto_url()))
                .collect(Collectors.toList());
    }

    @Override
    public OutUser bindOutput(User user) {
        return new OutUser(user.getIdentityKey(), user.getName(), user.getEmail(), user.getPhotoUrl(), user.getSummary(), user.getRating());
    }

    @Override
    public User bindInput(InUser inUser) {
        return new User(inUser.getEmail(), inUser.getPassword(), 0, inUser.getName(), inUser.getSummary(), inUser.getPhoto_url());
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


    @PutMapping("/account/user/{id}")
    public void updateUser(
            @RequestHeader("apiKey") String apiKey,
            @PathVariable long id,
            @RequestBody InUser inUser
    ){
        apiService.validateAPIKey(apiKey);
        userService.updateUser(id, inUser);
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