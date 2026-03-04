package swea.btype;

import java.util.Scanner;
import java.util.TreeSet;

public class StopoverTransfer_25004 {
    private final static int MAX_E = 2000;
    private final static int MAX_S = 3;
    private final static int CMD_INIT = 100;
    private final static int CMD_ADD = 200;
    private final static int CMD_CALC = 300;

    private final static StopoverTransfer_UserSolution usersolution = new StopoverTransfer_UserSolution();

    private static boolean run(Scanner sc) {
        int q = sc.nextInt();

        int n, m, k;
        String strTmp;
        int[] sCityArr = new int[MAX_E];
        int[] eCityArr = new int[MAX_E];
        int[] mLimitArr = new int[MAX_E];
        int[] mStopover = new int[MAX_S];
        int sCity, eCity, mLimit;
        int cmd, ans, ret = 0;
        boolean okay = false;

        for (int i = 0; i < q; ++i) {
            cmd = sc.nextInt();
            strTmp = sc.next();
            switch (cmd) {
                case CMD_INIT:
                    okay = true;
                    strTmp = sc.next();
                    n = sc.nextInt();
                    strTmp = sc.next();
                    k = sc.nextInt();
                    for (int j = 0; j < k; ++j) {
                        strTmp = sc.next();
                        sCityArr[j] = sc.nextInt();
                        strTmp = sc.next();
                        eCityArr[j] = sc.nextInt();
                        strTmp = sc.next();
                        mLimitArr[j] = sc.nextInt();
                    }
                    usersolution.init(n, k, sCityArr, eCityArr, mLimitArr);
                    break;
                case CMD_ADD:
                    strTmp = sc.next();
                    sCity = sc.nextInt();
                    strTmp = sc.next();
                    eCity = sc.nextInt();
                    strTmp = sc.next();
                    mLimit = sc.nextInt();
                    usersolution.add(sCity, eCity, mLimit);
                    break;
                case CMD_CALC:
                    strTmp = sc.next();
                    sCity = sc.nextInt();
                    strTmp = sc.next();
                    eCity = sc.nextInt();
                    strTmp = sc.next();
                    m = sc.nextInt();
                    for (int j = 0; j < m; ++j) {
                        strTmp = sc.next();
                        mStopover[j] = sc.nextInt();
                    }
                    strTmp = sc.next();
                    ans = sc.nextInt();
                    ret = usersolution.calculate(sCity, eCity, m, mStopover);
                    if (ret != ans)
                        okay = false;
                    break;
                default:
                    okay = false;
                    break;
            }
        }
        return okay;
    }

    public static void main(String[] args) throws Exception {
        int TC, MARK;

        System.setIn(new java.io.FileInputStream("./BType/StopoverTransfer/sample_input.txt"));

        Scanner sc = new Scanner(System.in);

        TC = sc.nextInt();
        MARK = sc.nextInt();

        for (int testcase = 1; testcase <= TC; ++testcase) {
            int score = run(sc) ? MARK : 0;
            System.out.println("#" + testcase + " " + score);
        }

        sc.close();
    }
}
class StopoverTransfer_UserSolution {

    static TreeSet<Road> roads;
    static int[] parents;

    public void init(int N, int K, int sCity[], int eCity[], int mLimit[]) {
        parents = new int[N];
        roads = new TreeSet<>();
        for (int k = 0; k < K; k++) {
            roads.add(new Road(sCity[k], eCity[k], mLimit[k]));
        }
    }

    public void add(int sCity, int eCity, int mLimit) {
        roads.add(new Road(sCity, eCity, mLimit));
    }

    public int calculate(int sCity, int eCity, int M, int mStopover[]) {
        for (int i = 0; i < parents.length; i++) {
            parents[i] = i;
        }
        for (Road road : roads) {
            union(road.start, road.end);
            boolean stopover = true;
            for (int i = 0; i < M; i++) {
                if (find(sCity) != find(mStopover[i])){
                    stopover = false;
                    break;
                }
            }
            if (find(sCity) == find(eCity) && stopover)
                return road.weight;

        }
        return -1;
    }

    int find(int x) {
        if (parents[x] != x)
            return parents[x] = find(parents[x]);
        return parents[x];
    }

    void union(int a, int b) {
        a = find(a);
        b = find(b);

        if (a <= b)
            parents[b] = a;
        else
            parents[a] = b;
    }

    class Road implements Comparable<Road> {
        int start, end, weight;

        Road (int start, int end, int weight) {
            this.start = start;
            this.end = end;
            this.weight = weight;
        }

        @Override
        public int compareTo(Road o) {
            if (weight != o.weight) return Integer.compare(o.weight, weight);
            if (start != o.start) return Integer.compare(start, o.start);
            return Integer.compare(end, o.end);
        }
    }
}