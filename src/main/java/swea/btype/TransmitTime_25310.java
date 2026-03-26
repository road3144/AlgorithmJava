package swea.btype;

import java.util.*;

public class TransmitTime_25310 {

    private static Scanner sc;
    private static TransmitTime_UserSolution3 usersolution = new TransmitTime_UserSolution3();

    private final static int CMD_INIT   = 0;
    private final static int CMD_ADD    = 1;
    private final static int CMD_REMOVE = 2;
    private final static int CMD_CHECK  = 3;

    private final static int MAX_LINE = 30000;

    private static int nodeA[] = new int[MAX_LINE];
    private static int nodeB[] = new int[MAX_LINE];
    private static int Time[] = new int[MAX_LINE];

    private static boolean run() throws Exception
    {
        int cmd, N, K;
        int ans, ret;

        boolean ok = false;

        int Q = sc.nextInt();
        for (int q = 0; q < Q; q++) {
            cmd = sc.nextInt();

            if (cmd == CMD_INIT) {
                N = sc.nextInt();
                K = sc.nextInt();
                for (int i = 0; i < K; i++) {
                    nodeA[i] = sc.nextInt();
                    nodeB[i] = sc.nextInt();
                    Time[i] = sc.nextInt();
                }
                usersolution.init(N, K, nodeA, nodeB, Time);
                ok = true;
            } else if (cmd == CMD_ADD) {
                nodeA[0] = sc.nextInt();
                nodeB[0] = sc.nextInt();
                Time[0] = sc.nextInt();
                usersolution.addLine(nodeA[0], nodeB[0], Time[0]);
            } else if (cmd == CMD_REMOVE) {
                nodeA[0] = sc.nextInt();
                nodeB[0] = sc.nextInt();
                usersolution.removeLine(nodeA[0], nodeB[0]);
            } else if (cmd == CMD_CHECK) {
                nodeA[0] = sc.nextInt();
                nodeB[0] = sc.nextInt();
                ans = sc.nextInt();
                ret = usersolution.checkTime(nodeA[0], nodeB[0]);
                if (ans != ret) {
                    ok = false;
                }
            }
            else ok = false;
        }
        return ok;
    }

    public static void main(String[] args) throws Exception {

        System.setIn(new java.io.FileInputStream("./BType/TransmitTime/sample_input.txt"));
        sc = new Scanner(System.in);

        int T = sc.nextInt();
        int MARK = sc.nextInt();

        for (int tc = 1; tc <= T; tc++) {
            int score = run() ? MARK : 0;
            System.out.println("#" + tc + " " + score);
        }

        sc.close();
    }
}

class TransmitTime_UserSolution {

    static class Edge implements Comparable<Edge>{
        int prev, node, cost;

        Edge(int prev, int node, int cost){
            this.prev = prev;
            this.node = node;
            this.cost = cost;
        }

        public int compareTo(Edge o){
            return this.cost - o.cost;
        }
    }

    static List<Edge>[] graph;
    static int[][] conInfo;
    static int[][][] adj, map;
    static boolean[] isChange;
    static PriorityQueue<Edge> pq = new PriorityQueue<>();
    static int n;

    public void init(int N, int K, int mNodeA[], int mNodeB[], int mTime[]) {
        n = N + 3;
        graph = new List[n+1];
        conInfo = new int[n+1][n+1];
        map = new int[n+1][31][31];
        adj = new int[n+1][31][31];
        isChange = new boolean[n+1];

        for (int i = 1; i < n+1; i++) {
            graph[i] = new ArrayList<>();
            for (int j = 0; j < 31; j++) {
                Arrays.fill(map[i][j], 1000000);
                Arrays.fill(adj[i][j], 1000000);
                map[i][j][j] = 0;
                adj[i][j][j] = 0;
            }
        }

        for (int i = 0; i < K; i++) {
            int a = mNodeA[i];
            int b = mNodeB[i];
            int cost = mTime[i];
            int groupA = (a / 100 == 0) ? a : a/100 + 3;
            int relativeA = a % 100;
            int groupB = (b / 100 == 0) ? b : b/100 + 3;
            int relativeB = b % 100;

            // 그룹 다르면 그룹 연결 정보 추가
            if (groupA != groupB){
                graph[groupA].add(new Edge(0, groupB, cost));
                graph[groupB].add(new Edge(0, groupA, cost));
                conInfo[groupA][groupB] = relativeA;
                conInfo[groupB][groupA] = relativeB;
            }else { // 같으면 그룹내 맵 경신
                adj[groupA][relativeA][relativeB] = cost;
                adj[groupA][relativeB][relativeA] = cost;
            }

        }

        // 각 그룹내 노드간 최단 거리 계산
        for (int i = 4; i < n+1; i++) {
            floyd(i);
        }

    }

    void floyd(int group){
        for (int i = 1; i <= 30; i++) {
            for (int j = 1; j <= 30; j++) {
                map[group][i][j] = adj[group][i][j];
            }
        }
        for (int k = 1; k < 31; k++) {
            for (int x = 1; x < 31; x++) {
                for (int y = 1; y < 31; y++) {
                    if (x == y) continue;
                    if (map[group][x][k] + map[group][k][y] < map[group][x][y])
                        map[group][x][y] = map[group][x][k] + map[group][k][y];
                }
            }
        }
        isChange[group] = false;
    }

    public void addLine(int mNodeA, int mNodeB, int mTime) {
        int a = mNodeA;
        int b = mNodeB;
        int cost = mTime;
        int groupA = (a / 100 == 0) ? a : a/100 + 3;
        int relativeA = a % 100;
        int groupB = (b / 100 == 0) ? b : b/100 + 3;
        int relativeB = b % 100;

        // 그룹 다르면 그룹 연결 정보 추가
        if (groupA != groupB){
            graph[groupA].add(new Edge(0, groupB, cost));
            graph[groupB].add(new Edge(0, groupA, cost));
            conInfo[groupA][groupB] = relativeA;
            conInfo[groupB][groupA] = relativeB;
        }else { // 같으면 그룹내 맵 경신
            adj[groupA][relativeA][relativeB] = cost;
            adj[groupA][relativeB][relativeA] = cost;
            isChange[groupA] = true;
        }
    }

    public void removeLine(int mNodeA, int mNodeB) {
        int a = mNodeA;
        int b = mNodeB;
        int groupA = (a / 100 == 0) ? a : a/100 + 3;
        int relativeA = a % 100;
        int groupB = (b / 100 == 0) ? b : b/100 + 3;
        int relativeB = b % 100;

        // 그룹 다르면 그룹 연결 정보 해제
        if (groupA != groupB){
            Edge target = null;
            for (Edge e : graph[groupA]){
                if (e.node == groupB){
                    target = e;
                    break;
                }
            }
            graph[groupA].remove(target);
            for (Edge e : graph[groupB]){
                if (e.node == groupA){
                    target = e;
                    break;
                }
            }
            graph[groupB].remove(target);

            conInfo[groupA][groupB] = 0;
            conInfo[groupB][groupA] = 0;
        }else { // 같으면 그룹내 맵 경신
            adj[groupA][relativeA][relativeB] = 1000000;
            adj[groupA][relativeB][relativeA] = 1000000;
            isChange[groupA] = true;
        }
    }

    public int checkTime(int mNodeA, int mNodeB) {
        int[][] dist = new int[n+1][31];
        for (int i = 0; i < n + 1; i++) {
            Arrays.fill(dist[i], Integer.MAX_VALUE);
        }

        pq.clear();
        dist[mNodeA][0] = 0;

        pq.add(new Edge(0, mNodeA, 0));

        while (!pq.isEmpty()){
            Edge now = pq.poll();
            int enter = conInfo[now.node][now.prev];
            if (dist[now.node][enter] < now.cost) continue;
            if (now.node == mNodeB) return now.cost;

            if (now.node >= 4 && isChange[now.node]) {
                floyd(now.node);
            }

            for (Edge next : graph[now.node]){
                int groupCnt = 0;
                if (4 <= now.node){
                    int start = enter;
                    int end = conInfo[now.node][next.node];
                    if (map[now.node][start][end] >= 1000000) continue;
                    groupCnt += map[now.node][start][end];
                }
                int total = now.cost + groupCnt + next.cost;
                int nextEnter = 0;
                if (next.node >= 4) {
                    nextEnter = conInfo[next.node][now.node]; // [수정] next 그룹에 어떤 대표 노드로 들어가는지
                }

                if (dist[next.node][nextEnter] < total) continue;
                dist[next.node][nextEnter] = total;
                pq.add(new Edge(now.node, next.node, total));
            }
        }

        return -1;
    }
}

class TransmitTime_UserSolution2 {

    static final int INF = 1_000_000_000;

    static class Edge implements Comparable<Edge> {
        int node, cost;

        Edge(int node, int cost) {
            this.node = node;
            this.cost = cost;
        }

        @Override
        public int compareTo(Edge o) {
            return this.cost - o.cost;
        }
    }

    static int n;
    static int groupCnt;
    static int[][][] adj;
    static int[][][] repDist;
    static int[][] outer;
    static int totalNodeCnt;
    static PriorityQueue<Edge> pq = new PriorityQueue<>();

    // 그룹 g의 대표 rep(1~3)를 전역 정점 번호로 변환
    static int getRepNode(int g, int rep) {
        return 3 + (g - 4) * 3 + rep;
    }

    // 원래 node 번호 -> group index
    static int getGroup(int node) {
        return (node / 100 == 0) ? node : node / 100 + 3;
    }

    // 원래 node 번호 -> group 내부 상대 번호
    static int getRelative(int node) {
        return node % 100;
    }

    void calcRepDist(int group) {
        for (int s = 1; s <= 3; s++) {
            int[] dist = new int[31];
            Arrays.fill(dist, INF);

            PriorityQueue<Edge> innerPQ = new PriorityQueue<>();
            dist[s] = 0;
            innerPQ.add(new Edge(s, 0));

            while (!innerPQ.isEmpty()) {
                Edge now = innerPQ.poll();
                if (dist[now.node] < now.cost) continue;

                for (int next = 1; next <= 30; next++) {
                    if (adj[group][now.node][next] >= INF) continue;
                    int total = now.cost + adj[group][now.node][next];
                    if (dist[next] <= total) continue;
                    dist[next] = total;
                    innerPQ.add(new Edge(next, total));
                }
            }

            for (int e = 1; e <= 3; e++) {
                repDist[group][s][e] = dist[e];
            }
        }
    }

    void connectOuter(int u, int v, int cost) {
        outer[u][v] = cost;
        outer[v][u] = cost;
    }

    void disconnectOuter(int u, int v) {
        outer[u][v] = INF;
        outer[v][u] = INF;
    }

    public void init(int N, int K, int mNodeA[], int mNodeB[], int mTime[]) {
        groupCnt = N;
        n = N + 3;

        adj = new int[n + 1][31][31];
        repDist = new int[n + 1][4][4];

        totalNodeCnt = 3 + N * 3;
        outer = new int[totalNodeCnt + 1][totalNodeCnt + 1];

        for (int i = 1; i <= totalNodeCnt; i++) {
            Arrays.fill(outer[i], INF);
            outer[i][i] = 0;
        }

        for (int g = 4; g <= n; g++) {
            for (int i = 1; i <= 30; i++) {
                Arrays.fill(adj[g][i], INF);
                adj[g][i][i] = 0;
            }
            for (int i = 1; i <= 3; i++) {
                Arrays.fill(repDist[g][i], INF);
                repDist[g][i][i] = 0;
            }
        }

        for (int i = 0; i < K; i++) {
            int a = mNodeA[i];
            int b = mNodeB[i];
            int cost = mTime[i];

            int groupA = getGroup(a);
            int groupB = getGroup(b);
            int relativeA = getRelative(a);
            int relativeB = getRelative(b);

            if (groupA != groupB) {
                int u = (groupA <= 3) ? groupA : getRepNode(groupA, relativeA);
                int v = (groupB <= 3) ? groupB : getRepNode(groupB, relativeB);
                connectOuter(u, v, cost);
            } else {
                adj[groupA][relativeA][relativeB] = cost;
                adj[groupA][relativeB][relativeA] = cost;
            }
        }

        for (int g = 4; g <= n; g++) {
            calcRepDist(g);
            for (int s = 1; s <= 3; s++) {
                for (int e = 1; e <= 3; e++) {
                    if (s == e) continue;
                    if (repDist[g][s][e] >= INF) continue;
                    connectOuter(getRepNode(g, s), getRepNode(g, e), repDist[g][s][e]);
                }
            }
        }
    }

    void refreshGroup(int group) {
        for (int s = 1; s <= 3; s++) {
            for (int e = 1; e <= 3; e++) {
                if (s == e) continue;
                disconnectOuter(getRepNode(group, s), getRepNode(group, e));
            }
        }

        calcRepDist(group);

        for (int s = 1; s <= 3; s++) {
            for (int e = 1; e <= 3; e++) {
                if (s == e) continue;
                if (repDist[group][s][e] >= INF) continue;
                connectOuter(getRepNode(group, s), getRepNode(group, e), repDist[group][s][e]);
            }
        }
    }

    public void addLine(int mNodeA, int mNodeB, int mTime) {
        int a = mNodeA;
        int b = mNodeB;
        int cost = mTime;

        int groupA = getGroup(a);
        int groupB = getGroup(b);
        int relativeA = getRelative(a);
        int relativeB = getRelative(b);

        if (groupA != groupB) {
            int u = (groupA <= 3) ? groupA : getRepNode(groupA, relativeA);
            int v = (groupB <= 3) ? groupB : getRepNode(groupB, relativeB);
            connectOuter(u, v, cost);
        } else {
            adj[groupA][relativeA][relativeB] = cost;
            adj[groupA][relativeB][relativeA] = cost;
            refreshGroup(groupA);
        }
    }

    public void removeLine(int mNodeA, int mNodeB) {
        int a = mNodeA;
        int b = mNodeB;

        int groupA = getGroup(a);
        int groupB = getGroup(b);
        int relativeA = getRelative(a);
        int relativeB = getRelative(b);

        if (groupA != groupB) {
            int u = (groupA <= 3) ? groupA : getRepNode(groupA, relativeA);
            int v = (groupB <= 3) ? groupB : getRepNode(groupB, relativeB);
            disconnectOuter(u, v);
        } else {
            adj[groupA][relativeA][relativeB] = INF;
            adj[groupA][relativeB][relativeA] = INF;
            refreshGroup(groupA);
        }
    }

    public int checkTime(int mNodeA, int mNodeB) {
        int[] dist = new int[totalNodeCnt + 1];
        Arrays.fill(dist, INF);

        pq.clear();
        dist[mNodeA] = 0;
        pq.add(new Edge(mNodeA, 0));

        while (!pq.isEmpty()) {
            Edge now = pq.poll();
            if (dist[now.node] < now.cost) continue;
            if (now.node == mNodeB) return now.cost;

            for (int next = 1; next <= totalNodeCnt; next++) {
                if (outer[now.node][next] >= INF) continue;

                int total = now.cost + outer[now.node][next];
                if (dist[next] <= total) continue;

                dist[next] = total;
                pq.add(new Edge(next, total));
            }
        }

        return -1;
    }
}


class TransmitTime_UserSolution3 {

    static class Edge implements Comparable<Edge>{
        int prev, node, cost;

        Edge(int prev, int node, int cost){
            this.prev = prev;
            this.node = node;
            this.cost = cost;
        }

        public int compareTo(Edge o){
            return this.cost - o.cost;
        }
    }

    static List<Edge>[] graph;
    static int[][] conInfo;
    static int[][][] adj, map;
    static boolean[] isChange;
    static PriorityQueue<Edge> pq = new PriorityQueue<>();
    static int n;
    static int[][] dist;
    static int[][] seen;
    static int curStamp;

    public void init(int N, int K, int mNodeA[], int mNodeB[], int mTime[]) {
        n = N + 3;
        graph = new List[n+1];
        conInfo = new int[n+1][n+1];
        map = new int[n+1][31][31];
        adj = new int[n+1][31][31];
        isChange = new boolean[n+1];
        dist = new int[n + 1][31];
        seen = new int[n + 1][31];
        curStamp = 0;

        for (int i = 1; i < n+1; i++) {
            graph[i] = new ArrayList<>();
            for (int j = 0; j < 31; j++) {
                Arrays.fill(map[i][j], 1000000);
                Arrays.fill(adj[i][j], 1000000);
                map[i][j][j] = 0;
                adj[i][j][j] = 0;
            }
        }

        for (int i = 0; i < K; i++) {
            int a = mNodeA[i];
            int b = mNodeB[i];
            int cost = mTime[i];
            int groupA = (a / 100 == 0) ? a : a/100 + 3;
            int relativeA = a % 100;
            int groupB = (b / 100 == 0) ? b : b/100 + 3;
            int relativeB = b % 100;

            // 그룹 다르면 그룹 연결 정보 추가
            if (groupA != groupB){
                graph[groupA].add(new Edge(0, groupB, cost));
                graph[groupB].add(new Edge(0, groupA, cost));
                conInfo[groupA][groupB] = relativeA;
                conInfo[groupB][groupA] = relativeB;
            }else { // 같으면 그룹내 맵 경신
                adj[groupA][relativeA][relativeB] = cost;
                adj[groupA][relativeB][relativeA] = cost;
            }

        }

        // 각 그룹내 노드간 최단 거리 계산
        for (int i = 4; i < n+1; i++) {
            floyd(i);
        }

    }

    void floyd(int group){
        for (int i = 1; i <= 30; i++) {
            for (int j = 1; j <= 30; j++) {
                map[group][i][j] = adj[group][i][j];
            }
        }
        for (int k = 1; k < 31; k++) {
            for (int x = 1; x < 31; x++) {
                for (int y = 1; y < 31; y++) {
                    if (x == y) continue;
                    if (map[group][x][k] + map[group][k][y] < map[group][x][y])
                        map[group][x][y] = map[group][x][k] + map[group][k][y];
                }
            }
        }
        isChange[group] = false;
    }

    public void addLine(int mNodeA, int mNodeB, int mTime) {
        int a = mNodeA;
        int b = mNodeB;
        int cost = mTime;
        int groupA = (a / 100 == 0) ? a : a/100 + 3;
        int relativeA = a % 100;
        int groupB = (b / 100 == 0) ? b : b/100 + 3;
        int relativeB = b % 100;

        // 그룹 다르면 그룹 연결 정보 추가
        if (groupA != groupB){
            graph[groupA].add(new Edge(0, groupB, cost));
            graph[groupB].add(new Edge(0, groupA, cost));
            conInfo[groupA][groupB] = relativeA;
            conInfo[groupB][groupA] = relativeB;
        }else { // 같으면 그룹내 맵 경신
            adj[groupA][relativeA][relativeB] = cost;
            adj[groupA][relativeB][relativeA] = cost;
            isChange[groupA] = true;
        }
    }

    public void removeLine(int mNodeA, int mNodeB) {
        int a = mNodeA;
        int b = mNodeB;
        int groupA = (a / 100 == 0) ? a : a/100 + 3;
        int relativeA = a % 100;
        int groupB = (b / 100 == 0) ? b : b/100 + 3;
        int relativeB = b % 100;

        // 그룹 다르면 그룹 연결 정보 해제
        if (groupA != groupB){
            Edge target = null;
            for (Edge e : graph[groupA]){
                if (e.node == groupB){
                    target = e;
                    break;
                }
            }
            graph[groupA].remove(target);
            for (Edge e : graph[groupB]){
                if (e.node == groupA){
                    target = e;
                    break;
                }
            }
            graph[groupB].remove(target);

            conInfo[groupA][groupB] = 0;
            conInfo[groupB][groupA] = 0;
        }else { // 같으면 그룹내 맵 경신
            adj[groupA][relativeA][relativeB] = 1000000;
            adj[groupA][relativeB][relativeA] = 1000000;
            isChange[groupA] = true;
        }
    }

    public int checkTime(int mNodeA, int mNodeB) {
        curStamp++; // 이번 탐색 번호 증가

        pq.clear();

        seen[mNodeA][0] = curStamp;
        dist[mNodeA][0] = 0;

        pq.add(new Edge(0, mNodeA, 0));

        while (!pq.isEmpty()){
            Edge now = pq.poll();
            int enter = (now.node <= 3) ? 0 : conInfo[now.node][now.prev];
            // 이번 탐색에서 기록된 값이 아니면 무시
            if (seen[now.node][enter] != curStamp || dist[now.node][enter] < now.cost) continue;
            if (now.node == mNodeB) return now.cost;

            if (now.node >= 4 && isChange[now.node]) {
                floyd(now.node);
            }

            for (Edge next : graph[now.node]){
                int groupCnt = 0;
                if (4 <= now.node){
                    int start = enter;
                    int end = conInfo[now.node][next.node];
                    if (map[now.node][start][end] >= 1000000) continue;
                    groupCnt += map[now.node][start][end];
                }
                int total = now.cost + groupCnt + next.cost;
                int nextEnter = 0;
                if (next.node >= 4) {
                    nextEnter = conInfo[next.node][now.node]; // next 그룹에 어떤 대표 노드로 들어가는지
                }

                //이번 탐색에서 이미 더 좋은 값 있으면 skip
                if (seen[next.node][nextEnter] == curStamp && dist[next.node][nextEnter] <= total) continue;

                seen[next.node][nextEnter] = curStamp;
                dist[next.node][nextEnter] = total;
                pq.add(new Edge(now.node, next.node, total));
            }
        }

        return -1;
    }
}
