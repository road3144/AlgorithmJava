package swea.btype;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeSet;

public class LineCityBuild_25289 {

    private static LineCityBuild_UserSolution usersolution = new LineCityBuild_UserSolution();

    private final static int CMD_INIT = 1;
    private final static int CMD_BUILD = 2;
    private final static int CMD_DEMOLISH = 3;

    private static boolean run(Scanner sc) throws Exception {

        int query_num = sc.nextInt();
        boolean ok = false;

        for (int q = 0; q < query_num; q++) {
            int query = sc.nextInt();

            if (query == CMD_INIT) {
                int N = sc.nextInt();
                usersolution.init(N);
                ok = true;
            } else if (query == CMD_BUILD) {
                int mLength = sc.nextInt();
                int ret = usersolution.build(mLength);
                int ans = sc.nextInt();
                if (ans != ret) {
                    ok = false;
                }
            } else if (query == CMD_DEMOLISH) {
                int mAddr = (int)sc.nextInt();
                int ret = usersolution.demolish(mAddr);
                int ans = sc.nextInt();
                if (ans != ret) {
                    ok = false;
                }
            }
        }
        return ok;
    }

    public static void main(String[] args) throws Exception {
        int T, MARK;
         System.setIn(new java.io.FileInputStream("./BType/LineCityBuild/sample_input.txt"));
        Scanner sc = new Scanner(System.in);

        T = sc.nextInt();
        MARK = sc.nextInt();

        for (int tc = 1; tc <= T; tc++) {
            int score = run(sc) ? MARK : 0;
            System.out.println("#" + tc + " " + score);
        }
        sc.close();
    }
}

class LineCityBuild_UserSolution {

    static class Empty implements Comparable<Empty>{
        int start, size;
        Building left, right;

        Empty(int start, int size){
            this.start = start;
            this.size = size;
        }

        public int compareTo(Empty o){
            if (this.size == o.size) return this.start - o.start;
            return o.size - this.size;
        }

        public String toString(){
            return "(" + start + ", " + size + ")";
        }
    }

    static class Building{
        int start, size;
        Empty left, right;

        Building(int start, int size){
            this.start = start;
            this.size = size;
        }
    }

    static TreeSet<Empty> emptyTreeSet;
    static Map<Integer, Building> buildingMap;

    public void init(int N){
        emptyTreeSet = new TreeSet<>();
        Empty e = new Empty(0, N);
        emptyTreeSet.add(e);
        buildingMap = new HashMap<>();
    }

    public int build(int mLength) {
        if (emptyTreeSet.isEmpty()) {
            return -1;
        }
        Empty e = emptyTreeSet.pollFirst();
        if (e.size < mLength) {
            emptyTreeSet.add(e);
            return -1;
        }
        int start = -1;
        int mid = e.start + (e.size - 1) / 2;
        if(e.size % 2 == 0){
            start = mid - (mLength - 1) / 2;
        } else {
            start = mid - mLength / 2;
        }
        int end = start + mLength - 1;

        Empty left = null, right = null;

        left = new Empty(e.start, start-1 - e.start + 1);


        right = new Empty(end+1, e.start + e.size - end - 1);

        Building b = new Building(start, end - start + 1);
        b.left = left;
        b.right = right;
        left.right = b;
        right.left = b;
        if (e.left != null)
            e.left.right = left;
        if (e.right != null)
            e.right.left = right;
        left.left = e.left;
        right.right = e.right;
        buildingMap.put(start, b);

        emptyTreeSet.add(left);
        emptyTreeSet.add(right);

        return b.start;
    }

    public int demolish(int mAddr) {
        if(!buildingMap.containsKey(mAddr)){
            return -1;
        }

        Building b = buildingMap.get(mAddr);

        int start = b.left.start;
        emptyTreeSet.remove(b.left);

        int end = b.right.start + b.right.size - 1;
        emptyTreeSet.remove(b.right);

        Empty e = new Empty(start, end - start + 1);

        //기존 빌딩에 빈공간 연결
        if (b.left.left != null)
            b.left.left.right = e;
        if (b.right.right != null)
            b.right.right.left = e;

        //새로 생긴 빈공간에 빌딩 연결
        if (b.left.left != null)
            e.left = b.left.left;
        if (b.right.right != null)
            e.right = b.right.right;

        buildingMap.remove(mAddr);
        emptyTreeSet.add(e);
        return b.size;
    }
}
