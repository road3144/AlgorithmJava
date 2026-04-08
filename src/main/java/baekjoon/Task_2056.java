package baekjoon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Task_2056 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int n = Integer.parseInt(st.nextToken());

        List<Integer>[] graph = new List[n + 1];
        int[] times = new int[n+1], startTime = new int[n+1];
        int[] indegree = new int[n + 1];
        for (int i = 0; i < n + 1; i++) {
            graph[i] = new ArrayList<>();
        }

        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            int t = Integer.parseInt(st.nextToken());
            times[i+1] = t;
            int c = Integer.parseInt(st.nextToken());
            for (int j = 0; j < c; j++) {
                int k = Integer.parseInt(st.nextToken());
                graph[k].add(i+1);
                indegree[i+1] += 1;
            }
        }

        Queue<Integer> q = new ArrayDeque<>();
        for (int i = 1; i < n+1; i++) {
            if(indegree[i] == 0) q.add(i);
        }

        int ans = 0;
        while (!q.isEmpty()){
            int now = q.poll();

            ans = Math.max(ans, startTime[now] + times[now]);

            for (int next : graph[now]){
                indegree[next] -= 1;
                startTime[next] = Math.max(startTime[next], startTime[now] + times[now]);
                if (indegree[next] == 0){
                    q.add(next);
                }
            }
        }
        System.out.println(ans);
    }
}
