package de.overcooked_industries.visitors;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.TypePath;

//Specially made to contribute to FabricMC/annotater
//Specificity should be taken for granted
public class MethodNamePrinterWithConditionMethodVisitor extends MethodVisitor  {
    private final StringBuilder outputBuilder;
    private final String name;
    private final String descriptor;


    protected MethodNamePrinterWithConditionMethodVisitor(int api, StringBuilder outputBuilder, String name, String descriptor) {
        super(api);
        this.outputBuilder = outputBuilder;
        this.name = name;
        this.descriptor = descriptor;
    }

    //We horribly misuse this method, since we don't actually need to construct an annotation visitor
    @Override
    public AnnotationVisitor visitTypeAnnotation(final int typeRef, final TypePath typePath, final String descriptor, final boolean visible) {
        var a = this.name.split("\\.");
        if ("Lnet/minecraft/world/entity/EntityEvent$Value;".equals(descriptor)) outputBuilder.append("@scope method ").append(a[0].replace("/",".")).append(" ").append(a[1]).append(" ").append(this.descriptor).append("\n");
        return null;
    }


}
