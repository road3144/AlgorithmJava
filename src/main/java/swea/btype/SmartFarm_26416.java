package swea.btype;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class SmartFarm_26416 {
    private static BufferedReader br;
    private static SmartFarm_UserSolution userSolution = new SmartFarm_UserSolution();

    private final static int CATEGORY_NUM = 3;

    private final static int CMD_INIT     = 100;
    private final static int CMD_SOW      = 200;
    private final static int CMD_WATER    = 300;
    private final static int CMD_HARVEST  = 400;

    private static boolean run() throws Exception {
        StringTokenizer st = new StringTokenizer(br.readLine(), " ");

        int Q = Integer.parseInt(st.nextToken());
        int N, mTime, mCategory, L, G, mRow, mCol, mHeight, mWidth, ans, ret;
        int[] mGrowthTime = new int[CATEGORY_NUM];

        boolean okay = false;
        for (int q = 0; q < Q; ++q)
        {
            st = new StringTokenizer(br.readLine(), " ");
            int cmd = Integer.parseInt(st.nextToken());

            if (cmd == CMD_INIT)
            {
                N = Integer.parseInt(st.nextToken());
                for(int i = 0; i < 3; ++i)
                    mGrowthTime[i] = Integer.parseInt(st.nextToken());

                userSolution.init(N, mGrowthTime);
                okay = true;
            }
            else if (cmd == CMD_SOW)
            {
                mTime = Integer.parseInt(st.nextToken());
                mRow = Integer.parseInt(st.nextToken());
                mCol = Integer.parseInt(st.nextToken());
                mCategory = Integer.parseInt(st.nextToken());
                ans = Integer.parseInt(st.nextToken());
                ret = userSolution.sow(mTime, mRow, mCol, mCategory);
                if(ans != ret)
                    okay = false;
            }
            else if (cmd == CMD_WATER)
            {
                mTime = Integer.parseInt(st.nextToken());
                G = Integer.parseInt(st.nextToken());
                mRow = Integer.parseInt(st.nextToken());
                mCol = Integer.parseInt(st.nextToken());
                mHeight = Integer.parseInt(st.nextToken());
                mWidth = Integer.parseInt(st.nextToken());
                ans = Integer.parseInt(st.nextToken());
                ret = userSolution.water(mTime, G, mRow, mCol, mHeight, mWidth);
                if(ans != ret)
                    okay = false;

            }
            else if(cmd == CMD_HARVEST)
            {
                mTime = Integer.parseInt(st.nextToken());
                L = Integer.parseInt(st.nextToken());
                mRow = Integer.parseInt(st.nextToken());
                mCol = Integer.parseInt(st.nextToken());
                mHeight = Integer.parseInt(st.nextToken());
                mWidth = Integer.parseInt(st.nextToken());
                ans = Integer.parseInt(st.nextToken());
                ret = userSolution.harvest(mTime, L, mRow, mCol, mHeight, mWidth);
                if(ans != ret)
                    okay = false;
            }
        }
        return okay;
    }

    public static void main(String[] args) throws Exception {
        int T, MARK;

         System.setIn(new java.io.FileInputStream("./BType/SmartFarm/sample_input.txt"));
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

class SmartFarm_UserSolution {

    static class Crop{
        int r, c, start, type, add;

        public Crop(int r, int c, int start, int type) {
            this.r = r;
            this.c = c;
            this.start = start;
            this.type = type;
            add = 0;
        }

        int getTotal(int cur){
            return (cur - start) / need[type] + add;
        }
    }

    static class Bucket{
        int lazy;

        List<Crop> has = new ArrayList<>();
    }

    static int BUCKET_SIZE = 33;
    static int MAX_N = 1000;
    static int MAX_BUCKET_CNT = (MAX_N + BUCKET_SIZE - 1)/BUCKET_SIZE;

    static int[] need = new int[3];
    static Crop[][] crops = new Crop[MAX_N][MAX_N];
    static Bucket[][] buckets = new Bucket[MAX_BUCKET_CNT][MAX_BUCKET_CNT];

    void init(int N, int mGrowthTime[])	{
        for (int i = 0; i < 3; i++) {
            need[i] = mGrowthTime[i];
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                crops[i][j] = null;
            }
        }

        for (int i = 0; i < MAX_BUCKET_CNT; i++) {
            for (int j = 0; j < MAX_BUCKET_CNT; j++) {
                if (buckets[i][j] == null)
                    buckets[i][j] = new Bucket();
                buckets[i][j].has.clear();
                buckets[i][j].lazy = 0;
            }
        }
    }

    int sow(int mTime, int mRow, int mCol, int mCategory) {
        if (crops[mRow][mCol] != null) {
            return 0;
        }
        Crop c = new Crop(mRow, mCol, mTime, mCategory);
        crops[mRow][mCol] = c;

        int br = mRow / BUCKET_SIZE;
        int bc = mCol / BUCKET_SIZE;
        c.add -= buckets[br][bc].lazy;
        buckets[br][bc].has.add(c);
        return 1;
    }

    int water(int mTime, int G, int mRow, int mCol, int mHeight, int mWidth) {
        int cnt = 0;
        int er = (mRow + mHeight - 1);
        int ec = (mCol + mWidth - 1);
        int sbr = mRow / BUCKET_SIZE;
        int ebr = er / BUCKET_SIZE;
        int sbc = mCol / BUCKET_SIZE;
        int ebc = ec / BUCKET_SIZE;
        for (int br = sbr; br <= ebr; br++) {
            for (int bc = sbc; bc <= ebc; bc++) {
                if (buckets[br][bc].has.isEmpty()) continue;

                if(mCol <= bc * BUCKET_SIZE && (1+bc) * BUCKET_SIZE - 1 <= ec && mRow <= br * BUCKET_SIZE && (br+1) * BUCKET_SIZE - 1 <= er){
                    buckets[br][bc].lazy += G;
                    cnt += buckets[br][bc].has.size();
                } else {
                    for (Crop c : buckets[br][bc].has){
                        if (mCol <= c.c && c.c <= ec && mRow <= c.r && c.r <= er){
                            c.add += G;
                            cnt++;
                        }

                    }
                }
            }
        }
        return cnt;
    }

    int harvest(int mTime, int L, int mRow, int mCol, int mHeight, int mWidth) {
        int cnt = 0;
        int er = (mRow + mHeight - 1);
        int ec = (mCol + mWidth - 1);
        int sbr = mRow / BUCKET_SIZE;
        int ebr = er / BUCKET_SIZE;
        int sbc = mCol / BUCKET_SIZE;
        int ebc = ec / BUCKET_SIZE;
        for (int br = sbr; br <= ebr; br++) {
            for (int bc = sbc; bc <= ebc; bc++) {
                if (buckets[br][bc].has.isEmpty()) continue;
                for (Crop c : buckets[br][bc].has){
                    if (mCol <= c.c && c.c <= ec && mRow <= c.r && c.r <= er){
                        if (c.getTotal(mTime) + buckets[br][bc].lazy < L) {
                            return 0;
                        } else
                            cnt++;
                    }
                }
            }
        }

        for (int br = sbr; br <= ebr; br++) {
            for (int bc = sbc; bc <= ebc; bc++) {
                if (buckets[br][bc].has.isEmpty()) continue;
                Iterator<Crop> it = buckets[br][bc].has.iterator();
                while (it.hasNext()){
                    Crop c = it.next();
                    if (mCol <= c.c && c.c <= ec && mRow <= c.r && c.r <= er){
                        crops[c.r][c.c] = null;
                        it.remove();
                    }
                }
            }
        }
        return cnt;
    }
}
