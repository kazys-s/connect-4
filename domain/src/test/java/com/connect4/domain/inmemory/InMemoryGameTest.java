package com.connect4.domain.inmemory;

import com.connect4.domain.Color;
import com.connect4.domain.Player;
import com.connect4.domain.Slot;
import com.connect4.domain.exceptions.GameIsInProgressException;
import com.connect4.domain.exceptions.GameIsNotInProgressException;
import com.connect4.domain.exceptions.IllegalMoveException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static com.connect4.domain.Game.Status.*;
import static com.connect4.domain.Outcome.draw;
import static com.connect4.domain.Outcome.win;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class InMemoryGameTest {
    private static final Player PLAYER_A = new Player(1, "Any");
    private static final Player PLAYER_B = new Player(2, "Any");
    private static final Player PLAYER_C = new Player(2, "Any");

    private final Board board = Mockito.mock(Board.class);
    private final InMemoryGame game = new InMemoryGame(1, new SlotAllocator(), board); // using real implementation as it's quite simple

    @Before
    public void setUp() throws Exception {
        when(board.getOutcome()).thenReturn(Optional.empty());
    }

    @Test
    public void gameWithSlotsAvailableShouldHaveStatusWaitingForPlayers() {
        assertThat(game.getStatus()).isEqualTo(WAITING_FOR_PLAYERS);
    }

    @Test
    public void playerCanJoinAGameThatIsWaitingForPlayers() {
        game.assignSlot(PLAYER_A, Color.BLUE);
        assertThat(game.getState().getStatus()).isEqualTo(WAITING_FOR_PLAYERS);
        assertThat(game.getState().getAllocatedSlots()).containsOnly(new Slot(PLAYER_A, Color.BLUE));
    }

    @Test(expected = GameIsInProgressException.class)
    public void playerCanNotJoinAGameThatIsInProgress() {
        allocatePlayers(PLAYER_A, PLAYER_B);
        game.assignSlot(PLAYER_C, Color.GREEN);
    }

    @Test
    public void gameWithNoSlotsAvailableShouldHaveStatusInProgress() {
        allocatePlayers(PLAYER_A, PLAYER_B);
        assertThat(game.getStatus()).isEqualTo(IN_PROGRESS);
    }

    @Test(expected = GameIsNotInProgressException.class)
    public void droppingDiscIfGameIsNotInProgressShouldBeForbidden() {
        assertThat(game.dropDisc(PLAYER_A, 0));
    }

    @Test
    public void secondPlayerShouldHaveTheFirstMove() {
        allocatePlayers(PLAYER_A, PLAYER_B);
        assertThat(game.getState().getCurrentPlayer().get()).isEqualTo(PLAYER_B);
    }

    @Test(expected = IllegalMoveException.class)
    public void playerCanNotDropDiskIfItsNotHisMove() {
        allocatePlayers(PLAYER_A, PLAYER_B);
        game.dropDisc(PLAYER_A, 0);
    }

    @Test
    public void currentPlayerShouldChangeAfterMoveIsMade() {
        allocatePlayers(PLAYER_A, PLAYER_B);
        game.dropDisc(PLAYER_B, 0);
        assertThat(game.getState().getCurrentPlayer()).isEqualTo(Optional.of(PLAYER_A));
    }

    @Test
    public void resultShouldBeEmptyIfBoardHasNoResult() {
        allocatePlayers(PLAYER_A, PLAYER_B);
        assertThat(game.getResult().isPresent()).isFalse();
    }

    @Test
    public void resultShouldBeDrawIfBoardHasResultDraw() {
        allocatePlayers(PLAYER_A, PLAYER_B);
        when(board.getOutcome()).thenReturn(Optional.of(draw()));
        assertThat(game.getResult()).isEqualTo(Optional.of(draw()));
    }

    @Test
    public void resultShouldBeWinIfBoardHasResultWin() {
        allocatePlayers(PLAYER_A, PLAYER_B);
        when(board.getOutcome()).thenReturn(Optional.of(win(Color.BLUE)));
        assertThat(game.getResult()).isEqualTo(Optional.of(win(PLAYER_B)));
    }

    @Test
    public void gameWithNoSlotsAndAResultShouldHaveStatusFinished() {
        allocatePlayers(PLAYER_A, PLAYER_B);
        when(board.getOutcome()).thenReturn(Optional.of(draw()));
        assertThat(game.getStatus()).isEqualTo(FINISHED);
    }

    private void allocatePlayers(Player playerA, Player playerB) {
        game.assignSlot(playerA, Color.RED);
        game.assignSlot(playerB, Color.BLUE);
    }


}