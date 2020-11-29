package com.github.hazork.mysouls.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Utils {

    public static <E> E poll(List<E> list) {
	int index = getRandom(list.size());
	E element = list.get(index);
	list.remove(index);
	return element;
    }

    public static int getRandom(int range) {
	return new Random().nextInt(range);
    }

    public static UUID[] multiply(UUID source, int mult) {
	List<UUID> list = Arrays.asList(new UUID[mult]);
	Collections.fill(list, source);
	return list.toArray(new UUID[mult]);
    }

}
