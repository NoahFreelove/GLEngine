package GLEngine.Core.Interfaces;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface EditorVariableAttribute {
    float min() default Float.MIN_VALUE;
    float max() default Float.MAX_VALUE;
    float step() default 0f; // 0 means no step locking
    boolean intLock() default false; // if true, the value is locked to an integer if the value is a float
    String header() default ""; // if not empty, the value is displayed in a header
    String tooltip() default ""; // if not empty, the value is displayed in a tooltip
}
