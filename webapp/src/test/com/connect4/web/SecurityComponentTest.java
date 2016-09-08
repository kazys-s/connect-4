package com.connect4.web;

import com.connect4.MvcTestBase;
import com.connect4.web.endpoints.players.PlayerDtos.PlayerDto;
import org.junit.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class SecurityComponentTest extends MvcTestBase {

    @Test
    public void playersShouldBeAccessibleToAnonymousUser() throws Exception {
        restActions.post("/players", new PlayerDto(null, "New User")).andExpect(status().isOk());
        restActions.get("/players").andExpect(status().isOk());
        restActions.get("/players/123456789").andExpect(status().isNotFound());
    }

    @Test
    public void gamesShouldNotBeAccessibleToAnonymousUser() throws Exception {
        restActions.post("/games").andExpect(status().isForbidden());
        restActions.get("/games").andExpect(status().isForbidden());
        restActions.get("/games/1").andExpect(status().isForbidden());
    }
}