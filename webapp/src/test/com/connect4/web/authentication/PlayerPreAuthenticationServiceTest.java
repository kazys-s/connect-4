package com.connect4.web.authentication;

import com.connect4.domain.Player;
import com.connect4.web.datastore.Repository;
import com.connect4.web.datastore.ResourceNotFoundException;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;


public class PlayerPreAuthenticationServiceTest {

    private final Repository<Player> repository = Mockito.mock(Repository.class);
    private final PlayerPreAuthenticationService service = new PlayerPreAuthenticationService(repository);

    @Test(expected = UsernameNotFoundException.class)
    public void usernameNotFoundShouldBeThrownForNonExistingId() {
        when(repository.get(anyInt())).thenThrow(new ResourceNotFoundException(3));
        service.loadUserDetails(new PreAuthenticatedAuthenticationToken("3", "N/A"));
    }

    @Test(expected = UsernameNotFoundException.class)
    public void usernameNotFoundShouldBeThrownForInvalidId() {
        service.loadUserDetails(new PreAuthenticatedAuthenticationToken("invalid", "N/A"));
    }

    @Test
    public void expectedPlayerUserDetailsShouldBeCreated() {
        Player player = new Player(1, "Name");
        when(repository.get(anyInt())).thenReturn(player);
        assertThat(service.loadUserDetails(new PreAuthenticatedAuthenticationToken(1, "N/A")))
                .isEqualTo(new PlayerUserDetails(player));
    }
}