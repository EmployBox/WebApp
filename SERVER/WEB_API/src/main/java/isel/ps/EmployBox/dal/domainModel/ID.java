package isel.ps.EmployBox.dal.domainModel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ID {
    boolean isIdentity() default false;
}
