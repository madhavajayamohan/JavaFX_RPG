import java.io.IOException;
import java.util.HashMap;

import AdventureModel.AdventureGame;
import AdventureModel.AdventureObject;
import AdventureModel.Room;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BasicAdventureTest {
    @Test
    void decreaseLives() throws IOException {
        AdventureGame game = new AdventureGame("Group_37_Final");
        game.player.decreaseLives();
        assertEquals(2, game.player.getLives());
    }

    @Test
    void increaseLives() throws IOException {
        AdventureGame game = new AdventureGame("Group_37_Final");
        game.player.increaseLives();
        assertEquals(4, game.player.getLives());
    }

    @Test
    void pickupExtraLives() throws IOException {
        AdventureGame game = new AdventureGame("Group_37_Final");
        game.interpretAction("WEST");
        game.interpretAction("WEST");
        game.interpretAction("WEST");
        game.interpretAction("WEST");
        game.interpretAction("WEST");
        game.interpretAction("take EXTRALIFE");
        assertEquals(4, game.player.getLives());
    }

    @Test
    void pickupImmunityBuff() throws IOException {
        AdventureGame game = new AdventureGame("Group_37_Final");
        game.interpretAction("WEST");
        game.interpretAction("take IMMUNITYBUFF");
        game.interpretAction("use IMMUNITYBUFF");
        assertEquals(true, game.player.getImmunity());
    }

    @Test
    void pickupAttackBuff() throws IOException {
        AdventureGame game = new AdventureGame("Group_37_Final");
        game.interpretAction("WEST");
        game.interpretAction("WEST");
        game.interpretAction("take ATTACKBUFF");
        game.interpretAction("use ATTACKBUFF");
        assertEquals(200, game.player.getAttackPower());
    }

    @Test
    void pickupDefenseBuff() throws IOException {
        AdventureGame game = new AdventureGame("Group_37_Final");
        game.interpretAction("WEST");
        game.interpretAction("WEST");
        game.interpretAction("WEST");
        game.interpretAction("take DEFENSEBUFF");
        game.interpretAction("use DEFENSEBUFF");
        assertEquals(200, game.player.getDefensePower());
    }

    @Test
    void poisonRoom() throws IOException {
        AdventureGame game = new AdventureGame("Group_37_Final");
        game.interpretAction("take KEYS");
        game.interpretAction("WEST");
        game.interpretAction("WEST");
        game.interpretAction("WEST");
        game.interpretAction("WEST");
        game.interpretAction("WEST");
        game.interpretAction("DOWN");
        assertEquals(50, game.player.getAttackPower());
    }

    @Test
    void mostRecentBuff1() throws IOException {
        AdventureGame game = new AdventureGame("Group_37_Final");
        game.interpretAction("WEST");
        game.interpretAction("WEST");
        game.interpretAction("take ATTACKBUFF");
        game.interpretAction("use ATTACKBUFF");
        game.interpretAction("WEST");
        game.interpretAction("take DEFENSEBUFF");
        game.interpretAction("use DEFENSEBUFF");
        assertEquals(200, game.player.getDefensePower());
    }

    @Test
    void mostRecentBuff2() throws IOException {
        AdventureGame game = new AdventureGame("Group_37_Final");
        game.interpretAction("take KEYS");
        game.interpretAction("WEST");
        game.interpretAction("take IMMUNITYBUFF");
        game.interpretAction("WEST");
        game.interpretAction("take ATTACKBUFF");
        game.interpretAction("use ATTACKBUFF");
        game.interpretAction("WEST");
        game.interpretAction("take DEFENSEBUFF");
        game.interpretAction("use DEFENSEBUFF");
        game.interpretAction("WEST");
        game.interpretAction("WEST");
        game.interpretAction("use IMMUNITYBUFF");
        game.interpretAction("DOWN");
        assertEquals(50, game.player.getAttackPower());
    }

    @Test
    void roomHints() throws IOException {
        AdventureGame game = new AdventureGame("Group_37_Final");
        assertEquals("To fight troll press FIGHT\n", game.player.getCurrentRoom().getHint());
    }

    void room4Hints() throws IOException {
        AdventureGame game = new AdventureGame("Group_37_Final");
        game.interpretAction("WEST");
        game.interpretAction("WEST");
        game.interpretAction("WEST");
        assertEquals("Go in direction where you can find penguins\n", game.player.getCurrentRoom().getHint());
    }
}
