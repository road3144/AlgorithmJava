package swea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class RectangleRoom_1861 {
    static int T, n, ans;
    static int[][] map;
    static int[] dx = {1, -1, 0, 0};
    static int[] dy = {0, 0, 1, -1};
    static StringBuilder sb = new StringBuilder();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        StringBuilder sb = new StringBuilder();

        T = Integer.parseInt(st.nextToken());
        for (int test_case = 1; test_case <= T; test_case++) {
            st = new StringTokenizer(br.readLine());
            n = Integer.parseInt(st.nextToken());
            map = new int[n][n];
            ans = 0;
            int val = 0;
            for (int i = 0; i < n; i++) {
                st = new StringTokenizer(br.readLine());
                for (int j = 0; j < n; j++) {
                    map[i][j] = Integer.parseInt(st.nextToken());
                }
            }
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    int cnt = bfs(new int[] {i, j});
                    if (ans < cnt) {
                        ans = cnt;
                        val = map[i][j];
                    } else if (ans == cnt && val > map[i][j]) {
                        val = map[i][j];
                    }
                }
            }
            sb.append("#").append(test_case).append(" ").append(val).append(" ").append(ans).append("\n");
        }
        System.out.print(sb);
    }

    static int bfs(int[] start) {
        int cnt = 1;
        Queue<int[]> q = new LinkedList<>();
        q.add(new int[] {start[0], start[1], cnt});

        while (!q.isEmpty()) {
            int[] tmp = q.poll();
            int x = tmp[0], y = tmp[1];
            cnt = tmp[2];
            for (int i = 0; i < 4; i++) {
                int nx = x + dx[i], ny = y + dy[i];

                if(nx < 0 || nx >= n || ny < 0 || ny >= n) continue;
                if(map[x][y] + 1 != map[nx][ny]) continue;
                q.add(new int[] {nx, ny, tmp[2] + 1});
            }
        }
        return cnt;
    }
}
