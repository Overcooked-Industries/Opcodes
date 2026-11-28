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
        return "method " + className.replace("/",".") + " " + methodName + " " + methodDescriptor + "\n";
    }

    public static boolean unpickableObjectsExactlyEqual(Object lookOutFor, Object cst) {
        return unpickableObjectsRoughlyEqual(lookOutFor, cst, EqualityBitflags.NONE.flag);
    }

    // byte, double, float, int, long, and short
    //TODO: Potentially switch-case
    //TODO: short, byte
    //TODO: this is may be too narrow, it checks exact type equality of both types even if one is wider
    public static boolean unpickableObjectsRoughlyEqual(Object lookOutFor, Object cst, int flags) {
        if(!(lookOutFor instanceof String || lookOutFor instanceof Number)) return false;
        if(cst == null) return false;

        if (cst instanceof Integer i && lookOutFor instanceof Integer loF) {
            boolean isWholeNumberDivisibleByNumberSearchedFor =  EqualityBitflags.matchWholeNumbersDivisibleByNumberSearchedFor(flags) && loF != 0  && (i % loF) == 0;
            boolean isMultipleOfNumberSearchedFor = EqualityBitflags.matchWholeNumbersThatAreMultiplesOfNumberSearchedFor(flags) && i != 0 && (loF % i) == 0;
            boolean isExactMatch = i.equals(loF);

            return isExactMatch || isWholeNumberDivisibleByNumberSearchedFor || isMultipleOfNumberSearchedFor;
        }

        if (lookOutFor instanceof Float f && cst instanceof Number n && (cst instanceof Float || cst instanceof Double)) {
            return Float.floatToRawIntBits(f) == Float.floatToRawIntBits(n.floatValue());
        }

        if (lookOutFor instanceof Double d && cst instanceof Double d2) {
            return Double.doubleToRawLongBits(d) == Double.doubleToRawLongBits(d2);
        }

        if (cst instanceof Long l && lookOutFor instanceof Long loF) {
            // ? l.equals(lOf) : ( || lOf % l == 0)
            boolean isWholeNumberDivisibleByNumberSearchedFor = EqualityBitflags.matchWholeNumbersDivisibleByNumberSearchedFor(flags) && loF != 0 && (l % loF) == 0;
            boolean isMultipleOfNumberSearchedFor = EqualityBitflags.matchWholeNumbersThatAreMultiplesOfNumberSearchedFor(flags) && l != 0 && (loF % l) == 0;
            boolean isExactMatch = l.equals(loF);

            return isExactMatch || isWholeNumberDivisibleByNumberSearchedFor || isMultipleOfNumberSearchedFor;
        }

        if (cst instanceof String s && lookOutFor instanceof String lOf) {
            return EqualityBitflags.usesContainsForStringMatching(flags) ? s.contains(lOf) : s.equals(lookOutFor);
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

    ///Contains flags to or together as inputs to [#unpickableObjectsExactlyEqual(java.lang.Object, java.lang.Object)], or-ed together
    public enum EqualityBitflags {
        NONE(0),
        USE_CONTAINS_FOR_STRING_MATCHING(1),
        MATCH_WHOLE_NUMBERS_DIVISIBLE_BY_NUMBER_SEARCHED_FOR(2),
        MATCH_WHOLE_NUMBERS_THAT_ARE_MULTIPLES_OF_NUMBER_SEARCHED_FOR(4);

        public final int flag;
        EqualityBitflags(int flag) {
            this.flag = flag;
        }


        public static boolean usesContainsForStringMatching(int flags) {
            return (flags & USE_CONTAINS_FOR_STRING_MATCHING.flag) > 0;
        }

        public static boolean matchWholeNumbersDivisibleByNumberSearchedFor(int flags) {
            return (flags & MATCH_WHOLE_NUMBERS_DIVISIBLE_BY_NUMBER_SEARCHED_FOR.flag) > 0;
        }

        public static boolean matchWholeNumbersThatAreMultiplesOfNumberSearchedFor(int flags) {
            return (flags & MATCH_WHOLE_NUMBERS_THAT_ARE_MULTIPLES_OF_NUMBER_SEARCHED_FOR.flag) > 0;
        }
    }


}
