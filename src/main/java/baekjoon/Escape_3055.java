package baekjoon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;


/*
물은 매 분 사방으로 퍼져 나간다.
고슴도치도 매 분 사방으로 이동할 수 있다.
고슴도치는 굴로 피신 해야 한다.
 bfs 사용

* */
public class Escape_3055 {
    static int r, c;

    static char[][] map;
    //거리(시간) 측정
    static int[][] dist;
    static int goalX, goalY;
    //사방탐색
    static int[] dx = {0, 0, 1, -1};
    static int[] dy = {1, -1, 0, 0};
    // 다음에 탐색할 시작 위치 큐
    static Queue<int[]> waterQueue = new LinkedList<>();
    static Queue<int[]> hedgehogQueue = new LinkedList<>();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        r = Integer.parseInt(st.nextToken());
        c = Integer.parseInt(st.nextToken());
        map = new char[r][];
        dist = new int[r][c];
        for (int i = 0; i < r; i++) {
            st = new StringTokenizer(br.readLine());
            map[i] = st.nextToken().toCharArray();
        }

        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                if (map[i][j] == 'S') // 시작 위치 큐에 저장
                    hedgehogQueue.add(new int[] {i, j});
                if (map[i][j] == '*')
                    waterQueue.add(new int[] {i, j});
                if (map[i][j] == 'D') {
                    goalX = i;
                    goalY = j;
                }
            }
        }

        // 큐가 모두 빌때 까지 반복
        while (!(waterQueue.isEmpty() && hedgehogQueue.isEmpty())) {
            // 고슴도치가 물이 이동할 위치로 이동하면 안되기 때문에 물 먼저 움직임
            moveWater();
            moveHedgehog();
        }

        if (dist[goalX][goalY] == 0)
            System.out.println("KAKTUS");
        else
            System.out.println(dist[goalX][goalY]);

    }

    //물의 움직임
    private static void moveWater() {
        Queue<int[]> nq = new LinkedList<>(); // 다음 탐색 시작 위치 저장 위한 임시 큐
        while (!waterQueue.isEmpty()) { // 현재 탐색 시작 위치 큐 시작
            int[] now = waterQueue.poll();
            int x = now[0], y = now[1];
            for (int i = 0; i < 4; i++) { // 현재 위치에서 사방탐색
                int nx = x + dx[i], ny = y + dy[i];
                if (nx < 0 || nx >= r || ny < 0 || ny >= c)
                    continue;
                if (map[nx][ny] == 'D' || map[nx][ny] == 'X'|| map[nx][ny] == '*') // 굴이거나, 돌이거나 물은 탐색 못한다
                    continue;
                nq.add(new int[] {nx, ny}); // 위에 조건 모두 통과 했으면 다음 시작 위치 큐에 저장
                map[nx][ny] = '*'; // 물이 찼음을 표시
            }
        }
        waterQueue = nq; // 모두 돈 뒤 탐색 시작 위치 큐에 임시큐 할당
    }

    //고슴도치의 움직임
    private static void moveHedgehog() {
        Queue<int[]> nq = new LinkedList<>();
        while (!hedgehogQueue.isEmpty()) {
            int[] now = hedgehogQueue.poll();
            int x = now[0], y = now[1];
            for (int i = 0; i < 4; i++) {
                int nx = x + dx[i], ny = y + dy[i];
                if (nx < 0 || nx >= r || ny < 0 || ny >= c)
                    continue;
                if (map[nx][ny] == 'X' || map[nx][ny] == '*') // 다음 위치에 돌이나 물이 아니면 이동
                    continue;
                if (dist[nx][ny] != 0) // 이전에 안들린 곳으로만 이동
                    continue;
                nq.add(new int[] {nx, ny});
                dist[nx][ny] = dist[x][y]+1;
            }
        }
        hedgehogQueue = nq;
    }
}
