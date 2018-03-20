package isel.ps.employbox.api.services;

import isel.ps.employbox.api.model.output.Company;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;
import java.util.Map;

import static isel.ps.employbox.api.services.ServiceUtils.getPage;

public class CompanyService {
    public static List<Company> getCompanies(Map<String, String> queryString) {
        String page = getPage(queryString);
        throw new NotImplementedException();
    }

    public static List<Company> getCompany(long cid) {
        throw new NotImplementedException();
    }

    public static void setCompany(isel.ps.EmployBox.api.model.input.Company company) {
        throw new NotImplementedException();
    }

    public static void deleteCompany(long cid) {
    }

    public static void updateCompany(isel.ps.EmployBox.api.model.input.Company company) {
    }
}
