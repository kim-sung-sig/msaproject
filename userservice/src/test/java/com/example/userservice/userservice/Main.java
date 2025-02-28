package com.example.userservice.userservice;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(br.readLine());

        int[] arr = new int[n];
        StringTokenizer st = new StringTokenizer(br.readLine());
        IntStream.range(0, n).forEach(i -> arr[i] = Integer.parseInt(st.nextToken()));
        br.close();

        if (arr.length == 1) {
            System.out.println((int) Math.pow(arr[0], 2));
            return;
        }

        Arrays.sort(arr);
        System.out.println(arr[0] * arr[arr.length - 1]);
    }

}
