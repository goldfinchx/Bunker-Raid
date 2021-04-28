package com.goldfinch.raid.core.parameters;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum RaidState {

    RECRUITING(true),
    COUNTDOWN(true),
    LIVE(false),
    ENDING(false);

    @Getter
    private final boolean isJoinable;

}
