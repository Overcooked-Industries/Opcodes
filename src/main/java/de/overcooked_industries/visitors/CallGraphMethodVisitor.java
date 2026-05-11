package de.overcooked_industries.visitors;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.function.Predicate;

public class CallGraphMethodVisitor extends MethodVisitor {
    private static final Predicate<String> isMinecraftClass = (name) -> name.startsWith("com/mojang") || name.startsWith("net/minecraft");
    private final StringBuilder outputBuilder;
    private final String name;

    /**
     * Constructs a new {@link OpCodePrinterMethodVisitor}.
     *
     * @param api the ASM API version implemented by this visitor. Must be one of the {@code
     *            ASM}<i>x</i> values in {@link Opcodes}.
     */
    protected CallGraphMethodVisitor(int api, StringBuilder outputBuilder, String name) {
        super(api);
        this.outputBuilder = outputBuilder;
        this.name = name;
    }

    @Override
    public void visitMethodInsn(final int opcode, final String owner, final String name, final String descriptor, final boolean isInterface) {
        String nextLine = String.format("%s calls: %s#%s\n", this.name, owner, name);
        if(isMinecraftClass.test(owner)) this.outputBuilder.append(nextLine);
    }

}
