package com.connect4.domain.inmemory;

import com.connect4.domain.Color;
import com.connect4.domain.Outcome;
import com.connect4.domain.exceptions.IllegalMoveException;
import lombok.NonNull;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static com.connect4.domain.Outcome.draw;
import static com.connect4.domain.Outcome.win;
import static java.util.stream.IntStream.range;

class Board {
    private static final int NO_OF_COLUMNS = 7;
    private static final int NO_OF_ROWS = 6;
    private static final int NO_OF_DISKS_TO_CONNECT = 4;

    private final Color board[][] = new Color[NO_OF_ROWS][NO_OF_COLUMNS];
    private final AtomicReference<Optional<Outcome<Color>>> outcome = new AtomicReference<>(Optional.empty());

    synchronized Color[][] getBoardSnapshot() {
        return board;
    }

    Optional<Outcome<Color>> getOutcome() {
        return outcome.get();
    }

    synchronized Board dropDisc(int column, @NonNull Color color) {
        if (getOutcome().isPresent()) {
            throw new IllegalMoveException("No moves allowed after end result");
        }

        if (isColumnOutOfBounds(column)) {
            throw new IllegalMoveException("Invalid column index: " + column);
        }

        if (isColumnFull(column)) {
            throw new IllegalMoveException("Column is full");
        }

        int row = processDiskDrop(column, color);
        outcome.set(outcomeAfterDrop(column, row));
        return this;
    }


    private int processDiskDrop(int column, Color color) {
        int row = range(0, NO_OF_ROWS).filter((r) -> colorAt(column, r) == null).findFirst().getAsInt();
        board[row][column] = color;
        return row;
    }

    private Optional<Outcome<Color>> outcomeAfterDrop(int column, int row) {
        if (isWin(column, row)) {
            return Optional.of(win(colorAt(column, row)));
        } else if (isDraw()) {
            return Optional.of(draw());
        } else {
            return Optional.empty();
        }
    }

    private boolean isWin(int column, int row) {
        return isWin(column, row, 1, 0) ||  // horizontal
                isWin(column, row, 0, 1) || // vertical
                isWin(column, row, 1, 1) || // up & right
                isWin(column, row, -1, 1);  // up & left

    }

    private boolean isWin(int column, int row, int columnDirection, int rowDirection) {
        int colorsInRow = 1 +
                countDiscsTowards(column, row, columnDirection, rowDirection, colorAt(column, row)) +
                countDiscsTowards(column, row, -columnDirection, -rowDirection, colorAt(column, row));

        return colorsInRow >= NO_OF_DISKS_TO_CONNECT;
    }

    private int countDiscsTowards(int column, int row, int columnDirection, int rowDirection, Color color) {
        return range(1, NO_OF_COLUMNS)
                .filter((i) -> {
                    int nextColumn = column - i * columnDirection;
                    int nextRow = row - i * rowDirection;

                    return isColumnOutOfBounds(nextColumn) || isRowOutOfBounds(nextRow) || colorAt(nextColumn, nextRow) != color;
                })
                .findFirst().getAsInt() - 1;
    }

    private boolean isDraw() {
        return range(0, NO_OF_COLUMNS).allMatch(this::isColumnFull);
    }

    private boolean isColumnFull(int column) {
        return colorAt(column, NO_OF_ROWS - 1) != null;
    }

    private boolean isRowOutOfBounds(int row) {
        return row < 0 || row >= NO_OF_ROWS;
    }

    private boolean isColumnOutOfBounds(int column) {
        return column < 0 || column >= NO_OF_COLUMNS;
    }

    private Color colorAt(int column, int row) {
        return board[row][column];
    }
}
