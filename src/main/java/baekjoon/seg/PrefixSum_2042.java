package baekjoon.seg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class PrefixSum_2042 {
    static int n, m, k;
    static long[] arr = new long[1000000];
    static long[] seg = new long[4000000];
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());

        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            arr[i] = Long.parseLong(st.nextToken());
        }

        build(0, n-1, 0);

        for (int i = 0; i < m+k; i++) {
            st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            long c = Long.parseLong(st.nextToken());
            if (a == 1){
                update(0, n-1, 0, c, b-1);
            } else {
                System.out.println(query(0, n-1, 0, b-1, (int)c-1));
            }
        }

    }

    static void build(int start, int end, int idx){
        if (start == end) {
            seg[idx] = arr[start];
            return;
        }

        int mid = (start + end) >> 1;

        build(start, mid, idx*2+1);
        build(mid+1, end, idx*2+2);

        seg[idx] = seg[idx*2+1] + seg[idx*2+2];
    }

    static long query(int start, int end, int idx, int left, int right){
        if(end < left || right < start) return 0;
        if (left <= start && end <= right) return seg[idx];

        int mid = (start + end) >> 1;
        return query(start, mid, idx*2+1, left, right) + query(mid+1, end, idx*2+2, left, right);
    }

    static void update(int start, int end, int idx, long value, int target){
        if(end < target || target < start) return;

        if(start == end) {
            seg[idx] = value;
            return;
        }

        int mid = (start + end) >> 1;
        update(start, mid, idx*2+1, value, target);
        update(mid+1, end, idx*2+2, value, target);
        seg[idx] = seg[idx*2+1] + seg[idx*2+2];
    }
}
