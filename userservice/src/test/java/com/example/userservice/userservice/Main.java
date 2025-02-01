package com.example.userservice.userservice;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class Main {

    static int[][] delta = new int[][] {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};

    public static void main(String[] args) throws Exception {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int N = Integer.parseInt(br.readLine());

            int[][] arr = new int[N][N];

            for (int i = 0; i < N; i++) {
                String line = br.readLine();
                for (int j = 0; j < N; j++) {
                    arr[i][j] = line.charAt(j) - '0';
                }
            }

            List<Integer> countList = new ArrayList<>();
            for (int x = 0; x < N; x++) {
                for (int y = 0; y < N; y++) {
                    if (arr[x][y] == 0) continue;
                    countList.add(bfs(x, y, arr));
                }
            }

            System.out.println(countList.size());
            countList.sort(Integer::compareTo);
            countList.forEach(System.out::println);
        }
    }

    static int bfs(int x, int y, int[][] arr) {
        int homeCount = 1;
        int N = arr.length;

        Queue<int[]> queue = new ArrayDeque<>();
        arr[x][y] = 0;
        queue.offer(new int[] {x, y});

        while (!queue.isEmpty()) {
            int[] current = queue.poll();

            for (int[] d : delta) {
                int nx = current[0] + d[0];
                int ny = current[1] + d[1];

                if (nx < 0 || ny < 0 || nx >= N || ny >= N) continue;
                if (arr[nx][ny] == 0) continue;

                arr[nx][ny] = 0;
                homeCount++;
                queue.offer(new int[] {nx, ny});
            }
        }

        return homeCount;
    }

    /*
    public static void main(String[] args){
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int N = Integer.parseInt(br.readLine());

            int[][] arr = new int[N][N];

            for (int i = 0; i < N; i++) {
                String line = br.readLine();
                for (int j = 0; j < N; j++) {
                    arr[i][j] = line.charAt(j) - '0';
                }
            }

            int[][] dp = new int[N][N];
            int count = 0;
            List<Integer> countList = new ArrayList<>();

            for (int x = 0; x < N; x++) {
                for (int y = 0; y < N; y++) {
                    if (arr[x][y] == 0) continue;
                    if (dp[x][y] != 0) continue;

                    countList.add(bfs(x, y, arr, dp, ++count));
                }
            }

            System.out.println(count);
            countList.stream().sorted().forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static int bfs(int x, int y, int[][] arr, int[][] dp, int sign) {
        int homeCount = 1;
        int N = arr.length;

        Queue<int[]> queue = new LinkedList<>();
        dp[x][y] = sign;
        queue.add(new int[] {x, y});

        while (!queue.isEmpty()) {
            int[] current = queue.poll();

            for (int[] d : delta) {
                int nx = current[0] + d[0];
                int ny = current[1] + d[1];

                if (nx < 0 || ny < 0 || nx >= N || ny >= N) continue;
                if (arr[nx][ny] == 0) continue;
                if (dp[nx][ny] != 0) continue;

                dp[nx][ny] = sign;
                homeCount++;
                queue.add(new int[] {nx, ny});
            }
        }

        return homeCount;
    }

     */

    static void print(int[][] arr) {
        for (int[] a : arr) {
            for (int b : a) {
                System.out.print(b + " ");
            }
            System.out.println("");
        }
    }

}
