
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import AdventureModel.AdventureGame;
import AdventureModel.Room;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ObjectHoverTests {
    @Test
    void properObjectDescTest() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        HashMap<Integer, Room> rooms = game.getRooms();
        assertEquals("a water bird", rooms.get(1).objectsInRoom.get(0).getDescription());
    }
    @Test
    void properObjectDescTest2() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        HashMap<Integer, Room> rooms = game.getRooms();
        assertEquals("a pirate chest", rooms.get(2).objectsInRoom.get(0).getDescription());
    }


}
