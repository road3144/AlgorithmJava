package swea;

import java.io.*;
import java.util.*;

public class AtomExtinction_5648 {
    static class Atom {
        Pos pos;      // 재사용되는 좌표 객체 (tick마다 x,y만 갱신)
        int d, k;     // 방향, 에너지
        Atom(int x, int y, int d, int k) {
            this.pos = new Pos(x, y);
            this.d = d; this.k = k;
        }
    }

    static final class Pos {
        int x, y;
        Pos(int x, int y) { this.x = x; this.y = y; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Pos)) return false;
            Pos p = (Pos) o;
            return x == p.x && y == p.y;
        }

        @Override
        public int hashCode() {
            return (x * 31) ^ y;
        }
    }


    static final int[] dx = {0, 0, -1, 1};
    static final int[] dy = {1, -1, 0, 0};

    // 0.5초 단위 시뮬을 위해 좌표 2배
    static final int LIM = 2000;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder out = new StringBuilder();

        int T = Integer.parseInt(br.readLine().trim());
        for (int tc = 1; tc <= T; tc++) {
            int N = Integer.parseInt(br.readLine().trim());
            ArrayList<Atom> atoms = new ArrayList<>(N);
            for (int i = 0; i < N; i++) {
                StringTokenizer st = new StringTokenizer(br.readLine());
                int x = Integer.parseInt(st.nextToken());
                int y = Integer.parseInt(st.nextToken());
                int d = Integer.parseInt(st.nextToken());
                int k = Integer.parseInt(st.nextToken());
                atoms.add(new Atom(x * 2, y * 2, d, k)); // 좌표 2배 스케일
            }

            long total = 0L;

            ArrayList<Atom> next = new ArrayList<>(atoms.size());

            // 틱(0.5초): 이동 -> 집계 -> 충돌 합 -> 생존자만 유지
            while (!atoms.isEmpty()) {
                // 좌표별 [count, sumEnergy]
                HashMap<Pos, int[]> cntSum = new HashMap<>(atoms.size() * 2);

                next.clear();

                // 1) 이동 + 범위 체크 + 좌표 집계
                for (int i = 0, sz = atoms.size(); i < sz; i++) {
                    Atom a = atoms.get(i);
                    int nx = a.pos.x + dx[a.d];
                    int ny = a.pos.y + dy[a.d];

                    if (nx < -LIM || nx > LIM || ny < -LIM || ny > LIM) continue;

                    // 같은 좌표만 갱신
                    a.pos.x = nx;
                    a.pos.y = ny;

                    next.add(a);

                    int[] cs = cntSum.get(a.pos);
                    if (cs == null) {
                        cs = new int[2];
                        cntSum.put(a.pos, cs);
                    }
                    cs[0] += 1;
                    cs[1] += a.k;
                }

                if (next.isEmpty()) break;

                // 2) 충돌좌표 합산
                long released = 0;
                for (int[] cs : cntSum.values()) {
                    if (cs[0] >= 2) released += cs[1];
                }
                total += released;

                // 3) 충돌좌표에 있던 원자 제외
                if (released > 0) {
                    atoms.clear();
                    for (int i = 0, sz = next.size(); i < sz; i++) {
                        Atom a = next.get(i);
                        int[] cs = cntSum.get(a.pos);
                        if (cs[0] < 2) atoms.add(a); // 생존자만 유지
                    }
                } else {
                    // 충돌 없으면 다음 틱 진행
                    ArrayList<Atom> tmp = atoms;
                    atoms = next;
                    next = tmp;
                }
            }

            out.append('#').append(tc).append(' ').append(total).append('\n');
        }

        System.out.print(out);
    }
}
