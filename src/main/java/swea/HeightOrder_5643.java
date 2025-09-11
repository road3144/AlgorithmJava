package swea;

import java.io.*;
import java.util.*;

public class HeightOrder_5643 {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();

        int T = Integer.parseInt(br.readLine().trim());
        for (int tc = 1; tc <= T; tc++) {
            int N = Integer.parseInt(br.readLine().trim());
            int M = Integer.parseInt(br.readLine().trim());

            // 1-indexed
            ArrayList<Integer>[] g = new ArrayList[N + 1];
            ArrayList<Integer>[] rg = new ArrayList[N + 1];
            for (int i = 1; i <= N; i++) {
                g[i] = new ArrayList<>();
                rg[i] = new ArrayList<>();
            }

            for (int i = 0; i < M; i++) {
                StringTokenizer st = new StringTokenizer(br.readLine());
                int a = Integer.parseInt(st.nextToken());
                int b = Integer.parseInt(st.nextToken());
                // a < b (a -> b)
                g[a].add(b);
                rg[b].add(a);
            }

            boolean[] vis = new boolean[N + 1];
            ArrayDeque<Integer> q = new ArrayDeque<>();
            int answer = 0;

            for (int s = 1; s <= N; s++) {

                Arrays.fill(vis, false);
                int bigger = 0;
                q.clear();
                q.add(s);
                vis[s] = true;
                while (!q.isEmpty()) {
                    int cur = q.poll();
                    for (int nx : g[cur]) {
                        if (!vis[nx]) {
                            vis[nx] = true;
                            q.add(nx);
                            bigger++;
                        }
                    }
                }

                // count smaller
                Arrays.fill(vis, false);
                int smaller = 0;
                q.clear();
                q.add(s);
                vis[s] = true;
                while (!q.isEmpty()) {
                    int cur = q.poll();
                    for (int nx : rg[cur]) {
                        if (!vis[nx]) {
                            vis[nx] = true;
                            q.add(nx);
                            smaller++;
                        }
                    }
                }

                if (bigger + smaller == N - 1) answer++;
            }

            sb.append('#').append(tc).append(' ').append(answer).append('\n');
        }

        System.out.print(sb);
    }
}
