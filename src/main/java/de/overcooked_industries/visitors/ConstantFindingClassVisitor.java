package de.overcooked_industries.visitors;

import de.overcooked_industries.util.UnpickUtil;
import org.objectweb.asm.*;

import java.util.function.BiFunction;

import static de.overcooked_industries.jar.JarProcessor.outputText;
import static de.overcooked_industries.jar.JarProcessor.seen;
import static de.overcooked_industries.util.UnpickUtil.unpickableObjectsRoughlyEqual;

public class ConstantFindingClassVisitor extends ClassVisitor {
    Object lookOutFor = "MC_DEBUG_";
    boolean exactMatch = true;
    String className;

    /**
     * Constructs a new {@link ConstantFindingClassVisitor}.
     *
     * @param api the ASM API version implemented by this visitor. Must be one of the {@code
     *            ASM}<i>x</i> values in {@link Opcodes}.
     */
    public ConstantFindingClassVisitor(int api, String name) {
        super(api);
        this.className = name;
        seen.clear();
    }

    //Some false positives due to arrays also loading numbers,
    //TODO: fix the false positives
    @Override
    public MethodVisitor visitMethod(
            final int access,
            final String methodName,
            final String descriptor,
            final String signature,
            final String[] exceptions
    ) {
        return new MethodVisitor(api) {
            @Override
            public void visitLdcInsn(Object value) {
                addToOutputIfValid(lookOutFor, value, (lookOutFor, constant) -> unpickableObjectsRoughlyEqual(lookOutFor, constant, exactMatch));
            }

            //Useful for chars/ints/bytes/shorts, as SIPUSH or BIPUSH may be used instead of LDC for these types
            @Override
            public void visitIntInsn(final int opcode, final int operand) {
                //We need to do checks for every single one of these because reasons
                if(!(lookOutFor instanceof Integer || lookOutFor instanceof Character || lookOutFor instanceof Byte || lookOutFor instanceof Short)) return;

                if(!(opcode == Opcodes.SIPUSH || opcode == Opcodes.BIPUSH)) return;

                //This is always widening or identity, so it's fine
                if(operand != (int) lookOutFor) return;

                addToOutputIfValid(lookOutFor, operand, UnpickUtil::unpickableObjectsEqual);
            }


            /*
             *     Useful for chars, ints, bytes
             *     Currently responds to:
             *     ICONST_M1, ICONST_0, ICONST_1, ICONST_2, ICONST_3, ICONST_4, ICONST_5,
             *     LCONST_0, LCONST_1, FCONST_0, FCONST_1, FCONST_2, DCONST_0, DCONST_1
             *     TODO: Also target negation and adding for actual inference
             */
            @Override
            public void visitInsn(final int opcode) {
                Number constant = UnpickUtil.toNumber(opcode);
                if (UnpickUtil.toNumber(opcode) != null) addToOutputIfValid(lookOutFor, constant, UnpickUtil::unpickableObjectsEqual);
            }


            //Useful for String concatenation
            @Override
            public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
                if(!("makeConcatWithConstants".equals(name) && "(Ljava/lang/String;)Ljava/lang/String;".equals(descriptor))) return;
                //TODO: Don't blindly trust we get given the right method handle (Should be right though, lol)

                //TODO: make matching more strict, don't just do .contains()
                for(Object object : bootstrapMethodArguments) {
                    if (object instanceof String string && addToOutputIfValid(lookOutFor, string, (lookOutFor, constant) -> unpickableObjectsRoughlyEqual(lookOutFor, constant, false))) return;
                }
            }



            //Helper methods


            boolean addToOutputIfValid(Object lookOutFor, Object constant, BiFunction<Object, Object, Boolean> equalityCheck) {
                boolean equal = equalityCheck.apply(lookOutFor, constant);
                if (!seen.contains(className + methodName) && equal) {
                    outputText.append(UnpickUtil.toUnpickScope(className, methodName, descriptor));
                    seen.add(className + methodName);
                }

                return equal;
            }

        };
    }
}
