package com.connect4.web.endpoints.games;

import com.connect4.domain.Color;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

public class GameActionDtos {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinGameActionDto extends GameActionDtos {
        @NotNull
        private Color color;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DropDiscActionDto extends GameActionDtos {
        private int column;
    }

}
