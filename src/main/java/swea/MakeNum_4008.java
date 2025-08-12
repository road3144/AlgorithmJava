package swea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class MakeNum_4008 {
    static int N;
    static int[] nums;
    static int[] ops; // ops[0]='+', ops[1]='-', ops[2]='*', ops[3]='/'
    static int minVal, maxVal;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        StringBuilder sb = new StringBuilder();

        int T = Integer.parseInt(st.nextToken());

        for (int tc = 1; tc <= T; tc++) {
            st = new StringTokenizer(br.readLine());
            N = Integer.parseInt(st.nextToken());

            // 연산자 개수: +, -, *, /
            ops = new int[4];
            st = new StringTokenizer(br.readLine());
            for (int i = 0; i < 4; i++) ops[i] = Integer.parseInt(st.nextToken());

            // 숫자들
            nums = new int[N];
            st = new StringTokenizer(br.readLine());
            for (int i = 0; i < N; i++) nums[i] = Integer.parseInt(st.nextToken());

            minVal = Integer.MAX_VALUE;
            maxVal = Integer.MIN_VALUE;

            // 첫 숫자를 누적값
            dfs(1, nums[0]);

            sb.append('#').append(tc).append(' ').append(maxVal - minVal).append('\n');
        }
        System.out.print(sb);
    }

    static void dfs(int idx, int acc) {
        if (idx == N) {
            minVal = Math.min(minVal, acc);
            maxVal = Math.max(maxVal, acc);
            return;
        }

        int x = nums[idx];

        // +
        if (ops[0] > 0) {
            ops[0]--;
            dfs(idx + 1, acc + x);
            ops[0]++;
        }
        // -
        if (ops[1] > 0) {
            ops[1]--;
            dfs(idx + 1, acc - x);
            ops[1]++;
        }
        // *
        if (ops[2] > 0) {
            ops[2]--;
            dfs(idx + 1, acc * x);
            ops[2]++;
        }
        // /
        if (ops[3] > 0) {
            ops[3]--;
            dfs(idx + 1, acc / x);
            ops[3]++;
        }
    }
}
