package codetree.samsung;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class StreetLamp {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int Q = Integer.parseInt(st.nextToken());
        for (int i = 0; i < Q; i++) {
            st = new StringTokenizer(br.readLine());
            int comm = Integer.parseInt(st.nextToken());

            if (comm == 100) {
                int n = Integer.parseInt(st.nextToken());
                int m = Integer.parseInt(st.nextToken());
                int[] info = new int[m];
                for (int j = 0; j < m; j++) {
                    info[j] = Integer.parseInt(st.nextToken());
                }
                init(n, m, info);
            } else if (comm == 200) {
                add();
            } else if (comm == 300) {
                int id = Integer.parseInt(st.nextToken());
                remove(id);
            } else if (comm == 400) {
                System.out.println(count());
            }
        }
    }

    static class Empty implements Comparable<Empty> {
        int start, end, size;
        Lamp left, right;

        Empty(int start, int end, int size) {
            this.start = start;
            this.end = end;
            this.size = size;
        }

        @Override
        public int compareTo(Empty o) {
            if (this.size != o.size) return o.size - this.size;
            return this.start - o.start;
        }
    }

    static class Lamp {
        int id, pos;
        Empty left, right;

        Lamp(int id, int pos) {
            this.id = id;
            this.pos = pos;
        }
    }

    static TreeSet<Empty> pq;
    static Map<Integer, Lamp> map;
    static int idx;
    static int n;
    static Lamp head, tail;

    static void init(int N, int m, int[] info) {
        n = N;
        map = new HashMap<>();
        pq = new TreeSet<>();
        idx = 1;

        Lamp l = new Lamp(idx++, info[0]);
        map.put(l.id, l);
        head = l;

        Lamp last = l;

        for (int i = 1; i < m; i++) {
            l = new Lamp(idx++, info[i]);
            map.put(l.id, l);

            Empty e = new Empty(last.pos + 1, l.pos - 1, l.pos - last.pos); // 수정
            e.left = last;
            e.right = l;
            last.right = e;
            l.left = e;
            pq.add(e);

            last = l;
        }

        tail = last;
    }

    static void add() {
        Empty e = pq.pollFirst();

        Lamp l = new Lamp(idx++, (e.start + e.end + 1) / 2);
        map.put(l.id, l);

        Empty left = new Empty(e.start, l.pos - 1, l.pos - e.left.pos); // 수정
        Empty right = new Empty(l.pos + 1, e.end, e.right.pos - l.pos); // 수정

        l.left = left;
        l.right = right;

        left.left = e.left;
        left.right = l;
        right.left = l;
        right.right = e.right;

        e.left.right = left;
        e.right.left = right;

        pq.add(left);
        pq.add(right);
    }

    static void remove(int id) {
        Lamp l = map.get(id);

        Empty left = l.left;
        Empty right = l.right;

        // 내부 구간 제거
        if (left != null) pq.remove(left);
        if (right != null) pq.remove(right);

        Lamp leftLamp = null;
        Lamp rightLamp = null;

        if (left != null) leftLamp = left.left;
        if (right != null) rightLamp = right.right;

        // head / tail 갱신
        if (leftLamp == null) {
            head = rightLamp;
            if (rightLamp != null) rightLamp.left = null;
        }
        if (rightLamp == null) {
            tail = leftLamp;
            if (leftLamp != null) leftLamp.right = null;
        }

        // 양쪽 다 있으면 새 내부 Empty 생성
        if (leftLamp != null && rightLamp != null) {
            Empty e = new Empty(leftLamp.pos + 1, rightLamp.pos - 1, rightLamp.pos - leftLamp.pos);
            e.left = leftLamp;
            e.right = rightLamp;
            leftLamp.right = e;
            rightLamp.left = e;
            pq.add(e);
        }

        map.remove(id);
    }

    static int count() {
        int mid = pq.isEmpty() ? 0 : pq.first().size;
        int left = 2 * (head.pos - 1);
        int right = 2 * (n - tail.pos);
        return Math.max(mid, Math.max(left, right));
    }
}
