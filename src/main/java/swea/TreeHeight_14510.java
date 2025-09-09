package swea;

import java.io.*;
import java.util.*;

public class TreeHeight_14510 {
    static int N, T;
    static int[] tree;

    static boolean feasible(int k, int[] diff) {
        long onesAvail = (k + 1) / 2; // ceil(k/2)
        long twosAvail = k / 2;       // floor(k/2)

        long O = 0; // 필요한 +1 개수 (홀수 gap 개수)
        long E = 0; // 필요한 +2 개수의 합 (모든 gap의 floor(gap/2))

        for (int d : diff) {
            if ((d & 1) == 1) O++;
            E += d / 2;
        }

        if (onesAvail < O) return false; // +1이 모자람
        long extraOnes = onesAvail - O;  // 남는 +1
        long twosFromExtraOnes = extraOnes / 2; // 2개의 +1 -> +2 하나 대체
        return twosAvail + twosFromExtraOnes >= E;
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        T = Integer.parseInt(br.readLine().trim());

        for (int tc = 1; tc <= T; tc++) {
            N = Integer.parseInt(br.readLine().trim());
            tree = new int[N];
            StringTokenizer st = new StringTokenizer(br.readLine());
            int max = 0;
            for (int i = 0; i < N; i++) {
                tree[i] = Integer.parseInt(st.nextToken());
                max = Math.max(max, tree[i]);
            }

            int[] diff = new int[N];
            for (int i = 0; i < N; i++) diff[i] = max - tree[i];

            // 이분 탐색으로 최소 k 찾기
            int lo = 0, hi = 200000; // 충분히 큰 상한
            while (lo < hi) {
                int mid = (lo + hi) >>> 1;
                if (feasible(mid, diff)) hi = mid;
                else lo = mid + 1;
            }

            sb.append('#').append(tc).append(' ').append(lo).append('\n');
        }
        System.out.print(sb);
    }
}
