package de.overcooked_industries.visitors;

import de.overcooked_industries.util.UnpickUtil;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static de.overcooked_industries.jar.JarProcessor.outputText;
import static de.overcooked_industries.jar.JarProcessor.seen;

public class ConstantFindingClassVisitor extends ClassVisitor {
    int lookOutFor = 20;
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
                if (UnpickUtil.unpickableLDCsEqual(lookOutFor, value) && !seen.contains(className + methodName)) {
                    outputText.append(UnpickUtil.toUnpickScope(className, methodName, descriptor));
                    seen.add(className + methodName);
                }
            }

            //Useful for chars/ints/bytes/shorts, as SIPUSH or BIPUSH may be used instead of LDC for these types
            @Override
            public void visitIntInsn(final int opcode, final int operand) {
                if(!(opcode == Opcodes.SIPUSH || opcode == Opcodes.BIPUSH)) return;
                if(operand != lookOutFor) return;

                if (UnpickUtil.unpickableLDCsEqual(lookOutFor, operand) && !seen.contains(className + methodName)) {
                    outputText.append(UnpickUtil.toUnpickScope(className, methodName, descriptor));
                    seen.add(className + methodName);
                }
            }


            /*
             *     Useful for chars, ints, bytes
             *     Currently reponds to:
             *     ICONST_M1, ICONST_0, ICONST_1, ICONST_2, ICONST_3, ICONST_4, ICONST_5,
             *     LCONST_0, LCONST_1, FCONST_0, FCONST_1, FCONST_2, DCONST_0, DCONST_1
             *     TODO: Also target negation and adding for actual inference
             */
            @Override
            public void visitInsn(final int opcode) {
                Object loaded = switch(opcode) {
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


                if (!seen.contains(className + methodName) && loaded != null && UnpickUtil.unpickableLDCsEqual(lookOutFor, loaded)) {
                    outputText.append(UnpickUtil.toUnpickScope(className, methodName, descriptor));
                    seen.add(className + methodName);
                }
            }
        };
    }
}
