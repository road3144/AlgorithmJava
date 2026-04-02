package codetree.samsung;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;
import java.util.StringTokenizer;

public class AIRobotVacuum {
    static int[][] map;
    static int[][] robots;

    static int n, k, l;

    static int[] dx = {-1, 0, 0, 1};
    static int[] dy = {0, -1, 1, 0};
    static int[][] cx = {{-1, 0, 0, 1}, {0, 0, 0, 1}, {-1, 0, 0, 1}, {-1, 0, 0, 0}};
    static int[][] cy = {{0, 0, 1, 0}, {-1, 0, 1, 0}, {0, 0, -1, 0}, {0, -1, 0, 1}};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());
        l = Integer.parseInt(st.nextToken());

        map = new int[n][n];
        robots = new int[k][2];

        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < n; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        for (int i = 0; i < k; i++) {
            st = new StringTokenizer(br.readLine());
            robots[i][0] = Integer.parseInt(st.nextToken()) - 1;
            robots[i][1] = Integer.parseInt(st.nextToken()) - 1;
        }

        for (int i = 0; i < l; i++) {
            simul();
        }
    }

    static void simul(){
        move();
        clean();
        add();
        spread();
        System.out.println(count());
    }

    static void move(){
        Queue<int[]> q = new ArrayDeque<>();
        boolean[][] visited = new boolean[n][n];
        // 각 로봇들 순서 대로 목표 찾고 이동
        for (int i = 0; i < k; i++) {
            q.clear();
            for (int j = 0; j < n; j++) {
                Arrays.fill(visited[j], false);
            }
            q.add(new int[] {robots[i][0], robots[i][1]});
            visited[robots[i][0]][robots[i][1]] = true;

            if (map[robots[i][0]][robots[i][1]] > 0) continue; // 이미 가장 가까운 오염 칸이 현재 위치

            while (!q.isEmpty()){
                int size = q.size();
                int tx = -1, ty = -1;

                for (int s = 0; s < size; s++) {
                    int[] tmp = q.poll();
                    int x = tmp[0], y = tmp[1];

                    for (int j = 0; j < 4; j++) {
                        int nx = x + dx[j];
                        int ny = y + dy[j];

                        if (nx < 0 || nx >= n || ny < 0 || ny >= n) continue;
                        if (map[nx][ny] == -1) continue;
                        if (isRobot(i, nx, ny)) continue;
                        if (visited[nx][ny]) continue;

                        visited[nx][ny] = true;

                        // 뻗을 때 먼지 칸이면 후보만 기록
                        if (map[nx][ny] > 0) {
                            if (tx == -1 || nx < tx || (nx == tx && ny < ty)) {
                                tx = nx;
                                ty = ny;
                            }
                        }
                        else {
                            q.add(new int[] {nx, ny});
                        }
                    }
                }

                // 이번 거리에서 후보를 찾았으면 그중 행/열 가장 작은 곳으로 이동
                if (tx != -1) {
                    robots[i][0] = tx;
                    robots[i][1] = ty;
                    break;
                }
            }
        }
    }


    static boolean isRobot(int idx, int r, int c){
        for (int i = 0; i < k; i++) {
            if (i == idx) continue;
            if (robots[i][0] == r && robots[i][1] == c) return true;
        }
        return false;
    }

    static void clean(){
        //각 청소기 돌면서 방향 찾고 청소
        for (int i = 0; i < k; i++) {
            int max = 0;
            int dir = 0;
            int x = robots[i][0], y = robots[i][1];
            //방향 찾기
            for (int j = 0; j < 4; j++) {
                int total = 0; // 이번 방향 먼지
                for (int m = 0; m < 4; m++) {
                    int nx = x + cx[j][m];
                    int ny = y + cy[j][m];
                    if (nx < 0 || nx >= n || ny < 0 || ny >= n) continue;
                    if (map[nx][ny] == -1) continue;
                    total += Math.min(map[nx][ny], 20);
                }
                if (max < total){
                    max = total;
                    dir = j;
                }
            }
            //먼지 제거
            for (int m = 0; m < 4; m++) {
                int nx = x + cx[dir][m];
                int ny = y + cy[dir][m];
                if (nx < 0 || nx >= n || ny < 0 || ny >= n) continue;
                if (map[nx][ny] == -1) continue;
                map[nx][ny] -= Math.min(20, map[nx][ny]);
            }
        }
    }

    static void add(){
        //먼지 적용
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                if (map[x][y] < 1) continue;
                map[x][y] += 5;
            }
        }
    }

    static void spread(){
        // 먼지 분산
        int[][] tmp = new int[n][n]; // 추가 되어야 할 먼지

        // 깨끗할 때 계산
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                if (map[x][y] != 0) continue;
                int sum = 0;
                // 깨끗한곳 기준 4방 탐색
                for (int i = 0; i < 4; i++) {
                    int nx = x + dx[i];
                    int ny = y + dy[i];
                    if (nx < 0 || nx >= n || ny < 0 || ny >= n) continue;
                    if (map[nx][ny] <= 0) continue;
                    sum += map[nx][ny];
                }
                tmp[x][y] = sum / 10;
            }
        }
        //먼지 적용
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                if (map[x][y] != 0) continue;
                map[x][y] += tmp[x][y];
            }
        }
    }

    static int count(){
        int cnt = 0;
        //먼지 적용
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                if (map[x][y] < 1) continue;
                cnt += map[x][y];
            }
        }
        return cnt;
    }
}
