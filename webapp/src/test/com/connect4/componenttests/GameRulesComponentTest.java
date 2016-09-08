package com.connect4.componenttests;

import com.connect4.MvcTestBase;
import com.connect4.domain.Color;
import com.connect4.web.endpoints.games.GameDto;
import com.connect4.web.endpoints.players.PlayerDtos.PlayerDto;
import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;

import static java.util.Arrays.asList;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * So that games of Connect 4 can be played
 * the Game service
 * will need to enforce the rules of Connect 4
 */
public class GameRulesComponentTest extends MvcTestBase {

    private PlayerDto myself;
    private PlayerDto friend;

    @Test
    public void dropIntoAFullColumnShouldNotBeAllowed() throws Exception {
        // GIVEN I am playing a game with my friend
        GameDto game = startGameFor("Myself", "Friend");

        // WHEN we fill a column
        game = gameActions.dropDiscsInThisOrder(game,
                friend, asList(2, 2, 2),
                myself, asList(2, 2, 2)
        );

        // THEN we should not be allowed to drop more discs
        gameActions.tryDropDisc(game, friend, 2)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Column is full"));
    }


    @Test
    public void dropIfItsNotMyTurnShouldNotBeAllowed() throws Exception {
        // GIVEN I am playing a game with my friend
        GameDto game = startGameFor("Myself", "Friend");

        // WHEN I make a turn
        gameActions.dropDisc(game, friend, 1);
        gameActions.dropDisc(game, myself, 1);

        // THEN I should not be allowed to make another one immediately
        gameActions.tryDropDisc(game, myself, 2)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("It's not your move"));
    }

    @Test
    public void dropOutOfBoardShouldNotBeAllowed() throws Exception {
        // GIVEN I am playing a game with my friend
        GameDto game = startGameFor("Myself", "Friend");

        // WHEN I try to drop into invalid column
        ResultActions resultActions = gameActions.tryDropDisc(game, friend, -1);

        // THEN I should get a notice
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Invalid column index: -1"));
    }

    private GameDto startGameFor(String myself, String friend) {
        this.myself = playerActions.registerNewPlayer(myself);
        this.friend = playerActions.registerNewPlayer(friend);
        GameDto game = gameActions.createNewGame(this.friend);
        game = gameActions.joinGame(game, this.myself, Color.BLUE);
        return gameActions.joinGame(game, this.friend, Color.RED);
    }
}
