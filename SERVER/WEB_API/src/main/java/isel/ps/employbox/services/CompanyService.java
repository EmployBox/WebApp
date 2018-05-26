package isel.ps.employbox.services;

import com.github.jayield.rapper.DataRepository;
import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.exceptions.UnauthorizedException;
import isel.ps.employbox.model.entities.Account;
import isel.ps.employbox.model.entities.Company;
import isel.ps.employbox.model.entities.Role;
import javafx.util.Pair;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@Service
public class CompanyService {
    private DataRepository<Account, Long> accountRepo;
    private DataRepository<Company, Long> companyRepo;

    public CompanyService( DataRepository<Account, Long> accountRepo, DataRepository<Company, Long> companyRepo){
        this.accountRepo = accountRepo;
        this.companyRepo = companyRepo; }

    public CompletableFuture<Stream<Company>> getCompanies() {
        return companyRepo.findAll().thenApply(Collection::stream);
    }

    public CompletableFuture<Company> getCompany(long cid) {
        return companyRepo.findById(cid)
                .thenApply(
                        optionalCompany -> optionalCompany.orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND_COMPANY)));
    }

    public CompletableFuture<Company> createCompany(Company company, String email) {
        return accountRepo.findWhere(new Pair<>("email", email))
                .thenCompose(list -> {
                            if (list.get(0).getRole() != Role.ADMINISTRATOR)
                                throw new UnauthorizedException(ErrorMessages.UN_AUTHORIZED);
                            return companyRepo.create(company);
                        }
                ).thenApply(res -> {
                    if (res.isPresent()) throw new BadRequestException(ErrorMessages.BAD_REQUEST_ITEM_CREATION);
                    return company;
                });
    }

    public Mono<Void> deleteCompany(long cid, String email) {
        return Mono.fromFuture(
                accountRepo.findWhere(new Pair<>("email", email))
                        .thenCompose(list -> {
                                    if (list.get(0).getRole() != Role.ADMINISTRATOR)
                                        throw new UnauthorizedException(ErrorMessages.UN_AUTHORIZED);
                                    return companyRepo.deleteById(cid);
                                }
                        ).thenAccept(res -> {
                    if (res.isPresent()) throw new BadRequestException(ErrorMessages.BAD_REQUEST_ITEM_DELETION);
                })
        );
    }

    public Mono<Void> updateCompany(Company company, String email) {
        return Mono.fromFuture(
                accountRepo.findWhere(new Pair<>("email", email))
                        .thenCompose(list -> {
                                    if (list.get(0).getRole() != Role.ADMINISTRATOR)
                                        throw new UnauthorizedException(ErrorMessages.UN_AUTHORIZED);
                                    return companyRepo.update(company);
                                }
                        ).thenAccept(res -> {
                    if (res.isPresent()) throw new BadRequestException(ErrorMessages.BAD_REQUEST_ITEM_UPDATE);
                })
        );
    }
}
