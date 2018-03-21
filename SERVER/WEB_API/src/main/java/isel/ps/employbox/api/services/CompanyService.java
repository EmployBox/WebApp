package isel.ps.employbox.api.services;

import isel.ps.employbox.dal.model.Company;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;
import java.util.Map;

import static isel.ps.employbox.api.services.utils.ServiceUtils.getPage;

public class CompanyService {
    public static List<Company> getCompanies(Map<String, String> queryString) {
        String page = getPage(queryString);
        throw new NotImplementedException();
    }

    public static List<Company> getCompany(long cid) {
        throw new NotImplementedException();
    }

    public static void setCompany(Company inCompany) {
        throw new NotImplementedException();
    }

    public static void deleteCompany(long cid) {
    }

    public static void updateCompany(Company inCompany) {
    }
}
