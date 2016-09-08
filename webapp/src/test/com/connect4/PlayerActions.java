package com.connect4;

import com.connect4.web.endpoints.players.PlayerDtos.PlayerDto;
import com.connect4.web.endpoints.players.PlayerDtos.RegisterPlayerDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.fest.assertions.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PlayerActions extends RestActions {

    public PlayerActions(MockMvc mvc, ObjectMapper objectMapper) {
        super(mvc, objectMapper);
    }

    public PlayerDto registerNewPlayer(String name) {
        ResultActions result = translateException(() -> post("/players", new RegisterPlayerDto(name)).andExpect(status().isOk()));
        PlayerDto playerDto = responseAs(result, PlayerDto.class);
        assertThat(playerDto.getId()).isNotNull();
        assertThat(playerDto.getName()).isEqualTo(name);
        return playerDto;
    }
}
