package de.overcooked_industries.util;

import org.objectweb.asm.Opcodes;

public class UnpickUtil {
    private UnpickUtil(){}

    //TODO: Perhaps inline or perhaps get these via reflection
    /**
     * Tag used to demarcate an ordinary argument.
     */
    public static final String TAG_ARG = "\u0001";

    /**
     * Tag used to demarcate a constant.
     */
    public static final String TAG_CONST = "\u0002";


    ///Creates an unpick group with many `@scope methods`s that unpicks to the given class
    public static String toUnpickScope(String className, String methodName, String methodDescriptor) {
        return "@scope method " + className.replace("/",".") + " " + methodName + " " + methodDescriptor + "\n";
    }

    public static boolean unpickableObjectsEqual(Object lookOutFor, Object cst) {
        return unpickableObjectsRoughlyEqual(lookOutFor, cst, true);
    }

    // byte, double, float, int, long, and short
    //TODO: Potentially switch-case
    //TODO: short, byte
    public static boolean unpickableObjectsRoughlyEqual(Object lookOutFor, Object cst, boolean exact) {
        if(!(lookOutFor instanceof String || lookOutFor instanceof Number)) return false;
        if(cst == null) return false;

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

        if (cst instanceof String s && lookOutFor instanceof String lOf) {
            return exact ? s.equals(lookOutFor) : s.contains(lOf);
        }

        return false;
    }

    //double, float, int, long
    public static Number toNumber(int opcode) {
        return switch(opcode) {
            case Opcodes.ICONST_M1 -> -1;
            case Opcodes.ICONST_0 -> 0;


            case Opcodes.ICONST_1 -> 1;
            case Opcodes.ICONST_2 -> 2;
            case Opcodes.ICONST_3 -> 3;
            case Opcodes.ICONST_4 -> 4;
            case Opcodes.ICONST_5 -> 5;

            case Opcodes.LCONST_0 -> 0L;
            case Opcodes.LCONST_1 -> 1L;

            case Opcodes.FCONST_0 -> 0f;
            case Opcodes.FCONST_1 -> 1f;
            case Opcodes.FCONST_2 -> 2f;

            case Opcodes.DCONST_0 -> 0.0;
            case Opcodes.DCONST_1 -> 1.0;

            default -> null;
        };
    }

    public static String stripFormatting(String arg) {
        return arg.replace(UnpickUtil.TAG_ARG,"").replace(UnpickUtil.TAG_CONST,"");
    }
}
