package swea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

public class Hanaro_1251 {
    static int T;
    static int n;
    static double ans, e;
    static int[] x, y, parents;
    static List<Edge> edgeList;
    static StringBuilder sb = new StringBuilder();

    static class Edge implements Comparable<Edge> {
        int start;
        int end;
        double weight;

        Edge(int start, int end, double weight) {
            this.start = start;
            this.end = end;
            this.weight = weight;
        }

        @Override
        public int compareTo(Edge o) {
            return Double.compare(weight, o.weight);
        }
    }

    public static void make() {
        for (int i = 0; i < n; i++) {
            parents[i] = i;
        }
    }

    public static int find(int a) {
        if (parents[a] == a) return a;
        return parents[a] = find(parents[a]);
    }

    public static boolean union(int a, int b) {
        a = find(a);
        b = find(b);
        if (a == b) return false;
        if (a < b) parents[b] = a;
        else parents[a] = b;
        return true;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        T = Integer.parseInt(st.nextToken());
        for (int test_case = 1; test_case <= T; test_case++) {
            sb.append("#").append(test_case).append(" ");
            st = new StringTokenizer(br.readLine());
            n = Integer.parseInt(st.nextToken());
            x = new int[n];
            y = new int[n];
            ans = 0;
            parents = new int[n];
            make();
            edgeList = new ArrayList<>();
            st = new StringTokenizer(br.readLine());
            for (int i = 0; i < n; i++) {
                x[i] = Integer.parseInt(st.nextToken());
            }
            st = new StringTokenizer(br.readLine());
            for (int i = 0; i < n; i++) {
                y[i] = Integer.parseInt(st.nextToken());
            }
            st = new StringTokenizer(br.readLine());
            e = Double.parseDouble(st.nextToken());
            for (int i = 0; i < n; i++) {
                for (int j = i+1; j < n; j++) {
                    double weight = Math.sqrt((Math.pow(x[i] - x[j], 2) + Math.pow(y[i] - y[j], 2)));
                    edgeList.add(new Edge(i, j, weight * weight * e));
                }
            }
            Collections.sort(edgeList);
            int cnt = 0;
            for (Edge e : edgeList){
                int a = e.start;
                int b = e.end;
                if (find(a) != find(b)) {
                    union(a, b);
                    ans += e.weight;
                    cnt ++;
                }
                if (cnt == n-1) break;
            }
            sb.append(Math.round(ans)).append("\n");
        }
        System.out.println(sb);
    }
}
