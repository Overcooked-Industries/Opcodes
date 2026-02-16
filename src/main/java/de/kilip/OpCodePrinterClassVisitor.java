package de.kilip;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class OpCodePrinterClassVisitor extends ClassVisitor {
    /**
     * Constructs a new {@link OpCodePrinterClassVisitor}.
     *
     * @param api the ASM API version implemented by this visitor. Must be one of the {@code
     *            ASM}<i>x</i> values in {@link Opcodes}.
     */
    public OpCodePrinterClassVisitor(int api) {
        super(api);
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String descriptor, final String signature, final String[] exceptions) {
        return new MethodVisitor(api) {
            @Override
            public void visitIntInsn(final int opcode, final int operand) {
                System.out.println(Integer.toHexString(opcode));
            }

            public void visitVarInsn(final int opcode, final int varIndex) {
                System.out.println(Integer.toHexString(opcode) + " " + Integer.toHexString(varIndex));

            }
        };
    }

}
