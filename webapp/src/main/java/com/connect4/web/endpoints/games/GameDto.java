package com.connect4.web.endpoints.games;

import com.connect4.domain.Color;
import com.connect4.domain.Game.Status;
import com.connect4.domain.Outcome.Type;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(NON_NULL)
public class GameDto {
    private int id;
    private Status status;
    private OutcomeDto outcome;
    private List<SlotDto> slots;
    private List<String> board;
    private Integer currentPlayer;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SlotDto {
        private int playerId;
        private Color color;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OutcomeDto {
        private Type type;
        private Integer playerId;
    }
}
