package swea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class HamburgerDiet_5215_DP {
    static int T;
    static int n, l;
    static int[][] data; // [i][0]=맛점수, [i][1]=칼로리

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder out = new StringBuilder();
        StringTokenizer st;

        st = new StringTokenizer(br.readLine());
        T = Integer.parseInt(st.nextToken());

        for (int tc = 1; tc <= T; tc++) {
            st = new StringTokenizer(br.readLine());
            n = Integer.parseInt(st.nextToken());
            l = Integer.parseInt(st.nextToken());

            data = new int[n][2];
            for (int i = 0; i < n; i++) {
                st = new StringTokenizer(br.readLine());
                data[i][0] = Integer.parseInt(st.nextToken()); // taste
                data[i][1] = Integer.parseInt(st.nextToken()); // cal
            }

            // 0/1 Knapsack (1D)
            int[] dp = new int[l + 1]; // dp[c] = 칼로리 합 c에서의 최대 맛점수

            for (int i = 0; i < n; i++) {
                int taste = data[i][0];
                int cal = data[i][1];
                // 역방향으로 갱신(같은 아이템 중복 사용 방지)
                for (int c = l; c >= cal; c--) {
                    dp[c] = Math.max(dp[c], dp[c - cal] + taste);
                }
            }

            int ans = 0;
            for (int c = 0; c <= l; c++) ans = Math.max(ans, dp[c]);

            out.append('#').append(tc).append(' ').append(ans).append('\n');
        }

        System.out.print(out);
    }
}
