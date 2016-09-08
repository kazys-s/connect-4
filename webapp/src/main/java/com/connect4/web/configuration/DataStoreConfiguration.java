package com.connect4.web.configuration;

import com.connect4.domain.Game;
import com.connect4.domain.Player;
import com.connect4.web.datastore.InMemoryRepository;
import com.connect4.web.datastore.Repository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataStoreConfiguration {

    @Bean
    public Repository<Player> playerRepository() {
        return new InMemoryRepository<>();
    }

    @Bean
    public Repository<Game> gameRepository() {
        return new InMemoryRepository<>();
    }
}
