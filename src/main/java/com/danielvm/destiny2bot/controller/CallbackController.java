package com.danielvm.destiny2bot.controller;

import com.danielvm.destiny2bot.config.BungieConfiguration;
import com.danielvm.destiny2bot.service.UserAuthorizationService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@Slf4j
@RestController
public class CallbackController {

    private final UserAuthorizationService userAuthorizationService;
    private final BungieConfiguration bungieConfiguration;

    public CallbackController(
            UserAuthorizationService userAuthorizationService,
            BungieConfiguration bungieConfiguration) {
        this.userAuthorizationService = userAuthorizationService;
        this.bungieConfiguration = bungieConfiguration;
    }

    /**
     * Handle the callback from Discord during OAuth2 authentication
     *
     * @param authorizationCode the authentication code (short-lived)
     * @return Redirect to start Bungie OAuth2
     */
    @GetMapping("/discord/callback")
    public RedirectView handleCallBackFromDiscord(
            @RequestParam("code") String authorizationCode,
            HttpSession httpSession) {
        userAuthorizationService.authenticateDiscordUser(authorizationCode, httpSession);
        return new RedirectView(bungieOAuth2Url());
    }

    /**
     * Handle the callback from Bungie during OAuth2 authentication
     *
     * @param authorizationCode the authentication code (short-lived)
     * @return Redirect to start Bungie OAuth2
     */
    @GetMapping("/bungie/callback")
    public ResponseEntity<?> handleCallBackFromBungie(
            @RequestParam("code") String authorizationCode,
            HttpSession httpSession) {
        userAuthorizationService.linkUserDetails(authorizationCode, httpSession);
        return ResponseEntity.noContent().build();
    }

    private String bungieOAuth2Url() {
        return bungieConfiguration.getAuthorizationUrl() +
                "?response_type=code" +
                "&client_id=" +
                bungieConfiguration.getClientId();

    }
}