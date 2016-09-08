package com.connect4.componenttests;

import com.connect4.MvcTestBase;
import com.connect4.domain.Color;
import com.connect4.web.endpoints.games.GameDto;
import com.connect4.web.endpoints.games.GameDto.OutcomeDto;
import com.connect4.web.endpoints.players.PlayerDtos.PlayerDto;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static com.connect4.domain.Outcome.Type.DRAW;
import static com.connect4.domain.Outcome.Type.WIN;
import static org.fest.assertions.Assertions.assertThat;

/**
 * As a Player
 * I need to be able to determine the outcome of the game
 * so that I know if I have won , lost, or drawn.
 * <p>
 * So that games of Connect 4 can be played
 * the Game service
 * will need to enforce the rules of Connect 4
 */
public class GameOutcomeComponentTest extends MvcTestBase {

    private PlayerDto myself;
    private PlayerDto friend;

    @Test
    public void friendShouldBeAWinnerIfHeConnects4First() {
        // GIVEN I am playing a game with my friend
        GameDto game = startGameFor("Myself", "Friend");

        // WHEN my friend connects 4
        game = dropDisksInThisOrder(game,
                friend, Arrays.asList(2, 2, 2, 2),
                myself, Arrays.asList(1, 1, 5)
        );

        // THEN my friend should be a winner
        assertThat(game.getResult()).isEqualTo(new OutcomeDto(WIN, friend.getId()));
    }

    @Test
    public void outcomeShouldBeDrawIfWeFillUpABoardWithoutConnecting4() {
        // GIVEN I am playing a game with my friend
        GameDto game = startGameFor("Myself", "Friend");

        // WHEN we fill up the board
        game = dropDisksInThisOrder(game,
                friend, Arrays.asList(0, 0, 0, 1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4, 5, 5, 5, 6, 6, 6),
                myself, Arrays.asList(0, 0, 0, 2, 2, 2, 1, 1, 1, 4, 4, 4, 3, 3, 3, 6, 6, 6, 5, 5, 5)
        );

        // THEN my friend should be a winner
        assertThat(game.getResult()).isEqualTo(new OutcomeDto(DRAW, null));
    }

    private GameDto startGameFor(String myself, String friend) {
        this.myself = playerActions.registerNewPlayer(myself);
        this.friend = playerActions.registerNewPlayer(friend);
        GameDto game = gameActions.createNewGame(this.friend);
        game = gameActions.joinGame(game, this.myself, Color.BLUE);
        return gameActions.joinGame(game, this.friend, Color.RED);
    }

    private GameDto dropDisksInThisOrder(GameDto game, PlayerDto firstPlayer, List<Integer> firstPlayerMoves, PlayerDto secondPlayer, List<Integer> secondPlayerMoves) {
        IntStream.range(0, firstPlayerMoves.size()).forEach(i -> {
            gameActions.dropDisc(game, firstPlayer, firstPlayerMoves.get(i));
            if (i < secondPlayerMoves.size()) {
                gameActions.dropDisc(game, secondPlayer, secondPlayerMoves.get(i));
            }
        });

        return gameActions.getGame(game.getId(), firstPlayer);
    }
}
