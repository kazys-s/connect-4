package com.connect4.web.authentication;

import com.connect4.domain.Player;
import com.connect4.web.datastore.Repository;
import com.connect4.web.datastore.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class PlayerPreAuthenticationService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {


    private final Repository<Player> players;

    @Autowired
    public PlayerPreAuthenticationService(Repository<Player> players) {
        this.players = players;
    }

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
        try {
            Integer id = Integer.valueOf(token.getPrincipal().toString());
            return new PlayerUserDetails(players.get(id));
        } catch (ResourceNotFoundException | NumberFormatException e) {
            throw new UsernameNotFoundException("Unknown user id: " + token.getPrincipal());
        }
    }
}
