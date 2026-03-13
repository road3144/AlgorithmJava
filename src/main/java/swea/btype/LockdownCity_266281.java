package swea.btype;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

class LockdownCity_266281 {
    private static BufferedReader br;
    private static LockdownCity_UserSolution2 userSolution = new LockdownCity_UserSolution2();

    private final static int MAX_N          = 100;
    private final static int MAX_M          = 4;
    private final static int CMD_INIT       = 100;
    private final static int CMD_CHANGE     = 200;
    private final static int CMD_CALCULATE  = 300;

    private static boolean run() throws Exception {
        StringTokenizer st = new StringTokenizer(br.readLine(), " ");
        int N, M, L, mRow, mCol, mDir, mLength, sRow, sCol, eRow, eCol;
        String mGrade[][] = new String[MAX_N][MAX_N];

        int Q = Integer.parseInt(st.nextToken());
        boolean okay = false;

        for (int q = 0; q < Q; ++q)
        {
            st = new StringTokenizer(br.readLine(), " ");
            int cmd = Integer.parseInt(st.nextToken());

            if (cmd == CMD_INIT)
            {
                N = Integer.parseInt(st.nextToken());
                M = Integer.parseInt(st.nextToken());
                for(int i = 0; i < N; ++i) {
                    st = new StringTokenizer(br.readLine(), " ");
                    for(int j = 0; j < N; ++j)
                        mGrade[i][j] = st.nextToken();
                }
                userSolution.init(N, M, mGrade);
                okay = true;
            }
            else if (cmd == CMD_CHANGE)
            {
                mRow = Integer.parseInt(st.nextToken());
                mCol = Integer.parseInt(st.nextToken());
                mDir = Integer.parseInt(st.nextToken());
                mLength = Integer.parseInt(st.nextToken());
                String mChgGrade = st.nextToken();
                userSolution.change(mRow, mCol, mDir, mLength, mChgGrade);
            }
            else if (cmd == CMD_CALCULATE)
            {
                L = Integer.parseInt(st.nextToken());
                sRow = Integer.parseInt(st.nextToken());
                sCol = Integer.parseInt(st.nextToken());
                eRow = Integer.parseInt(st.nextToken());
                eCol = Integer.parseInt(st.nextToken());
                String ansGrade = st.nextToken();
                String ret = userSolution.calculate(L, sRow, sCol, eRow, eCol);

                if(ansGrade.compareTo(ret)!= 0) {
                    okay = false;
                }
            }
        }
        return okay;
    }

    public static void main(String[] args) throws Exception {
        int T, MARK;

         System.setIn(new java.io.FileInputStream("./BType/LockdownCity/sample_input.txt"));
        br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine(), " ");

        T = Integer.parseInt(st.nextToken());
        MARK = Integer.parseInt(st.nextToken());

        for (int tc = 1; tc <= T; tc++) {
            int score = run() ? MARK : 0;
            System.out.println("#" + tc + " " + score);
        }

        br.close();
    }
}
// 일반 bfs
class LockdownCity_UserSolution {

    static int[][] map;
    static int n;
    static int[] dx = {0, 0, 1, -1};
    static int[] dy = {1, -1, 0, 0};

    void init(int N, int M, String mGrade[][]) {
        n =N;
        map = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int tmp = 0;
                int t = 2;
                for (char c : mGrade[i][j].toCharArray()){
                    tmp += (c - 'A' + 1) * (int)Math.pow(27, t);
                    t--;
                }
                map[i][j] = tmp;
            }
        }
    }

    void change(int mRow, int mCol, int mDir, int mLength, String mGrade) {
        int tmp = 0;
        int t = 2;
        for (char c : mGrade.toCharArray()){
            tmp += (c - 'A' + 1) * (int) Math.pow(27, t);
            t--;
        }

        if (mDir == 0) {
            for (int i = 0; i < mLength; i++) {
                map[mRow+i][mCol] = tmp;
            }
        } else {
            for (int i = 0; i < mLength; i++) {
                map[mRow][mCol+i] = tmp;
            }
        }
    }

    String calculate(int L, int sRow, int sCol, int eRow, int eCol) {
        StringBuilder ret = new StringBuilder();

        int[][] tmp = new int[n][n];

        Queue<int[]> q = new ArrayDeque<>();
        q.add(new int[] {sRow, sCol, 0});

        while (!q.isEmpty()) {
            int[] now = q.poll();
            int x = now[0], y = now[1], dist = now[2];

            for (int i = 0; i < 4; i++) {
                int nx = x + dx[i];
                int ny = y + dy[i];
                if (nx >= n || nx < 0 || ny < 0 || ny >= n) continue;
                if (tmp[nx][ny] != 0 && tmp[x][y] <= tmp[nx][ny]) continue;
                if (dist + 1 > L) continue;
                q.add(new int[] {nx, ny, dist+1});
                if (x == sRow && y == sCol)
                    tmp[nx][ny] = map[nx][ny];
                else
                    tmp[nx][ny] = Math.min(map[nx][ny], tmp[x][y]);
            }
        }
        int srtInt = tmp[eRow][eCol];
        int t = 2;
        while (srtInt >= 1){
            char c = (char) ((srtInt / (int)Math.pow(27,t)) + 'A' - 1);
            ret.append(c);
            srtInt = srtInt % (int)Math.pow(27,t);
            t--;
        }

        return ret.toString();
    }
}

//다익으로 최적화
class LockdownCity_UserSolution2 {

    static class Edge implements Comparable<Edge>{
        int nx, ny;
        int dist;
        int min;

        public Edge(int nx, int ny, int dist, int min) {
            this.nx = nx;
            this.ny = ny;
            this.dist = dist;
            this.min = min;
        }

        public int compareTo(Edge o){
            return o.min - this.min;
        }

    }

    static int[][] map;
    static int n;
    static int[] dx = {0, 0, 1, -1};
    static int[] dy = {1, -1, 0, 0};

    void init(int N, int M, String mGrade[][]) {
        n =N;
        map = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int tmp = 0;
                int t = 2;
                for (char c : mGrade[i][j].toCharArray()){
                    tmp += (c - 'A' + 1) * (int)Math.pow(27, t);
                    t--;
                }
                map[i][j] = tmp;
            }
        }
    }

    void change(int mRow, int mCol, int mDir, int mLength, String mGrade) {
        int tmp = 0;
        int t = 2;
        for (char c : mGrade.toCharArray()){
            tmp += (c - 'A' + 1) * (int) Math.pow(27, t);
            t--;
        }

        if (mDir == 0) {
            for (int i = 0; i < mLength; i++) {
                map[mRow+i][mCol] = tmp;
            }
        } else {
            for (int i = 0; i < mLength; i++) {
                map[mRow][mCol+i] = tmp;
            }
        }
    }

    String calculate(int L, int sRow, int sCol, int eRow, int eCol) {
        StringBuilder ret = new StringBuilder();

        int[][] tmp = new int[n][n];

        PriorityQueue<Edge> q = new PriorityQueue<>();
        q.add(new Edge(sRow, sCol, 0, 0));

        while (!q.isEmpty()) {
            Edge now = q.poll();
            int x = now.nx, y = now.ny, dist = now.dist;

            if (x == eRow && y == eCol)
                break;

            for (int i = 0; i < 4; i++) {
                int nx = x + dx[i];
                int ny = y + dy[i];
                if (nx >= n || nx < 0 || ny < 0 || ny >= n) continue;
                if (tmp[nx][ny] != 0 && tmp[x][y] <= tmp[nx][ny]) continue;
                if (dist + 1 > L) continue;
                if (dist + 1 + Math.abs(eRow-nx) + Math.abs(eCol-ny) > L) continue;

                if (x == sRow && y == sCol)
                    tmp[nx][ny] = map[nx][ny];
                else
                    tmp[nx][ny] = Math.min(map[nx][ny], tmp[x][y]);
                q.add(new Edge(nx, ny, dist+1, tmp[nx][ny]));
            }
        }
        int srtInt = tmp[eRow][eCol];
        int t = 2;
        while (srtInt >= 1){
            char c = (char) ((srtInt / (int)Math.pow(27,t)) + 'A' - 1);
            ret.append(c);
            srtInt = srtInt % (int)Math.pow(27,t);
            t--;
        }

        return ret.toString();
    }
}
