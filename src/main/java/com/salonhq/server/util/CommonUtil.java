package com.salonhq.server.util;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CommonUtil {

    private CommonUtil() {}

    /**
     * Maps a list of items using the provided mapper function and collects the results into a new list.
     */
    public static <T, R> List<R> mapToList(List<T> items, Function<T, R> mapper) {
        return items.stream().map(mapper).collect(Collectors.toList());
    }
}
