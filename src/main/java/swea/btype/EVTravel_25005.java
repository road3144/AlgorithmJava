package swea.btype;
import java.util.*;

public class EVTravel_25005 {

    private final static int MAX_N = 500;
    private final static int MAX_M = 5;
    private final static int MAX_K = 4000;
    private final static int CMD_INIT = 100;
    private final static int CMD_ADD = 200;
    private final static int CMD_REMOVE = 300;
    private final static int CMD_COST = 400;

    private final static EVTravel_UserSolution2 usersolution = new EVTravel_UserSolution2();

    private static boolean run(Scanner sc) {
        int q = sc.nextInt();

        int n, m, k, b;
        int[] mChargeArr = new int[MAX_N];
        int[] mIdArr = new int[MAX_K];
        int[] sCityArr = new int[MAX_K];
        int[] eCityArr = new int[MAX_K];
        int[] mTimeArr = new int[MAX_K];
        int[] mPowerArr = new int[MAX_K];
        int[] mCityArr = new int[MAX_M];
        int mId, sCity, eCity, mTime, mPower;
        int cmd, ans, ret = 0;
        boolean okay = false;

        for (int i = 0; i < q; ++i) {
            cmd = sc.nextInt();
            switch (cmd) {
                case CMD_INIT:
                    okay = true;
                    n = sc.nextInt();
                    k = sc.nextInt();
                    for (int j = 0; j < n; ++j) {
                        mChargeArr[j] = sc.nextInt();
                    }
                    for (int j = 0; j < k; ++j) {
                        mIdArr[j] = sc.nextInt();
                        sCityArr[j] = sc.nextInt();
                        eCityArr[j] = sc.nextInt();
                        mTimeArr[j] = sc.nextInt();
                        mPowerArr[j] = sc.nextInt();;
                    }
                    usersolution.init(n, mChargeArr, k, mIdArr, sCityArr, eCityArr, mTimeArr, mPowerArr);
                    break;
                case CMD_ADD:
                    mId = sc.nextInt();
                    sCity = sc.nextInt();
                    eCity = sc.nextInt();
                    mTime = sc.nextInt();
                    mPower = sc.nextInt();
                    usersolution.add(mId, sCity, eCity, mTime, mPower);
                    break;
                case CMD_REMOVE:
                    mId = sc.nextInt();
                    usersolution.remove(mId);
                    break;
                case CMD_COST:
                    b = sc.nextInt();
                    sCity = sc.nextInt();
                    eCity = sc.nextInt();
                    ans = sc.nextInt();
                    m = sc.nextInt();
                    for (int j = 0; j < m; ++j) {
                        mCityArr[j] = sc.nextInt();
                        mTimeArr[j] = sc.nextInt();
                    }
                    ret = usersolution.cost(b, sCity, eCity, m, mCityArr, mTimeArr);
                    if (ans != ret)
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

        System.setIn(new java.io.FileInputStream("./BType/EVTravel/sample_input.txt"));

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

class EVTravel_UserSolution {

    static class Edge implements Comparable<Edge>{
        int id, node, time, power, type;
        boolean alive = true;

        Edge(int id, int node, int time, int power, int type){
            this.id = id;
            this.node = node;
            this.time = time;
            this.power = power;
            this.type = type; // 전기차 0 전염병 1
        }

        public int compareTo(Edge o){
            if (this.time != o.time) return this.time - o.time;
            return o.type - this.type;
        }
    }

    static int[] charge;
    static List<Edge>[] graph;
    static int n;
    static Map<Integer, Edge> edgeMap;

    public void init(int N, int mCharge[], int K, int mId[], int sCity[], int eCity[], int mTime[], int mPower[]) {
        n = N;
        charge = new int[N];
        edgeMap = new HashMap<>();
        for (int i = 0; i < N; i++) {
            charge[i] = mCharge[i];
        }
        graph = new List[N];
        for (int i = 0; i < N; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int i = 0; i < K; i++) {
            int s = sCity[i];
            int e = eCity[i];
            int id = mId[i];
            int time = mTime[i];
            int power = mPower[i];
            Edge edge = new Edge(id, e, time, power, 0);
            graph[s].add(edge);
            edgeMap.put(id, edge);
        }
        for (int i = 0; i < N; i++) {
            graph[i].add(new Edge(0, i, 1, -charge[i], 0)); // 충전하는 경우 추가
        }
    }

    public void add(int mId, int sCity, int eCity, int mTime, int mPower) {
        Edge edge = new Edge(mId, eCity, mTime, mPower, 0);
        graph[sCity].add(edge);
        edgeMap.put(mId, edge);
        graph[sCity].add(edge);
    }

    public void remove(int mId) {
        edgeMap.get(mId).alive = false;
    }

    public int cost(int B, int sCity, int eCity, int M, int mCity[], int mTime[]) {
        int ans = Integer.MAX_VALUE;
        PriorityQueue<Edge> pq = new PriorityQueue<>();
        int[] fluDist = new int[n];
        int[][] dist = new int[B+1][n];
        Arrays.fill(fluDist, Integer.MAX_VALUE);
        for (int i = 0; i < B+1; i++) {
            Arrays.fill(dist[i], Integer.MAX_VALUE);
        }

        for (int i = 0; i < M; i++) {
            pq.add(new Edge(0, mCity[i], mTime[i], 0, 1));
            fluDist[mCity[i]] = mTime[i];
        }

        //전염병 큐
        while (!pq.isEmpty()){
            Edge now = pq.poll();
            if (fluDist[now.node] < now.time) continue;

            for (Edge next : graph[now.node]){
                if (!next.alive) continue;
                if (now.node == next.node) continue;
                int totalTime = now.time + next.time;
                // 전염병 처리
                if (fluDist[next.node] <= totalTime) continue;

                fluDist[next.node] = totalTime;
                pq.add(new Edge(0, next.node, totalTime, 0, now.type));

            }
        }

        pq.add(new Edge(0, sCity, 0, B, 0));

        dist[B][sCity] = 0;

        while (!pq.isEmpty()){
            Edge now = pq.poll();
            if (now.node == eCity) return now.time;
            if (dist[now.power][now.node] < now.time) continue;
            if (fluDist[now.node] <= now.time) continue;


            for (Edge next : graph[now.node]){
                if (!next.alive) continue;
                int totalTime = now.time + next.time;
                int remainPower = now.power - next.power;
                if (remainPower > B) remainPower = B;
                //전기차 처리
                if(now.type == 0){
                    if (remainPower < 0) continue;
                    if (fluDist[next.node] <= totalTime) continue;
                    if (dist[remainPower][next.node] <= totalTime) continue;

                    dist[remainPower][next.node] = totalTime;
                    pq.add(new Edge(0, next.node, totalTime, remainPower, now.type));
                }
            }
        }

        for (int i = 0; i < B+1; i++) {
            ans = Math.min(ans, dist[i][eCity]);
        }

//        for (int i = 0; i < B+1; i++) {
//            System.out.println(Arrays.toString(dist[i]));
//        }
//        System.out.println(Arrays.toString(fluDist));
//        System.out.println(ans);

        return (ans == Integer.MAX_VALUE) ? -1 : ans;
    }
}

