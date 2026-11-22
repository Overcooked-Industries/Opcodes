package de.overcooked_industries.visitors;

import de.overcooked_industries.util.UnpickUtil;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static de.overcooked_industries.jar.JarProcessor.outputText;

public class ConstantFindingClassVisitor extends ClassVisitor {
    float lookOutFor = Float.floatToRawIntBits(1.0E-5F);
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
                if (value instanceof Float f && Float.floatToRawIntBits(f) == lookOutFor || value instanceof Double d && Float.floatToRawIntBits((float) d.doubleValue()) == lookOutFor) {
                    outputText.append(UnpickUtil.toUnpickScope(className, methodName, descriptor));
                }
            }
        };
    }
}
