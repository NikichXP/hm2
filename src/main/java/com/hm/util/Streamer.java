package com.hm.util;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Created by NikichXP on 09.12.2016.
 */
public class Streamer {

	public static <T> Stream<T> arrStream(T... arr){
		return Arrays.asList(arr).stream();
	}
}
