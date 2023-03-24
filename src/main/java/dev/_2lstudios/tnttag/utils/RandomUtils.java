package dev._2lstudios.tnttag.utils;

import java.util.List;

public class RandomUtils {
    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static <T> T getRandomElement(List<T> list) {
        int max = list.size() - 1;
        int index = RandomUtils.getRandomNumber(0, max);
        return list.get(index);
    }
}
