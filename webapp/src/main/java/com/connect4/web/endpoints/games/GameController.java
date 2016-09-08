package com.connect4.web.endpoints.games;

import com.connect4.domain.Color;
import com.connect4.domain.Game;
import com.connect4.domain.Player;
import com.connect4.domain.inmemory.InMemoryGame;
import com.connect4.web.datastore.Repository;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(GameController.GAMES_URL)
@Api(tags = "Games")
public class GameController {
    public static final String GAMES_URL = "/games";

    private final Repository<Game> repository;
    private final GameDtoFactory dtoFactory;

    public GameController(Repository<Game> repository, GameDtoFactory dtoFactory) {
        this.repository = repository;
        this.dtoFactory = dtoFactory;
    }

    @RequestMapping(method = POST)
    public GameDto createGame() {
        return toDto(repository.create(InMemoryGame::new));
    }

    @RequestMapping(value = "/{id}", method = GET)
    public GameDto getGame(@PathVariable int id) {
        return toDto(repository.get(id));
    }

    @RequestMapping(value = "/{id}/join/{color}", method = POST)
    public GameDto joinGame(Player player, @PathVariable int id, @PathVariable Color color) {
        Game game = repository.get(id);
        game.assignSlot(player, color);
        return toDto(game);
    }

    @RequestMapping(value = "/{id}/dropDisc/{column}", method = POST)
    public GameDto dropDisc(Player player, @PathVariable int id, @PathVariable int column) {
        Game game = repository.get(id);
        game.dropDisc(player, column);
        return toDto(game);
    }

    private GameDto toDto(Game game) {
        return dtoFactory.toDto(game.getId(), game.getState());
    }
}
