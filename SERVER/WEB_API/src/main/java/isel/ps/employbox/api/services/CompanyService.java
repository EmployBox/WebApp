package isel.ps.employbox.api.services;

import isel.ps.employbox.api.model.input.InCompany;
import isel.ps.employbox.api.model.output.OutCompany;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;
import java.util.Map;

import static isel.ps.employbox.api.services.utils.ServiceUtils.getPage;

public class CompanyService {
    public static List<OutCompany> getCompanies(Map<String, String> queryString) {
        String page = getPage(queryString);
        throw new NotImplementedException();
    }

    public static List<OutCompany> getCompany(long cid) {
        throw new NotImplementedException();
    }

    public static void setCompany(InCompany inCompany) {
        throw new NotImplementedException();
    }

    public static void deleteCompany(long cid) {
    }

    public static void updateCompany(InCompany inCompany) {
    }
}
