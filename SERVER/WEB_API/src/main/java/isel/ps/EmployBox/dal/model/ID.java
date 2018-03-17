package isel.ps.EmployBox.dal.model;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ID {
    boolean isIdentity() default false;
}
