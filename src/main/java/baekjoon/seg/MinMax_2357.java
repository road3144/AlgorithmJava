package baekjoon.seg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class MinMax_2357 {
    static int n, m;
    static int[] arr = new int[100000];
    static int[] minSeg = new int[400000];
    static int[] maxSeg = new int[400000];

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            arr[i] = Integer.parseInt(st.nextToken());
        }

        min_build(0, n-1, 0);
        max_build(0, n-1, 0);

        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            System.out.println(min_find(0, n-1, 0, a-1, b-1) + " " + max_find(0, n-1, 0, a-1, b-1));
        }

    }

    static void min_build(int start, int end, int idx){
        if (start == end){
            minSeg[idx] = arr[start];
            return;
        }
        int mid = (start + end) >> 1;
        min_build(start, mid, idx*2+1);
        min_build(mid+1, end, idx*2+2);
        minSeg[idx] = Math.min(minSeg[idx * 2 + 1], minSeg[idx * 2 + 2]);
    }

    static void max_build(int start, int end, int idx){
        if (start == end){
            maxSeg[idx] = arr[start];
            return;
        }
        int mid = (start + end) >> 1;
        max_build(start, mid, idx*2+1);
        max_build(mid+1, end, idx*2+2);
        maxSeg[idx] = Math.max(maxSeg[idx * 2 + 1], maxSeg[idx * 2 + 2]);
    }

    static int min_find(int start, int end, int idx, int left, int right){
        if (end < left || right < start) return Integer.MAX_VALUE;
        if (left <= start && end <= right){
            return minSeg[idx];
        }
        int mid = (start + end) >> 1;
        int n1 = min_find(start, mid, idx*2+1, left, right);
        int n2 =  min_find(mid+1, end, idx*2+2, left, right);
        return Math.min(n1, n2);
    }

    static int max_find(int start, int end, int idx, int left, int right){
        if (end < left || right < start) return 0;
        if (left <= start && end <= right){
            return maxSeg[idx];
        }
        int mid = (start + end) >> 1;
        return Math.max(max_find(start, mid, idx*2+1, left, right), max_find(mid+1, end, idx*2+2, left, right));
    }

}
