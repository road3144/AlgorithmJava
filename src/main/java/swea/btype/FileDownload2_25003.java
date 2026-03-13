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

        System.setIn(new java.io.FileInputStream("./BType/FileDownload2/sample_input.txt"));

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

    static class Edge {
        int id;
        int to;
        int dist;

        Edge(int id, int to, int dist) {
            this.id = id;
            this.to = to;
            this.dist = dist;
        }
    }

    // start 컴퓨터 -> to 컴퓨터까지 가는 경로 (사용되는 링크들)
    static class PathInfo {
        int to;
        int[] edgeIds;

        PathInfo(int to, int[] edgeIds) {
            this.to = to;
            this.edgeIds = edgeIds;
        }
    }

    // 진행 중 다운로드 작업
    static class Job {
        int com;
        int fileId;
        long downloaded; // 현재 시각까지 다운로드된 양
        int routeCnt;    // 현재 활성 경로 수

        Job(int com, int fileId) {
            this.com = com;
            this.fileId = fileId;
            this.downloaded = 0L;
            this.routeCnt = 0;
        }
    }

    int n;
    int currentTime;

    List<Edge>[] graph;
    List<PathInfo>[] nearPaths; // 각 컴퓨터에서 거리 ≤5로 갈 수 있는 모든 컴퓨터 경로

    Map<Integer, Integer> fileSizeMap; // fileId -> size
    Set<Integer>[] hasFile;            // 완료된 파일 보유 상태
    Map<Integer, Job>[] activeByCom;   // 컴퓨터별 진행중 다운로드
    List<Job> activeJobs;              // 전체 진행중 다운로드 목록

    boolean[] aliveLink;

    static final int MAX_LINK_ID = 50000;

    void init(int N, int mFileCnt[], int mFileID[][], int mFileSize[][]) {

        n = N;
        currentTime = 0;

        graph = new ArrayList[n + 1];
        nearPaths = new ArrayList[n + 1];
        hasFile = new HashSet[n + 1];
        activeByCom = new HashMap[n + 1];
        activeJobs = new ArrayList<>();
        fileSizeMap = new HashMap<>();
        aliveLink = new boolean[MAX_LINK_ID + 1];

        for (int i = 1; i <= n; i++) {
            graph[i] = new ArrayList<>();
            nearPaths[i] = new ArrayList<>();
            hasFile[i] = new HashSet<>();
            activeByCom[i] = new HashMap<>();
        }

        // 초기 파일 정보 저장
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < mFileCnt[i]; j++) {
                int fid = mFileID[i][j];
                int fsize = mFileSize[i][j];

                fileSizeMap.put(fid, fsize);
                hasFile[i + 1].add(fid); // 컴퓨터 번호는 1부터
            }
        }
    }

    void makeNet(int K, int mID[], int mComA[], int mComB[], int mDis[]) {

        // 그래프 구성
        for (int i = 0; i < K; i++) {

            int id = mID[i];
            int a = mComA[i];
            int b = mComB[i];
            int d = mDis[i];

            graph[a].add(new Edge(id, b, d));
            graph[b].add(new Edge(id, a, d));

            aliveLink[id] = true;
        }

        // 거리 ≤5 경로 전처리
        precomputeNearPaths();
    }

    void removeLink(int mTime, int mID) {

        // 먼저 시간 진행
        advanceTime(mTime);

        // 링크 비활성화
        aliveLink[mID] = false;
    }

    int downloadFile(int mTime, int mComA, int mFileID) {

        advanceTime(mTime);

        // 네트워크에 파일 없음
        if (!fileSizeMap.containsKey(mFileID))
            return 0;

        int routes = countAvailableSources(mComA, mFileID);

        if (routes == 0)
            return 0;

        // 다운로드 작업 생성
        Job job = new Job(mComA, mFileID);
        job.routeCnt = routes;

        activeJobs.add(job);
        activeByCom[mComA].put(mFileID, job);

        return routes;
    }

    int getFileSize(int mTime, int mComA, int mFileID) {

        advanceTime(mTime);

        // 이미 완료된 파일
        if (hasFile[mComA].contains(mFileID)) {
            return fileSizeMap.get(mFileID);
        }

        Job job = activeByCom[mComA].get(mFileID);

        if (job != null) {
            long full = fileSizeMap.get(mFileID);
            return (int) Math.min(full, job.downloaded);
        }

        return 0;
    }

    // 시간 진행 처리
    void advanceTime(int targetTime) {

        if (currentTime >= targetTime)
            return;

        while (currentTime < targetTime) {

            // 현재 시점에서 모든 다운로드의 경로 수 재계산
            recomputeAllRouteCounts();

            int nextFinishTime = Integer.MAX_VALUE;

            // 가장 먼저 끝나는 다운로드 찾기
            for (Job job : activeJobs) {

                if (job.routeCnt == 0)
                    continue;

                long fileSize = fileSizeMap.get(job.fileId);
                long remain = fileSize - job.downloaded;

                if (remain <= 0) {
                    nextFinishTime = currentTime;
                    continue;
                }

                long speed = 9L * job.routeCnt;
                long need = (remain + speed - 1) / speed;
                long finishTime = currentTime + need;

                if (finishTime < nextFinishTime)
                    nextFinishTime = (int) finishTime;
            }

            // 다음 완료가 target 이후라면 바로 진행
            if (nextFinishTime > targetTime || nextFinishTime == Integer.MAX_VALUE) {

                int delta = targetTime - currentTime;
                progressAll(delta);
                currentTime = targetTime;
                break;
            }

            // 완료 시점까지 진행
            int delta = nextFinishTime - currentTime;

            if (delta > 0) {
                progressAll(delta);
                currentTime = nextFinishTime;
            }

            // 완료된 작업 처리
            finishCompletedJobs();
        }
    }

    // 모든 다운로드 진행
    void progressAll(int delta) {

        if (delta <= 0)
            return;

        for (Job job : activeJobs) {

            if (job.routeCnt == 0)
                continue;

            job.downloaded += 9L * job.routeCnt * delta;
        }
    }

    // 다운로드 완료 처리
    void finishCompletedJobs() {

        List<Job> remain = new ArrayList<>(activeJobs.size());

        for (Job job : activeJobs) {

            long fullSize = fileSizeMap.get(job.fileId);

            if (job.downloaded >= fullSize) {

                // 파일 획득
                hasFile[job.com].add(job.fileId);
                activeByCom[job.com].remove(job.fileId);

            } else {

                remain.add(job);
            }
        }

        activeJobs = remain;
    }

    // 현재 시점에서 가능한 다운로드 source 계산
    int countAvailableSources(int from, int fileId) {

        int cnt = 0;

        for (PathInfo p : nearPaths[from]) {

            int to = p.to;

            if (!hasFile[to].contains(fileId))
                continue;

            if (!isPathAlive(p.edgeIds))
                continue;

            cnt++;
        }

        return cnt;
    }

    // 경로에 포함된 링크가 모두 살아있는지 확인
    boolean isPathAlive(int[] edgeIds) {

        for (int id : edgeIds) {
            if (!aliveLink[id])
                return false;
        }

        return true;
    }

    // 거리 ≤5 경로 전처리
    void precomputeNearPaths() {

        for (int start = 1; start <= n; start++) {

            int[] path = new int[5];

            dfsNear(start, start, 0, 0, path, 0);
        }
    }

    void dfsNear(int start, int now, int parent, int distSum, int[] path, int edgeCnt) {

        if (now != start) {
            nearPaths[start].add(new PathInfo(now, Arrays.copyOf(path, edgeCnt)));
        }

        for (Edge next : graph[now]) {

            if (next.to == parent)
                continue;

            if (distSum + next.dist > 5)
                continue;

            path[edgeCnt] = next.id;

            dfsNear(start, next.to, now, distSum + next.dist, path, edgeCnt + 1);
        }
    }

    void recomputeAllRouteCounts() {

        for (Job job : activeJobs) {
            job.routeCnt = countAvailableSources(job.com, job.fileId);
        }
    }
}
