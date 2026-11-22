package de.overcooked_industries.util;

public class UnpickUtil {
    private UnpickUtil(){}

    ///Creates an unpick group with many `@scope methods`s that unpicks to the given class
    public static String toUnpickScope(String className, String methodName, String methodDescriptor) {
        return "@scope method " + className.replace("/",".") + " " + methodName + " " + methodDescriptor + "\n";
    }

}
