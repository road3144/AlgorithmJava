package swea.btype;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class AncientCommunication_26113 {

    private final static int MAX_K = 10000;
    private final static int CMD_INIT = 100;
    private final static int CMD_ADD = 200;
    private final static int CMD_REMOVE = 300;
    private final static int CMD_CALC = 400;

    private final static AncientCommunication_UserSolution4 usersolution = new AncientCommunication_UserSolution4();

    private static boolean run(BufferedReader br) throws Exception {
        int q = Integer.parseInt(br.readLine());

        int n, m, k;
        int[] mIdArr = new int[MAX_K];
        int[] sCityArr = new int[MAX_K];
        int[] eCityArr = new int[MAX_K];
        int[] mDistArr = new int[MAX_K];
        int mId, mCity, sCity, eCity, mDist;
        int cmd, ans, ret = 0;
        boolean okay = false;

        for (int i = 0; i < q; ++i) {
            StringTokenizer st = new StringTokenizer(br.readLine(), " ");
            cmd = Integer.parseInt(st.nextToken());
            switch (cmd) {
                case CMD_INIT:
                    okay = true;
                    n = Integer.parseInt(st.nextToken());
                    m = Integer.parseInt(st.nextToken());
                    k = Integer.parseInt(st.nextToken());
                    for (int j = 0; j < k; ++j) {
                        StringTokenizer road = new StringTokenizer(br.readLine(), " ");
                        mIdArr[j] = Integer.parseInt(road.nextToken());
                        sCityArr[j] = Integer.parseInt(road.nextToken());
                        eCityArr[j] = Integer.parseInt(road.nextToken());
                        mDistArr[j] = Integer.parseInt(road.nextToken());
                    }
                    usersolution.init(n, m, k, mIdArr, sCityArr, eCityArr, mDistArr);
                    break;
                case CMD_ADD:
                    mId = Integer.parseInt(st.nextToken());
                    sCity = Integer.parseInt(st.nextToken());
                    eCity = Integer.parseInt(st.nextToken());
                    mDist = Integer.parseInt(st.nextToken());
                    usersolution.add(mId, sCity, eCity, mDist);
                    break;
                case CMD_REMOVE:
                    mId = Integer.parseInt(st.nextToken());
                    usersolution.remove(mId);
                    break;
                case CMD_CALC:
                    mCity = Integer.parseInt(st.nextToken());
                    ans = Integer.parseInt(st.nextToken());
                    ret = usersolution.calculate(mCity);
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

        System.setIn(new java.io.FileInputStream("./BType/AncientCommunication/sample_input.txt"));

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine(), " ");

        TC = Integer.parseInt(st.nextToken());
        MARK = Integer.parseInt(st.nextToken());

        for (int testcase = 1; testcase <= TC; ++testcase) {
            int score = run(br) ? MARK : 0;
            System.out.println("#" + testcase + " " + score);
        }

        br.close();
    }
}

// 시초남
class AncientCommunication_UserSolution {

    static class Edge implements Comparable<Edge>{
        int id, node, cost, max;
        boolean alive = true;

        Edge(int id, int node, int cost, int max){
            this.id = id;
            this.node = node;
            this.cost = cost;
            this.max = max;
        }

        public int compareTo(Edge o){
            return this.cost - o.cost;
        }
    }

    static List<Edge>[] graph;
    static Map<Integer, Edge> map;
    static int n, s;
    static PriorityQueue<Edge> pq = new PriorityQueue<>();

    public void init(int N, int mCapital, int K, int mId[], int sCity[], int eCity[], int mDistance[]) {
        n = N;
        s = mCapital;
        graph = new List[n];
        map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int i = 0; i < K; i++) {
            int id = mId[i];
            int s = sCity[i];
            int e = eCity[i];
            int cost = mDistance[i];
            Edge edge = new Edge(id, e, cost, 0);
            map.put(id, edge);
            graph[s].add(edge);
        }
    }

    public void add(int mId, int sCity, int eCity, int mDistance) {
        Edge edge = new Edge(mId, eCity, mDistance, 0);
        graph[sCity].add(edge);
        map.put(mId, edge);
    }

    public void remove(int mId) {
        map.get(mId).alive = false;
    }

    public int calculate(int mCity) {
        int[] dist = new int[n];
        int[] maxCost = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(maxCost, Integer.MAX_VALUE);
        pq.clear();
        pq.add(new Edge(0, s, 0, 0));
        dist[s] = 0;
        maxCost[s] = 0;

        while (!pq.isEmpty()){
            Edge now = pq.poll();
            if (dist[now.node] < now.cost) continue;
            if (now.node == mCity) break;

            for (Edge next : graph[now.node]){
                if (!next.alive) continue;
                int total = now.cost + next.cost;
                if (dist[next.node] < total) continue;

                // 최단 거리인 상태에서
                int max = Integer.max(now.max, next.cost);
                // 거리가 같다면 (최단 거리 여러개 있는 경우) 더작은거
                if (dist[next.node] == total)
                    maxCost[next.node] = Math.min(maxCost[next.node], max);
                else // 더 짧으면 무조건 교체
                    maxCost[next.node] = max;
                dist[next.node] = total;
                pq.add(new Edge(0, next.node, total, max));

            }
        }

//        System.out.println(Arrays.toString(dist));
//        System.out.println(Arrays.toString(maxCost));

        return (maxCost[mCity] == Integer.MAX_VALUE) ? -1 : maxCost[mCity];
    }
}

// 프루닝 해보자
class AncientCommunication_UserSolution2 {

    static class Edge implements Comparable<Edge>{
        int id, node, cost, max;
        boolean alive = true;

        Edge(int id, int node, int cost, int max){
            this.id = id;
            this.node = node;
            this.cost = cost;
            this.max = max;
        }

        public int compareTo(Edge o){
            return this.cost - o.cost;
        }
    }

    static List<Edge>[] graph;
    static Map<Integer, Edge> map;
    static int n, s;
    static PriorityQueue<Edge> pq = new PriorityQueue<>();

    public void init(int N, int mCapital, int K, int mId[], int sCity[], int eCity[], int mDistance[]) {
        n = N;
        s = mCapital;
        graph = new List[n];
        map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int i = 0; i < K; i++) {
            int id = mId[i];
            int s = sCity[i];
            int e = eCity[i];
            int cost = mDistance[i];
            Edge edge = new Edge(id, e, cost, 0);
            map.put(id, edge);
            graph[s].add(edge);
        }
    }

    public void add(int mId, int sCity, int eCity, int mDistance) {
        Edge edge = new Edge(mId, eCity, mDistance, 0);
        graph[sCity].add(edge);
        map.put(mId, edge);
    }

    public void remove(int mId) {
        map.get(mId).alive = false;
    }

    public int calculate(int mCity) {
        int[] dist = new int[n];
        int[] maxCost = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(maxCost, Integer.MAX_VALUE);
        pq.clear();
        pq.add(new Edge(0, s, 0, 0));
        dist[s] = 0;
        maxCost[s] = 0;

        while (!pq.isEmpty()){
            Edge now = pq.poll();
            if (dist[now.node] < now.cost) continue;
            if (now.node == mCity) break;

            for (Edge next : graph[now.node]){
                if (!next.alive) continue;
                int newDist = now.cost + next.cost;
                int newMax = Math.max(now.max, next.cost);

                if (newDist < dist[next.node] || // 거리가 더 짧거나
                        (newDist == dist[next.node] && newMax < maxCost[next.node])) { // 같아도 초고값 작으면 갱신
                    dist[next.node] = newDist;
                    maxCost[next.node] = newMax;
                    pq.add(new Edge(0, next.node, newDist, newMax));
                }
            }
        }

//        System.out.println(Arrays.toString(dist));
//        System.out.println(Arrays.toString(maxCost));

        return (maxCost[mCity] == Integer.MAX_VALUE) ? -1 : maxCost[mCity];
    }
}

// 미리 계산하기
class AncientCommunication_UserSolution3 {

    static class Edge implements Comparable<Edge>{
        int id, node, cost, max;
        boolean alive = true;

        Edge(int id, int node, int cost, int max){
            this.id = id;
            this.node = node;
            this.cost = cost;
            this.max = max;
        }

        public int compareTo(Edge o){
            return this.cost - o.cost;
        }
    }

    static List<Edge>[] graph;
    static Map<Integer, Edge> map;
    static int n, s;
    static PriorityQueue<Edge> pq = new PriorityQueue<>();
    static int[] dist, mx;

    boolean better(int nd, int nm, int od, int om) {
        return nd < od || (nd == od && nm < om);
    }

    void rebuild() {
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(mx, Integer.MAX_VALUE);
        pq.clear();
        pq.add(new Edge(0, s, 0, 0));
        dist[s] = 0;
        mx[s] = 0;

        while (!pq.isEmpty()){
            Edge now = pq.poll();
            if (dist[now.node] < now.cost) continue;

            for (Edge next : graph[now.node]){
                if (!next.alive) continue;
                int newDist = now.cost + next.cost;
                int newMax = Math.max(now.max, next.cost);

                if (better(newDist, newMax, dist[next.node], mx[next.node])) { // 같아도 초고값 작으면 갱신
                    dist[next.node] = newDist;
                    mx[next.node] = newMax;
                    pq.add(new Edge(0, next.node, newDist, newMax));
                }
            }
        }
    }

    public void init(int N, int mCapital, int K, int mId[], int sCity[], int eCity[], int mDistance[]) {
        n = N;
        s = mCapital;
        graph = new List[n];
        map = new HashMap<>(K * 2);
        dist = new int[n];
        mx = new int[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int i = 0; i < K; i++) {
            int id = mId[i];
            int s = sCity[i];
            int e = eCity[i];
            int cost = mDistance[i];
            Edge edge = new Edge(id, e, cost, 0);
            map.put(id, edge);
            graph[s].add(edge);
        }
        rebuild();
    }

    void propagate(int start, int startDist, int startMax){
        pq.clear();
        dist[start] = startDist;
        mx[start] = startMax;
        pq.add(new Edge(0, start, startDist, startMax));
        while (!pq.isEmpty()){
            Edge now = pq.poll();
            if (dist[now.node] < now.cost) continue;

            for (Edge next : graph[now.node]){
                if (!next.alive) continue;
                int newDist = now.cost + next.cost;
                int newMax = Math.max(now.max, next.cost);

                if (better(newDist, newMax, dist[next.node], mx[next.node])) { // 같아도 초고값 작으면 갱신
                    dist[next.node] = newDist;
                    mx[next.node] = newMax;
                    pq.add(new Edge(0, next.node, newDist, newMax));
                }
            }
        }
    }

    public void add(int mId, int sCity, int eCity, int mDistance) {
        Edge edge = new Edge(mId, eCity, mDistance, 0);
        graph[sCity].add(edge);
        map.put(mId, edge);

        if (dist[sCity] == Integer.MAX_VALUE) return;
        int nd = dist[sCity] + mDistance;
        int nm = Math.max(mx[sCity], mDistance);

        if (better(nd, nm, dist[eCity], mx[eCity])) {
            propagate(eCity, nd, nm);
        }
    }

    public void remove(int mId) {
        map.get(mId).alive = false;
        rebuild();
    }

    public int calculate(int mCity) {

        return (mx[mCity] == Integer.MAX_VALUE) ? -1 : mx[mCity];
    }
}

// 미리 계산하기 최적화
class AncientCommunication_UserSolution4 {

    static class Edge implements Comparable<Edge>{
        int id, start, node, cost, max;
        boolean alive = true;

        Edge(int id, int start, int node, int cost, int max){
            this.id = id;
            this.start = start;
            this.node = node;
            this.cost = cost;
            this.max = max;
        }

        public int compareTo(Edge o){
            return this.cost - o.cost;
        }
    }

    static List<Edge>[] graph;
    static Map<Integer, Edge> map;
    static int n, s;
    static PriorityQueue<Edge> pq = new PriorityQueue<>();
    static int[] dist, mx;

    boolean better(int nd, int nm, int od, int om) {
        return nd < od || (nd == od && nm < om);
    }

    void rebuild() {
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(mx, Integer.MAX_VALUE);
        pq.clear();
        pq.add(new Edge(0, 0, s, 0, 0));
        dist[s] = 0;
        mx[s] = 0;

        while (!pq.isEmpty()){
            Edge now = pq.poll();
            if (dist[now.node] < now.cost) continue;

            for (Edge next : graph[now.node]){
                if (!next.alive) continue;
                int newDist = now.cost + next.cost;
                int newMax = Math.max(now.max, next.cost);

                if (better(newDist, newMax, dist[next.node], mx[next.node])) { // 같아도 초고값 작으면 갱신
                    dist[next.node] = newDist;
                    mx[next.node] = newMax;
                    pq.add(new Edge(0, 0,  next.node, newDist, newMax));
                }
            }
        }
    }

    public void init(int N, int mCapital, int K, int mId[], int sCity[], int eCity[], int mDistance[]) {
        n = N;
        s = mCapital;
        graph = new List[n];
        map = new HashMap<>(K * 2);
        dist = new int[n];
        mx = new int[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int i = 0; i < K; i++) {
            int id = mId[i];
            int s = sCity[i];
            int e = eCity[i];
            int cost = mDistance[i];
            Edge edge = new Edge(id, s, e, cost, 0);
            map.put(id, edge);
            graph[s].add(edge);
        }
        rebuild();
    }

    void propagate(int start, int startDist, int startMax){
        pq.clear();
        dist[start] = startDist;
        mx[start] = startMax;
        pq.add(new Edge(0, 0, start, startDist, startMax));
        while (!pq.isEmpty()){
            Edge now = pq.poll();
            if (dist[now.node] < now.cost) continue;

            for (Edge next : graph[now.node]){
                if (!next.alive) continue;
                int newDist = now.cost + next.cost;
                int newMax = Math.max(now.max, next.cost);

                if (better(newDist, newMax, dist[next.node], mx[next.node])) { // 같아도 초고값 작으면 갱신
                    dist[next.node] = newDist;
                    mx[next.node] = newMax;
                    pq.add(new Edge(0, 0, next.node, newDist, newMax));
                }
            }
        }
    }

    public void add(int mId, int sCity, int eCity, int mDistance) {
        Edge edge = new Edge(mId, sCity, eCity, mDistance, 0);
        graph[sCity].add(edge);
        map.put(mId, edge);

        if (dist[sCity] == Integer.MAX_VALUE) return;
        int nd = dist[sCity] + mDistance;
        int nm = Math.max(mx[sCity], mDistance);

        if (better(nd, nm, dist[eCity], mx[eCity])) {
            propagate(eCity, nd, nm);
        }
    }

    public void remove(int mId) {
        Edge e = map.get(mId);

        graph[e.start].remove(e);
        if (dist[e.start] != Integer.MAX_VALUE &&
                dist[e.start] + e.cost == dist[e.node] &&
                mx[e.node] == Math.max(mx[e.start], e.cost)) {
            rebuild();
        }
    }

    public int calculate(int mCity) {

        return (mx[mCity] == Integer.MAX_VALUE) ? -1 : mx[mCity];
    }
}
