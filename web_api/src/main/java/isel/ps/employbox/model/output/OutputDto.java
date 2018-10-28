package isel.ps.employbox.model.output;

public interface OutputDto<T> {
    //String HOSTNAME = "http://35.230.153.165/api";
    String HOSTNAME = "http://localhost:8080";
    T getCollectionItemOutput();
}
