
package org.example.presentation;

import org.example.dao.IDao;
import org.example.metier.IMetier;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class Presentation2 {

    public static void main(String[] args) {
        try {
            // 1. Charger les noms des classes depuis config.txt
            String[] classNames = loadClassNames();

            // 2. Instancier le DAO
            IDao dao = (IDao) instantiateClass(classNames[0]);
            System.out.println(
                    "[CONTAINER] DAO instancié : "
                            + dao.getClass().getName()
            );

            // 3. Instancier le Métier
            IMetier metier = (IMetier) instantiateClass(classNames[1]);
            System.out.println(
                    "[CONTAINER] Métier instancié : "
                            + metier.getClass().getName()
            );

            // 4. Injecter le DAO dans le Métier via la réflexion
            injectDependency(metier, "setDao", dao);

            System.out.println(
                    "[CONTAINER] Dépendance DAO injectée avec succès."
            );

            // 5. Exécuter le calcul
            double resultat = metier.calcul();
            System.out.println("Résultats = " + resultat);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Lit les noms des classes depuis src/main/resources/config.txt.
     */
    private static String[] loadClassNames() throws IOException {
        String[] classNames = new String[2];

        InputStream input = Presentation2.class
                .getClassLoader()
                .getResourceAsStream("config.txt");

        if (input == null) {
            throw new FileNotFoundException(
                    "config.txt introuvable dans src/main/resources"
            );
        }

        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(input))) {

            for (int i = 0; i < 2; i++) {
                String line = reader.readLine();

                if (line == null || line.trim().isEmpty()) {
                    throw new IOException(
                            "Ligne " + (i + 1)
                                    + " manquante ou vide dans config.txt"
                    );
                }

                classNames[i] = line.trim();
            }
        }

        return classNames;
    }

    /**
     * Instancie une classe à partir de son nom complet.
     */
    private static Object instantiateClass(String className)
            throws Exception {

        Class<?> clazz = Class.forName(className);
        Constructor<?> constructor = clazz.getDeclaredConstructor();

        return constructor.newInstance();
    }

    /**
     * Injecte une dépendance dans un objet via une méthode setter.
     */
    private static void injectDependency(
            Object target,
            String setterName,
            Object dependency) throws Exception {

        Class<?> targetClass = target.getClass();

        Method setterMethod = targetClass.getMethod(
                setterName,
                dependency.getClass().getInterfaces()[0]
        );

        setterMethod.invoke(target, dependency);
    }
}
