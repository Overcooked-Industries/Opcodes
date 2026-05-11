package de.overcooked_industries.visitors;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import static de.overcooked_industries.jar.JarProcessor.outputText;

public class CallGraphClassVisitor extends ClassVisitor {
    private final String name;

    public CallGraphClassVisitor(int api, String name) {
        super(api);
        this.name = name;
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String descriptor, final String signature, final String[] exceptions) {
        return new CallGraphMethodVisitor(api, outputText, this.name+"#"+name);
    }

}

