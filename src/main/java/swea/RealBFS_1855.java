package swea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.*;

public class RealBFS_1855 {

    static int[] parent, level;
    static List<Integer>[] graph;
    static int log;
    static int[][] up;

    public static void main(String[] args) throws Exception {
        FastScanner fs = new FastScanner(System.in);
        int T = fs.nextInt();

        for (int tc = 1; tc <= T; tc++) {

            int n = fs.nextInt();
            parent = new int[n+1];
            level = new int[n+1];
            graph = new List[n+1];
            for (int i = 1; i < n+1; i++) {
                graph[i] = new ArrayList<>();
            }
            if (n == 1) {
                System.out.println("#" + tc + " " + 0);
                continue;
            }

            parent[1] = 1;
            level[1] = 1;

            for (int i = 2; i < n+1; i++) {
                int a = fs.nextInt();
                parent[i] = a;
                level[i] = level[a] + 1;
                graph[a].add(i);
            }
            // log 값 설정
            log = 1;
            while ((1 << log) <= n) log++;

            up = new int[log][n+1];

            for (int i = 1; i < n+1; i++) {
                up[0][i] = parent[i];
            }

            for (int k = 1; k < log; k++) {
                for (int i = 1; i < n + 1; i++) {
                    up[k][i] = up[k-1][up[k-1][i]];
                }
            }

            Queue<Integer> q = new ArrayDeque<>();
            q.add(1);
            int last = 1;
            long ans = 0;
            while (!q.isEmpty()){
                int now = q.poll();

                if (now != 1) {
                    int com = find(now, last);
                    ans += (level[last] - level[com]) + (level[now] - level[com]);
                    last = now;
                }

                for (int a : graph[now])
                    q.add(a);
            }
            System.out.println("#" + tc + " " + ans);
        }
    }

    static int find(int x, int y){
        if (level[x] < level[y]){
            int t = x;
            x = y;
            y =t;
        }

        int dif = level[x] - level[y];
        for (int k = 0; k < log; k++) {
            if (((dif >> k) & 1) == 1) x = up[k][x];
        }

        if (x == y) return x;

        for (int k = log-1; k >= 0; k--) {
            if (up[k][x] != up[k][y]){
                x = up[k][x];
                y = up[k][y];
            }
        }
        return up[0][x];
    }

    // 공백/개행/빈줄/EOF에 강한 입력기
    static class FastScanner {
        private final InputStream in;
        private final byte[] buffer = new byte[1 << 16];
        private int ptr = 0, len = 0;

        FastScanner(InputStream is) { in = is; }

        private int read() throws IOException {
            if (ptr >= len) {
                len = in.read(buffer);
                ptr = 0;
                if (len <= 0) return -1;
            }
            return buffer[ptr++];
        }

        int nextInt() throws IOException {
            int c;
            do {
                c = read();
                if (c == -1) return Integer.MIN_VALUE; // 입력 끝(정상 데이터면 안 옴)
            } while (c <= ' ');

            int sign = 1;
            if (c == '-') { sign = -1; c = read(); }

            int val = 0;
            while (c > ' ') {
                val = val * 10 + (c - '0');
                c = read();
            }
            return val * sign;
        }
    }

}
