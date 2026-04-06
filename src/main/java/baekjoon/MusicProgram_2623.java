package baekjoon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class MusicProgram_2623 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());

        List<Integer>[] graph = new List[n + 1];
        int[] indegree = new int[n + 1];
        for (int i = 0; i < n + 1; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int t = Integer.parseInt(st.nextToken());
            int a = 0, b = 0;
            for (int j = 0; j < t; j++) {
                if(j==0){
                    a = Integer.parseInt(st.nextToken());
                    continue;
                }
                b = Integer.parseInt(st.nextToken());
                graph[a].add(b);
                indegree[b] += 1;
                a = b;
            }
        }

        Queue<Integer> q = new ArrayDeque<>();
        for (int i = 1; i < n + 1; i++) {
            if (indegree[i] == 0)
                q.add(i);
        }
        List<Integer> ans = new ArrayList<>();
        while (!q.isEmpty()){
            int now = q.poll();
            ans.add(now);
            for (int next : graph[now]){
                indegree[next] -= 1;
                if (indegree[next] == 0)
                    q.add(next);
            }
        }
        if (ans.size() != n){
            System.out.println(0);
        } else {
            for (int i : ans){
                System.out.println(i);
            }
        }
    }
}
