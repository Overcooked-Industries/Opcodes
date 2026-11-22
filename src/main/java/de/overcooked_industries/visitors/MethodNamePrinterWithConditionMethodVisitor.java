package de.overcooked_industries.visitors;

import de.overcooked_industries.util.UnpickUtil;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.TypePath;

//Specially made to contribute to FabricMC/annotater
//Specificity should be taken for granted
public class MethodNamePrinterWithConditionMethodVisitor extends MethodVisitor  {
    private final StringBuilder outputBuilder;
    private final String className;
    private final String methodName;
    private final String methodDescriptor;


    protected MethodNamePrinterWithConditionMethodVisitor(int api, StringBuilder outputBuilder, String className, String methodName, String methodDescriptor) {
        super(api);
        this.outputBuilder = outputBuilder;
        this.className = className;
        this.methodName = methodName;
        this.methodDescriptor = methodDescriptor;
    }

    //We horribly misuse this method, since we don't actually need to construct an annotation visitor
    @Override
    public AnnotationVisitor visitTypeAnnotation(final int typeRef, final TypePath typePath, final String descriptor, final boolean visible) {
        if ("Lnet/minecraft/world/entity/EntityEvent$Value;".equals(descriptor)) outputBuilder.append(UnpickUtil.toUnpickScope(className, methodName, methodDescriptor));
        return null;
    }


}
