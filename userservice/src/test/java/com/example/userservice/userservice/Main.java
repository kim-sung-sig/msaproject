package com.example.userservice.userservice;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {

    static int N, M; // 도시의 개수, 버스 노선의 개수
    static Edge[] bus; // 버스 노선 정보

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine(), " ");
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        bus = new Edge[M];
        for(int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine(), " ");
            int src = Integer.parseInt(st.nextToken());
            int dest = Integer.parseInt(st.nextToken());
            int weight = Integer.parseInt(st.nextToken());
            bus[i] = new Edge(src, dest, weight);
        }
        br.close();

        bellmanFord(N, M, bus, 1);
    }

    static void bellmanFord(int V, int E, Edge[] edges, int start){
        long INF = Long.MAX_VALUE;
        long[] dist = new long[V + 1];
        Arrays.fill(dist, INF);
        dist[start] = 0;

        // 가중치 갱신
        for (int i = 0; i < V - 1; i++) {
            for (Edge edge : edges) {
                if (dist[edge.src] == INF) continue;
                if (dist[edge.src] + edge.weight < dist[edge.dest]) dist[edge.dest] = dist[edge.src] + edge.weight;
            }
        }

        // 어떤 도시로 가는 과정에서 시간을 무한히 오래 전으로 되돌릴 수 있는지 확인
        for (Edge edge : edges) {
            if (dist[edge.src] == INF) continue;
            if (dist[edge.src] + edge.weight < dist[edge.dest]) {
                System.out.println("-1");
                return;
            }
        }

        // 해당 도시로 가는 경로가 없을 경우 -1 출력
        StringBuilder sb = new StringBuilder();
        for (int i = 2; i <= V; i++) sb.append(dist[i] == INF ? "-1\n" : dist[i] + "\n");
        System.out.println(sb.toString());
    }

}

class Edge {
    int src, dest, weight;
    public Edge(int s, int d, int w) {
        this.src = s;
        this.dest = d;
        this.weight = w;
    }
}