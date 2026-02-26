package swea;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

class OnlineMart_24995 {
    private static final int CMD_INIT           = 100;
    private static final int CMD_SELL           = 200;
    private static final int CMD_CLOSE_SALE     = 300;
    private static final int CMD_DISCOUNT       = 400;
    private static final int CMD_SHOW           = 500;

    private static OnlineMart_UserSolution usersolution = new OnlineMart_UserSolution();

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
        int Q;
        int mID, mCategory, mCompany, mPrice, mAmount;
        int mHow, mCode;

        int ret = -1, cnt, ans;

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
                    usersolution.init();
                    okay = true;
                    break;
                case CMD_SELL:
                    mID =  Integer.parseInt(st.nextToken());
                    mCategory = Integer.parseInt(st.nextToken());
                    mCompany = Integer.parseInt(st.nextToken());
                    mPrice = Integer.parseInt(st.nextToken());
                    ret = usersolution.sell(mID, mCategory, mCompany, mPrice);
                    ans = Integer.parseInt(st.nextToken());
                    if (ret != ans)
                        okay = false;
                    break;
                case CMD_CLOSE_SALE:
                    mID =  Integer.parseInt(st.nextToken());
                    ret = usersolution.closeSale(mID);
                    ans = Integer.parseInt(st.nextToken());
                    if (ret != ans)
                        okay = false;
                    break;
                case CMD_DISCOUNT:
                    mCategory = Integer.parseInt(st.nextToken());
                    mCompany = Integer.parseInt(st.nextToken());
                    mAmount = Integer.parseInt(st.nextToken());
                    ret = usersolution.discount(mCategory, mCompany, mAmount);
                    ans = Integer.parseInt(st.nextToken());
                    if (ret != ans)
                        okay = false;
                    break;
                case CMD_SHOW:
                    mHow = Integer.parseInt(st.nextToken());
                    mCode = Integer.parseInt(st.nextToken());
                    RESULT res = usersolution.show(mHow, mCode);
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

    public static void main(String[] args) throws Exception {
        System.setIn(new java.io.FileInputStream("./BType/OnlineMart/sample_input.txt"));

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

class OnlineMart_UserSolution {

    static class Item implements Comparable<Item> {
        boolean sell;
        int id, price, cate, com;
        long offset;

        public Item(int id, boolean sell, int price, int cate, int com, long offset) {
            this.sell = sell;
            this.id = id;
            this.price = price;
            this.cate = cate;
            this.com = com;
            this.offset = offset;
        }

        @Override
        public int compareTo(Item o){
            long a = this.price + this.offset;   // base
            long b = o.price + o.offset;         // base
            if (a != b) return Long.compare(a, b);
            return Integer.compare(this.id, o.id);
        }
    }

    static Map<Integer, Item> map;
    static int[][] cnt;
    static long[][] discount;
    static PriorityQueue<Item>[][] info;

    public void init() {
        discount = new long[5][5];
        cnt = new int[5][5];
        map = new HashMap<>();
        info = new PriorityQueue[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                info[i][j] = new PriorityQueue<>();
            }
        }
        return;
    }

    public int sell(int mID, int mCategory, int mCompany, int mPrice) {
        Item now = new Item(mID, true, mPrice, mCategory, mCompany, discount[mCategory-1][mCompany-1]);
        map.put(mID, now);
        info[mCategory-1][mCompany-1].offer(now);
        cnt[mCategory-1][mCompany-1]++;
        return cnt[mCategory-1][mCompany-1];
    }

    public int closeSale(int mID) {
        Item now = map.get(mID);
        if (now == null || !map.get(mID).sell) return -1;
        long p = now.price + now.offset - discount[now.cate-1][now.com-1];
        now.sell = false;
        cnt[now.cate-1][now.com-1]--;
        return (int) p;
    }

    public int discount(int mCategory, int mCompany, int mAmount) {
        int c = mCategory-1, co = mCompany-1;
        clean(c, co);                 // (권장) 먼저 죽은 top 제거
        discount[c][co] += mAmount;
        clean(c, co);
        return cnt[mCategory-1][mCompany-1];
    }

    private void clean(int c, int co) {
        PriorityQueue<Item> q = info[c][co];
        while (!q.isEmpty()) {
            Item top = q.peek();
            if (!top.sell) { q.poll(); continue; }

            long cur = top.price + top.offset - discount[c][co];
            if (cur <= 0) {
                top.sell = false;
                q.poll();
                cnt[c][co]--;
                continue;
            }
            break;
        }
    }

    OnlineMart_24995.RESULT show(int mHow, int mCode) {
        OnlineMart_24995.RESULT res = new OnlineMart_24995.RESULT();

        // 후보 힙: (현재가격, id) 기준
        PriorityQueue<Node> cand = new PriorityQueue<>((a, b) -> {
            if (a.curPrice != b.curPrice) return Long.compare(a.curPrice, b.curPrice);
            return Integer.compare(a.item.id, b.item.id);
        });

        // show 동안 잠깐 poll한 것들(끝나고 복구)
        ArrayList<Item>[][] popped = new ArrayList[5][5];
        for (int i = 0; i < 5; i++) for (int j = 0; j < 5; j++) popped[i][j] = new ArrayList<>();

        // 어떤 (c,co)들을 볼지 목록 구성
        ArrayList<int[]> groups = new ArrayList<>();
        if (mHow == 0) {
            for (int c = 0; c < 5; c++) for (int co = 0; co < 5; co++) groups.add(new int[]{c, co});
        } else if (mHow == 1) { // category
            int c = mCode - 1;
            for (int co = 0; co < 5; co++) groups.add(new int[]{c, co});
        } else { // mHow == 2, company
            int co = mCode - 1;
            for (int c = 0; c < 5; c++) groups.add(new int[]{c, co});
        }

        // 각 그룹 top(=peek)만 cand에 넣기
        for (int[] g : groups) {
            int c = g[0], co = g[1];
            clean(c, co); // 죽은/0이하 top 제거
            if (!info[c][co].isEmpty()) {
                Item it = info[c][co].peek();
                long cur = it.price + it.offset - discount[c][co];
                cand.offer(new Node(c, co, it, cur));
            }
        }

        int idx = 0;
        while (idx < 5 && !cand.isEmpty()) {
            Node x = cand.poll();
            int c = x.c, co = x.co;

            // 이미 다른 과정(clean/closeSale)로 죽었을 수 있으니 재검증
            clean(c, co);
            if (info[c][co].isEmpty()) continue;

            Item top = info[c][co].peek();
            // cand에 넣었던 item과 현재 peek이 다르면(중간에 바뀜) skip
            if (top != x.item) continue;

            long cur = top.price + top.offset - discount[c][co];
            if (!top.sell || cur <= 0) {
                clean(c, co);
                continue;
            }

            // 결과 확정
            res.IDs[idx++] = top.id;

            // 다음 후보를 얻기 위해 top을 잠깐 poll해서 2등을 peek로 노출
            Item removed = info[c][co].poll();
            popped[c][co].add(removed);

            clean(c, co);
            if (!info[c][co].isEmpty()) {
                Item nxt = info[c][co].peek();
                long nxtCur = nxt.price + nxt.offset - discount[c][co];
                cand.offer(new Node(c, co, nxt, nxtCur));
            }
        }

        // show는 조회이므로 원복
        for (int[] g : groups) {
            int c = g[0], co = g[1];
            for (Item it : popped[c][co]) info[c][co].offer(it);
        }

        res.cnt = idx;
        return res;
    }
    static class Node {
        int c, co;     // 0~4
        Item item;     // 원본 Item 참조
        long curPrice; // 현재 가격(할인 반영)

        Node(int c, int co, Item item, long curPrice) {
            this.c = c; this.co = co;
            this.item = item;
            this.curPrice = curPrice;
        }
    }
}
