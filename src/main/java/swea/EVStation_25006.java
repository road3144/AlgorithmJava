package swea;

import java.util.*;

public class EVStation_25006 {

    private final static int MAX_N = 300;
    private final static int MAX_K = 2000;
    private final static int CMD_INIT = 100;
    private final static int CMD_ADD = 200;
    private final static int CMD_REMOVE = 300;
    private final static int CMD_COST = 400;

    private final static EVStation_UserSolution usersolution = new EVStation_UserSolution();

    private static boolean run(Scanner sc) {
        int q = sc.nextInt();

        int n, k;
        String strTmp;
        int[] mCostArr = new int[MAX_N];
        int[] mIdArr = new int[MAX_K];
        int[] sCityArr = new int[MAX_K];
        int[] eCityArr = new int[MAX_K];
        int[] mDistArr = new int[MAX_K];
        int mId, sCity, eCity, mDist;
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
                    for (int j = 0; j < n; ++j) {
                        strTmp = sc.next();
                        mCostArr[j] = sc.nextInt();
                    }
                    for (int j = 0; j < k; ++j) {
                        strTmp = sc.next();
                        mIdArr[j] = sc.nextInt();
                        strTmp = sc.next();
                        sCityArr[j] = sc.nextInt();
                        strTmp = sc.next();
                        eCityArr[j] = sc.nextInt();
                        strTmp = sc.next();
                        mDistArr[j] = sc.nextInt();
                    }
                    usersolution.init(n, mCostArr, k, mIdArr, sCityArr, eCityArr, mDistArr);
                    break;
                case CMD_ADD:
                    strTmp = sc.next();
                    mId = sc.nextInt();
                    strTmp = sc.next();
                    sCity = sc.nextInt();
                    strTmp = sc.next();
                    eCity = sc.nextInt();
                    strTmp = sc.next();
                    mDist = sc.nextInt();
                    usersolution.add(mId, sCity, eCity, mDist);
                    break;
                case CMD_REMOVE:
                    strTmp = sc.next();
                    mId = sc.nextInt();
                    usersolution.remove(mId);
                    break;
                case CMD_COST:
                    strTmp = sc.next();
                    sCity = sc.nextInt();
                    strTmp = sc.next();
                    eCity = sc.nextInt();
                    strTmp = sc.next();
                    ans = sc.nextInt();
                    ret = usersolution.cost(sCity, eCity);
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

        System.setIn(new java.io.FileInputStream("./BType/EVStation/sample_input.txt"));

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

class EVStation_UserSolution {

    static class Edge{
        int from, node, dist;

        Edge(int from, int node, int dist){
            this.from = from;
            this.node = node;
            this.dist = dist;
        }
    }

    static class Path implements Comparable<Path> {
        int node, total, minCost;

        Path(int node, int total, int minCost){
            this.node = node;
            this.total = total;
            this.minCost = minCost;
        }

        public int compareTo(Path o){
            return this.total - o.total;
        }
    }

    static int n;
    static Map<Integer, Edge> map;
    static int[] costInfo;
    static int[][] cost;
    static List<Edge>[] graph;
    static PriorityQueue<Path> pq = new PriorityQueue<>();

    public void init(int N, int mCost[], int K, int mId[], int sCity[], int eCity[], int mDistance[]) {
        n = N;
        map = new HashMap<>();
        cost = new int[n][20001];
        costInfo = new int[n];
        graph = new List[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
            costInfo[i] = mCost[i];
        }

        for (int i = 0; i < K; i++) {
            Edge e = new Edge(sCity[i], eCity[i], mDistance[i]);
            map.put(mId[i], e);
            graph[sCity[i]].add(e);
        }
    }

    public void add(int mId, int sCity, int eCity, int mDistance) {
        Edge e = new Edge(sCity, eCity, mDistance);
        map.put(mId, e);
        graph[sCity].add(e);
    }

    public void remove(int mId) {
        if (!map.containsKey(mId)) return;
        Edge e = map.get(mId);
        graph[e.from].remove(e);
        map.remove(mId);
    }

    public int cost(int sCity, int eCity) {
        for (int i = 0; i < n; i++) {
            Arrays.fill(cost[i], Integer.MAX_VALUE);
        }

        cost[sCity][costInfo[sCity]] = 0;
        pq.clear();
        pq.add(new Path(sCity, 0, costInfo[sCity]));

        while (!pq.isEmpty()){
            Path now = pq.poll();
            if (cost[now.node][now.minCost] < now.total) continue;
            if (now.node == eCity) return now.total;
            for (Edge next : graph[now.node]){
                int nextMinCost = Math.min(now.minCost, costInfo[next.node]);
                int nextTotal = now.total + now.minCost * next.dist;
                if (cost[next.node][nextMinCost] <= nextTotal) continue;

                cost[next.node][nextMinCost] = nextTotal;
                pq.add(new Path(next.node, nextTotal, Math.min(now.minCost, costInfo[next.node])));
            }
        }

        return -1;
    }
}
