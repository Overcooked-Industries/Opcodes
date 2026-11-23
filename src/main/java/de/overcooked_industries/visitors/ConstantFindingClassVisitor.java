package de.overcooked_industries.visitors;

import de.overcooked_industries.util.UnpickUtil;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static de.overcooked_industries.jar.JarProcessor.outputText;
import static de.overcooked_industries.jar.JarProcessor.seen;

public class ConstantFindingClassVisitor extends ClassVisitor {
    int lookOutFor = 58;
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

            //Useful for chars - BIPUSH
            @Override
            public void visitIntInsn(final int opcode, final int operand) {
                if(operand != lookOutFor) return;

                System.out.println((char) operand);
                if (UnpickUtil.unpickableLDCsEqual(lookOutFor, operand) && !seen.contains(className + methodName)) {
                    outputText.append(UnpickUtil.toUnpickScope(className, methodName, descriptor));
                    seen.add(className + methodName);
                }
            }
        };
    }
}
