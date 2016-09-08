package com.connect4.web.endpoints.players;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

public class PlayerDtos {

    @Data
    @AllArgsConstructor
    public static class PlayerDto {
        @NotNull
        private Integer id;

        @NotNull
        private String name;
    }

    @Data
    @AllArgsConstructor
    public static class RegisterPlayerDto {
        @NotNull
        private String name;
    }

}
