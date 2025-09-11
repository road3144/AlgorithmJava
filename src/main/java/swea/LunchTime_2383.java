package swea;

import java.io.*;
import java.util.*;

/**
 * SWEA - 점심 식사시간 (사람 ≤ 10, 계단 = 2)
 */
public class LunchTime_2383 {
    static class Pos {
        int r, c;
        Pos(int r, int c) { this.r = r; this.c = c; }
    }
    static class Stair {
        int r, c, k; // 위치와 계단 길이 K
        Stair(int r, int c, int k) { this.r = r; this.c = c; this.k = k; }
    }

    static int N;
    static List<Pos> people;
    static Stair[] stairs;
    static int P;          // 사람 수
    static int[][] dist;   // dist[p][s] : p번 사람이 s번 계단 입구까지의 맨해튼 거리

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder out = new StringBuilder();
        int T = Integer.parseInt(br.readLine().trim());

        for (int tc = 1; tc <= T; tc++) {
            N = Integer.parseInt(br.readLine().trim());
            people = new ArrayList<>();
            List<Stair> stairList = new ArrayList<>(2);

            for (int r = 0; r < N; r++) {
                StringTokenizer st = new StringTokenizer(br.readLine());
                for (int c = 0; c < N; c++) {
                    int v = Integer.parseInt(st.nextToken());
                    if (v == 1) {
                        people.add(new Pos(r, c));
                    } else if (v >= 2) {
                        stairList.add(new Stair(r, c, v));
                    }
                }
            }

            stairs = new Stair[2];
            stairs[0] = stairList.get(0);
            stairs[1] = stairList.get(1);

            P = people.size();
            dist = new int[P][2];
            for (int i = 0; i < P; i++) {
                Pos p = people.get(i);
                for (int s = 0; s < 2; s++) {
                    Stair st = stairs[s];
                    dist[i][s] = Math.abs(p.r - st.r) + Math.abs(p.c - st.c);
                }
            }

            int answer = solve();
            out.append('#').append(tc).append(' ').append(answer).append('\n');
        }
        System.out.print(out.toString());
    }

    // 모든 배정(bitmask)을 탐색: 0=계단0, 1=계단1
    static int solve() {
        int best = Integer.MAX_VALUE;
        int totalMasks = 1 << P;

        for (int mask = 0; mask < totalMasks; mask++) {
            // 각 계단별 도착시간 목록 준비
            List<Integer> arr0 = new ArrayList<>();
            List<Integer> arr1 = new ArrayList<>();
            for (int i = 0; i < P; i++) {
                if (((mask >> i) & 1) == 0) arr0.add(dist[i][0]);
                else arr1.add(dist[i][1]);
            }

            int t0 = simulateStair(arr0, stairs[0].k);
            int t1 = simulateStair(arr1, stairs[1].k);
            int done = Math.max(t0, t1);
            if (done < best) best = done;
        }
        return best;
    }

    // 한 계단에 대해 도착시간 리스트와 계단 길이 K가 주어졌을 때 완료시간 계산
    static int simulateStair(List<Integer> arrivals, int K) {
        if (arrivals.isEmpty()) return 0;

        Collections.sort(arrivals);

        // 최소 완료시각 힙: 현재 계단 위에 있는 사람들의 "완료시각" 저장
        PriorityQueue<Integer> onStair = new PriorityQueue<>();
        int lastFinish = 0;

        for (int a : arrivals) {
            int t = a + 1; // 도착 후 1분 뒤 내려가기 시작 가능

            // t 시각 이전/동시에 이미 끝난 사람들 정리
            while (!onStair.isEmpty() && onStair.peek() <= t) {
                onStair.poll();
            }

            if (onStair.size() < 3) {
                int finish = t + K;
                onStair.add(finish);
                if (finish > lastFinish) lastFinish = finish;
            } else {
                // 가득 찼다면 가장 빨리 끝나는 사람의 완료시각까지 대기
                int earliest = onStair.poll(); // 이 시각부터 한 칸 비게 됨
                // 같은 시각에 끝나는 사람들 추가 정리
                while (!onStair.isEmpty() && onStair.peek() <= earliest) {
                    onStair.poll();
                }
                int finish = earliest + K; // earliest 시각에 바로 진입
                onStair.add(finish);
                if (finish > lastFinish) lastFinish = finish;
            }
        }
        return lastFinish;
    }
}
