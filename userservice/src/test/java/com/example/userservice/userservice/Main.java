package com.example.userservice.userservice;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int t = Integer.parseInt(br.readLine());

        StringBuilder sb = new StringBuilder();
        while(t-- > 0) {
            StringTokenizer st = new StringTokenizer(br.readLine(), " ");
            int n = Integer.parseInt(st.nextToken());
            int m = Integer.parseInt(st.nextToken());
            int k = Integer.parseInt(st.nextToken());

            List<int[]> points = new ArrayList<>();
            boolean[][] visited = new boolean[n][m];
            for(int i = 0; i < k; i++) {
                st = new StringTokenizer(br.readLine(), " ");
                int x = Integer.parseInt(st.nextToken());
                int y = Integer.parseInt(st.nextToken());
                visited[x][y] = true;
                points.add(new int[]{x, y});
            }

            int[] dx = {-1, 1, 0, 0};
            int[] dy = {0, 0, -1, 1};

            int result = 0;
            for (int[] point : points) {
                int i = point[0];
                int j = point[1];
                if (!visited[i][j]) continue;

                result++;

                Queue<int[]> queue = new LinkedList<>();
                queue.offer(point);
                visited[i][j] = false;

                while (!queue.isEmpty()) {
                    int[] cur = queue.poll();
                    int x = cur[0], y = cur[1];

                    for (int d = 0; d < 4; d++) {
                        int nx = x + dx[d], ny = y + dy[d];

                        if (0 <= nx && 0 <= ny && nx < n && ny < m && visited[nx][ny]) {
                            queue.offer(new int[]{nx, ny});
                            visited[nx][ny] = false;
                        }
                    }
                }
            }

            sb.append(result).append("\n");
        }

        System.out.println(sb);
    }

}