package com.connect4.web.authentication;

import com.connect4.domain.Player;
import com.google.common.collect.ImmutableList;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class PlayerUserDetails extends User {
    public static final String DEFAULT_ROLE = "ROLE_USER";

    private final Player player;

    public PlayerUserDetails(Player player) {
        super(player.getId().toString(), "N/A", ImmutableList.of(new SimpleGrantedAuthority(DEFAULT_ROLE)));
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
