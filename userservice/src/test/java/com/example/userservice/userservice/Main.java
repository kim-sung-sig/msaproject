package com.example.userservice.userservice;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(br.readLine());
        int 거스름돈 = 1000 - n;

        int count = 0;
        int[] 동전 = { 500, 100, 50, 10, 5, 1 };
        for (int i = 0; i < 동전.length; i++) {
            count += 거스름돈 / 동전[i];
            거스름돈 %= 동전[i];
        }

        System.out.println(count);
    }

}
