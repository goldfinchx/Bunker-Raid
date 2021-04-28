package com.goldfinch.raid.items.parameters;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor@Getter
public enum ItemType {

    COLD_WEAPON("Холодное оружие"),
    FOOD("Еда"),
    MEDICS("Медицина"),
    OTHER("Другое"),
    ARMOR("Броня"),
    AMMO("Патроны"),
    WEAPON("Оружие"),
    ELECTRICITY("Электронника");



    private final String title;
}
