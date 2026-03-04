package swea.btype;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class StarTile_25008 {

    private static StarTile_UserSolution2 usersolution = new StarTile_UserSolution2();

    private final static int CMD_INIT     = 0;
    private final static int CMD_CNT      = 1;
    private final static int CMD_POSITION = 2;

    private final static int MAX_SIZE = 1000;

    private static int Map[][] = new int[MAX_SIZE][MAX_SIZE];
    private static int Piece[][] = new int[5][5];
    private static int Data[] = new int[40000];

    private static void init_map(int N)
    {
        int idx = 0;

        int x = 0;
        for (int i = 0; i < (N / 25); i++) {
            for (int y = 0; y < N; y++) {
                int data = Data[idx++];
                int bit = 1;
                for (int m = 0; m < 25; m++) {
                    if ((data & bit) != 0) Map[y][x + m] = 1;
                    else Map[y][x + m] = 0;
                    bit <<= 1;
                }
            }
            x += 25;
        }

        int dcnt = N % 25;
        if (dcnt != 0) {
            for (int y = 0; y < N; y++) {
                int data = Data[idx++];
                int bit = 1;
                for (int m = 0; m < dcnt; m++) {
                    if ((data & bit) != 0) Map[y][x + m] = 1;
                    else Map[y][x + m] = 0;
                    bit <<= 1;
                }
            }
        }
    }

    private static void make_piece(int data)
    {
        int bit = 1;
        for (int i = 0; i < 5; i++) {
            for (int k = 0; k < 5; k++) {
                if ((data & bit) != 0) Piece[i][k] = 1;
                else Piece[i][k] = 0;
                bit <<= 1;
            }
        }
    }

    private static boolean run(BufferedReader br) throws Exception
    {
        int cmd, ans, ret;
        int N, row, col, cnt;

        boolean ok = false;

        StringTokenizer st = new StringTokenizer(br.readLine(), " ");
        int Q = Integer.parseInt(st.nextToken());

        for (int i = 0; i < Q; i++) {
            st = new StringTokenizer(br.readLine(), " ");
            cmd = Integer.parseInt(st.nextToken());

            if (cmd == CMD_INIT) {
                N = Integer.parseInt(st.nextToken());
                cnt = Integer.parseInt(st.nextToken());
                for (int k = 0; k < cnt; k++) {
                    st = new StringTokenizer(br.readLine(), " ");
                    Data[k] = Integer.parseInt(st.nextToken());
                }

                init_map(N);
                usersolution.init(N, Map);
                ok = true;
            } else if (cmd == CMD_CNT) {
                Data[0] = Integer.parseInt(st.nextToken());
                ans = Integer.parseInt(st.nextToken());
                make_piece(Data[0]);

                ret = usersolution.getCount(Piece);
                if (ret != ans) {
                    ok = false;
                }
            } else if (cmd == CMD_POSITION) {
                row = Integer.parseInt(st.nextToken());
                col = Integer.parseInt(st.nextToken());
                ans = Integer.parseInt(st.nextToken());

                ret = usersolution.getPosition(row, col);
                if (ret != ans) {
                    ok = false;
                }
            }
            else ok = false;
        }
        return ok;
    }

    public static void main(String[] args) throws Exception {

        System.setIn(new java.io.FileInputStream("./BType/StarTile/sample_input.txt"));

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer line = new StringTokenizer(br.readLine(), " ");

        int T = Integer.parseInt(line.nextToken());
        int MARK = Integer.parseInt(line.nextToken());

        for (int tc = 1; tc <= T; tc++) {
            int score = run(br) ? MARK : 0;
            System.out.println("#" + tc + " " + score);
        }

        br.close();
    }
}

class StarTile_UserSolution {

    static int[][] map, tileMap, centerMap;
    static int n;
    static Map<Integer, Integer> cnt, starMap;

    public void init(int N, int mPlane[][]) {
        n = N;
        map = mPlane;
        tileMap = new int[n][n];
        centerMap = new int[n][n];
        cnt = new HashMap<>();
        starMap = new HashMap<>();

        for (int i = 0; i < n-4; i++) {
            for (int j = 0; j < n - 4; j++) {
                // 별 타일이면
                if (checkCon1(i, j) && checkCon2(i, j)){
                    // 별 비트맵 만들기
                    int[] rotated = new int[4];
                    for (int x = 0; x < 5; x++) {
                        for (int y = 0; y < 5; y++) {
                            rotated[0] += map[i+x][j+y] << (x*5 + y);
                            rotated[1] += map[i+x][j+y] << ((4-y)*5 + x);
                            rotated[2] += map[i+x][j+y] << ((4-x)*5 + (4-y));
                            rotated[3] += map[i+x][j+y] << (y*5 + (4-x));

                        }
                    }
                    int base = rotated[0];
                    // 있는지 체크
                    if (!starMap.containsKey(base)) { // 없으면 추가
                        starMap.put(base, base);
                        starMap.put(rotated[1], base);
                        starMap.put(rotated[2], base);
                        starMap.put(rotated[3], base);
                        cnt.put(base, 0);
                    } else {
                        base = starMap.get(base);
                    }

                    // 타일 추가
                    for (int x = 0; x < 5; x++) {
                        for (int y = 0; y < 5; y++) {
                            tileMap[i+x][j+y] = base;
                            centerMap[i+x][j+y] = (i + 2) * 10000 + (j+2);
                        }
                    }
                    cnt.put(base, cnt.get(base) + 1);
                }
            }
        }


    }

    public boolean checkCon1(int r, int c) {
        int ans = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (map[r+i][c+j] == 1) ans++;
            }
        }
        return ans == 7;
    }

    public boolean checkCon2(int r, int c){
        boolean has = false;
        for (int i = 0; i < 5; i++) {
            if (map[r+0][c+i] == 1) {
                has = true;
                break;
            }
        }
        for (int i = 0; i < 5; i++) {
            if (map[r+4][c+i] == 1) {
                has = true;
                break;
            }
        }
        for (int i = 0; i < 5; i++) {
            if (map[r+i][c+0] == 1) {
                has = true;
                break;
            }
        }
        for (int i = 0; i < 5; i++) {
            if (map[r+i][c+4] == 1) {
                has = true;
                break;
            }
        }
        return has;
    }

    public int getCount(int mPiece[][]) {
        int star = 0;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                star += mPiece[i][j] << (i*5 + j);
            }
        }
        if (!starMap.containsKey(star)) return 0;


        return cnt.get(starMap.get(star));
    }

    public int getPosition(int mRow, int mCol) {
        int base = tileMap[mRow][mCol];
        int ans = Integer.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tileMap[i][j] == base) return centerMap[i][j];
            }
        }

        return ans;
    }
}

// center값 미리 저장
class StarTile_UserSolution2 {

    static int[][] map, tileMap;
    static int n;
    static Map<Integer, Integer> cnt, starMap, centerMap;

    public void init(int N, int mPlane[][]) {
        n = N;
        map = mPlane;
        tileMap = new int[n][n];
        centerMap = new HashMap<>();
        cnt = new HashMap<>();
        starMap = new HashMap<>();

        for (int i = 0; i < n-4; i++) {
            for (int j = 0; j < n - 4; j++) {
                // 별 타일이면
                if (checkCon1(i, j) && checkCon2(i, j)){
                    // 별 비트맵 만들기
                    int[] rotated = new int[4];
                    for (int x = 0; x < 5; x++) {
                        for (int y = 0; y < 5; y++) {
                            rotated[0] += map[i+x][j+y] << (x*5 + y);
                            rotated[1] += map[i+x][j+y] << ((4-y)*5 + x);
                            rotated[2] += map[i+x][j+y] << ((4-x)*5 + (4-y));
                            rotated[3] += map[i+x][j+y] << (y*5 + (4-x));

                        }
                    }
                    int base = rotated[0];
                    // 있는지 체크
                    if (!starMap.containsKey(base)) { // 없으면 추가
                        starMap.put(base, base);
                        starMap.put(rotated[1], base);
                        starMap.put(rotated[2], base);
                        starMap.put(rotated[3], base);
                        cnt.put(base, 0);
                        centerMap.put(base, (i+2)*10000 + (j+2));
                    } else {
                        base = starMap.get(base);
                    }

                    // 타일 추가
                    for (int x = 0; x < 5; x++) {
                        for (int y = 0; y < 5; y++) {
                            tileMap[i+x][j+y] = base;
                        }
                    }
                    cnt.put(base, cnt.get(base) + 1);
                }
            }
        }


    }

    public boolean checkCon1(int r, int c) {
        int ans = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (map[r+i][c+j] == 1) ans++;
            }
        }
        return ans == 7;
    }

    public boolean checkCon2(int r, int c){
        boolean has = false;
        for (int i = 0; i < 5; i++) {
            if (map[r+0][c+i] == 1) {
                has = true;
                break;
            }
        }
        for (int i = 0; i < 5; i++) {
            if (map[r+4][c+i] == 1) {
                has = true;
                break;
            }
        }
        for (int i = 0; i < 5; i++) {
            if (map[r+i][c+0] == 1) {
                has = true;
                break;
            }
        }
        for (int i = 0; i < 5; i++) {
            if (map[r+i][c+4] == 1) {
                has = true;
                break;
            }
        }
        return has;
    }

    public int getCount(int mPiece[][]) {
        int star = 0;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                star += mPiece[i][j] << (i*5 + j);
            }
        }
        if (!starMap.containsKey(star)) return 0;


        return cnt.get(starMap.get(star));
    }

    public int getPosition(int mRow, int mCol) {
        int base = tileMap[mRow][mCol];

        return centerMap.get(base);
    }
}
