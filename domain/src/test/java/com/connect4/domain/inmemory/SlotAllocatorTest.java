package com.connect4.domain.inmemory;

import com.connect4.domain.Player;
import com.connect4.domain.Slot;
import com.connect4.domain.exceptions.ColorAlreadyTakenException;
import com.connect4.domain.exceptions.NoSlotAvailableException;
import com.connect4.domain.exceptions.PlayerAlreadyJoinedException;
import org.junit.Test;

import static com.connect4.domain.Color.*;
import static org.fest.assertions.Assertions.assertThat;

public class SlotAllocatorTest {
    private static final Player PLAYER_A = new Player(1, "Any");
    private static final Player PLAYER_B = new Player(2, "Any");
    private static final Player PLAYER_C = new Player(3, "Any");

    private final SlotAllocator slotAllocator = new SlotAllocator();

    @Test
    public void firstPlayerShouldGetASlot() {
        assertThat(slotAllocator.allocate(PLAYER_A, RED)).isEqualTo(new Slot(PLAYER_A, RED));
    }

    @Test(expected = PlayerAlreadyJoinedException.class)
    public void firstPlayerCanNotGetSlotTwice() {
        slotAllocator.allocate(PLAYER_A, RED);
        slotAllocator.allocate(PLAYER_A, BLUE);
    }

    @Test(expected = PlayerAlreadyJoinedException.class)
    public void playersAreComparedById() {
        slotAllocator.allocate(new Player(1, "Name 1"), RED);
        slotAllocator.allocate(new Player(1, "Name 2"), BLUE);
    }

    @Test
    public void secondPlayerShouldGetASlot() {
        slotAllocator.allocate(PLAYER_A, RED);
        assertThat(slotAllocator.allocate(PLAYER_B, BLUE)).isEqualTo(new Slot(PLAYER_B, BLUE));
    }

    @Test(expected = ColorAlreadyTakenException.class)
    public void secondPlayerCannotChooseTheSameColor() {
        slotAllocator.allocate(PLAYER_A, RED);
        slotAllocator.allocate(PLAYER_B, RED);
    }

    @Test(expected = NoSlotAvailableException.class)
    public void thirdPlayerShouldNotGetASlot() {
        slotAllocator.allocate(PLAYER_A, RED);
        slotAllocator.allocate(PLAYER_B, BLUE);
        slotAllocator.allocate(PLAYER_C, GREEN);
    }

}