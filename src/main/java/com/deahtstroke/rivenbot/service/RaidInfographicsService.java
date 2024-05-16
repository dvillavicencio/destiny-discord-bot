package com.deahtstroke.rivenbot.service;

import com.deahtstroke.rivenbot.dto.discord.Interaction;
import com.deahtstroke.rivenbot.dto.discord.Option;
import com.deahtstroke.rivenbot.exception.ImageRetrievalException;
import com.deahtstroke.rivenbot.util.InteractionUtils;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Service
@Slf4j
public class RaidInfographicsService {

  private static final String RAID_OPTION_NAME = "raid";
  private static final String ENCOUNTER_OPTION_NAME = "encounter";
  private static final String ASSETS_BASE_PATH = "classpath:static/raids/%s/%s/*.*";

  private final PathMatchingResourcePatternResolver resourcePatternResolver;

  public RaidInfographicsService(PathMatchingResourcePatternResolver resourcePatternResolver) {
    this.resourcePatternResolver = resourcePatternResolver;
  }

  /**
   * Retrieve all images for all encounters given a raid name and an encounter name
   *
   * @param interaction The interaction from where to extract raid and encounter info
   * @return HashMap with an indexed key and a value of the corresponding classpath resource
   * @throws IOException In case something unexpected happens when retrieving the files in memory
   */
  public Mono<Map<Long, Resource>> retrieveEncounterImages(Interaction interaction) {
    List<Option> options = interaction.getData().getOptions();
    String raidDirectory = InteractionUtils.retrieveInteractionOption(options, RAID_OPTION_NAME);
    String encounterDirectory = InteractionUtils.retrieveInteractionOption(options,
        ENCOUNTER_OPTION_NAME);

    String basePath = ASSETS_BASE_PATH.formatted(raidDirectory, encounterDirectory);
    return Mono.defer(() -> {
      try {
        Resource[] resources = resourcePatternResolver.getResources(basePath);
        return Flux.fromIterable(Arrays.asList(resources))
            .index()
            .collectMap(Tuple2::getT1, Tuple2::getT2);
      } catch (IOException e) {
        log.error("There was an error retrieving images for raid [{}] at encounter [{}]",
            raidDirectory, encounterDirectory);
        return Mono.error(new ImageRetrievalException("Retrieving images failed", e));
      }
    });
  }
}
