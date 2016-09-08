package com.connect4.web.endpoints;

import com.connect4.MvcTestBase;
import com.connect4.web.endpoints.players.PlayerDtos.RegisterPlayerDto;
import org.junit.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ExceptionMappingComponentTest extends MvcTestBase {

    @Test
    public void entityNotFoundShouldBeResolvedTo404() throws Exception {
        restActions.get("/players/99999")
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value("Resource not found, id: 99999"));
    }

    @Test
    public void invalidRequestShouldBeResolvedTo400() throws Exception {
        restActions.post("/players", new RegisterPlayerDto(null))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Validation failed: name may not be null"));
    }

}