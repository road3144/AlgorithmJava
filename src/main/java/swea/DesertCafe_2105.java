package swea;

import java.io.*;
import java.util.*;

public class DesertCafe_2105 {
    static int N, ans;
    static int[][] A;
    static boolean[][] visited;     // 좌표 방문 체크 (출발점으로 돌아오는 경우만 예외)
    static boolean[] ate;           // 디저트 종류(1..100) 방문 체크

    // 대각선: ↘, ↙, ↖, ↗ (오로지 이 순서로만 회전 가능)
    static final int[] dr = { 1, 1, -1, -1 };
    static final int[] dc = { 1, -1, -1,  1 };

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        int T = Integer.parseInt(br.readLine().trim());

        for (int tc = 1; tc <= T; tc++) {
            N = Integer.parseInt(br.readLine().trim());
            A = new int[N][N];
            for (int i = 0; i < N; i++) {
                StringTokenizer st = new StringTokenizer(br.readLine());
                for (int j = 0; j < N; j++)
                    A[i][j] = Integer.parseInt(st.nextToken());
            }

            ans = -1;
            visited = new boolean[N][N];
            ate = new boolean[101];

            // 모든 출발점 시도 \전부 시도해도 N<=20이라 충분)
            for (int sr = 0; sr < N; sr++) {
                for (int sc = 0; sc < N; sc++) {
                    Arrays.fill(ate, false);
                    for (int i = 0; i < N; i++)
                        Arrays.fill(visited[i], false);

                    visited[sr][sc] = true;
                    ate[A[sr][sc]] = true;

                    // 시작은 항상 방향 0(↘)로 출발. 이후 DFS 내부에서 직진/회전을 선택.
                    dfs(sr, sc, 0, 0, 1, sr, sc);

                    visited[sr][sc] = false;
                    ate[A[sr][sc]] = false;
                }
            }

            sb.append("#").append(tc).append(" ").append(ans).append('\n');
        }

        System.out.print(sb.toString());
    }

    static boolean in(int r, int c) {
        return 0 <= r && r < N && 0 <= c && c < N;
    }

    // DFS에서 한 스텝 이동을 수행한다.
    // r,c : 현재 위치
    // dir : 현재 진행 방향(0..3)
    // turns : 지금까지 회전 횟수(0..3)
    // cnt : 지금까지 먹은 디저트 수
    // sr,sc : 출발 좌표
    static void dfs(int r, int c, int dir, int turns, int cnt, int sr, int sc) {
        // 1) 현재 방향으로 한 칸 전진
        int nr = r + dr[dir];
        int nc = c + dc[dir];

        if (in(nr, nc)) {
            if (nr == sr && nc == sc) {
                // 반드시 3번 회전(4방향 사용)했고 최소 4개 이상 먹은 경우만 유효
                if (turns == 3 && cnt >= 4)
                    ans = Math.max(ans, cnt);
            } else if (!visited[nr][nc] && !ate[A[nr][nc]]) {
                visited[nr][nc] = true;
                ate[A[nr][nc]] = true;
                dfs(nr, nc, dir, turns, cnt + 1, sr, sc);
                visited[nr][nc] = false;
                ate[A[nr][nc]] = false;
            }
        }

        // 2) 다음 방향으로 회전 후 한 칸 전진 (회전은 최대 3번)
        if (turns < 3) {
            int nd = dir + 1;
            int rr = r + dr[nd];
            int cc = c + dc[nd];
            if (in(rr, cc)) {
                if (rr == sr && cc == sc) {
                    if (turns + 1 == 3 && cnt >= 4) ans = Math.max(ans, cnt);
                } else if (!visited[rr][cc] && !ate[A[rr][cc]]) {
                    visited[rr][cc] = true;
                    ate[A[rr][cc]] = true;
                    dfs(rr, cc, nd, turns + 1, cnt + 1, sr, sc);
                    visited[rr][cc] = false;
                    ate[A[rr][cc]] = false;
                }
            }
        }
    }
}
