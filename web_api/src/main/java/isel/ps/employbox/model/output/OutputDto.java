package isel.ps.employbox.model.output;

public interface OutputDto<T> {
    String HOSTNAME = "http://localhost:8080";
    T getCollectionItemOutput();
}
