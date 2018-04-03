package isel.ps.employbox.services;

import isel.ps.employbox.exceptions.InvalidApiKeyException;
import org.springframework.stereotype.Service;

@Service
public class APIService {

    public void validateAPIKey(String apiKey){
        if(apiKey.equals(""))
            throw new InvalidApiKeyException();
    }

    public String generateNewAPIKey(){
        return null;
    }
}
