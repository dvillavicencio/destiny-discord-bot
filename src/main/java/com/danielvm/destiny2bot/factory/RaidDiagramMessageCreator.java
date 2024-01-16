package com.danielvm.destiny2bot.factory;

import com.danielvm.destiny2bot.dto.discord.Component;
import com.danielvm.destiny2bot.dto.discord.Interaction;
import com.danielvm.destiny2bot.dto.discord.InteractionResponse;
import com.danielvm.destiny2bot.dto.discord.InteractionResponseData;
import com.danielvm.destiny2bot.dto.discord.Option;
import com.danielvm.destiny2bot.dto.discord.SelectOption;
import com.danielvm.destiny2bot.enums.Raid;
import com.danielvm.destiny2bot.exception.ResourceNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import reactor.core.publisher.Mono;

@org.springframework.stereotype.Component
public class RaidDiagramMessageCreator implements CommandResponseCreator,
    AutocompleteResponseSourceCreator {

  @Override
  public Mono<InteractionResponse> createResponse(Interaction interaction) {
    return null;
  }

  public Mono<InteractionResponse> autocompleteResponse(Interaction interaction) {
    Option raidOption = interaction.getData().getOptions().stream()
        .filter(opt -> Objects.equals(opt.getName(), "raid"))
        .findFirst()
        .orElseThrow(
            () -> new ResourceNotFoundException("No raid option found for given interaction"));
    Raid raid = Arrays.stream(Raid.values())
        .filter(r -> Objects.equals(raidOption.getValue(), r.name()))
        .findFirst()
        .orElseThrow(() -> new ResourceNotFoundException(
            "No raid found for the given option selected in 'Raid' field")
        );

    return raid.getEncounters()
        .map(encounter -> SelectOption.builder()
            .label(encounter.getEncounterName())
            .value(encounter.getEncounterName())
            .description(encounter.getDescription())
            .build())
        .collectList()
        .map(selectOptions -> new InteractionResponse(4,
            InteractionResponseData.builder()
                .content("The following are the encounters for " + raid.getLabel())
                .components(List.of(
                    Component.builder()
                        .type(1)
                        .components(
                            List.of(Component.builder()
                                .customId("select_raid_encounter")
                                .type(3)
                                .options(selectOptions)
                                .build()))
                        .build()))
                .build()));
  }
}