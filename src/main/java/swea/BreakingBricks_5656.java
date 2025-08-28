package swea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;
import java.util.StringTokenizer;

public class BreakingBricks_5656 {
    static int T, n, w, h, ans;
    static int[][] map;
    static int[] dx = {0, 0, 1, -1};
    static int[] dy = {1, -1, 0, 0};

    static StringBuilder sb = new StringBuilder();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        StringBuilder sb = new StringBuilder();

        T = Integer.parseInt(st.nextToken());
        for (int test_case = 1; test_case <= T; test_case++) {
            st = new StringTokenizer(br.readLine());
            n = Integer.parseInt(st.nextToken());
            w = Integer.parseInt(st.nextToken());
            h = Integer.parseInt(st.nextToken());
            ans = 0;
            map = new int[h][w];
            for (int i = 0; i < h; i++) {
                st = new StringTokenizer(br.readLine());
                for (int j = 0; j < w; j++) {
                    map[i][j] = Integer.parseInt(st.nextToken());
                    if (map[i][j] != 0) ans++;
                }
            }
            dfs(0, map, ans);

            sb.append('#').append(test_case).append(' ').append(ans).append('\n');
        }
        System.out.println(sb);
    }

    static void dfs(int depth, int[][]b, int remain) {
        if (remain == 0 || ans == 0) {
            ans = 0;
            return;
        }
        if (depth == n) {
            ans = Math.min(ans, remain);
            return;
        }
        for (int i = 0; i < w; i++) {
            int r = top(b, i);

            if (r == -1){
                dfs(depth+1, b, remain);
                continue;
            }

            int[][] next = copy(b);
            int broken = boom(next, r, i);
            gravity(next);

            dfs(depth+1, next, remain - broken);
        }
    }

    static int boom(int[][] b, int sx, int sy){
        int broken = 0;
        Queue<int[]> q = new ArrayDeque<>();
        if (b[sx][sy] > 1) q.offer(new int[] {sx, sy, b[sx][sy]});
        broken++;
        b[sx][sy] = 0;

        while (!q.isEmpty()) {
            int[] now = q.poll();
            int x = now[0], y = now[1], p = now[2];

            for (int i = 0; i < 4; i++) {
                for (int k = 1; k < p; k++) {
                    int nx = x + dx[i] * k, ny = y + dy[i] * k;
                    if (nx < 0 || nx >= h || ny < 0 || ny >=w) continue;
                    if (b[nx][ny] == 0) continue;

                    int v = b[nx][ny];
                    if (v > 1) q.offer(new int[] {nx, ny, v});
                    b[nx][ny] = 0;
                    broken++;
                }
            }
        }

        return broken;
    }

    static void gravity(int[][]b) {
        for (int c = 0; c < w; c++) {
            int write = h - 1;
            for (int r = h - 1; r >= 0; r--) {
                if (b[r][c] != 0) {
                    if (write != r) {
                        b[write][c] = b[r][c];
                        b[r][c] = 0;
                    }
                    write--;
                }
            }
        }
    }

    static int[][] copy(int[][] origin){
        int[][] dst = new int[h][w];
        for (int i = 0; i < h; i++) {
            dst[i] = Arrays.copyOf(origin[i], w);
        }
        return dst;
    }

    static int top(int[][] b, int col){
        for (int r = 0; r < h; r++) {
            if (b[r][col] != 0) return r;
        }
        return -1;
    }

}
