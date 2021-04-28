package com.goldfinch.raid.utils;

import java.util.concurrent.ThreadLocalRandom;

public class IntegerUtils {

    public static int getRandomInteger(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

}
