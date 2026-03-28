package swea.btype;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

class DrawSquare_24999 {

    private final static int CMD_INIT = 1;
    private final static int CMD_DRAW = 2;
    private final static int CMD_RECT = 3;
    private final static int CMD_CNT = 4;

    private final static DrawSquare_UserSolution3 USERSOLUTION = new DrawSquare_UserSolution3();

    private static boolean run(BufferedReader br) throws Exception {

        int query_num = Integer.parseInt(br.readLine());
        boolean ok = false;

        for (int q = 0; q < query_num; q++) {
            StringTokenizer st = new StringTokenizer(br.readLine(), " ");
            int query = Integer.parseInt(st.nextToken());

            if (query == CMD_INIT) {
                int L = Integer.parseInt(st.nextToken());
                int N = Integer.parseInt(st.nextToken());
                USERSOLUTION.init(L, N);
                ok = true;
            } else if (query == CMD_DRAW) {
                int mID = Integer.parseInt(st.nextToken());
                int mRow = Integer.parseInt(st.nextToken());
                int mCol = Integer.parseInt(st.nextToken());
                int mHeight = Integer.parseInt(st.nextToken());
                int mWidth = Integer.parseInt(st.nextToken());
                int ret = USERSOLUTION.draw(mID, mRow, mCol, mHeight, mWidth);
                int ans = Integer.parseInt(st.nextToken());
                if (ans != ret) {
                    ok = false;
                }
            } else if (query == CMD_RECT) {
                int mID = Integer.parseInt(st.nextToken());
                int ret = USERSOLUTION.getRectCount(mID);
                int ans = Integer.parseInt(st.nextToken());
                if (ans != ret) {
                    ok = false;
                }
            } else if (query == CMD_CNT) {
                int ret = USERSOLUTION.countGroup();
                int ans = Integer.parseInt(st.nextToken());
                if (ans != ret) {
                    ok = false;
                }
            }
        }
        return ok;
    }

    public static void main(String[] args) throws Exception {
        int T, MARK;
         System.setIn(new java.io.FileInputStream("./BType/DrawSquare/sample_input.txt"));

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine(), " ");

        T = Integer.parseInt(st.nextToken());
        MARK = Integer.parseInt(st.nextToken());

        for (int tc = 1; tc <= T; tc++) {
            int score = run(br) ? MARK : 0;
            System.out.println("#" + tc + " " + score);
        }
        br.close();
    }
}

class DrawSquare_UserSolution {

    Map<Integer, Integer> parents;
    Map<Integer, Integer> group;
    int[][] map;

    public void init(int L, int N) {
        parents = new HashMap<>();
        group = new HashMap<>();
        map = new int[N][N];
    }

    public int draw(int mID, int mRow, int mCol, int mHeight, int mWidth) {
        parents.put(mID, mID);
        group.put(mID, 1);
        int parent = find(mID);
        for (int i = mRow; i < mRow + mHeight; i++) {
            for (int j = mCol; j <mCol +  mWidth; j++) {
                if (map[i][j] != 0) {
                    int otherP = find(map[i][j]);
                    if (parent != otherP) {
                        union(mID, otherP);
                        parent = find(mID);
                    }
                }
                map[i][j] = find(parent);
            }
        }

        return group.get(find(mID));
    }

    public int getRectCount(int mID) {
        if (!parents.containsKey(mID)) {
            return 0;
        }
        return group.get(find(mID));
    }

    public int countGroup() {
        int ans = 0;
        for (int id : parents.keySet()){
            if (id == find(id)) ans++;
        }
        return ans;
    }

    public int find(int x) {
        if (x == parents.get(x))
            return x;
        parents.put(x, find(parents.get(x)));
        return parents.get(x);
    }

    public void union(int a, int b){
        a = find(a);
        b = find(b);
        if (a < b) {
            parents.put(b, a);
            group.put(a, group.get(a) + group.get(b));
        } else if (b < a) {
            parents.put(a, b);
            group.put(b, group.get(b) + group.get(a));
        }
    }

}


// 사각형 전체 검사 하는게 아니라 섹션 나눠서 검사
class DrawSquare_UserSolution2 {

    static Map<Integer, List<Square>> sections;
    static Map<Integer, Integer> parents;
    static Map<Integer, Integer> group;
    static int n, l, m;

    static class Square {
        int id, row, col, height, width;

        Square(int id, int row, int col, int height, int width){
            this.id = id;
            this.row = row;
            this.col = col;
            this.height = height;
            this.width = width;
        }

        public boolean cross(int r1, int r2, int c1, int c2) {
            if (row + height - 1 < r1) return false;
            if (r2 < row) return false;
            if (col + width - 1 < c1) return false;
            if (c2 < col) return false;
            return true;
        }
    }

    public void init(int L, int N) {

        parents = new HashMap<>();
        group = new HashMap<>();
        sections = new HashMap<>();
        l = L;
        m = (N%L > 0) ? N/L + 1 : N/L;
        n = N;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                sections.put(i * m + j, new ArrayList<>());
            }
        }
    }

    public int draw(int mID, int mRow, int mCol, int mHeight, int mWidth) {
        parents.put(mID, mID);
        group.put(mID, 1);
        Square now = new Square(mID, mRow, mCol, mHeight, mWidth);
        int sr = (mRow / l), sc = mCol / l;
        int idx = sr * m + sc;

        // 시작 위치
        for (Square sq : sections.get(idx)) {
            if (now.cross(sq.row, sq.row + sq.height-1, sq.col, sq.col + sq.width-1)){
                union(now.id, sq.id);
            }
        }
        sections.get(idx).add(now);

        // 마지막 열 아니고 아래로 넘어가면
        if (sr + 1 < m && mRow + mHeight > (sr + 1) * l) {
            for (Square sq : sections.get(idx + m)) {
                if (now.cross(sq.row, sq.row + sq.height-1, sq.col, sq.col + sq.width-1)){
                    union(now.id, sq.id);
                }
            }
            sections.get(idx + m).add(now);
        }

        // 마지막 행 아니고 오른쪽 넘어가면
        if (sc + 1 < m && mCol + mWidth > (sc + 1) * l) {
            for (Square sq : sections.get(idx + 1)) {
                if (now.cross(sq.row, sq.row + sq.height-1, sq.col, sq.col + sq.width-1)){
                    union(now.id, sq.id);
                }
            }
            sections.get(idx + 1).add(now);
        }

        // 마지막 행멸 아니고 모두 넘어가는 경우
        if ((sr + 1 < m && mRow + mHeight > (sr + 1) * l) && (sc + 1 < m && mCol + mWidth > (sc + 1) * l)) {
            ;
            for (Square sq : sections.get(idx + m + 1)) {
                if (now.cross(sq.row, sq.row + sq.height-1, sq.col, sq.col + sq.width-1)){
                    union(now.id, sq.id);
                }
            }
            sections.get(idx + m + 1).add(now);
        }

        return group.get(find(mID));
    }

    public int getRectCount(int mID) {
        if (!parents.containsKey(mID)) {
            return 0;
        }
        return group.get(find(mID));
    }

    public int countGroup() {
        int ans = 0;
        for (int id : parents.keySet()){
            if (id == find(id)) ans++;
        }
        return ans;
    }

    public int find(int x) {
        if (x == parents.get(x))
            return x;
        parents.put(x, find(parents.get(x)));
        return parents.get(x);
    }

    public void union(int a, int b){
        a = find(a);
        b = find(b);
        if (a < b) {
            parents.put(b, a);
            group.put(a, group.get(a) + group.get(b));
        } else if (b < a) {
            parents.put(a, b);
            group.put(b, group.get(b) + group.get(a));
        }
    }

}

// Map => arr 최적화
class DrawSquare_UserSolution3 {

    static Map<Integer, List<Square>> sections;
    static Map<Integer, Integer> map;
    static int n, l, m, idx;
    static int[] parent, cnt;

    static class Square {
        int id, row, col, height, width;

        Square(int id, int row, int col, int height, int width){
            this.id = id;
            this.row = row;
            this.col = col;
            this.height = height;
            this.width = width;
        }

        public boolean cross(int r1, int r2, int c1, int c2) {
            if (row + height - 1 < r1) return false;
            if (r2 < row) return false;
            if (col + width - 1 < c1) return false;
            if (c2 < col) return false;
            return true;
        }
    }

    public void init(int L, int N) {
        idx = 0;
        map = new HashMap<>();
        parent = new int[15000];
        cnt = new int[15000];

        sections = new HashMap<>();
        l = L;
        m = (N%L > 0) ? N/L + 1 : N/L;
        n = N;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                sections.put(i * m + j, new ArrayList<>());
            }
        }
    }

    public int draw(int mID, int mRow, int mCol, int mHeight, int mWidth) {
        map.put(mID, idx);
        parent[idx] = idx;
        cnt[idx] = 1;
        Square now = new Square(idx, mRow, mCol, mHeight, mWidth);
        int sr = (mRow / l), sc = mCol / l;
        int sidx = sr * m + sc;

        // 시작 위치
        for (Square sq : sections.get(sidx)) {
            if (now.cross(sq.row, sq.row + sq.height-1, sq.col, sq.col + sq.width-1)){
                union(now.id, sq.id);
            }
        }
        sections.get(sidx).add(now);

        // 마지막 행 아니고 아래로 넘어가면
        if (sr + 1 < m && mRow + mHeight > (sr + 1) * l) {
            for (Square sq : sections.get(sidx + m)) {
                if (now.cross(sq.row, sq.row + sq.height-1, sq.col, sq.col + sq.width-1)){
                    union(now.id, sq.id);
                }
            }
            sections.get(sidx + m).add(now);
        }

        // 마지막 열 아니고 오른쪽 넘어가면
        if (sc + 1 < m && mCol + mWidth > (sc + 1) * l) {
            for (Square sq : sections.get(sidx + 1)) {
                if (now.cross(sq.row, sq.row + sq.height-1, sq.col, sq.col + sq.width-1)){
                    union(now.id, sq.id);
                }
            }
            sections.get(sidx + 1).add(now);
        }

        // 마지막 행멸 아니고 모두 넘어가는 경우
        if ((sr + 1 < m && mRow + mHeight > (sr + 1) * l) && (sc + 1 < m && mCol + mWidth > (sc + 1) * l)) {
            ;
            for (Square sq : sections.get(sidx + m + 1)) {
                if (now.cross(sq.row, sq.row + sq.height-1, sq.col, sq.col + sq.width-1)){
                    union(now.id, sq.id);
                }
            }
            sections.get(sidx + m + 1).add(now);
        }

        return cnt[find(idx++)];
    }

    public int getRectCount(int mID) {
        if (!map.containsKey(mID)) {
            return 0;
        }
        return cnt[find(map.get(mID))];
    }

    public int countGroup() {
        int ans = 0;

        for (int i = 0; i < idx; i++) {
            if (i == find(i)) ans++;
        }
        return ans;
    }

    public int find(int x) {
        if (x == parent[x])
            return x;

        return parent[x] = find(parent[x]);
    }

    public void union(int a, int b){
        a = find(a);
        b = find(b);
        if (a < b) {
            parent[b] = a;
            cnt[a] += cnt[b];
        } else if (b < a) {
            parent[a] = b;
            cnt[b] += cnt[a];
        }
    }

}