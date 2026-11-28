package de.overcooked_industries.visitors;

import de.overcooked_industries.util.UnpickUtil;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;

import static de.overcooked_industries.jar.JarProcessor.outputText;

public class FieldNamePrinterClassVisitor extends ClassVisitor {
    private final String className;
    Object lookOutFor = 1000;

    /**
     * Constructs a new {@link FieldNamePrinterClassVisitor}.
     *
     * @param api the ASM API version implemented by this visitor. Must be one of the {@code
     *            ASM}<i>x</i> values in {@link Opcodes}.
     */
    public FieldNamePrinterClassVisitor(int api, String name) {
        super(api);
        this.className = name;
    }

    @Override
    public FieldVisitor visitField(
            final int access,
            final String name,
            final String descriptor,
            final String signature,
            final Object value) {
        outputText.append("net.minecraft.world.entity.EntityEvent.").append(name.replace("/",".").replace("#",".")).append("\n");
        if(UnpickUtil.unpickableObjectsRoughlyEqual(lookOutFor, value, 0)) System.out.println(className + "#" + name);
        return null;
    }
}
