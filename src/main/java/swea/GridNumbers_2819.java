package swea;

import java.io.*;
import java.util.*;

public class GridNumbers_2819 {
    static final int N = 4;
    static int[][] board = new int[N][N];
    static final int[] dx = {1, -1, 0, 0};
    static final int[] dy = {0, 0, 1, -1};
    static Set<String> set;       // 서로 다른 7자리 수 저장
    static char[] buf = new char[7]; // 현재 경로의 숫자들 (길이 7)

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();

        int T = Integer.parseInt(br.readLine().trim());
        for (int t = 1; t <= T; t++) {
            // 입력: 4줄 x 4칸
            for (int i = 0; i < N; i++) {
                StringTokenizer st = new StringTokenizer(br.readLine());
                for (int j = 0; j < N; j++) {
                    board[i][j] = Integer.parseInt(st.nextToken());
                }
            }

            set = new HashSet<>();

            // 모든 시작점에서 DFS
            for (int x = 0; x < N; x++) {
                for (int y = 0; y < N; y++) {
                    buf[0] = (char) ('0' + board[x][y]);
                    dfs(x, y, 1); // 현재 길이 1 (시작칸 포함), 총 7칸이 되도록 진행
                }
            }

            sb.append('#').append(t).append(' ').append(set.size()).append('\n');
        }

        System.out.print(sb.toString());
    }

    // idx: 현재까지 채운 글자 수 (0~6), 7이 되면 완료
    static void dfs(int x, int y, int idx) {
        if (idx == 7) {
            set.add(new String(buf));
            return;
        }
        for (int dir = 0; dir < 4; dir++) {
            int nx = x + dx[dir];
            int ny = y + dy[dir];
            if (0 <= nx && nx < N && 0 <= ny && ny < N) {
                buf[idx] = (char) ('0' + board[nx][ny]);
                dfs(nx, ny, idx + 1);
            }
        }
    }
}
