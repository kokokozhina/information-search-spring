package com.kokokozhina.utils;



import javafx.util.Pair;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.Math.min;

@Service
public class LevenshteinDistance {

    private Integer count(String s, String t) {

        int n = s.length() + 1;
        int m = t.length() + 1;
        int[][] dp = new int[n][m];
        for(int i = 1; i < n; i++) {
            dp[i][0] = i;
        }
        for(int j = 1; j < m; j++) {
            dp[0][j] = j;
        }
        for(int i = 1; i < n; i++) {
            for(int j = 1; j < m; j++) {
                dp[i][j] = min(dp[i][j - 1] + 1, min(dp[i - 1][j] + 1,
                        dp[i - 1][j - 1] + ((s.charAt(i - 1) == t.charAt(j - 1)) ? 0 : 1)));
            }
        }

        return dp[n - 1][m  -1];
    }

    public List<Pair<Integer, String>> getTopMatches(List<String> dictionary, int numberOfMatches, String s) {
        System.out.println("LevenshteinDistance started at " + LocalTime.now());

        List<Pair<Integer, String>> topMatches = new ArrayList<>(Collections.nCopies(numberOfMatches, null));

        for(int i = 0; i < dictionary.size(); i++) {
            String word = dictionary.get(i);
            int curDist = count(word, s);
            if (i < numberOfMatches) {
                topMatches.set(i, new Pair<>(curDist, word));
            } else if (topMatches.get(numberOfMatches - 1).getKey() > curDist) {
                topMatches.set(numberOfMatches - 1, new Pair<>(curDist, word));
                topMatches.sort((o1, o2) -> {
                    if (o1.getKey() < o2.getKey()) {
                        return -1;
                    } else if (o1.getKey().equals(o2.getKey())) {
                        return 0;
                    } else {
                        return 1;
                    }
                });
            }
        }
        System.out.println("LevenshteinDistance ended at " + LocalTime.now());

        return topMatches;
    }

}
