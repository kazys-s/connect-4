package com.connect4.domain.inmemory;

import com.connect4.domain.exceptions.IllegalMoveException;
import org.junit.Test;

import java.util.stream.IntStream;

import static com.connect4.domain.Color.BLUE;
import static com.connect4.domain.Color.RED;
import static com.connect4.domain.Outcome.draw;
import static com.connect4.domain.Outcome.win;
import static java.util.stream.IntStream.range;
import static org.fest.assertions.Assertions.assertThat;

public class BoardTest {

    private final Board board = new Board();

    @Test(expected = IllegalMoveException.class)
    public void shouldThrowIllegalMoveExceptionIfColumnNegative() {
        board.dropDisc(-1, BLUE);
    }

    @Test(expected = IllegalMoveException.class)
    public void shouldThrowIllegalMoveExceptionIfColumnTooBig() {
        board.dropDisc(7, BLUE);
    }

    @Test(expected = IllegalMoveException.class)
    public void shouldThrowIllegalMoveExceptionIfResultIsPresent() {
        range(0, 5).forEach((row) -> board.dropDisc(0, RED));
    }

    @Test
    public void columnShouldFitMaxDiscs() {
        range(0, 6).forEach((row) -> board.dropDisc(0, row % 2 == 0 ? BLUE : RED));
    }

    @Test(expected = IllegalMoveException.class)
    public void columnShouldNotFit7Discs() {
        range(0, 6 + 1).forEach((row) -> board.dropDisc(0, row % 2 == 0 ? BLUE : RED));
    }

    @Test
    public void stackingFourDiscsVerticallyShouldProduceAWinner() {
        range(0, 4).forEach((any) -> board.dropDisc(0, BLUE));
        assertThat(board.getOutcome().get()).isEqualTo(win(BLUE));
    }

    @Test
    public void stackingFourDiscsHorizontallyShouldProduceAWinner() {
        range(0, 4).forEach((col) -> board.dropDisc(col, BLUE));
        assertThat(board.getOutcome().get()).isEqualTo(win(BLUE));
    }

    @Test
    public void stackingFourDiscsDiagonalyUpRightShouldProduceAWinner() {
        board.dropDisc(1, RED)
                .dropDisc(2, RED).dropDisc(2, RED)
                .dropDisc(3, RED).dropDisc(3, RED).dropDisc(3, RED);

        range(0, 4).forEach((col) -> board.dropDisc(col, BLUE));
        assertThat(board.getOutcome().get()).isEqualTo(win(BLUE));
    }

    @Test
    public void stackingFourDiscsDiagonalyUpLeftShouldProduceAWinner() {
        board.dropDisc(2, RED)
                .dropDisc(1, RED).dropDisc(1, RED)
                .dropDisc(0, RED).dropDisc(0, RED).dropDisc(0, RED);

        range(0, 4).forEach((col) -> board.dropDisc(col, BLUE));
        assertThat(board.getOutcome().get()).isEqualTo(win(BLUE));
    }

    @Test
    public void winningByPlacingDiscInTheMiddle() {
        range(0, 3).forEach((col) -> board.dropDisc(col, BLUE));
        range(4, 7).forEach((col) -> board.dropDisc(col, BLUE));
        assertThat(board.getOutcome().isPresent()).isEqualTo(false);

        board.dropDisc(3, BLUE);
        assertThat(board.getOutcome().get()).isEqualTo(win(BLUE));
    }

    @Test
    public void fillingSpaceWithDisksShouldResultInDraw() {
        range(0, 6).forEach((row) -> {
            IntStream.of(0, 2, 4, 6).forEach((col) -> board.dropDisc(col, row < 3 ? RED : BLUE));
            IntStream.of(1, 3, 5).forEach((col) -> board.dropDisc(col, row < 3 ? BLUE : RED));
        });
        assertThat(board.getOutcome().get()).isEqualTo(draw());
    }
}