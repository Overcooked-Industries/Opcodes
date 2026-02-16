package de.kilip.visitors;

import de.kilip.util.StringUtils;
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
        IO.println(StringUtils.toHexString(opcode));
    }

    @Override
    public void visitVarInsn(final int opcode, final int varIndex) {
        IO.println(StringUtils.toHexString(opcode) + " " + StringUtils.toHexString(varIndex));
    }

    @Override
    public void visitParameter(final String name, final int access) {
        IO.println(name);
    }

    @Override
    public void visitFieldInsn(final int opcode, final String owner, final String name, final String descriptor) {
        IO.println(opcode + owner + name + descriptor);
    }
}
