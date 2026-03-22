package swea.btype;

import java.util.*;

public class OTT_24992 {

    private static OTT_UserSolution2 usersolution = new OTT_UserSolution2();

    private final static int CMD_INIT       = 100;
    private final static int CMD_ADD        = 200;
    private final static int CMD_ERASE      = 300;
    private final static int CMD_WATCH      = 400;
    private final static int CMD_SUGGEST    = 500;

    public final static class RESULT
    {
        int cnt;
        int[] IDs = new int[5];

        RESULT()
        {
            cnt = -1;
        }
    }

    private static boolean run(Scanner sc) throws Exception
    {
        int Q, N;
        int mID, mGenre, mTotal, mRating, uID;

        int ret = -1, cnt, ans;

        RESULT res;

        Q = sc.nextInt();

        boolean okay = false;

        for (int q = 0; q < Q; ++q)
        {
            int cmd;
            cmd = sc.nextInt();

            switch(cmd)
            {
                case CMD_INIT:
                    N = sc.nextInt();
                    usersolution.init(N);
                    okay = true;
                    break;
                case CMD_ADD:
                    mID = sc.nextInt();
                    mGenre = sc.nextInt();
                    mTotal = sc.nextInt();
                    ret = usersolution.add(mID, mGenre, mTotal);
                    ans = sc.nextInt();
                    if (ret != ans)
                        okay = false;
                    break;
                case CMD_ERASE:
                    mID = sc.nextInt();
                    ret = usersolution.erase(mID);
                    ans = sc.nextInt();
                    if (ret != ans)
                        okay = false;
                    break;
                case CMD_WATCH:
                    uID = sc.nextInt();
                    mID = sc.nextInt();
                    mRating = sc.nextInt();
                    ret = usersolution.watch(uID, mID, mRating);
                    ans = sc.nextInt();
                    if (ret != ans)
                        okay = false;
                    break;
                case CMD_SUGGEST:
                    uID = sc.nextInt();
                    res = usersolution.suggest(uID);
                    cnt = sc.nextInt();
                    if (res.cnt != cnt)
                        okay = false;
                    for (int i = 0; i < cnt; ++i)
                    {
                        ans = sc.nextInt();
                        if (res.IDs[i] != ans)
                            okay = false;
                    }
                    break;
                default:
                    okay = false;
                    break;
            }
        }

        return okay;
    }

    public static void main(String[] args) throws Exception
    {
        System.setIn(new java.io.FileInputStream("./BType/OTT/sample_input.txt"));

        Scanner sc = new Scanner(System.in);

        int TC = sc.nextInt();
        int MARK = sc.nextInt();

        for (int testcase = 1; testcase <= TC; ++testcase)
        {
            int score = run(sc) ? MARK : 0;
            System.out.println("#" + testcase + " " + score);
        }

        sc.close();
    }
}

class OTT_UserSolution {

    static class Movie implements Comparable<Movie>{
        int id, genre, point, time;
        boolean alive = true;

        Movie(int id, int genre, int point, int time){
            this.id = id;
            this.genre = genre;
            this.point = point;
            this.time = time;
        }

        public int compareTo(Movie o){
            if (o.point != this.point) return o.point - this.point;
            return o.time - this.time;
        }

        @Override
        public boolean equals(Object obj) {
            Movie o = (Movie) obj;
            return this.id == o.id;
        }

        @Override
        public int hashCode(){
            return Objects.hash(this.id);
        }

    }

    static class Review implements Comparable<Review>{
        int mId, point, time;

        Review(int id, int point, int time){
            this.mId = id;
            this.point = point;
            this.time = time;
        }

        public int compareTo(Review o){
            if (o.time != this.time) return o.time - this.time;
            return o.point - this.point;
        }

        @Override
        public boolean equals(Object obj) {
            Review o = (Review) obj;
            return this.mId == o.mId;
        }

        @Override
        public int hashCode(){
            return Objects.hash(this.mId);
        }
    }

    static Map<Integer, Movie> map;
    static TreeSet<Review>[] userReview;
    static TreeSet<Movie>[] genreMovie;
    static Map<Integer, Set<Integer>> viewers;
    static Map<Integer, Review>[] reviewMap;
    static int time;

    void init(int N) {
        userReview = new TreeSet[N+1];
        genreMovie = new TreeSet[6];
        viewers = new HashMap<>();
        reviewMap = new Map[N+1];

        for (int i = 0; i < N+1; i++) {
            userReview[i] = new TreeSet<>();
            reviewMap[i] = new HashMap<>();
        }

        for (int i = 0; i < 6; i++) {
            genreMovie[i] = new TreeSet<>();
        }
        map = new HashMap<>(10000);
        time = 0;
    }

    int add(int mID, int mGenre, int mTotal) {
        if (map.containsKey(mID)) return 0;
        Movie m = new Movie(mID, mGenre, mTotal, time++);
        map.put(mID, m);
        genreMovie[mGenre].add(m);
        viewers.put(mID, new HashSet<>());
        return 1;
    }

    int erase(int mID) {
        if (!map.containsKey(mID)) return 0;
        if (!map.get(mID).alive) return 0;

        //장르에서 삭제
        Movie tarM = map.get(mID);
        genreMovie[tarM.genre].remove(tarM);

        // 시청 기록 지우기
        for (Integer uId : viewers.get(mID)){
            Review target = reviewMap[uId].get(mID);
            userReview[uId].remove(target);
            reviewMap[uId].remove(mID);
        }
        map.remove(mID);
        return 1;
    }

    int watch(int uID, int mID, int mRating) {
        if (!map.containsKey(mID)) return 0;
        if (!map.get(mID).alive) return 0;
        Review r = new Review(mID, mRating, time++);
        if (userReview[uID].contains(r)) return 0;
        if (viewers.get(mID).contains(uID)) return 0;
        // 시청목록 추가
        userReview[uID].add(r);
        viewers.get(mID).add(uID);
        reviewMap[uID].put(mID, r);
        //장르 재 정열
        Movie target = map.get(mID);
        genreMovie[target.genre].remove(target);
        target.point += r.point;
        genreMovie[target.genre].add(target);
        return 1;
    }

    OTT_24992.RESULT suggest(int uID) {
        OTT_24992.RESULT res = new OTT_24992.RESULT();
        res.cnt = 0;
        List<Integer> recom = new ArrayList<>();
        if (userReview[uID].isEmpty()){ // 시청 기록 없을때
            PriorityQueue<Movie> cand = new PriorityQueue<>();

            for (int i = 1; i <= 5; i++) {
                int cnt = 0;
                Iterator<Movie> it = genreMovie[i].iterator();
                while (it.hasNext() && cnt < 5){
                    cand.add(it.next());
                    cnt++;
                }
            }
            int cnt = 0;
            while (!cand.isEmpty() && cnt < 5){
                Movie m = cand.poll();
                if (viewers.get(m.id).contains(uID)) continue;
                recom.add(m.id);
                cnt++;
            }

        } else {
            // 가장 높은 거 고르기
            int cnt = 0;
            int maxId = 0;
            int maxValue = 0;
            Iterator<Review> it = userReview[uID].iterator();
            while (it.hasNext() && cnt < 5){
                Review now = it.next();
                if (maxValue < now.point){
                    maxId = now.mId;
                    maxValue = now.point;
                }
                cnt++;
            }

            if (maxId == 0){
                return res;
            }

            // 가장 높은 장르에서 추천 ㄱㄱ
            cnt = 0;
            Iterator<Movie> mit = genreMovie[map.get(maxId).genre].iterator();
            while (mit.hasNext() && cnt < 5){
                Movie m = mit.next();
                if (viewers.get(m.id).contains(uID)) continue;
                recom.add(m.id);
                cnt++;
            }
        }
        res.cnt = recom.size();
        for (int i = 0; i < recom.size(); i++) {
            res.IDs[i] = recom.get(i);
        }
        return res;
    }
}

// 자료구조 최적화 버전
class OTT_UserSolution2 {

    static class Movie implements Comparable<Movie>{
        int id, genre, point, time;

        Movie(int id, int genre, int point, int time){
            this.id = id;
            this.genre = genre;
            this.point = point;
            this.time = time;
        }

        public int compareTo(Movie o){
            if (o.point != this.point) return o.point - this.point;
            return o.time - this.time;
        }

        @Override
        public boolean equals(Object obj) {
            Movie o = (Movie) obj;
            return this.id == o.id;
        }

        @Override
        public int hashCode(){
            return this.id;
        }

    }

    static class Review implements Comparable<Review>{
        int mId, point, time;

        Review(int id, int point, int time){
            this.mId = id;
            this.point = point;
            this.time = time;
        }

        public int compareTo(Review o){
            if (o.time != this.time) return o.time - this.time;
            return o.point - this.point;
        }

        @Override
        public boolean equals(Object obj) {
            Review o = (Review) obj;
            return this.mId == o.mId;
        }

        @Override
        public int hashCode(){
            return this.mId;
        }
    }

    static Map<Integer, Movie> map;
    static TreeSet<Review>[] userReview;
    static TreeSet<Movie>[] genreMovie;
    static Map<Integer, Set<Integer>> viewers;
    static Map<Integer, Review>[] reviewMap;
    static int time;

    void init(int N) {
        userReview = new TreeSet[N+1];
        genreMovie = new TreeSet[6];
        viewers = new HashMap<>();
        reviewMap = new Map[N+1];

        for (int i = 0; i < N+1; i++) {
            userReview[i] = new TreeSet<>();
            reviewMap[i] = new HashMap<>();
        }

        for (int i = 0; i < 6; i++) {
            genreMovie[i] = new TreeSet<>();
        }
        map = new HashMap<>(10000);
        time = 0;
    }

    int add(int mID, int mGenre, int mTotal) {
        if (map.containsKey(mID)) return 0;
        Movie m = new Movie(mID, mGenre, mTotal, time++);
        map.put(mID, m);
        genreMovie[mGenre].add(m);
        viewers.put(mID, new HashSet<>());
        return 1;
    }

    int erase(int mID) {
        if (!map.containsKey(mID)) return 0;

        //장르에서 삭제
        Movie tarM = map.get(mID);
        genreMovie[tarM.genre].remove(tarM);

        // 시청 기록 지우기
        for (Integer uId : viewers.get(mID)){
            Review target = reviewMap[uId].get(mID);
            userReview[uId].remove(target);
            reviewMap[uId].remove(mID);
        }
        map.remove(mID);
        return 1;
    }

    int watch(int uID, int mID, int mRating) {
        if (!map.containsKey(mID)) return 0;
        if (reviewMap[uID].containsKey(mID)) return 0;
        Review r = new Review(mID, mRating, time++);

        // 시청목록 추가
        userReview[uID].add(r);
        viewers.get(mID).add(uID);
        reviewMap[uID].put(mID, r);
        //장르 재 정열
        Movie target = map.get(mID);
        genreMovie[target.genre].remove(target);
        target.point += r.point;
        genreMovie[target.genre].add(target);
        return 1;
    }

    OTT_24992.RESULT suggest(int uID) {
        OTT_24992.RESULT res = new OTT_24992.RESULT();
        res.cnt = 0;
        if (userReview[uID].isEmpty()){ // 시청 기록 없을때
            PriorityQueue<Movie> cand = new PriorityQueue<>();

            for (int i = 1; i <= 5; i++) {
                int cnt = 0;
                Iterator<Movie> it = genreMovie[i].iterator();
                while (it.hasNext() && cnt < 5){
                    cand.add(it.next());
                    cnt++;
                }
            }

            while (!cand.isEmpty() && res.cnt < 5){
                Movie m = cand.poll();
                if (reviewMap[uID].containsKey(m.id)) continue;
                res.IDs[res.cnt++] = m.id;
            }

        } else {
            // 가장 높은 거 고르기
            int cnt = 0;
            int maxId = 0;
            int maxValue = 0;
            Iterator<Review> it = userReview[uID].iterator();
            while (it.hasNext() && cnt < 5){
                Review now = it.next();
                if (maxValue < now.point){
                    maxId = now.mId;
                    maxValue = now.point;
                }
                cnt++;
            }

            if (maxId == 0){
                return res;
            }

            // 가장 높은 장르에서 추천 ㄱㄱ

            Iterator<Movie> mit = genreMovie[map.get(maxId).genre].iterator();
            while (mit.hasNext() && res.cnt < 5){
                Movie m = mit.next();
                // [최적화] 시청 여부 체크를 reviewMap으로 통일
                if (reviewMap[uID].containsKey(m.id)) continue;

                // [최적화] List<Integer> 없이 바로 결과 배열에 저장
                res.IDs[res.cnt++] = m.id;
            }
        }

        return res;
    }
}
