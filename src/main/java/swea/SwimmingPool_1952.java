package swea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class SwimmingPool_1952 {
    static int T;
    static int[] plan, price, dp;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        StringBuilder sb = new StringBuilder();

        T = Integer.parseInt(st.nextToken());
        for (int test_case = 1; test_case <= T; test_case++) {
            st = new StringTokenizer(br.readLine());
            price = new int[4];
            for (int i = 0; i < 4; i++) {
                price[i] = Integer.parseInt(st.nextToken());
            }
            st = new StringTokenizer(br.readLine());
            plan = new int[13];
            dp = new int[16];

            for (int i = 1; i <= 12; i++) {
                plan[i] = Integer.parseInt(st.nextToken());
            }

            for (int m = 12; m >= 1; m--) {
                if (plan[m] == 0) {
                    dp[m] = dp[m + 1];
                } else {
                    int dayOrMonth = Math.min(plan[m] * price[0], price[1]) + dp[m + 1];
                    int threeMonth = price[2] + dp[Math.min(m + 3, 13)];
                    dp[m] = Math.min(dayOrMonth, threeMonth);
                }
            }

            int ans = Math.min(dp[1], price[3]);
            sb.append("#").append(test_case).append(" ").append(ans).append("\n");
        }
        System.out.print(sb);
    }

}
