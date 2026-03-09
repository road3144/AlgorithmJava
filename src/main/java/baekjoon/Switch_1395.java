package baekjoon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Switch_1395 {

    static int n;
    static int[] tree, lazy;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        tree = new int[(n+1) * 4];
        lazy = new int[(n+1) * 4];

        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int o = Integer.parseInt(st.nextToken());
            int s = Integer.parseInt(st.nextToken());
            int t = Integer.parseInt(st.nextToken());
            if (o == 0) updateRange(1, n, 1, s, t);
            else System.out.println(sum(1, n, 1, s, t));
        }

    }

    static void push(int start, int end, int idx){
        if (lazy[idx] != 0){
            tree[idx] = (end - start + 1) - tree[idx];
            if (start != end){
                lazy[idx*2] = (lazy[idx*2] + 1) % 2;
                lazy[idx*2+1] = (lazy[idx*2+1] + 1) % 2;
            }
            lazy[idx] = 0;
        }
    }

    static void updateRange(int start, int end, int idx, int left, int right){
        push(start, end, idx);
        if (end < left || right < start) return;
        if (left <= start && end <= right){
            tree[idx] = (end - start + 1) - tree[idx];
            if (start != end){
                lazy[idx*2] = (lazy[idx*2] + 1) % 2;
                lazy[idx*2+1] = (lazy[idx*2+1] + 1) % 2;
            }
            return;
        }
        int mid = (start + end) / 2;
        updateRange(start, mid, idx*2, left, right);
        updateRange(mid+1, end, idx*2+1, left, right);
        tree[idx] = tree[idx*2] + tree[idx*2+1];
    }

    static int sum(int start, int end, int idx, int left, int right){
        push(start, end, idx);
        if (end < left || right < start) return 0;
        if (left <= start && end <= right) return tree[idx];
        int mid = (start + end) / 2;
        return sum(start, mid, idx*2, left, right) + sum(mid+1, end, idx*2+1, left, right);
    }

}
