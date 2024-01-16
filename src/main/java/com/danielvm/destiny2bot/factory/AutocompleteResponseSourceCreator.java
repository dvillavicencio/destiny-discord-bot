package com.danielvm.destiny2bot.factory;

import com.danielvm.destiny2bot.dto.discord.Interaction;
import com.danielvm.destiny2bot.dto.discord.InteractionResponse;
import reactor.core.publisher.Mono;

/**
 * Implementation of this interface are responsible for creating the autocomplete responses that are
 * generally used before sending a slash-command to the Discord bot
 */
public interface AutocompleteResponseSourceCreator {

  /**
   * Create an interaction Response for an autocomplete request
   *
   * @param interaction Interaction data in-case the autocomplete message source needs context
   * @return {@link InteractionResponse}
   */
  Mono<InteractionResponse> autocompleteResponse(Interaction interaction);
}
