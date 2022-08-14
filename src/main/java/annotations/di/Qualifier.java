package annotations.di;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
//moze da anotira i polje i klasu
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface Qualifier {

    String value();
}
