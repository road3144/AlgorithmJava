package swea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Maze2_1227 {
    static int[][] tmp;
    static char[][] map;
    static Queue<int[]> q;
    static int[] dx = {1, -1, 0, 0};
    static int[] dy = {0, 0, 1, -1};
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        for (int t = 1; t < 11; t++) {
            br.readLine();
            map = new char[100][];
            tmp = new int[100][100];
            q = new LinkedList<>();
            StringTokenizer st;

            for (int i = 0; i < 100; i++) {
                st = new StringTokenizer(br.readLine());
                map[i] = st.nextToken().toCharArray();
            }
            int gx=0, gy=0;
            for (int i = 0; i < 100; i++) {
                for (int j = 0; j < 100; j++) {
                    if (map[i][j] == '2') {
                        q.offer(new int[]{i, j});
                        bfs();
                    }
                    if (map[i][j] == '3'){
                        gx = i;
                        gy = j;
                    }
                }
            }
            System.out.println("#" + t + " " + ((tmp[gx][gy] >= 1)? 1 : 0));
        }
    }

    private static void bfs() {
        while (!q.isEmpty()){
            int[] now = q.poll();
            for (int i = 0; i < 4; i++) {
                int nx = now[0] + dx[i];
                int ny = now[1] + dy[i];
                if (nx < 0 || nx >= 100 || ny < 0 || ny >= 100)
                    continue;
                if (map[nx][ny] == '1')
                    continue;
                if (tmp[nx][ny] != 0)
                    continue;
                tmp[nx][ny] = tmp[now[0]][now[1]] + 1;
                q.offer(new int[] {nx, ny});
            }
        }
    }
}
