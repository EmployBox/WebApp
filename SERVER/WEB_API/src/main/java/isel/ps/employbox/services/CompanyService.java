package isel.ps.employbox.services;

import com.github.jayield.rapper.mapper.DataMapper;
import com.github.jayield.rapper.unitofwork.UnitOfWork;
import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.model.binders.CollectionPage;
import isel.ps.employbox.model.entities.Company;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

import static com.github.jayield.rapper.mapper.MapperRegistry.getMapper;
import static isel.ps.employbox.services.ServiceUtils.handleExceptions;

@Service
public class CompanyService {
    private AccountService accountService;

    public CompanyService( AccountService accountService){
        this.accountService = accountService;
    }

    public CompletableFuture<CollectionPage<Company>> getCompanies(int page, int pageSize) {
        return ServiceUtils.getCollectionPageFuture(Company.class, page, pageSize);
    }

    public CompletableFuture<Company> getCompany(long cid) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Company, Long> companyMapper = getMapper(Company.class, unitOfWork);
        CompletableFuture<Company> future = companyMapper.findById(cid)
                .thenCompose(res -> unitOfWork.commit().thenApply(aVoid -> res))
                .thenApply(optionalCompany -> optionalCompany.orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND_COMPANY)));
        return handleExceptions(future, unitOfWork);
    }

    public CompletableFuture<Company> createCompany(Company company) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Company, Long> companyMapper = getMapper(Company.class, unitOfWork);
        CompletableFuture<Company> future = companyMapper.create( company)
                .thenCompose(res -> unitOfWork.commit().thenApply(aVoid -> company));
        return handleExceptions(future, unitOfWork);
    }

    public Mono<Void> updateCompany(Company company, String email) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Company, Long> companyMapper = getMapper(Company.class, unitOfWork);
        CompletableFuture<Void> future = accountService.getAccount(company.getIdentityKey(), email)
                .thenCompose(account -> companyMapper.update( company))
                .thenCompose(res -> unitOfWork.commit());
        return Mono.fromFuture(
                handleExceptions(future, unitOfWork)
        );
    }

    public Mono<Void> deleteCompany(long cid, String email) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Company, Long> companyMapper = getMapper(Company.class, unitOfWork);
        CompletableFuture<Void> future = accountService.getAccount(cid, email)
                .thenCompose(account -> companyMapper.deleteById(cid))
                .thenCompose(res -> unitOfWork.commit());
        return Mono.fromFuture(
                handleExceptions(future, unitOfWork)
        );
    }
}
