package com.connect4.web.endpoints.games;

import com.connect4.domain.Color;
import com.connect4.domain.Outcome;
import com.connect4.domain.Player;
import com.connect4.domain.Slot;
import com.connect4.web.endpoints.games.GameDto.SlotDto;
import org.junit.Test;

import java.util.Collections;
import java.util.Optional;

import static com.connect4.domain.Color.BLUE;
import static com.connect4.domain.Color.RED;
import static com.connect4.domain.Game.GameState;
import static com.connect4.domain.Game.Status.FINISHED;
import static com.connect4.domain.Game.Status.WAITING_FOR_PLAYERS;
import static com.connect4.domain.Outcome.Type.DRAW;
import static com.connect4.domain.Outcome.Type.WIN;
import static com.connect4.domain.Outcome.win;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

public class GameDtoFactoryTest {

    private final GameDtoFactory dtoFactory = new GameDtoFactory();
    private final GameState validState = GameState.builder()
            .status(FINISHED)
            .result(Optional.empty())
            .allocatedSlots(Collections.emptyList())
            .boardSnapshot(new Color[0][0])
            .currentPlayer(Optional.empty())
            .build();

    @Test
    public void convertStatus() {
        GameDto dto = toDto(validState.withStatus(WAITING_FOR_PLAYERS));
        assertThat(dto.getStatus()).isEqualTo(WAITING_FOR_PLAYERS);
    }

    @Test
    public void convertSlots() {
        GameDto dto = toDto(validState.withAllocatedSlots(singletonList(new Slot(new Player(1, "Name"), BLUE))));
        assertThat(dto.getSlots()).containsExactly(new SlotDto(1, BLUE));
    }

    @Test
    public void convertEmptyCurrentPlayer() {
        GameDto dto = toDto(validState.withCurrentPlayer(Optional.empty()));
        assertThat(dto.getCurrentPlayer()).isNull();
    }

    @Test
    public void convertExistingCurrentPlayers() {
        GameDto dto = toDto(validState.withCurrentPlayer(Optional.of(new Player(2, "Name2"))));
        assertThat(dto.getCurrentPlayer()).isEqualTo(2);
    }

    @Test
    public void convertNoResult() {
        GameDto dto = toDto(validState.withResult(Optional.empty()));
        assertThat(dto.getOutcome()).isEqualTo(null);
    }

    @Test
    public void convertDraw() {
        GameDto dto = toDto(validState.withResult(Optional.of(Outcome.draw())));
        assertThat(dto.getOutcome()).isEqualTo(new GameDto.OutcomeDto(DRAW, null));
    }

    @Test
    public void convertWin() {
        GameDto dto = toDto(validState.withResult(Optional.of(win(new Player(5, "Name")))));
        assertThat(dto.getOutcome()).isEqualTo(new GameDto.OutcomeDto(WIN, 5));
    }

    @Test
    public void convertBoard() {
        GameDto dto = toDto(validState.withBoardSnapshot(new Color[][]{
                new Color[]{RED, BLUE, BLUE, BLUE, RED},
                new Color[]{null, BLUE, null, null, RED},
                new Color[]{null, null, null, null, null}
        }));

        assertThat(dto.getBoard()).containsExactly(
                ".....",
                ".B..R",
                "RBBBR"
        );
    }


    private GameDto toDto(GameState state) {
        return dtoFactory.toDto(1, state);
    }
}