package bowling;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

class MultiplayerGameTest {

    @Test
    void testExampleGameFlow() {
        IPartieMultiJoueurs partie = new PartieMultiJoueurs();
        String[] players = { "Pierre", "Paul" };

        assertEquals("Prochain tir : joueur Pierre, tour n° 1, boule n° 1",
                partie.demarreNouvellePartie(players));

        assertEquals("Prochain tir : joueur Pierre, tour n° 1, boule n° 2",
                partie.enregistreLancer(5));

        assertEquals("Prochain tir : joueur Paul, tour n° 1, boule n° 1",
                partie.enregistreLancer(3));

        assertEquals("Prochain tir : joueur Pierre, tour n° 2, boule n° 1",
                partie.enregistreLancer(10));

        assertEquals("Prochain tir : joueur Pierre, tour n° 2, boule n° 2",
                partie.enregistreLancer(7));

        assertEquals("Prochain tir : joueur Paul, tour n° 2, boule n° 1",
                partie.enregistreLancer(3));

        assertEquals(18, partie.scorePour("Pierre"));
        assertEquals(10, partie.scorePour("Paul"));

        assertThrows(IllegalArgumentException.class, () -> partie.scorePour("Jacques"));
    }
}
