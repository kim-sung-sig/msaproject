package com.example.userservice.userservice;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class Main {

    static List<String> moeum = List.of("a", "e", "i", "o", "u"); // 모음 리스트

    public static void main(String[] args) throws Exception {
        var reader = new BufferedReader(new InputStreamReader(System.in));
        var st = new StringTokenizer(reader.readLine(), " ");
        int L = Integer.parseInt(st.nextToken());
        int C = Integer.parseInt(st.nextToken());

        String[] arr = new String[C];
        st = new StringTokenizer(reader.readLine(), " ");
        for (int i = 0; i < C; i++) arr[i] = st.nextToken();
        Arrays.sort(arr);

        List<String> result = new ArrayList<>();
        generateCombinations(arr, L, 0, new StringBuilder(), result);
        result.forEach(System.out::println);

    }

    static void generateCombinations(String[] arr, int L, int startIndex, StringBuilder current, List<String> result) {

        if (current.length() == L) {
            String candidate = current.toString();
            if (isValid(candidate)) result.add(candidate);
            return;
        }

        for (int i = startIndex; i < arr.length; i++) {
            current.append(arr[i]);
            generateCombinations(arr, L, i + 1, current, result);
            current.deleteCharAt(current.length() - 1); // 백트래킹
        }
    }

    static boolean isValid(String s) {
        int mc = 0;
        int jc = 0;
        for (String str : s.split("")) {
            if (moeum.contains(str)) mc++;
            else jc++;
        }
        return mc >= 1 && jc >= 2;
    }

}
