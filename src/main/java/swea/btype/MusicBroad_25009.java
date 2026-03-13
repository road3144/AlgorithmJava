package swea.btype;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class MusicBroad_25009 {

    private static MusicBroad_UserSolution3 usersolution = new MusicBroad_UserSolution3();

    private final static int CMD_INIT   = 0;
    private final static int CMD_ADD	= 1;
    private final static int CMD_REMOVE	= 2;
    private final static int CMD_GETCNT	= 3;

    private static boolean run(BufferedReader br) throws Exception
    {
        int id, stime, etime;
        int ret, ans;

        boolean ok = false;

        StringTokenizer st = new StringTokenizer(br.readLine(), " ");
        int Q = Integer.parseInt(st.nextToken());

        for (int q = 0; q < Q; q++) {
            st = new StringTokenizer(br.readLine(), " ");
            int cmd = Integer.parseInt(st.nextToken());

            if (cmd == CMD_INIT) {
                stime = Integer.parseInt(st.nextToken());
                usersolution.init(stime);
                ok = true;
            } else if (cmd == CMD_ADD) {
                id = Integer.parseInt(st.nextToken());
                stime = Integer.parseInt(st.nextToken());
                etime = Integer.parseInt(st.nextToken());
                usersolution.add(id, stime, etime);
            } else if (cmd == CMD_REMOVE) {
                id = Integer.parseInt(st.nextToken());
                usersolution.remove(id);
            }
            else if (cmd == CMD_GETCNT) {
                stime = Integer.parseInt(st.nextToken());
                ret = usersolution.getCnt(stime);
                ans = Integer.parseInt(st.nextToken());
                if (ret != ans) {
                    ok = false;
                }
            }
            else ok = false;
        }
        return ok;
    }

    public static void main(String[] args) throws Exception {

        System.setIn(new java.io.FileInputStream("./BType/MusicBroad/sample_input.txt"));

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


class MusicBroad_UserSolution {

    static class People{
        int start, end;

        People(int start, int end){
            this.start = start;
            this.end = end;
        }
    }

    static int m;
    static Map<Integer, List<Integer>> bucketInfo;
    static Set<Integer> isLive;
    static Map<Integer, People> peopleMap;

    void init(int musicTime) {
        m = musicTime;
        bucketInfo = new HashMap<>();
        isLive = new HashSet<>();
        peopleMap = new HashMap<>();
    }

    void add(int mID, int mStart, int mEnd) {
        People people = new People(mStart, mEnd);
        isLive.add(mID);
        peopleMap.put(mID, people);
        int si = mStart / 1000;
        int ei = mEnd / 1000;

        for (int i = si; i <= ei; i++) {
            if (!bucketInfo.containsKey(i))
                bucketInfo.put(i, new ArrayList<>());
            bucketInfo.get(i).add(mID);

        }
    }

    void remove(int mID) {
        isLive.remove(mID);
    }

    int getCnt(int mBSTime) {
        int si = mBSTime / 1000;
        Set<Integer> counted = new HashSet<>();
        if (bucketInfo.containsKey(si)){
            for (Integer mId : bucketInfo.get(si)){
                People people = peopleMap.get(mId);
                if (!isLive.contains(mId)) continue;
                if (people.start <= mBSTime && mBSTime + m <= people.end){
                    counted.add(mId);
                }
            }
        }
        if (bucketInfo.containsKey(si+1)){
            for (Integer mId : bucketInfo.get(si+1)){
                People people = peopleMap.get(mId);
                if (!isLive.contains(mId)) continue;
                if (people.start <= mBSTime && mBSTime + m <= people.end){
                    counted.add(mId);
                }
            }
        }

        return counted.size();
    }
}

//세그 구간합 + lazy
class MusicBroad_UserSolution2 {

    static class People{
        int start, end;

        People(int start, int end){
            this.start = start;
            this.end = end;
        }
    }

    static int m;
    static Map<Integer, People> peopleMap;
    static Set<Integer> removed;
    static long[] tree, lazy;

    void init(int musicTime) {
        m = musicTime;
        peopleMap = new HashMap<>();
        tree = new long[8000000];
        lazy = new long[8000000];
        removed = new HashSet<>();
    }

    void add(int mID, int mStart, int mEnd) {
        removed.remove(mID);
        if (peopleMap.containsKey(mID)) {
            remove(mID);
            removed.remove(mID);
        }
        People people = new People(mStart, mEnd);
        peopleMap.put(mID, people);
        update(1, 2000000, 1, mStart, mEnd-m, 1);
    }

    void remove(int mID) {
        if (!removed.contains(mID)){
            People people = peopleMap.get(mID);
            update(1, 2000000, 1, people.start, people.end-m, -1);
        }
        removed.add(mID);
        peopleMap.remove(mID);
    }

    int getCnt(int mBSTime) {
        int ans = (int) sum(1, 2000000, 1, mBSTime, mBSTime);
        return ans;
    }

    void push(int start, int end, int idx){
        if (lazy[idx] != 0){
            tree[idx] += (end - start + 1) * lazy[idx];
            if (start != end){
                lazy[idx * 2] += lazy[idx];
                lazy[idx * 2 + 1] += lazy[idx];
            }
            lazy[idx] = 0;
        }
    }

    void update(int start, int end, int idx, int left, int right, int value){
        push(start, end, idx);

        if (end < left || right < start) return;
        if (left <= start && end <= right){
            tree[idx] += (long) (end - start + 1) * value;
            if (start != end){
                lazy[idx*2] += value;
                lazy[idx*2+1] += value;
            }
            return;
        }

        int mid = (start + end) / 2;
        update(start, mid, idx*2, left, right, value);
        update(mid+1, end, idx*2+1, left, right, value);
        tree[idx] = tree[idx*2] + tree[idx*2+1];
    }

    long sum(int start, int end, int idx, int left, int right){
        push(start, end, idx);
        if (end < left || right < start) return 0;
        if (left <= start && end <= right) return tree[idx];

        int mid = (start + end) / 2;
        return sum(start, mid, idx*2, left, right) + sum(mid+1, end, idx*2+1, left, right);
    }
}

//세그 구간합 없애고 lazy만
class MusicBroad_UserSolution3 {

    static class People{
        int start, end;

        People(int start, int end){
            this.start = start;
            this.end = end;
        }
    }

    static int m;
    static Map<Integer, People> peopleMap;
    static Set<Integer> removed;
    static int[] lazy;

    void init(int musicTime) {
        m = musicTime;
        peopleMap = new HashMap<>();
        lazy = new int[8000000];
        removed = new HashSet<>();
    }

    void add(int mID, int mStart, int mEnd) {
        removed.remove(mID);
        if (peopleMap.containsKey(mID)) {
            remove(mID);
            removed.remove(mID);
        }
        People people = new People(mStart, mEnd);
        peopleMap.put(mID, people);
        update(1, 2000000, 1, mStart, mEnd-m, 1);
    }

    void remove(int mID) {
        if (!removed.contains(mID)){
            People people = peopleMap.get(mID);
            update(1, 2000000, 1, people.start, people.end-m, -1);
        }
        removed.add(mID);
        peopleMap.remove(mID);
    }

    int getCnt(int mBSTime) {
        return (int) sum(1, 2000000, 1, mBSTime, 0);
    }

    void update(int start, int end, int idx, int left, int right, int value){

        if (end < left || right < start) return;
        if (left <= start && end <= right){
            lazy[idx] += value;
            return;
        }

        int mid = (start + end) / 2;
        update(start, mid, idx*2, left, right, value);
        update(mid+1, end, idx*2+1, left, right, value);
    }

    long sum(int start, int end, int idx, int target, int sum){
        sum += lazy[idx];
        if (start == end) return sum;

        int mid = (start + end) / 2;
        if (target <= mid) return sum(start, mid, idx*2, target, sum);
        return sum(mid+1, end, idx*2+1, target, sum);
    }
}
