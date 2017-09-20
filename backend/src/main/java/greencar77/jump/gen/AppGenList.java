package greencar77.jump.gen;

import java.lang.reflect.Method;

import greencar77.jump.builder.Builder;
import greencar77.jump.builder.java.MavenProjBuilder;
import greencar77.jump.builder.js.AngularAppBuilder;
import greencar77.jump.builder.webapp.WebAppBuilder;

public class AppGenList {
    public static void main(String[] args) {
        Class<?>[] classes = new Class<?>[] {
            WebAppBuilder.class,
            AngularAppBuilder.class,
            MavenProjBuilder.class
            };

        for (Class<?> clazz: classes) {
            System.out.println();
            System.out.println(clazz.getSimpleName().replaceAll("Builder", "") + "Ctrl");
            System.out.println("\t" + "$scope.appGenMethods = [");
            for (Method method: clazz.getDeclaredMethods()) {
                if (method.getName().startsWith(Builder.APPLICATION_GEN_PREFIX)) {
                    System.out.println("\t\t" + "'" + method.getName() + "',");
                }
            }
            System.out.println("\t" + "];");
        }
    }
}
