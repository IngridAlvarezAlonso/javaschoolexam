package com.tsystems.javaschool.tasks.subsequence;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class Subsequence {

    /**
     * Checks if it is possible to get a sequence which is equal to the first
     * one by removing some elements from the second one.
     *
     * @param list2 first sequence
     * @param list2 second sequence
     * @return <code>true</code> if possible, otherwise <code>false</code>
     */
    @SuppressWarnings("rawtypes")
    //Method names parameters changed (CLEAN CODE)
    public boolean find(List list1, List list2) throws IllegalArgumentException{
        int list1pos = 0;

        if(null == list1)
            throw new IllegalArgumentException("x is null");
        if(null == list2)
            throw new IllegalArgumentException("y is null");

        for (Object list2Element : list2) {
            if (list1pos < list1.size()) {
                if (list1.get(list1pos).equals(list2Element))
                    list1pos++;
            }
        }

        return list1pos == list1.size();
    }
}
