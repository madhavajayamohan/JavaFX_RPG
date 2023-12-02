import java.io.IOException;
import java.util.HashMap;

import AdventureModel.AdventureGame;
import AdventureModel.Room;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BasicAdventureTest {
    @Test
    void getCommandsTest() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        String commands = game.player.getCurrentRoom().getCommands();
        assertEquals("DOWN,NORTH,IN,WEST,UP,SOUTH", commands);
    }

    @Test
    void getObjectString() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        String objects = game.player.getCurrentRoom().getObjectString();
        assertEquals("a water bird", objects);
    }
    @Test
    void properObjectDescTestRoom1() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        HashMap<Integer, Room> rooms = game.getRooms();
        assertEquals("a water bird", rooms.get(1).objectsInRoom.get(0).getDescription());
    }
    @Test
    void properObjectDescTestRoom2() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        HashMap<Integer, Room> rooms = game.getRooms();
        assertEquals("a pirate chest", rooms.get(2).objectsInRoom.get(0).getDescription());
    }
}
