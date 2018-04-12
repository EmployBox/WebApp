package isel.ps.employbox.services;

import isel.ps.employbox.RapperRepository;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.model.entities.Company;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class CompanyService {
    private RapperRepository<Company, Long> companyRepo;

    public CompanyService(RapperRepository<Company, Long> companyRepo){ this.companyRepo = companyRepo; }

    public Stream<Company> getCompanies() {
        return StreamSupport.stream(companyRepo.findAll().join().spliterator(),false);
    }

    public Company getCompany(long cid) {
        Optional<Company> ocomp = companyRepo.findById(cid).join();
        if(!ocomp.isPresent())
            throw new ResourceNotFoundException("Company doesnt exist");
        return ocomp.get();
    }

    public void createCompany(Company company, String name) { companyRepo.create(company); }

    public void deleteCompany(long cid, String email) {
        companyRepo.deleteById(cid);
    }

    public void updateCompany(Company company, String name) { companyRepo.update(company); }
}
