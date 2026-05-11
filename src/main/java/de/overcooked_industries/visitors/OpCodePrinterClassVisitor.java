package de.overcooked_industries.visitors;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static de.overcooked_industries.jar.JarProcessor.outputText;

public class OpCodePrinterClassVisitor extends ClassVisitor {
    /**
     * Constructs a new {@link OpCodePrinterClassVisitor}.
     *
     * @param api the ASM API version implemented by this visitor. Must be one of the {@code
     *            ASM}<i>x</i> values in {@link Opcodes}.
     */
    public OpCodePrinterClassVisitor(int api, String name) {
        super(api);
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String descriptor, final String signature, final String[] exceptions) {
        return new OpCodePrinterMethodVisitor(api, outputText);
    }

}
