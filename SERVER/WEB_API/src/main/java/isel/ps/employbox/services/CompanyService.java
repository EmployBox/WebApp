package isel.ps.employbox.services;

import isel.ps.employbox.RapperRepository;
import isel.ps.employbox.model.entities.Company;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
public class CompanyService {

    private RapperRepository<Company, Long> repo;

    public CompanyService(RapperRepository<Company, Long> repo){ this.repo = repo; }

    public Stream<Company> getCompanies() {
        return null;
    }

    public Company getCompany(long cid) { return null; }

    public void setCompany(Company company) { repo.create(company); }

    public void deleteCompany(long cid) { repo.deleteById(cid); }

    public void updateCompany(Company company) { repo.update(company); }
}
