package com.connect4.componenttests;

import com.connect4.MvcTestBase;
import com.connect4.domain.Color;
import com.connect4.web.endpoints.games.GameDto;
import com.connect4.web.endpoints.players.PlayerDtos.PlayerDto;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * As a Player
 * I need to be able to start a game
 * so that I can play Connect 4 with a friend
 * <p>
 * As a Player
 * I need to be able to drop my colour disc into a non full column on a gameboard that my friend or I have started
 * so that I can participate in a game of Connect 4 with a friend
 * <p>
 */
public class GameStartComponentTest extends MvcTestBase {

    @Test
    public void registerAndStartAGame() {
        // GIVEN I am registered
        PlayerDto myself = playerActions.registerNewPlayer("John");

        // WHEN I create a game
        GameDto game = gameActions.createNewGame(myself);

        // THEN I can join it
        game = gameActions.joinGame(game, myself, Color.BLUE);
    }

    @Test
    public void myselfAndFriendShouldBeAbleToPlayTheSameGame() {
        // GIVEN I and my friend are registered users
        PlayerDto myself = playerActions.registerNewPlayer("myself");
        PlayerDto friend = playerActions.registerNewPlayer("friend");

        // WHEN we join the same game
        GameDto game = gameActions.createNewGame(friend);
        gameActions.joinGame(game, myself, Color.BLUE);
        gameActions.joinGame(game, friend, Color.RED);

        // THEN we can drop some discs
        gameActions.dropDisc(game, friend, 2);
        gameActions.dropDisc(game, myself, 2);
        gameActions.dropDisc(game, friend, 1);
        game = gameActions.dropDisc(game, myself, 2);

        // AND see where they landed
        assertThat(game.getBoard()).containsExactly(
                ".......",
                ".......",
                ".......",
                "..B....",
                "..B....",
                ".RR...."
        );
    }

}
