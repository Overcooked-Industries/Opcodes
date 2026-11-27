package de.overcooked_industries.visitors;

import de.overcooked_industries.util.UnpickUtil;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static de.overcooked_industries.jar.JarProcessor.outputText;
import static de.overcooked_industries.jar.JarProcessor.seen;

public class ConstantFindingClassVisitor extends ClassVisitor {
    int lookOutFor = 1;
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
                addToOutputIfValid(lookOutFor, value);
            }

            //Useful for chars/ints/bytes/shorts, as SIPUSH or BIPUSH may be used instead of LDC for these types
            @Override
            public void visitIntInsn(final int opcode, final int operand) {
                if(!(opcode == Opcodes.SIPUSH || opcode == Opcodes.BIPUSH)) return;
                if(operand != lookOutFor) return;

                addToOutputIfValid(lookOutFor, operand);
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
                if (UnpickUtil.toNumber(opcode) != null) addToOutputIfValid(lookOutFor, constant);
            }

            
            //Helper methods


            void addToOutputIfValid(Object lookOutFor, Object constant) {
                if (!seen.contains(className + methodName) && UnpickUtil.unpickableObjectsEqual(lookOutFor, constant)) {
                    outputText.append(UnpickUtil.toUnpickScope(className, methodName, descriptor));
                    seen.add(className + methodName);
                }
            }

        };
    }
}
