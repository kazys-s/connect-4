package com.connect4.web.endpoints.players;

import com.connect4.domain.Player;
import com.connect4.web.datastore.Repository;
import com.connect4.web.endpoints.players.PlayerDtos.PlayerDto;
import com.connect4.web.endpoints.players.PlayerDtos.RegisterPlayerDto;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(PlayerController.PLAYERS_URL)
@Api(tags = "Players")
public class PlayerController {
    public static final String PLAYERS_URL = "/players";

    private final Repository<Player> repository;

    @Autowired
    public PlayerController(Repository<Player> repository) {
        this.repository = repository;
    }

    @RequestMapping(value = "", method = GET)
    public List<PlayerDto> listAllPlayers() {
        return repository.findAll().map(this::toDto).collect(toList());
    }

    @RequestMapping(value = "/{id}", method = GET)
    public PlayerDto getPlayer(@PathVariable("id") final int id) {
        return toDto(repository.get(id));
    }

    @RequestMapping(method = POST)
    public PlayerDto registerNewPlayer(@Valid @RequestBody RegisterPlayerDto dto) {
        return toDto(repository.create((id) -> new Player(id, dto.getName())));
    }

    private PlayerDto toDto(Player entity) {
        return new PlayerDto(entity.getId(), entity.getName());
    }
}
