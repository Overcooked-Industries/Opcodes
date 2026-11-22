import de.overcooked_industries.jar.JarProcessor;
import de.overcooked_industries.visitors.ConstantFindingClassVisitor;
import de.overcooked_industries.visitors.FieldNamePrinterClassVisitor;
import de.overcooked_industries.visitors.MethodNamePrinterWithConditionClassVisitor;

private static final String url = "https://piston-data.mojang.com/v1/objects/e877b6a07acd633fb3bb475002175cec036e7b87/client.jar";

void main()  {
    JarProcessor.process(url, (name) -> name.endsWith(".class"), ConstantFindingClassVisitor::new);
}