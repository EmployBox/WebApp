package isel.ps.employbox.services;

import com.github.jayield.rapper.DataRepository;
import com.github.jayield.rapper.Transaction;
import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.model.binder.CollectionPage;
import isel.ps.employbox.model.entities.Company;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.sql.Connection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class CompanyService {
    private DataRepository<Company, Long> companyRepo;
    private AccountService accountService;

    public CompanyService(DataRepository<Company, Long> companyRepo, AccountService accountService){
        this.companyRepo = companyRepo;
        this.accountService = accountService;
    }

    public CompletableFuture<CollectionPage<Company>> getCompanies(int pageSize, int page) {
        List[] list = new List[1];
        CollectionPage[] ret = new CollectionPage[1];

        return new Transaction(Connection.TRANSACTION_SERIALIZABLE)
                .andDo(() ->
                        companyRepo.findAll(page, pageSize)
                                .thenCompose(listRes -> {
                                    list[0] = listRes;
                                    return companyRepo.getNumberOfEntries(/*todo filter support*/);
                                })
                                .thenAccept(numberOfEntries ->
                                        ret[0] = new CollectionPage(
                                                numberOfEntries,
                                                pageSize,
                                                page,
                                                list[0]
                                        ))
                ).commit()
                .thenApply(__ -> ret[0]);
    }

    public CompletableFuture<Company> getCompany(long cid) {
        return companyRepo.findById(cid)
                .thenApply(optionalCompany -> optionalCompany.orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND_COMPANY)));
    }

    public CompletableFuture<Company> createCompany(Company company) {
        return companyRepo.create(company)
                .thenApply(res -> company);
    }

    public Mono<Void> updateCompany(Company company, String email) {
        return Mono.fromFuture(
                accountService.getAccount(company.getIdentityKey(), email)
                        .thenCompose(account -> companyRepo.update(company))
        );
    }

    public Mono<Void> deleteCompany(long cid, String email) {
        return Mono.fromFuture(
                accountService.getAccount(cid, email)
                        .thenCompose(account -> companyRepo.deleteById(cid))
        );
    }
}
