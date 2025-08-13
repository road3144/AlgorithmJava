package swea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;

public class HighMountain_4796 {
    static int T, n;
    static int[] arr;
    static StringBuilder sb = new StringBuilder();

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        T = sc.nextInt();
        for (int test_case = 1; test_case <= T; test_case++) {
            n = sc.nextInt();
            int ans = 0;
            arr = new int[n];
            for (int i = 0; i < n; i++) {
                arr[i] = sc.nextInt();
            }
            int[] tmp = new int[n];
            int[] tmp2 = new int[n];
            int cnt = 0;
            int cnt2 = 0;
            for (int i = 1; i < n; i++) {
                if (arr[i] > arr[i-1]){
                    tmp[i] = ++cnt;
                } else {
                    cnt = 0;
                }
            }
            for (int i = n-1; i >= 1; i--) {
                if (arr[i] < arr[i-1]){
                    tmp2[i-1] = ++cnt2;
                } else {
                    cnt2 = 0;
                }
            }
            for (int i = 0; i < n; i++) {
                if (tmp[i] != 0 && tmp2[i] != 0)
                    ans += tmp[i] * tmp2[i];
            }
            sb.append('#').append(test_case).append(' ').append(ans).append('\n');
        }
        System.out.println(sb);
    }
}
