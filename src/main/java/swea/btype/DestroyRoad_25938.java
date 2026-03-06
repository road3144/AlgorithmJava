package swea.btype;

import java.util.*;

public class DestroyRoad_25938 {

    private final static int MAX_K = 5000;
    private final static int CMD_INIT = 100;
    private final static int CMD_ADD = 200;
    private final static int CMD_REMOVE = 300;
    private final static int CMD_CALC = 400;

    private final static Destroy_UserSolution usersolution = new Destroy_UserSolution();

    private static boolean run(Scanner sc) {
        int q = sc.nextInt();

        int n, k;
        int[] mIdArr = new int[MAX_K];
        int[] sCityArr = new int[MAX_K];
        int[] eCityArr = new int[MAX_K];
        int[] mTimeArr = new int[MAX_K];
        int mId, sCity, eCity, mTime;
        int cmd, ans, ret = 0;
        boolean okay = false;

        for (int i = 0; i < q; ++i) {
            cmd = sc.nextInt();
            switch (cmd) {
                case CMD_INIT:
                    okay = true;
                    n = sc.nextInt();
                    k = sc.nextInt();
                    for (int j = 0; j < k; ++j) {
                        mIdArr[j] = sc.nextInt();
                        sCityArr[j] = sc.nextInt();
                        eCityArr[j] = sc.nextInt();
                        mTimeArr[j] = sc.nextInt();
                    }
                    usersolution.init(n, k, mIdArr, sCityArr, eCityArr, mTimeArr);
                    break;
                case CMD_ADD:
                    mId = sc.nextInt();
                    sCity = sc.nextInt();
                    eCity = sc.nextInt();
                    mTime = sc.nextInt();
                    usersolution.add(mId, sCity, eCity, mTime);
                    break;
                case CMD_REMOVE:
                    mId = sc.nextInt();
                    usersolution.remove(mId);
                    break;
                case CMD_CALC:
                    sCity = sc.nextInt();
                    eCity = sc.nextInt();
                    ans = sc.nextInt();
                    ret = usersolution.calculate(sCity, eCity);
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

        System.setIn(new java.io.FileInputStream("./BType/DestroyRoad/sample_input.txt"));

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

class Destroy_UserSolution {

    static class Edge implements Comparable<Edge>{
        int id;
        int node;
        int cost;

        Edge (int id, int node, int cost){
            this.id = id;
            this.node = node;
            this.cost = cost;
        }

        @Override
        public int compareTo(Edge o){
            return cost - o.cost;
        }

        @Override
        public boolean equals(Object o){
            Edge other = (Edge) o;
            return id == other.id;
        }

        @Override
        public int hashCode(){
            return Objects.hash(id);
        }
    }

    static ArrayList<Edge>[] graph;
    static Map<Integer, Integer> map;
    static int[] dist, prev;
    static int INF = Integer.MAX_VALUE;

    public void init(int N, int K, int mId[], int sCity[], int eCity[], int mTime[]) {
        graph = new ArrayList[N];
        map = new HashMap<>();
        dist = new int[N];
        prev = new int[N];
        for (int i = 0; i < N; i++) {
            graph[i] = new ArrayList<>();
        }

        for (int i = 0; i < K; i++) {
            graph[sCity[i]].add(new Edge(mId[i], eCity[i], mTime[i]));
            map.put(mId[i], sCity[i]);
        }
    }

    public void add(int mId, int sCity, int eCity, int mTime) {
        graph[sCity].add(new Edge(mId, eCity, mTime));
        map.put(mId, sCity);
    }

    public void remove(int mId) {
        graph[map.get(mId)].remove(new Edge(mId, 0, 0));
    }

    public int calculate(int sCity, int eCity) {
        PriorityQueue<Edge> pq = new PriorityQueue<>();
        int origin = 0;
        Arrays.fill(dist, INF);
        Arrays.fill(prev, -1);

        pq.add(new Edge(0, sCity, 0));
        dist[sCity] = 0;
        while (!pq.isEmpty()){
            Edge now = pq.poll();

            if (now.cost > dist[now.node]) continue;

            for (Edge next : graph[now.node]){
                int total  = now.cost + next.cost;
                if (total < dist[next.node]){
                    prev[next.node] = now.node;
                    dist[next.node] = total;
                    pq.add(new Edge(0, next.node, total));
                }
            }
        }
        origin = dist[eCity];

        if (origin == INF) return -1;

        int sv = prev[eCity], ev = eCity;
        int max = -1;

        while (sv > -1) {
            Arrays.fill(dist, INF);
            pq.add(new Edge(0, sCity, 0));
            dist[sCity] = 0;
            while (!pq.isEmpty()){
                Edge now = pq.poll();

                if (now.cost > dist[now.node]) continue;

                for (Edge next : graph[now.node]){
                    int total  = now.cost + next.cost;
                    if (total < dist[next.node] && !(now.node == sv && next.node == ev)){
                        dist[next.node] = total;
                        pq.add(new Edge(0, next.node, total));
                    }
                }
            }
            if (dist[eCity] == INF) return -1;

            max = Math.max(max, dist[eCity]);

            ev = sv;
            sv = prev[ev];
        }
        if (max == -1) return -1;

        return max - origin;
    }
}
