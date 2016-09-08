package com.connect4.componenttests;

import com.connect4.MvcTestBase;
import com.connect4.domain.Color;
import com.connect4.web.endpoints.games.GameDto;
import com.connect4.web.endpoints.players.PlayerDtos.PlayerDto;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * So that many people can play at the same time
 * the Game Service
 * will need to support multiple concurrent games
 */
public class MultiplayerComponentTest extends MvcTestBase {

    @Test
    public void registerAndStartAndJoinMultipleGames() {
        // GIVEN I am registered
        PlayerDto myself = playerActions.registerNewPlayer("Myself");

        // WHEN I create multiple games
        GameDto game1 = gameActions.createNewGame(myself);
        GameDto game2 = gameActions.createNewGame(myself);
        GameDto game3 = gameActions.createNewGame(myself);

        // THEN I can some of them
        gameActions.joinGame(game1, myself, Color.BLUE);
        gameActions.joinGame(game2, myself, Color.BLUE);
        gameActions.joinGame(game3, myself, Color.RED);
    }

    @Test
    public void viewGamesCreatedByFriend() {
        // GIVEN My friend and I are registered
        PlayerDto friend = playerActions.registerNewPlayer("Friend");
        PlayerDto myself = playerActions.registerNewPlayer("Myself");

        // WHEN My friend creates some games
        GameDto game1 = gameActions.createNewGame(friend);
        GameDto game2 = gameActions.createNewGame(friend);

        // THEN I can see them on a list
        List<GameDto> games = gameActions.listGames(myself);
        assertThat(games).contains(game1, game2);

        // AND I can get game details
        assertThat(gameActions.getGame(game1.getId(), myself))
                .isEqualTo(game1);
    }

    @Test
    public void registerAndViewOtherUsers() {
        // GIVEN My friend and I are registered
        PlayerDto friend = playerActions.registerNewPlayer("Friend");
        PlayerDto myself = playerActions.registerNewPlayer("Myself");

        // WHEN I view player list
        List<PlayerDto> player = playerActions.listPlayers();

        // THEN I can see us on a list
        assertThat(player).contains(friend, myself);

        // AND I can fetch our details
        assertThat(playerActions.getPlayer(friend.getId())).isEqualTo(friend);
        assertThat(playerActions.getPlayer(myself.getId())).isEqualTo(myself);
    }
}
