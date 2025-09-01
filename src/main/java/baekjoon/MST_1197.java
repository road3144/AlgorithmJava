package baekjoon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class MST_1197 {

    static int v, e;
    static int[] parent;
    static Edge[] edges;

    static class Edge implements Comparable<Edge>{
        int a;
        int b;
        int cost;
        public Edge(int a, int b, int cost){
            this.a = a;
            this.b = b;
            this.cost = cost;
        }

        @Override
        public int compareTo(Edge o) {
            return this.cost - o.cost;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        StringTokenizer st = new StringTokenizer(br.readLine());
        v = Integer.parseInt(st.nextToken());
        e = Integer.parseInt(st.nextToken());
        parent = new int[v+1];
        edges = new Edge[e];
        long ans = 0;
        for (int i = 1; i <= v; i++) {
            parent[i] = i;
        }

        for (int i = 0; i < e; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            int cost = Integer.parseInt(st.nextToken());
            edges[i] = new Edge(x, y, cost);
        }

        Arrays.sort(edges);
        for (Edge e : edges) {
            int x = e.a;
            int y = e.b;
            int cost = e.cost;

            if (find(x) != find(y)) {
                union(x, y);
                ans += cost;
            }
        }
        System.out.println(ans);
    }

    static int find(int x){
        if(parent[x] == x) return x;
        return parent[x] = find(parent[x]);
    }

    static void union(int x, int y){
        int px = find(x);
        int py = find(y);
        if (px <  py)
            parent[py] = px;
        else
            parent[px] = py;
    }
}
