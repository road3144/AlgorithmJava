package swea.btype;

import java.util.*;

public class MarathonCourse_25000 {

    private static MarathonCourse_UserSolution2 usersolution = new MarathonCourse_UserSolution2();

    private final static int CMD_INIT   = 100;
    private final static int CMD_ADD	= 200;
    private final static int CMD_REMOVE	= 300;
    private final static int CMD_GETLEN	= 400;

    private static boolean run(Scanner sc)
    {
        int id[] = new int[10];
        int sa[] = new int[10];
        int sb[] = new int[10];
        int len[] = new int[10];
        String strTmp;

        boolean ok = false;

        int Q = sc.nextInt();

        for (int q = 0; q < Q; q++) {
            int cmd = sc.nextInt();

            if (cmd == CMD_INIT) {
                int N = sc.nextInt();
                usersolution.init(N);
                ok = true;
            } else if (cmd == CMD_ADD) {
                strTmp = sc.next();
                int K = sc.nextInt();
                for (int i = 0; i < K; i++) {
                    strTmp = sc.next();
                    id[i] = sc.nextInt();
                    strTmp = sc.next();
                    sa[i] = sc.nextInt();
                    strTmp = sc.next();
                    sb[i] = sc.nextInt();
                    strTmp = sc.next();
                    len[i] = sc.nextInt();
                }
                usersolution.addRoad(K, id, sa, sb, len);
            } else if (cmd == CMD_REMOVE) {
                strTmp = sc.next();
                id[0] = sc.nextInt();
                usersolution.removeRoad(id[0]);
            }
            else if (cmd == CMD_GETLEN) {
                strTmp = sc.next();
                id[0] = sc.nextInt();
                int ret = usersolution.getLength(id[0]);
                strTmp = sc.next();
                int ans = sc.nextInt();
                if (ret != ans) {
                    ok = false;
                }
            }
            else ok = false;
        }
        return ok;
    }

    public static void main(String[] args) throws Exception {

        System.setIn(new java.io.FileInputStream("./BType/MarathonCourse/sample_input.txt"));

        Scanner sc = new Scanner(System.in);

        int T = sc.nextInt();
        int MARK = sc.nextInt();

        for (int tc = 1; tc <= T; tc++) {
            int score = run(sc) ? MARK : 0;
            System.out.println("#" + tc + " " + score);
        }

        sc.close();
    }
}


class MarathonCourse_UserSolution {

    static class Road{
        int id, start, end, len;

        Road(int id, int start, int end, int len){
            this.id = id;
            this.start = start;
            this.end = end;
            this.len = len;
        }
    }

    static class PathInfo{
        int[] path;
        int len;

        PathInfo(int[] path, int len){
            this.path = path;
            this.len = len;
        }
    }

    static Map<Integer, Road> roadMap;
    static List<Road>[] graph;
    static List<PathInfo>[] pathInfos;
    static int n;
    static Set<Integer> used = new HashSet<>();

    void init(int N) {
        graph = new List[N+1];
        n = N;
        roadMap = new HashMap<>();
        pathInfos = new ArrayList[N+1];
        for (int i = 1; i < N + 1; i++) {
            graph[i] = new ArrayList<>();
            pathInfos[i] = new ArrayList<>();
        }
    }

    void addRoad(int K, int mID[], int mSpotA[], int mSpotB[], int mLen[]) {
        for (int i = 0; i < K; i++) {
            Road r = new Road(mID[i], mSpotA[i], mSpotB[i], mLen[i]);
            graph[r.start].add(r);
            graph[r.end].add(r);
            roadMap.put(r.id, r);
        }
    }

    void removeRoad(int mID) {
        if (!roadMap.containsKey(mID)) return;
        Road r = roadMap.get(mID);
        graph[r.start].remove(r);
        graph[r.end].remove(r);
        roadMap.remove(mID);
    }

    int getLength(int mSpot) {
        for (int i = 1; i < n+1; i++) {
            pathInfos[i].clear();
        }
        int[] path = new int[5];
        path[0] = 0;
        used.clear();

        dfs(mSpot, 1, mSpot, 0, path);

        int ans = 0;
        for (int i = 1; i < n + 1; i++) {
            if (pathInfos[i].size() < 2) continue;

            for (PathInfo a : pathInfos[i]){
                for (PathInfo b : pathInfos[i]){
                    if (disjoint(a.path, b.path) && a.len + b.len <= 42195){
                        ans = Math.max(ans, a.len + b.len);
                    }
                }
            }
        }
        return (ans == 0) ? -1 : ans;
    }

    boolean disjoint(int[] a, int[] b){
        for (int i = 1; i < 5; i++) {
            for (int j = 1; j < 5; j++) {
                if (a[i] == b[j]) return false;
            }
        }
        return true;
    }

    void dfs(int start, int depth, int node, int total, int[] path){
        if (depth == 5){
            PathInfo p = new PathInfo(path.clone(), total);
            pathInfos[node].add(p);
            return;
        }

        for (Road r : graph[node]){
            int next = (r.start != node) ? r.start : r.end;
            if (!used.contains(r.id) && next != start) {
                path[depth] = r.id;
                used.add(r.id);
                dfs(start, depth + 1, next, total + r.len, path);
                path[depth] = 0;
                used.remove(r.id);
            }
        }
    }
}

// 최적화
class MarathonCourse_UserSolution2 {

    static class Road{
        int id, start, end, len;

        Road(int id, int start, int end, int len){
            this.id = id;
            this.start = start;
            this.end = end;
            this.len = len;
        }
    }

    static class PathInfo{
        int[] path;
        int len;

        PathInfo(int[] path, int len){
            this.path = path;
            this.len = len;
        }
    }

    static Map<Integer, Road> roadMap;
    static List<Road>[] graph;
    static List<PathInfo>[] pathInfos;
    static int n;

    void init(int N) {
        graph = new List[N+1];
        n = N;
        roadMap = new HashMap<>();
        pathInfos = new ArrayList[N+1];
        for (int i = 1; i < N + 1; i++) {
            graph[i] = new ArrayList<>();
            pathInfos[i] = new ArrayList<>();
        }
    }

    void addRoad(int K, int mID[], int mSpotA[], int mSpotB[], int mLen[]) {
        for (int i = 0; i < K; i++) {
            Road r = new Road(mID[i], mSpotA[i], mSpotB[i], mLen[i]);
            graph[r.start].add(r);
            graph[r.end].add(r);
            roadMap.put(r.id, r);
        }
    }

    void removeRoad(int mID) {
        if (!roadMap.containsKey(mID)) return;
        Road r = roadMap.get(mID);
        graph[r.start].remove(r);
        graph[r.end].remove(r);
        roadMap.remove(mID);
    }

    int getLength(int mSpot) {
        for (int i = 1; i < n+1; i++) {
            pathInfos[i].clear();
        }
        int[] path = new int[5];
        path[0] = 0;

        dfs(mSpot, 1, mSpot, 0, path);

        int ans = 0;
        for (int i = 1; i < n + 1; i++) {
            if (pathInfos[i].size() < 2) continue;

            for (int x = 0; x < pathInfos[i].size(); x++) {
                PathInfo a = pathInfos[i].get(x);
                for (int y = x + 1; y < pathInfos[i].size(); y++) {
                    PathInfo b = pathInfos[i].get(y);
                    if (disjoint(a.path, b.path)) {
                        int sum = a.len + b.len;
                        if (sum <= 42195) ans = Math.max(ans, sum);
                    }
                }
            }
        }
        return (ans == 0) ? -1 : ans;
    }

    boolean disjoint(int[] a, int[] b){
        for (int i = 1; i < 5; i++) {
            for (int j = 1; j < 5; j++) {
                if (a[i] == b[j]) return false;
            }
        }
        return true;
    }

    void dfs(int start, int depth, int node, int total, int[] path){
        if (depth == 5){
            PathInfo p = new PathInfo(path.clone(), total);
            pathInfos[node].add(p);
            return;
        }

        for (Road r : graph[node]){
            int next = (r.start != node) ? r.start : r.end;
            if (!isUsed(path, depth, r.id) && next != start) {
                path[depth] = r.id;
                dfs(start, depth + 1, next, total + r.len, path);
                path[depth] = 0;
            }
        }
    }

    boolean isUsed(int[] path, int depth, int id){
        for (int i = 1; i < depth; i++) {
            if (path[i] == id) return true;
        }
        return false;
    }
}
