package swea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class CropsHarvest_2805 {
    static int T, n;
    StringBuilder sb = new StringBuilder();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        StringBuilder sb = new StringBuilder();

        T = Integer.parseInt(st.nextToken());
        for (int test_case = 1; test_case <= T; test_case++) {
            st = new StringTokenizer(br.readLine());
            n = Integer.parseInt(st.nextToken());
            int ans = 0;
            int mid = n/2;

            for (int i = 0; i < n; i++) {
                st = new StringTokenizer(br.readLine());
                String input = st.nextToken();
                int off = Math.abs(mid - i);
                int end = n - 1 - off;
                for (int j = off; j <= end; j++) {
                    ans += input.charAt(j) - '0';
                }
            }
            sb.append('#').append(test_case).append(' ').append(ans).append('\n');
        }
        System.out.println(sb);
    }
}
