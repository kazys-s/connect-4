package com.connect4.web.endpoints.games;

import com.connect4.domain.Color;
import com.connect4.domain.Game.GameState;
import com.connect4.domain.Outcome;
import com.connect4.domain.Player;
import com.connect4.domain.Slot;
import com.connect4.web.endpoints.games.GameDto.OutcomeDto;
import com.connect4.web.endpoints.games.GameDto.SlotDto;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

@Component
class GameDtoFactory {

    GameDto toDto(Integer id, GameState state) {
        return GameDto.builder()
                .id(id)
                .status(state.getStatus())
                .outcome(toOutcomeDto(state.getResult()))
                .slots(toSlotDtos(state.getAllocatedSlots()))
                .currentPlayer(state.getCurrentPlayer().isPresent() ? state.getCurrentPlayer().get().getId() : null)
                .board(toBoard(state.getBoardSnapshot()))
                .build();
    }

    private List<String> toBoard(Color[][] boardState) {
        // converting board of Colors to list of strings containing first letter of each color
        // this is done just easier representation in SWAGGER, real client would probably prefer different format
        return range(0, boardState.length).boxed().map((row) -> {
            Color colors[] = boardState[boardState.length - row - 1];
            return range(0, colors.length).boxed().map(col -> toSymbol(colors[col])).collect(joining());
        }).collect(toList());
    }

    private String toSymbol(Color color) {
        return (color == null) ? "." : color.toString().substring(0, 1);
    }

    private OutcomeDto toOutcomeDto(Optional<Outcome<Player>> optionalResult) {
        if (optionalResult.isPresent()) {
            Outcome<Player> outcome = optionalResult.get();
            boolean winnerPresent = outcome.getWinner().isPresent();
            return new OutcomeDto(outcome.getType(), winnerPresent ? outcome.getWinner().get().getId() : null);
        } else {
            return null;
        }
    }

    private List<SlotDto> toSlotDtos(Collection<Slot> slots) {
        return slots.stream().map(s -> new SlotDto(s.getPlayer().getId(), s.getColor())).collect(toList());
    }
}
