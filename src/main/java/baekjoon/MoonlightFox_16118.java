package baekjoon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class MoonlightFox_16118 {

    static class Edge implements Comparable<Edge>{
        int node;
        long cost;
        int state;

        Edge(int node, long cost, int state){
            this.node = node;
            this.cost = cost;
            this.state = state;
        }

        @Override
        public int compareTo(Edge o){
            return Long.compare(cost, o.cost);
        }

    }

    static ArrayList<Edge>[] graph;
    static long[][] distW;
    static long[] distF;
    static long INF = Long.MAX_VALUE;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        graph = new ArrayList[n+1];
        for (int i = 1; i < n+1; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken()) * 2;
            graph[a].add(new Edge(b, c, 0));
            graph[b].add(new Edge(a, c, 0));
        }

        distW = new long[2][n+1];
        distF = new long[n+1];

        Arrays.fill(distF, INF);
        Arrays.fill(distW[0], INF);
        Arrays.fill(distW[1], INF);

        PriorityQueue<Edge> pq = new PriorityQueue<>();
        distF[1] = 0;
        //여우 다익
        pq.add(new Edge(1, 0, 0));
        while (!pq.isEmpty()){
            Edge now = pq.poll();

            if (now.cost > distF[now.node]) continue;
            for (Edge next : graph[now.node]){
                long total = now.cost + next.cost;
                if (total < distF[next.node]){
                    distF[next.node] = total;
                    pq.add(new Edge(next.node, total, 0));
                }
            }
        }

        distW[0][1] = 0;
        //늑대 다익
        pq.add(new Edge(1, 0, 0));
        while (!pq.isEmpty()){
            Edge now = pq.poll();

            if (now.cost > distW[now.state][now.node]) continue;

            for (Edge next : graph[now.node]){
                long total = now.cost + ((now.state==0)? next.cost/2 : next.cost*2);
                if (total < distW[1 - now.state][next.node]){
                    distW[1-now.state][next.node] = total;
                    pq.add(new Edge(next.node, total, 1- now.state));
                }
            }
        }

        int ans = 0;
        for (int i = 2; i < n+1; i++) {
            if (Math.min(distW[0][i], distW[1][i]) > distF[i])
                ans++;
        }
        System.out.println(ans);
    }
}
