package com.github.hazork.mysouls.utils;

import java.util.List;
import java.util.Random;

public class Utils {

    public static <E> E poll(List<E> list) {
	E element = list.get(getRandom(list.size()));
	list.remove(element);
	return element;
    }

    public static int getRandom(int range) {
	return new Random().nextInt(range);
    }

}
