package com.example.userservice.userservice;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        int k = Integer.parseInt(st.nextToken());

        int count = 0;
        for (int i = 0; i < n; i++) {
            String row = br.readLine();

            int[] dp = new int[m + 1];
            for (int j = 1; j <= m; j++) {
                dp[j] = dp[j - 1] + (row.charAt(j - 1) == '0' ? 1 : 0);
            }

            for (int j = 0; j <= m - k; j++) {
                if (dp[j + k] - dp[j] == k) count++;
            }
        }
        System.out.println(count);
    }

}
