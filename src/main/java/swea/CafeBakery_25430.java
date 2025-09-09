package swea;

import java.util.*;

public class CafeBakery_25430 {
    private final static int MAX_E = 30000;
    private final static int MAX_SHOP = 1000;
    private final static int CMD_INIT = 100;
    private final static int CMD_ADD = 200;
    private final static int CMD_CALC = 300;

    private final static UserSolution usersolution = new UserSolution();

    private static boolean run(Scanner sc) {
        int q = sc.nextInt();

        int n, k, m, p, r;
        int[] sBuildingArr = new int[MAX_E];
        int[] eBuildingArr = new int[MAX_E];
        int[] mDistArr = new int[MAX_E];
        int[] mCoffee = new int[MAX_SHOP];
        int[] mBakery = new int[MAX_SHOP];
        int sBuilding, eBuilding, mDist;
        int cmd, ans, ret = 0;
        boolean okay = false;

        for (int i = 0; i < q; ++i) {
            cmd = sc.nextInt();
            switch (cmd) {
                case CMD_INIT:
                    okay = true;
                    n = sc.nextInt();
                    k = sc.nextInt();
                    for (int j = 0; j < MAX_E; ++j) {
                        sBuildingArr[j] = -1;
                        eBuildingArr[j] = -1;
                        mDistArr[j] = 0;
                    }
                    for (int j = 0; j < k; ++j) {
                        sBuildingArr[j] = sc.nextInt();
                        eBuildingArr[j] = sc.nextInt();
                        mDistArr[j] = sc.nextInt();
                    }
                    usersolution.init(n, k, sBuildingArr, eBuildingArr, mDistArr);
                    break;
                case CMD_ADD:
                    sBuilding = sc.nextInt();
                    eBuilding = sc.nextInt();
                    mDist = sc.nextInt();
                    usersolution.add(sBuilding, eBuilding, mDist);
                    break;
                case CMD_CALC:
                    m = sc.nextInt();
                    p = sc.nextInt();
                    r = sc.nextInt();
                    for (int j = 0; j < MAX_SHOP; ++j) {
                        mCoffee[j] = -1;
                        mBakery[j] = -1;
                    }
                    for (int j = 0; j < m; ++j) {
                        mCoffee[j] = sc.nextInt();
                    }
                    for (int j = 0; j < p; ++j) {
                        mBakery[j] = sc.nextInt();
                    }
                    ret = usersolution.calculate(m, mCoffee, p, mBakery, r);
                    ans = sc.nextInt();
                    if (ans != ret)
                        okay =false;
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

        //System.setIn(new java.io.FileInputStream("res/sample_input.txt"));

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


class UserSolution {

    static ArrayList<Node>[] map;
    static int n, r;
    static int INF = Integer.MAX_VALUE;
    static int[] cDistance, bDistance;

    class Node implements Comparable<Node> {
        int node;
        int cost;
        Node(int node, int cost){
            this.node = node;
            this.cost = cost;
        }
        @Override
        public int compareTo(Node o) {
            return this.cost - o.cost;
        }
    }

    public void init(int N, int K, int sBuilding[], int eBuilding[], int mDistance[]) {
        n = N;
        map = new ArrayList[N];
        for (int i = 0; i < K; i++) {
            int s = sBuilding[i];
            int e = eBuilding[i];
            int c = mDistance[i];
            if (map[s] == null) {
                map[s] = new ArrayList<>();
            }
            map[s].add(new Node(e, c));
            if (map[e] == null) {
                map[e] = new ArrayList<>();
            }
            map[e].add(new Node(s, c));

        }
    }

    public void add(int sBuilding, int eBuilding, int mDistance) {
        if (map[sBuilding] == null) {
            map[sBuilding] = new ArrayList<>();
        }
        map[sBuilding].add(new Node(eBuilding, mDistance));
        if (map[eBuilding] == null) {
            map[eBuilding] = new ArrayList<>();
        }
        map[eBuilding].add(new Node(sBuilding, mDistance));
    }

    public int calculate(int M, int mCoffee[], int P, int mBakery[], int R) {
        r = R;
        boolean[] isCafe = new boolean[n];
        boolean[] isBakery = new boolean[n];
        cDistance = new int[n];
        bDistance = new int[n];
        Arrays.fill(cDistance, INF);
        Arrays.fill(bDistance, INF);
        for (int i = 0; i < M; ++i) {
            isCafe[mCoffee[i]] = true;
        }
        for (int i = 0; i < P; ++i) {
            isBakery[mBakery[i]] = true;
        }

        cDijkstra(M, mCoffee);
        bDijkstra(P, mBakery);

        int ans = INF;

        for (int i = 0; i < n; i++) {
            if (isCafe[i] || isBakery[i]) continue;
            if (bDistance[i] <= r && cDistance[i] <= r) {
                ans = Math.min(ans, cDistance[i] + bDistance[i]);
            }
        }

        if (ans == INF) return -1;
        return ans;
    }

    void cDijkstra(int m, int[] coffee){

        PriorityQueue<Node> pq = new PriorityQueue<>();

        for (int i = 0; i < m; i++) {
            cDistance[coffee[i]] = 0;
            pq.add(new Node(coffee[i], 0));
        }

        while (!pq.isEmpty()){
            Node now = pq.poll();
            int cost = now.cost;
            if (cost > cDistance[now.node]) continue;
            if (cost > r) continue;

            for (Node n : map[now.node]) {
                int dist = cost +  n.cost;
                if (dist < cDistance[n.node] && dist <= r) {
                    cDistance[n.node] = dist;
                    pq.add(new Node(n.node, dist));
                }
            }
        }
    }

    void bDijkstra(int p, int[] bakery){
        PriorityQueue<Node> pq = new PriorityQueue<>();

        for (int i = 0; i < p; i++) {
            bDistance[bakery[i]] = 0;
            pq.add(new Node(bakery[i], 0));
        }

        while (!pq.isEmpty()){
            Node now = pq.poll();
            int cost = now.cost;
            if (cost > bDistance[now.node]) continue;
            if (cost > r) continue;

            for (Node n : map[now.node]) {
                int dist = cost +  n.cost;
                if (dist < bDistance[n.node] && dist <= r) {
                    bDistance[n.node] = dist;
                    pq.add(new Node(n.node, dist));
                }
            }
        }
    }


}