package de.overcooked_industries.util;

import org.objectweb.asm.ConstantDynamic;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Type;

public class UnpickUtil {
    private UnpickUtil(){}

    ///Creates an unpick group with many `@scope methods`s that unpicks to the given class
    public static String toUnpickScope(String className, String methodName, String methodDescriptor) {
        return "@scope method " + className.replace("/",".") + " " + methodName + " " + methodDescriptor + "\n";
    }

    //TODO: Potentially switch-case
    public static boolean unpickableLDCsEqual(Object lookOutFor, Object cst) {
        if(!(lookOutFor instanceof String || lookOutFor instanceof Number)) return false;

        if (cst instanceof Integer i && lookOutFor instanceof Integer i2) {
            return (int) i == i2;
        }

        if (lookOutFor instanceof Float f && cst instanceof Number n && (cst instanceof Float || cst instanceof Double)) {
            return Float.floatToRawIntBits(f) == Float.floatToRawIntBits(n.floatValue());
        }

        if (lookOutFor instanceof Double d && cst instanceof Double d2) {
            return Double.doubleToRawLongBits(d) == Double.doubleToRawLongBits(d2);
        }

        if (cst instanceof Long l) {
            return l.equals(lookOutFor);
        }

        if (cst instanceof String s) {
            return s.equals(lookOutFor);
        }

        return false;
    }

}
