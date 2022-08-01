package GLEngine.Core.Interfaces;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface EditorRange {
    float min();
    float max();
    boolean intLock() default false; // if true, the value is locked to an integer if the value is a float
}
