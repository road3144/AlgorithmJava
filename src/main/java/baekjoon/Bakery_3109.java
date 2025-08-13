package baekjoon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Bakery_3109 {
    static int R, C;
    static boolean[][] visited;
    static char[][] map;
    static int[] dx = {-1, 0, 1};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        R = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());
        visited = new boolean[R][C];
        map = new char[R][];
        for (int i = 0; i < R; i++) {
            st = new StringTokenizer(br.readLine());
            map[i] = st.nextToken().toCharArray();
        }
        int ans = 0;
        for (int i = 0; i < R; i++) {
            visited[i][0] = true;        // 시작 칸 사용 표시
            if (dfs(i, 0)) ans++;     // 이 줄에서 파이프 하나 성공하면 카운트 +1
        }
        System.out.println(ans);
    }

    static boolean dfs(int r, int c) {
        if (c == C - 1) return true; // 마지막 열 도착

        for (int d = 0; d < 3; d++) {
            int nr = r + dx[d];
            int nc = c + 1;
            if (nr < 0 || nr >= R) continue;
            if (map[nr][nc] == 'x' || visited[nr][nc]) continue;

            visited[nr][nc] = true;          // 이 칸은 더 이상 못 씀
            if (dfs(nr, nc))
                return true;    // 하나라도 성공하면 바로 확정
        }
        return false;
    }
}
