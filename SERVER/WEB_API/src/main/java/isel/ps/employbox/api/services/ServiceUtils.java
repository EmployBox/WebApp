package isel.ps.employbox.api.services;

import java.util.Map;

public class ServiceUtils {

    public static String getPage(Map<String,String> queryString){
        String page = queryString.get("page");
        if (page == null)
            page = "0";

        return page;
    }
}
