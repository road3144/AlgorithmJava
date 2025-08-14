package swea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Cook_4012 {
    static int T, n, ans;
    static int[][] s;
    static int[] selected;

    static StringBuilder sb = new StringBuilder();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        StringBuilder sb = new StringBuilder();

        T = Integer.parseInt(st.nextToken());
        for (int test_case = 1; test_case <= T; test_case++) {
            st = new StringTokenizer(br.readLine());
            n = Integer.parseInt(st.nextToken());
            s = new int[n][n];
            selected = new int[n/2];
            ans = Integer.MAX_VALUE;
            int val = 0;
            for (int i = 0; i < n; i++) {
                st = new StringTokenizer(br.readLine());
                for (int j = 0; j < n; j++) {
                    s[i][j] = Integer.parseInt(st.nextToken());
                }
            }
            dfs(0, 0);
            sb.append("#").append(test_case).append(" ").append(ans).append("\n");
        }
        System.out.println(sb);
    }

    static void dfs(int depth, int start) {
        if (depth == n/2) {
            int flavor = 0, nonFlavor = 0;
            int[] non = new int[n/2];
            int cnt = 0;
            for (int i = 0; i < n; i++) {
                boolean in = false;
                for (int j = 0; j < n/2; j++) {
                    if(i == selected[j]){
                        in = true;
                        break;
                    }
                }
                if (!in)
                    non[cnt++] = i;
            }

            for (int i = 0; i < n / 2; i++) {
                for (int j = i+1; j < n/2; j++) {
                    if (i==j) continue;
                    int ingrid1 = selected[i];
                    int ingrid2 = selected[j];
                    flavor += s[ingrid1][ingrid2] + s[ingrid2][ingrid1];
                }
            }

            for (int i = 0; i < n / 2; i++) {
                for (int j = i+1; j < n/2; j++) {
                    if (i==j) continue;
                    int ingrid1 = non[i];
                    int ingrid2 = non[j];
                    nonFlavor += s[ingrid1][ingrid2] + s[ingrid2][ingrid1];
                }
            }
            ans = Math.min(ans, Math.abs(flavor - nonFlavor));
            return;
        }

        for (int i = start; i < n; i++) {
            selected[depth] = i;
            dfs(depth+1, i+1);
        }
    }
}
