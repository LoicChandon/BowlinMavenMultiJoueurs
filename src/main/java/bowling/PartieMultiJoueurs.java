package bowling;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Implémentation d'une partie multijoueurs.
 */
public class PartieMultiJoueurs implements IPartieMultiJoueurs {

    private String[] noms;
    private Map<String, PartieMonoJoueur> parties;
    private int currentPlayerIndex;
    private boolean demarree;

    public PartieMultiJoueurs() {
        this.demarree = false;
    }

    @Override
    public String demarreNouvellePartie(String[] nomsDesJoueurs) throws IllegalArgumentException {
        if (nomsDesJoueurs == null || nomsDesJoueurs.length == 0) {
            throw new IllegalArgumentException("Il faut au moins un joueur");
        }
        this.noms = nomsDesJoueurs.clone();
        this.parties = new LinkedHashMap<>();
        for (String nom : this.noms) {
            if (nom == null || nom.isEmpty()) {
                throw new IllegalArgumentException("Nom de joueur invalide");
            }
            parties.put(nom, new PartieMonoJoueur());
        }
        this.currentPlayerIndex = 0;
        this.demarree = true;
        return messageProchainTir(this.noms[currentPlayerIndex]);
    }

    @Override
    public String enregistreLancer(int nombreDeQuillesAbattues) throws IllegalStateException {
        if (!demarree) {
            throw new IllegalStateException("La partie n'est pas démarrée.");
        }

        // Si la partie est déjà terminée pour tous
        if (tousTermines()) {
            return "Partie terminée";
        }

        String joueurCourant = noms[currentPlayerIndex];
        PartieMonoJoueur partie = parties.get(joueurCourant);

        // enregistrer le lancer pour le joueur courant (on ignore la valeur de retour)
        partie.enregistreLancer(nombreDeQuillesAbattues);

        // Si après le lancer tous les joueurs sont terminés -> fin de partie
        if (tousTermines()) {
            return "Partie terminée";
        }

        // Si le joueur courant doit encore lancer (même tour), on reste sur lui
        if (!partie.estTerminee() && partie.numeroProchainLancer() != 1) {
            return messageProchainTir(joueurCourant);
        }

        // Sinon on passe au joueur suivant non terminé
        advanceToNextPlayer();
        return messageProchainTir(noms[currentPlayerIndex]);
    }

    @Override
    public int scorePour(String nomDuJoueur) throws IllegalArgumentException {
        if (!demarree) {
            throw new IllegalArgumentException("La partie n'est pas démarrée.");
        }
        PartieMonoJoueur p = parties.get(nomDuJoueur);
        if (p == null) {
            throw new IllegalArgumentException("Le joueur ne participe pas à cette partie");
        }
        return p.score();
    }

    // Avance currentPlayerIndex vers le suivant qui n'est pas terminé
    private void advanceToNextPlayer() {
        int taille = noms.length;
        for (int i = 1; i <= taille; i++) {
            int idx = (currentPlayerIndex + i) % taille;
            PartieMonoJoueur p = parties.get(noms[idx]);
            if (!p.estTerminee()) {
                currentPlayerIndex = idx;
                return;
            }
        }
        // Si on n'a trouvé personne (tous terminés), garder l'index comme est
    }

    private boolean tousTermines() {
        for (PartieMonoJoueur p : parties.values()) {
            if (!p.estTerminee()) return false;
        }
        return true;
    }

    private String messageProchainTir(String nomJoueur) {
        PartieMonoJoueur p = parties.get(nomJoueur);
        int tour = p.numeroTourCourant();
        int boule = p.numeroProchainLancer();
        return "Prochain tir : joueur " + nomJoueur + ", tour n° " + tour + ", boule n° " + boule;
    }
}
