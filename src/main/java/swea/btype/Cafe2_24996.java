package swea.btype;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

class Solution
{
    private static final int CMD_INIT           = 100;
    private static final int CMD_ORDER	        = 200;
    private static final int CMD_SUPPLY			= 300;
    private static final int CMD_CANCEL			= 400;
    private static final int CMD_GET_STATUS		= 500;
    private static final int CMD_HURRY			= 600;

    private static UserSolution2 usersolution = new UserSolution2();

    private static final int MAX_NUM_BEVERAGES = 10;

    public static class RESULT
    {
        int cnt;
        int[] IDs = new int[5];

        RESULT()
        {
            cnt = -1;
        }
    }

    private static boolean run(BufferedReader br) throws Exception
    {
        int Q, N, M;
        int mID, mBeverage;

        int[] mBeverages = new int[MAX_NUM_BEVERAGES];

        int ret = -1, ans, cnt;

        RESULT res;

        StringTokenizer st = new StringTokenizer(br.readLine(), " ");
        Q = Integer.parseInt(st.nextToken());

        boolean okay = false;

        for (int q = 0; q < Q; ++q)
        {
            st = new StringTokenizer(br.readLine(), " ");
            int cmd = Integer.parseInt(st.nextToken());

            switch(cmd)
            {
                case CMD_INIT:
                    N = Integer.parseInt(st.nextToken());
                    usersolution.init(N);
                    okay = true;
                    break;
                case CMD_ORDER:
                    mID = Integer.parseInt(st.nextToken());
                    M = Integer.parseInt(st.nextToken());
                    for (int i = 0; i < M; ++i)
                        mBeverages[i] = Integer.parseInt(st.nextToken());
                    ret = usersolution.order(mID, M, mBeverages);
                    ans = Integer.parseInt(st.nextToken());
                    if (ret != ans)
                        okay = false;
                    break;
                case CMD_SUPPLY:
                    mBeverage =  Integer.parseInt(st.nextToken());
                    ret = usersolution.supply(mBeverage);
                    ans = Integer.parseInt(st.nextToken());
                    if (ret != ans)
                        okay = false;
                    break;
                case CMD_CANCEL:
                    mID = Integer.parseInt(st.nextToken());
                    ret = usersolution.cancel(mID);
                    ans = Integer.parseInt(st.nextToken());
                    if (ret != ans)
                        okay = false;
                    break;
                case CMD_GET_STATUS:
                    mID = Integer.parseInt(st.nextToken());
                    ret = usersolution.getStatus(mID);
                    ans = Integer.parseInt(st.nextToken());
                    if (ret != ans)
                        okay = false;
                    break;
                case CMD_HURRY:
                    res = usersolution.hurry();
                    cnt = Integer.parseInt(st.nextToken());
                    if (res.cnt != cnt)
                        okay = false;
                    for (int i = 0; i < cnt; ++i)
                    {
                        ans = Integer.parseInt(st.nextToken());
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
        //System.setIn(new java.io.FileInputStream("res/sample_input.txt"));

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine(), " ");

        int TC = Integer.parseInt(st.nextToken());
        int MARK = Integer.parseInt(st.nextToken());

        for (int testcase = 1; testcase <= TC; ++testcase)
        {
            int score = run(br) ? MARK : 0;
            System.out.println("#" + testcase + " " + score);
        }

        br.close();
    }
}
/** 채점용 Solution 클래스는 수정하지 말 것 */
class UserSolution2
{
    // 주문 상태
    private static final int ACTIVE = 0;
    private static final int COMPLETED = 1;
    private static final int CANCELED = 2;

    // 주문 객체
    private static class Order {
        int id;
        long seq;                // 도착 순서
        int state;               // ACTIVE / COMPLETED / CANCELED
        int remain;              // 남은 전체 음료 수
        int[] need;              // 각 음료별 남은 개수
        int[] assigned;          // 각 음료별 이미 배치된(공급되어 이 주문에 들어온) 개수

        Order(int id, long seq, int N) {
            this.id = id;
            this.seq = seq;
            this.state = ACTIVE;
            this.remain = 0;
            this.need = new int[N + 1];
            this.assigned = new int[N + 1];
        }
    }

    // hurry()를 위한 키 스냅샷
    private static class Node {
        int rem;
        long seq;
        int id;
        Node(int rem, long seq, int id) {
            this.rem = rem;
            this.seq = seq;
            this.id = id;
        }
    }

    // 비교자: 남은 음료 수 내림차순 -> 순서 오름차순 -> id 오름차순
    private static final Comparator<Node> HURRY_CMP = (a, b) -> {
        if (a.rem != b.rem) return b.rem - a.rem;         // desc
        if (a.seq != b.seq) return Long.compare(a.seq, b.seq); // asc
        return Integer.compare(a.id, b.id);               // asc
    };

    // 상태
    private int N;                                 // 음료 종류 수 (1..N)
    private long seqGen;                           // 주문 도착 순서 생성기
    private int activeCnt;                         // ACTIVE 주문 수

    private Map<Integer, Order> orders;            // mID -> Order
    private ArrayDeque<Order>[] qByBeverage;       // 각 음료별 대기 큐(해당 음료가 필요한 주문들)
    private TreeSet<Node> hurrySet;                // hurry 우선순위 집합
    private Map<Integer, Node> nodeById;           // id -> Node (TreeSet 갱신용 스냅샷)

    @SuppressWarnings("unchecked")
    public void init(int N)
    {
        this.N = N;
        this.seqGen = 0;
        this.activeCnt = 0;

        orders = new HashMap<>(30000);
        qByBeverage = new ArrayDeque[N + 1];
        for (int b = 1; b <= N; b++) qByBeverage[b] = new ArrayDeque<>();

        hurrySet = new TreeSet<>(HURRY_CMP);
        nodeById = new HashMap<>(30000);
    }

    public int order(int mID, int M, int mBeverages[])
    {
        Order o = new Order(mID, ++seqGen, N);

        // 구성 채우기
        for (int i = 0; i < M; i++) {
            int b = mBeverages[i];
            o.need[b]++;
            o.remain++;
        }

        // 각 음료별 큐에 한 번씩만 추가
        for (int b = 1; b <= N; b++) {
            if (o.need[b] > 0) {
                qByBeverage[b].addLast(o);
            }
        }

        // hurrySet 등록
        if (o.remain > 0) {
            Node nd = new Node(o.remain, o.seq, o.id);
            hurrySet.add(nd);
            nodeById.put(o.id, nd);
        }
        orders.put(o.id, o);
        activeCnt++;

        return activeCnt;
    }

    public int supply(int mBeverage)
    {
        return processSupply(mBeverage);
    }

    public int cancel(int mID)
    {
        Order o = orders.get(mID);
        if (o == null) return -1; // 문제 조건상 존재 보장이나 안전망

        if (o.state == COMPLETED) return 0;
        if (o.state == CANCELED)  return -1;

        int beforeRemain = o.remain;

        // 상태 변경 및 hurrySet에서 제거
        o.state = CANCELED;
        activeCnt--;
        Node nd = nodeById.remove(o.id);
        if (nd != null) hurrySet.remove(nd);

        // 이 주문에 이미 배치되었던 음료들을 재배치 (공급 로직과 동일)
        for (int b = 1; b <= N; b++) {
            int cnt = o.assigned[b];
            for (int i = 0; i < cnt; i++) {
                processSupply(b); // 재배치, 실패하면 버림
            }
        }

        // 이후 이 주문이 큐에 남아있어도 lazy pop으로 자연 제거됨
        return beforeRemain;
    }

    public int getStatus(int mID)
    {
        Order o = orders.get(mID);
        if (o == null) return -1; // 안전망
        if (o.state == COMPLETED) return 0;
        if (o.state == CANCELED)  return -1;
        return o.remain;
    }

    public Solution.RESULT hurry()
    {
        Solution.RESULT res = new Solution.RESULT();
        int k = 0;

        // 우선순위대로 최대 5개
        for (Node nd : hurrySet) {
            if (k == 5) break;
            res.IDs[k++] = nd.id;
        }
        res.cnt = k;
        return res;
    }

    // ==== 내부 유틸 ====

    /** 음료 b 1개를 공급하여 가장 먼저 받은 적합한 주문에 배치한다. 성공 시 주문 id, 실패 시 -1 */
    private int processSupply(int b) {
        ArrayDeque<Order> q = qByBeverage[b];

        // 큐 앞에서 유효하지 않은 주문(완료/취소이거나 더 이상 그 음료가 필요없음)은 제거
        while (!q.isEmpty()) {
            Order front = q.peekFirst();
            if (front.state != ACTIVE || front.need[b] == 0) {
                q.pollFirst();
            } else break;
        }

        if (q.isEmpty()) return -1;

        Order o = q.peekFirst(); // 제거하지 않고 같은 주문에 연속 공급 가능

        // hurrySet에서 기존 노드 제거
        Node old = nodeById.get(o.id);
        if (old != null) {
            hurrySet.remove(old);
        }

        // 공급 적용
        o.need[b]--;
        o.assigned[b]++;
        o.remain--;

        // 해당 음료가 더 이상 필요 없으면 큐에서 제거
        if (o.need[b] == 0) {
            q.pollFirst();
        }

        // 주문 완료 처리
        if (o.remain == 0) {
            o.state = COMPLETED;
            activeCnt--;
            nodeById.remove(o.id); // old 이미 제거됨
            // 다른 음료 큐에 남아있더라도 lazy pop으로 자연 제거
        } else {
            // 남아있으면 hurrySet에 갱신된 키로 재삽입
            Node nn = new Node(o.remain, o.seq, o.id);
            hurrySet.add(nn);
            nodeById.put(o.id, nn);
        }

        return o.id;
    }
}