package swea;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class HikingTrail_1949 {
    static int N, K, ans, maxH;
    static int[][] map;
    static boolean[][] visited;
    static final int[] dx = { -1, 1, 0, 0 };
    static final int[] dy = { 0, 0, -1, 1 };

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder out = new StringBuilder();
        StringTokenizer st;

        int T = Integer.parseInt(br.readLine().trim());
        for (int tc = 1; tc <= T; tc++) {
            st = new StringTokenizer(br.readLine());
            N = Integer.parseInt(st.nextToken());
            K = Integer.parseInt(st.nextToken());

            map = new int[N][N];
            visited = new boolean[N][N];
            maxH = 0;

            for (int i = 0; i < N; i++) {
                st = new StringTokenizer(br.readLine());
                for (int j = 0; j < N; j++) {
                    map[i][j] = Integer.parseInt(st.nextToken());
                    maxH = Math.max(maxH, map[i][j]);
                }
            }

            ans = 0;

            // 모든 최고 봉우리에서 시작
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (map[i][j] == maxH) {
                        visited[i][j] = true;
                        dfs(i, j, 1, false);
                        visited[i][j] = false;
                    }
                }
            }

            out.append('#').append(tc).append(' ').append(ans).append('\n');
        }

        System.out.print(out);
    }

    // x,y에서 현재 길이 len, cutUsed=공사 사용 여부
    static void dfs(int x, int y, int len, boolean cutUsed) {
        ans = Math.max(ans, len);

        for (int dir = 0; dir < 4; dir++) {
            int nx = x + dx[dir];
            int ny = y + dy[dir];
            if (nx < 0 || nx >= N || ny < 0 || ny >= N || visited[nx][ny]) continue;

            // 1) 그냥 내려갈 수 있으면 이동
            if (map[nx][ny] < map[x][y]) {
                visited[nx][ny] = true;
                dfs(nx, ny, len + 1, cutUsed);
                visited[nx][ny] = false;
            }
            // 2) 아직 공사를 안 썼고 깎으면 내려갈 수 있는 경우
            else if (!cutUsed && map[nx][ny] - K < map[x][y]) {
                int original = map[nx][ny];
                // 현재보다 1 낮게 설정 그래야 가장 멀리감
                map[nx][ny] = map[x][y] - 1;

                visited[nx][ny] = true;
                dfs(nx, ny, len + 1, true);
                visited[nx][ny] = false;

                map[nx][ny] = original; // 복원
            }
        }
    }
}
