package swea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class JobOrder_1267 {
    static int v, e;
    static int[] degree;
    static boolean[] visited;
    static boolean[][] map;
    static Queue<Integer> q;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        for (int t = 1; t < 11; t++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            v = Integer.parseInt(st.nextToken());
            e = Integer.parseInt(st.nextToken());
            q = new LinkedList<>();
            degree = new int[v+1];
            visited = new boolean[v+1];
            map = new boolean[v+1][v+1];
            st = new StringTokenizer(br.readLine());
            for (int i = 0; i < e; i++) {
                int s = Integer.parseInt(st.nextToken());
                int g = Integer.parseInt(st.nextToken());
                map[s][g] = true;
                degree[g] += 1;
            }

            for (int i = 1; i < v + 1; i++) {
                if (degree[i] == 0)
                    q.offer(i);
            }

            List<Integer> ans = bfs();
            System.out.print("#" + t + " ");
            for (int i : ans) {
                System.out.print(i + " ");
            }
            System.out.println();
        }
    }

    private static List<Integer> bfs(){
        List<Integer> ans = new LinkedList<>();
        while (!q.isEmpty()) {
            int now = q.poll();
            ans.add(now);
            for (int i = 1; i < v + 1; i++) {
                if (map[now][i]) {
                    degree[i] -= 1;
                    map[now][i] = false;
                    if (degree[i] == 0)
                        q.offer(i);
                }
            }
        }
        return ans;
    }
}
