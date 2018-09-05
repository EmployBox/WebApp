package isel.ps.employbox.controllers.UserAccountControllers;

import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.binders.UserBinder;
import isel.ps.employbox.model.entities.UserAccount;
import isel.ps.employbox.model.input.InUserAccount;
import isel.ps.employbox.model.output.Collections.HalCollectionPage;
import isel.ps.employbox.model.output.OutUser;
import isel.ps.employbox.services.UserAccountService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

import static isel.ps.employbox.ErrorMessages.BAD_REQUEST_IDS_MISMATCH;

@RestController
@RequestMapping("/accounts/users")
public class UserAccountController {

    private final UserAccountService userAccountService;
    private final UserBinder userBinder;

    public UserAccountController(UserAccountService userAccountService, UserBinder userBinder) {
        this.userAccountService = userAccountService;
        this.userBinder = userBinder;
    }

    @GetMapping
    public Mono<HalCollectionPage<UserAccount>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer ratingLow,
            @RequestParam(required = false) Integer ratingHigh,
            @RequestParam(required = false) String orderColumn,
            @RequestParam(required = false, defaultValue = "ASC") String orderClause
    ){
        CompletableFuture<HalCollectionPage<UserAccount>> future = userAccountService.getAllUsers(page, pageSize, name, ratingLow, ratingHigh, orderColumn, orderClause)
                .thenCompose(userAccountCollectionPage -> userBinder.bindOutput(userAccountCollectionPage, this.getClass()));
        return Mono.fromFuture(future);
    }

    @GetMapping("/{id}")
    public Mono<OutUser> getUser(@PathVariable long id){
        CompletableFuture<OutUser> future = userAccountService.getUser(id)
                .thenCompose(userBinder::bindOutput);
        return Mono.fromFuture(future);
    }


    @PostMapping
    public Mono<OutUser> createUser( @RequestBody InUserAccount inUserAccount){
        UserAccount userAccount = userBinder.bindInput(inUserAccount);
        CompletableFuture<OutUser> future = userAccountService.createUser(userAccount)
                .thenCompose(userBinder::bindOutput);
        return Mono.fromFuture(future);
    }

    @PutMapping("/{id}")
    public Mono<Void> updateUser(
            @PathVariable long id,
            @RequestBody InUserAccount inUserAccount,
            Authentication authentication
    ) {
        if(inUserAccount.getId() != id) throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
        UserAccount userAccount = userBinder.bindInput(inUserAccount);
        return userAccountService.updateUser(userAccount, authentication.getName());
    }


    @DeleteMapping("/{id}")
    public Mono<Void> deleteUser( @PathVariable long id, Authentication authentication){
        return userAccountService.deleteUser(id, authentication.getName());
    }

}