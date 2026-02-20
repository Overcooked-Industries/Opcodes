package de.overcooked_industries.visitors;

import de.overcooked_industries.util.StringUtils;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class OpCodePrinterMethodVisitor extends MethodVisitor {
    private final StringBuilder outputBuilder;
    public String output;

    /**
     * Constructs a new {@link OpCodePrinterMethodVisitor}.
     *
     * @param api the ASM API version implemented by this visitor. Must be one of the {@code
     *            ASM}<i>x</i> values in {@link Opcodes}.
     */
    protected OpCodePrinterMethodVisitor(int api, StringBuilder outputBuilder) {
        super(api);
        this.outputBuilder = outputBuilder;
    }

    /**
     * Visits an instruction with a single int operand.
     *
     * @param opcode  the opcode of the instruction to be visited. This opcode is either BIPUSH, SIPUSH
     *                or NEWARRAY.
     * @param operand the operand of the instruction to be visited.<br>
     *                When opcode is BIPUSH, operand value should be between Byte.MIN_VALUE and Byte.MAX_VALUE.
     *                <br>
     *                When opcode is SIPUSH, operand value should be between Short.MIN_VALUE and Short.MAX_VALUE.
     *                <br>
     *                When opcode is NEWARRAY, operand value should be one of {@link Opcodes#T_BOOLEAN}, {@link
     *                Opcodes#T_CHAR}, {@link Opcodes#T_FLOAT}, {@link Opcodes#T_DOUBLE}, {@link Opcodes#T_BYTE},
     *                {@link Opcodes#T_SHORT}, {@link Opcodes#T_INT} or {@link Opcodes#T_LONG}.
     */
    @Override
    public void visitIntInsn(final int opcode, final int operand) {
        outputBuilder.append(StringUtils.toHexString(opcode)).append("\n");
    }

    /**
     * Visits a local variable instruction. A local variable instruction is an instruction that loads
     * or stores the value of a local variable.
     *
     * @param opcode   the opcode of the local variable instruction to be visited. This opcode is either
     *                 ILOAD, LLOAD, FLOAD, DLOAD, ALOAD, ISTORE, LSTORE, FSTORE, DSTORE, ASTORE or RET.
     * @param varIndex the operand of the instruction to be visited. This operand is the index of a
     *                 local variable.
     */
    @Override
    public void visitVarInsn(final int opcode, final int varIndex) {
        outputBuilder.append(StringUtils.toHexString(opcode)).append(" ").append(StringUtils.toHexString(varIndex)).append("\n");
    }

    /**
     * Visits a parameter of this method.
     *
     * @param name   parameter name or {@literal null} if none is provided.
     * @param access the parameter's access flags, only {@code ACC_FINAL}, {@code ACC_SYNTHETIC}
     *               or/and {@code ACC_MANDATED} are allowed (see {@link Opcodes}).
     */
    @Override
    public void visitParameter(final String name, final int access) {
        outputBuilder.append(name).append("\n");
    }

    /**
     * Visits a field instruction. A field instruction is an instruction that loads or stores the
     * value of a field of an object.
     *
     * @param opcode     the opcode of the type instruction to be visited. This opcode is either
     *                   GETSTATIC, PUTSTATIC, GETFIELD or PUTFIELD.
     * @param owner      the internal name of the field's owner class (see {@link Type#getInternalName()}).
     * @param name       the field's name.
     * @param descriptor the field's descriptor (see {@link Type}).
     */
    @Override
    public void visitFieldInsn(final int opcode, final String owner, final String name, final String descriptor) {
        outputBuilder.append(StringUtils.toHexString(opcode)).append(" ").append(owner).append(" ").append(name).append(" ").append(descriptor).append("\n");
    }

    /**
     * Visits a zero operand instruction.
     *
     * @param opcode the opcode of the instruction to be visited. This opcode is either NOP,
     *               ACONST_NULL, ICONST_M1, ICONST_0, ICONST_1, ICONST_2, ICONST_3, ICONST_4, ICONST_5,
     *               LCONST_0, LCONST_1, FCONST_0, FCONST_1, FCONST_2, DCONST_0, DCONST_1, IALOAD, LALOAD,
     *               FALOAD, DALOAD, AALOAD, BALOAD, CALOAD, SALOAD, IASTORE, LASTORE, FASTORE, DASTORE,
     *               AASTORE, BASTORE, CASTORE, SASTORE, POP, POP2, DUP, DUP_X1, DUP_X2, DUP2, DUP2_X1, DUP2_X2,
     *               SWAP, IADD, LADD, FADD, DADD, ISUB, LSUB, FSUB, DSUB, IMUL, LMUL, FMUL, DMUL, IDIV, LDIV,
     *               FDIV, DDIV, IREM, LREM, FREM, DREM, INEG, LNEG, FNEG, DNEG, ISHL, LSHL, ISHR, LSHR, IUSHR,
     *               LUSHR, IAND, LAND, IOR, LOR, IXOR, LXOR, I2L, I2F, I2D, L2I, L2F, L2D, F2I, F2L, F2D, D2I,
     *               D2L, D2F, I2B, I2C, I2S, LCMP, FCMPL, FCMPG, DCMPL, DCMPG, IRETURN, LRETURN, FRETURN,
     *               DRETURN, ARETURN, RETURN, ARRAYLENGTH, ATHROW, MONITORENTER, or MONITOREXIT.
     */
    @Override
    public void visitInsn(final int opcode) {
        outputBuilder.append(StringUtils.toHexString(opcode)).append("\n");
    }

    /**
     * Visits a jump instruction. A jump instruction is an instruction that may jump to another
     * instruction.
     *
     * @param opcode the opcode of the type instruction to be visited. This opcode is either IFEQ,
     *               IFNE, IFLT, IFGE, IFGT, IFLE, IF_ICMPEQ, IF_ICMPNE, IF_ICMPLT, IF_ICMPGE, IF_ICMPGT,
     *               IF_ICMPLE, IF_ACMPEQ, IF_ACMPNE, GOTO, JSR, IFNULL or IFNONNULL.
     * @param label  the operand of the instruction to be visited. This operand is a label that
     *               designates the instruction to which the jump instruction may jump.
     */
    public void visitJumpInsn(final int opcode, final Label label) {
        outputBuilder.append(opcode).append(" to ").append(StringUtils.toHexString(label.getOffset())).append("\n");
    }
}
