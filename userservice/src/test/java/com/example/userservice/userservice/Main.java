package com.example.userservice.userservice;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(br.readLine());
        StringTokenizer st = new StringTokenizer(br.readLine());

        int cnt = 0;
        int cur = Integer.parseInt(st.nextToken());
        for (int i = 0; i < n - 1; i++) {
            int next = Integer.parseInt(st.nextToken());
            if (cur <= next) cnt++;
            cur = next;
        }

        System.out.println(cnt + 1);
    }

}
