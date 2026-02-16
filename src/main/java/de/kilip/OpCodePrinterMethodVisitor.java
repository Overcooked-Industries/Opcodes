package de.kilip;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class OpCodePrinterMethodVisitor extends MethodVisitor {

    /**
     * Constructs a new {@link MethodVisitor}.
     *
     * @param api the ASM API version implemented by this visitor. Must be one of the {@code
     *            ASM}<i>x</i> values in {@link Opcodes}.
     */
    protected OpCodePrinterMethodVisitor(int api) {
        super(api);
    }

    @Override
    public void visitIntInsn(final int opcode, final int operand) {
        System.out.println("0x" + Integer.toHexString(opcode));
    }

    @Override
    public void visitVarInsn(final int opcode, final int varIndex) {
        System.out.println("0x" + Integer.toHexString(opcode) + " 0x" + Integer.toHexString(varIndex));
    }
}
