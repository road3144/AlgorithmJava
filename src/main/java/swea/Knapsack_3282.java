package swea;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Knapsack_3282 {
    static int N, K;
    static int[][] data;
    static int[] dp;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        StringBuilder sb = new StringBuilder();
        int T = Integer.parseInt(st.nextToken());

        for (int tc = 1; tc <= T; tc++) {
            st = new StringTokenizer(br.readLine());
            N = Integer.parseInt(st.nextToken());
            K = Integer.parseInt(st.nextToken());
            data = new int[N][2];
            dp = new int[K+1];

            for (int i = 0; i < N; i++) {
                st = new StringTokenizer(br.readLine());
                int v = Integer.parseInt(st.nextToken());
                int c = Integer.parseInt(st.nextToken());
                data[i][0] = v;
                data[i][1] = c;
            }

            for (int i = 0; i < N; i++) {
                int v = data[i][0];
                int c = data[i][1];
                for (int w = K; w >= v; w--) {
                    dp[w] = Math.max(dp[w], dp[w-v] + c);
                }
            }

            sb.append("#").append(tc).append(" ").append(dp[K]).append("\n");
        }
        System.out.print(sb);
    }
}
