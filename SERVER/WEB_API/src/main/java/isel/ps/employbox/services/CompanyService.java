package isel.ps.employbox.services;

import isel.ps.employbox.RapperRepository;
import isel.ps.employbox.model.entities.Company;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    private RapperRepository<Company, Long> repo;

    public CompanyService(RapperRepository<Company, Long> repo){ this.repo = repo; }

    public List<Company> getCompanies() {
        return repo.findAll();
    }

    public Optional<Company> getCompany(long cid) { return repo.findById(cid); }

    public void setCompany(Company company) { repo.create(company); }

    public void deleteCompany(long cid) { repo.deleteById(cid); }

    public void updateCompany(Company company) { repo.update(company); }
}
