package com.danielvm.destiny2bot.client;

import com.danielvm.destiny2bot.dto.discord.user.UserResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;

public interface DiscordClient {

    /**
     * Gets the current Discord user details
     *
     * @param bearerToken The bearer token of the Discord user
     * @return {@link UserResponse}
     */
    @GetExchange("/users/@me")
    ResponseEntity<UserResponse> getUser(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken);
}