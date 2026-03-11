package swea.btype;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class FileDownload2_25003 {

    private static FileDownload2_UserSolution usersolution = new FileDownload2_UserSolution();

    private final static int CMD_INIT   	= 0;
    private final static int CMD_MAKENET	= 1;
    private final static int CMD_REMOVELINK	= 2;
    private final static int CMD_DOWNLOAD	= 3;
    private final static int CMD_GETSIZE	= 4;

    private final static int MAX_COM = 1000;
    private final static int MAX_ONEFILE = 50;

    private static int fileCnt[] = new int[MAX_COM];
    private static int fileID[][] = new int[MAX_COM][MAX_ONEFILE];
    private static int fileSize[][] = new int[MAX_COM][MAX_ONEFILE];

    private static int linkID[] = new int[MAX_COM];
    private static int linkA[] = new int[MAX_COM];
    private static int linkB[] = new int[MAX_COM];
    private static int linkDis[] = new int[MAX_COM];

    private static int oriFileID[] = new int[500];
    private static int oriFileSize[] = new int[500];

    private static boolean run(BufferedReader br) throws Exception
    {
        int N, K, time, idx, comA, comB, dis;
        int ret, ans;

        boolean ok = false;

        StringTokenizer st = new StringTokenizer(br.readLine(), " ");
        int Q = Integer.parseInt(st.nextToken());

        for (int q = 0; q < Q; q++) {
            st = new StringTokenizer(br.readLine(), " ");
            int cmd = Integer.parseInt(st.nextToken());

            if (cmd == CMD_INIT) {
                N = Integer.parseInt(st.nextToken());
                K = Integer.parseInt(st.nextToken());
                for (int i = 0; i < K; i++) {
                    st = new StringTokenizer(br.readLine(), " ");
                    oriFileID[i] = Integer.parseInt(st.nextToken());
                    oriFileSize[i] = Integer.parseInt(st.nextToken());
                }
                for (int i = 0; i < N; i++) {
                    st = new StringTokenizer(br.readLine(), " ");
                    fileCnt[i] = Integer.parseInt(st.nextToken());
                    for (int k = 0; k < fileCnt[i]; k++) {
                        idx = Integer.parseInt(st.nextToken());
                        fileID[i][k] = oriFileID[idx];
                        fileSize[i][k] = oriFileSize[idx];
                    }
                }
                usersolution.init(N, fileCnt, fileID, fileSize);
                ok = true;
            } else if (cmd == CMD_MAKENET) {
                K = Integer.parseInt(st.nextToken());
                for (int i = 0; i < K; i++) {
                    st = new StringTokenizer(br.readLine(), " ");
                    linkID[i] = Integer.parseInt(st.nextToken());
                    linkA[i] = Integer.parseInt(st.nextToken());
                    linkB[i] = Integer.parseInt(st.nextToken());
                    linkDis[i] = Integer.parseInt(st.nextToken());
                }
                usersolution.makeNet(K, linkID, linkA, linkB, linkDis);
            } else if (cmd == CMD_REMOVELINK) {
                time = Integer.parseInt(st.nextToken());
                idx = Integer.parseInt(st.nextToken());
                usersolution.removeLink(time, idx);
            }
            else if (cmd == CMD_DOWNLOAD) {
                time = Integer.parseInt(st.nextToken());
                comA = Integer.parseInt(st.nextToken());
                idx = Integer.parseInt(st.nextToken());
                ans = Integer.parseInt(st.nextToken());

                ret = usersolution.downloadFile(time, comA, idx);
                if (ret != ans) {
                    ok = false;
                }
            }
            else if (cmd == CMD_GETSIZE) {
                time = Integer.parseInt(st.nextToken());
                comA = Integer.parseInt(st.nextToken());
                idx = Integer.parseInt(st.nextToken());
                ans = Integer.parseInt(st.nextToken());

                ret = usersolution.getFileSize(time, comA, idx);
                if (ret != ans) {
                    ok = false;
                }
            }
            else ok = false;
        }
        return ok;
    }

    public static void main(String[] args) throws Exception {

        //System.setIn(new java.io.FileInputStream("res/sample_input.txt"));

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer line = new StringTokenizer(br.readLine(), " ");

        int T = Integer.parseInt(line.nextToken());
        int MARK = Integer.parseInt(line.nextToken());

        for (int tc = 1; tc <= T; tc++) {
            int score = run(br) ? MARK : 0;
            System.out.println("#" + tc + " " + score);
        }

        br.close();
    }
}


class FileDownload2_UserSolution {

    static class File{
        int id;
        int size;

        File(int id, int size){
            this.id = id;
            this.size = size;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            File file = (File) o;
            return id == file.id;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(id);
        }
    }

    static class Link{
        int id;
        int s, e;
        int cost;

        Link(int id, int s, int e, int cost){
            this.id = id;
            this.s = s;
            this.e = e;
            this.cost = cost;
        }
    }

    static class Download implements Comparable<Download> {
        List<Set<Link>> routes;
        int fileId;
        int time;
        int cost;

        Download(List<Set<Link>> routes, int fileId, int time, int cost){
            this.routes = routes;
            this.fileId = fileId;
            this.time = time;
            this.cost = cost;
        }

        @Override
        public int compareTo(Download o){
            return Integer.compare(this.time, o.time);
        }
    }

    static Set<Integer>[] hasMap;
    static List<Link>[] graph;
    static Queue<Download>[] downloads;
    static Queue<int[]>[] removes; // 0: id, 1: time
    static Map<Integer, File> fileMap;
    static Map<Integer, Link> linkMap;
    static int n;

    void init(int N, int mFileCnt[], int mFileID[][], int mFileSize[][]) {
        graph = new List[N+1];
        hasMap = new Set[N+1];
        fileMap = new HashMap<>();
        linkMap = new HashMap<>();
        downloads = new Queue[N+1];
        removes = new Queue[N+1];
        n = N;

        for (int i = 1; i < N+1; i++) {
            graph[i] = new ArrayList<>();
            hasMap[i] = new HashSet<>();
            downloads[i] = new ArrayDeque<>();
            removes[i] = new ArrayDeque<>();
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < mFileCnt[i]; j++) {
                if(!fileMap.containsKey(mFileID[i][j]))
                    fileMap.put(mFileID[i][j], new File(mFileID[i][j], mFileSize[i][j]));
                hasMap[i].add(mFileID[i][j]);
            }
        }
    }

    void makeNet(int K, int mID[], int mComA[], int mComB[], int mDis[]) {
        for (int i = 0; i < K; i++) {
            linkMap.put(mID[i], new Link(mID[i], mComA[i], mComB[i], mDis[i]));
            graph[mComA[i]].add(new Link(mID[i], 0, mComB[i], mDis[i]));
            graph[mComB[i]].add(new Link(mID[i], 0, mComA[i], mDis[i]));
        }
    }

    void removeLink(int mTime, int mID) {
        Link removed = linkMap.get(mID);

        while (!downloads[removed.s].isEmpty()){
            Download now = downloads[removed.s].poll();

            if (now.time < mTime) break;
            // 이미 지난거 처리
            if (now.time + now.cost <= mTime){
                hasMap[removed.s].add(now.fileId);
            } else {
                int origin = now.routes.size();

                for (Set<Link> route : now.routes){
                    if(route.contains(linkMap.get(mID))){
                        now.routes.remove(route);
                    }
                }

                if (!now.routes.isEmpty()){
                    now.cost = (int) Math.ceil((double) (fileMap.get(now.fileId).size - (mTime - now.time) * 9) / now.routes.size());
                }
            }

        }

        linkMap.remove(removed.id);
    }

    int downloadFile(int mTime, int mComA, int mFileID) {
        List<Set<Link>> routes = new ArrayList<>();
        dfs(routes, new HashSet<>(), mComA, 0, mFileID);
        if (!routes.isEmpty()) {
            int cost = (int) Math.ceil((double) fileMap.get(mFileID).size / (routes.size() * 9));
            downloads[mComA].add(new Download(routes, mFileID, mTime, cost));
        }
        return routes.size();
    }

    void dfs(List<Set<Link>> routes, Set<Link> route, int com, int distance, int fileId){
        if (distance > 5) return;
        if (hasMap[com].contains(fileId)){
            routes.add(route);
            return;
        }

        for (Link next : graph[com]){
            if (distance + next.cost <= 5 && linkMap.containsKey(next.id)){
                route.add(linkMap.get(next.id));
                dfs(routes, route, next.e, distance+ next.cost, fileId);
                route.remove(linkMap.get(next.id));
            }
        }
    }

    int getFileSize(int mTime, int mComA, int mFileID) {
        Queue<Download> downloads1 = downloads[mComA];
        Queue<int[]> removes1 = removes[mComA];
        int[] nowCut = removes1.peek();
        while (!downloads[mComA].isEmpty()){
            Download now = downloads1.poll();

        }
        return 0;
    }
}
