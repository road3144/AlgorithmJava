package swea;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Counselor_1494 {
    static int N, HALF;
    static int[] xs, ys;
    static long totalX, totalY, answer;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder out = new StringBuilder();
        int T = Integer.parseInt(br.readLine().trim());

        for (int tc = 1; tc <= T ; tc++) {
            N = Integer.parseInt(br.readLine().trim());
            HALF = N / 2;
            xs = new int[N];
            ys = new int[N];
            totalX = totalY = 0;

            for (int i = 0; i < N; i++) {
                StringTokenizer st = new StringTokenizer(br.readLine());
                xs[i] = Integer.parseInt(st.nextToken());
                ys[i] = Integer.parseInt(st.nextToken());
                totalX += xs[i];
                totalY += ys[i];
            }

            answer = Long.MAX_VALUE;
            dfs(0, 0, 0L, 0L);
            out.append("#").append(tc).append(" ").append(answer).append('\n');
        }

        System.out.print(out.toString());
    }

    // idx: 현재 인덱스, picked: 선택한 개수, sumX/sumY: 선택한 점들의 합
    static void dfs(int idx, int picked, long sumX, long sumY) {
        // 필요한 만큼 다 골랐으면 결과 계산
        if (picked == HALF) {
            long rx = totalX - 2 * sumX;
            long ry = totalY - 2 * sumY;
            long val = rx * rx + ry * ry;
            if (val < answer) answer = val;
            return;
        }
        // 끝까지 갔으면 종료
        if (idx == N) return;

        // 가지치기: 남은 칸으로 HALF까지 못 채우면 종료
        if (picked + (N - idx) < HALF) return;

        // 선택하는 경우
        dfs(idx + 1, picked + 1, sumX + xs[idx], sumY + ys[idx]);

        // 선택하지 않는 경우
        dfs(idx + 1, picked, sumX, sumY);
    }
}
