package com.danielvm.destiny2bot.dto.destiny;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerPGCREntry {

  private DestinyUserInfo destinyUserInfo;

  private String characterClass;

  private Integer lightLevel;
}
