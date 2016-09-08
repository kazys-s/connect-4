package com.connect4;

import com.connect4.domain.Color;
import com.connect4.web.endpoints.games.GameDto;
import com.connect4.web.endpoints.games.GameDto.SlotDto;
import com.connect4.web.endpoints.players.PlayerDtos.PlayerDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.stream.IntStream;

import static com.connect4.domain.Game.Status.WAITING_FOR_PLAYERS;
import static org.fest.assertions.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GameActions extends RestActions {

    public GameActions(MockMvc mvc, ObjectMapper objectMapper) {
        super(mvc, objectMapper);
    }


    public GameDto createNewGame(PlayerDto player) {
        ResultActions result = translateException(() -> postAs("/games", player.getId()).andExpect(status().isOk()));
        GameDto dto = responseAs(result, GameDto.class);
        assertThat(dto.getId()).isNotNull();
        assertThat(dto.getStatus()).isEqualTo(WAITING_FOR_PLAYERS);
        assertThat(dto.getSlots()).isEmpty();
        return dto;
    }

    public List<GameDto> listGames(PlayerDto player) {
        ResultActions result = translateException(() -> getAs("/games", player.getId()).andExpect(status().isOk()));
        List<GameDto> dtos = responseAsList(result, GameDto.class);
        return dtos;
    }

    public GameDto getGame(int gameId, PlayerDto player) {
        ResultActions result = translateException(() -> getAs("/games/" + gameId, player.getId()).andExpect(status().isOk()));
        GameDto dto = responseAs(result, GameDto.class);
        assertThat(dto.getId()).isNotNull();
        return dto;
    }

    public GameDto joinGame(GameDto game, PlayerDto player, Color color) {
        ResultActions result = translateException(() -> postAs("/games/" + game.getId() + "/join/" + color, player.getId()).andExpect(status().isOk()));
        GameDto dto = responseAs(result, GameDto.class);
        assertThat(dto.getSlots()).contains(new SlotDto(player.getId(), color));
        return dto;
    }

    public GameDto dropDisc(GameDto game, PlayerDto player, int column) {
        ResultActions result = translateException(() -> tryDropDisc(game, player, column).andExpect(status().isOk()));
        GameDto dto = responseAs(result, GameDto.class);
        assertThat(dto.getBoard().stream().anyMatch(s -> s.charAt(column) != '.')).isTrue();
        return dto;
    }

    public ResultActions tryDropDisc(GameDto game, PlayerDto player, int column) {
        return postAs("/games/" + game.getId() + "/dropDisc/" + column, player.getId());
    }


    public GameDto dropDiscsInThisOrder(GameDto game, PlayerDto firstPlayer, List<Integer> firstPlayerMoves, PlayerDto secondPlayer, List<Integer> secondPlayerMoves) {
        IntStream.range(0, firstPlayerMoves.size()).forEach(i -> {
            dropDisc(game, firstPlayer, firstPlayerMoves.get(i));
            if (i < secondPlayerMoves.size()) {
                dropDisc(game, secondPlayer, secondPlayerMoves.get(i));
            }
        });

        return getGame(game.getId(), firstPlayer);
    }
}
