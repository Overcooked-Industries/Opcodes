import de.overcooked_industries.jar.JarProcessor;

private static final String url = "https://piston-data.mojang.com/v1/objects/ada715d3943e7584f04aca8ec44f5d3cd767353a/client.jar";

void main()  {
    JarProcessor.process(url, (name) -> name.endsWith(".class"));
}