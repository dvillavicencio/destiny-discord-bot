package com.danielvm.destiny2bot.dto.destiny;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BungieNetMembership {

  private String membershipId;

  private String uniqueName;

  private String displayName;

  private Boolean isDeleted;

  private String locale;
}
