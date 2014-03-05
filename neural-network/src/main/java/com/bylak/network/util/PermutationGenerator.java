package com.bylak.network.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: bylak
 * Date: 08.02.14
 * Time: 16:19
 * To change this template use File | Settings | File Templates.
 */
public final class PermutationGenerator {
    public List<Integer> generatePermutation(final int startIndex, final int stopIndex) {
        final List<Integer> permutation = new ArrayList<>();

        for (int i = startIndex; i < stopIndex; i++) {
            permutation.add(i);
        }

        java.util.Collections.shuffle(permutation);

        return permutation;
    }
}
