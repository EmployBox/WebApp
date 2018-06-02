package isel.ps.employbox.services;

import com.github.jayield.rapper.DataRepository;
import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.model.binder.CollectionPage;
import isel.ps.employbox.model.entities.Account;
import isel.ps.employbox.model.entities.Company;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Service
public class CompanyService {
    private DataRepository<Account, Long> accountRepo;
    private DataRepository<Company, Long> companyRepo;
    private AccountService accountService;

    public CompanyService(DataRepository<Account, Long> accountRepo, DataRepository<Company, Long> companyRepo, AccountService accountService){
        this.accountRepo = accountRepo;
        this.companyRepo = companyRepo;
        this.accountService = accountService;
    }

    public CompletableFuture<CollectionPage<Company>> getCompanies(int page) {
        return companyRepo.findAll(page, CollectionPage.PAGE_SIZE)
                .thenApply(list -> new CollectionPage(
                        page,
                        21,
                        list.stream()
                ));
    }

    public CompletableFuture<Company> getCompany(long cid) {
        return companyRepo.findById(cid)
                .thenApply(
                        optionalCompany -> optionalCompany.orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND_COMPANY))
                );
    }

    public CompletableFuture<Company> createCompany(Company company) {
        return companyRepo.create(company)
                .thenApply(res -> {
                    if (res.isPresent()) throw new BadRequestException(ErrorMessages.BAD_REQUEST_ITEM_CREATION);
                    return company;
                });
    }

    public Mono<Void> updateCompany(Company company, String email) {
        return Mono.fromFuture(
                accountService.getAccount(company.getIdentityKey(), email)
                .thenCompose(account -> companyRepo.update(company)
                        .thenAccept(res -> {
                            if (res.isPresent()) throw new BadRequestException(ErrorMessages.BAD_REQUEST_ITEM_UPDATE);
                        })
                )
        );
    }

    public Mono<Void> deleteCompany(long cid, String email) {
        return Mono.fromFuture(
                accountService.getAccount(cid, email)
                        .thenCompose(account -> companyRepo.deleteById(cid)
                                .thenAccept(res -> res.ifPresent(throwable -> {
                                    throw new BadRequestException(ErrorMessages.BAD_REQUEST_ITEM_DELETION);
                                }))
                        )
        );
    }
}
