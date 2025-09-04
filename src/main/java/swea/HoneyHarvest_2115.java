package swea;

import java.io.*;
import java.util.*;

public class HoneyHarvest_2115 {
    static int N, M, C;
    static int[][] A;
    static int[][] best; // best[r][c]: (r,c)부터 가로로 M칸에서 얻을 수 있는 최대 이익(브루트포스)

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();

        int T = Integer.parseInt(br.readLine().trim());
        for (int tc = 1; tc <= T; tc++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            N = Integer.parseInt(st.nextToken());
            M = Integer.parseInt(st.nextToken());
            C = Integer.parseInt(st.nextToken());

            A = new int[N][N];
            for (int i = 0; i < N; i++) {
                st = new StringTokenizer(br.readLine());
                for (int j = 0; j < N; j++) {
                    A[i][j] = Integer.parseInt(st.nextToken());
                }
            }

            int W = N - M + 1;
            best = new int[N][W];

            // 1) 각 가로 구간에 대해 부분집합 완전탐색으로 최대 이익 계산
            for (int r = 0; r < N; r++) {
                for (int c = 0; c < W; c++) {
                    best[r][c] = maxProfitBruteforce(r, c);
                }
            }

            // 2) 서로 겹치지 않는 두 구간 쌍의 합 최댓값
            int ans = 0;
            for (int r1 = 0; r1 < N; r1++) {
                for (int c1 = 0; c1 < W; c1++) {
                    for (int r2 = r1; r2 < N; r2++) {
                        for (int c2 = 0; c2 < W; c2++) {
                            if (r1 == r2 && overlap(c1, c2)) continue; // 같은 행이면 겹치면 안됨
                            ans = Math.max(ans, best[r1][c1] + best[r2][c2]);
                        }
                    }
                }
            }

            sb.append('#').append(tc).append(' ').append(ans).append('\n');
        }

        System.out.print(sb.toString());
    }

    // 같은 행에서 시작열 c1, c2인 두 M-구간이 겹치면 true
    static boolean overlap(int c1, int c2) {
        int e1 = c1 + M - 1;
        int e2 = c2 + M - 1;
        return !(e1 < c2 || e2 < c1);
    }

    // (r,c)부터 M칸 구간의 부분집합(2^M)을 전부 탐색하여
    // 합이 C 이하인 경우의 제곱합 최대값을 반환
    static int maxProfitBruteforce(int r, int c) {
        int[] w = new int[M];
        for (int i = 0; i < M; i++) w[i] = A[r][c + i];

        int bestVal = 0;
        int totalMasks = 1 << M;
        for (int mask = 1; mask < totalMasks; mask++) {
            int sum = 0;
            int value = 0; // 제곱합
            for (int i = 0; i < M; i++) {
                if ((mask & (1 << i)) != 0) {
                    sum += w[i];
                    value += w[i] * w[i];
                }
            }
            if (sum <= C) bestVal = Math.max(bestVal, value);
        }
        return bestVal;
    }
}
