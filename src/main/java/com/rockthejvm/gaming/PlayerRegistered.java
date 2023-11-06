package com.rockthejvm.gaming;

import java.time.Instant;
import java.util.UUID;

public class PlayerRegistered implements ServerEvent {
  private Instant eventTime;
  private UUID playerId;
  private String nickname;

  public PlayerRegistered(Instant eventTime, UUID playerId, String nickname) {
    this.eventTime = eventTime;
    this.playerId = playerId;
    this.nickname = nickname;
  }

  @Override
  public Instant getEventTime() {
    return eventTime;
  }

  public UUID getPlayerId() {
    return playerId;
  }

  @Override
  public String getId() {
    return "player|" + playerId + "|" + nickname;
  }
}
