package com.example.userservice.userservice;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Main {

    public static void main(String[] args) {
        Solution solution = new Solution();
        int[] xs = {10, 10, 2};
        int[] ys = {40, 40, 5};
        int[] ns = {5, 30, 4};

        int[] results = {2, 1, -1};

        for (int i = 0; i < 3; i++) {
            int x = xs[i], y = ys[i], n = ns[i], result = results[i];

            int answer = solution.solution(x, y, n);
            System.out.println(answer);
            assertEquals(result, answer);
        }
    }

}

class Solution {

    public int solution(int x, int y, int n) {
        if (x == y) return 0;

        int[] dp = new int[y+1];
        final int MAX = 1_000_001;
        Arrays.fill(dp, MAX);
        dp[x] = 0;

        Queue<Integer> q = new LinkedList<>();
        q.offer(x);

        while (!q.isEmpty()) {
            int cur = q.poll();

            for (int val : new int[]{cur + n, cur * 2, cur * 3}) {
                if (val > y) continue;

                if(dp[val] > dp[cur] + 1) {
                    dp[val] = dp[cur] + 1;
                    q.offer(val);
                }
            }
        }

        return dp[y] == MAX ? -1 : dp[y];
    }

}