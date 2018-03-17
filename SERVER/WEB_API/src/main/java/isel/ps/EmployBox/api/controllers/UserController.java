package isel.ps.EmployBox.api.controllers;

import isel.ps.EmployBox.api.model.output.User;
import isel.ps.EmployBox.api.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
