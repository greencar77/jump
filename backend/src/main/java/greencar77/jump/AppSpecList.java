package greencar77.jump;

import java.lang.reflect.Method;

import greencar77.jump.builder.Builder;
import greencar77.jump.builder.java.PredefinedMavenProjBuilder;
import greencar77.jump.builder.js.PredefinedAngularAppBuilder;
import greencar77.jump.builder.webapp.PredefinedWebAppBuilder;

public class AppSpecList {
    public static void main(String[] args) {
        Class<?>[] classes = new Class<?>[] {
            PredefinedWebAppBuilder.class,
            PredefinedAngularAppBuilder.class,
            PredefinedMavenProjBuilder.class
            };

        for (Class<?> clazz: classes) {
            for (Method method: clazz.getDeclaredMethods()) {
                if (method.getName().startsWith(Builder.SPEC_METHOD_PREFIX)) {
                    System.out.println(clazz.getSimpleName() + "\t" + method.getName());
                }
            }
        }
    }
}
