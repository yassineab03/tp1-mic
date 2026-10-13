
package org.example.metier;

import org.example.dao.IDao;

public class MetierImplV2 implements IMetier {

    // Dépendance injectable
    private IDao dao;

    // Injection de la dépendance
    public void setDao(IDao dao) {
        this.dao = dao;
    }

    @Override
    public double calcul() {
        if (dao == null) {
            throw new IllegalStateException(
                    "Le DAO n'a pas été injecté."
            );
        }

        // Récupérer la valeur depuis le DAO
        double valeurBase = dao.getValue();

        // Nouvelle logique métier : multiplier par 3
        double resultat = valeurBase * 3;

        System.out.println(
                "[METIER V2] Calcul effectué. Résultat : " + resultat
        );

        return resultat;
    }
}
