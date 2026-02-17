package baekjoon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class SumNumbers_2015 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int n = Integer.parseInt(st.nextToken());
        int k = Integer.parseInt(st.nextToken());
        long answer = 0;
        int[] sum = new int[n+1];
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, 1);
        st = new StringTokenizer(br.readLine());

        for (int i=1; i < n+1; i++) {
            sum[i] = sum[i-1] + Integer.parseInt(st.nextToken());
        }

        for (int j=1; j < n+1; j++){
            answer += map.getOrDefault(sum[j]-k, 0);
            map.put(sum[j], map.getOrDefault(sum[j], 0) + 1);
        }

        System.out.println(answer);
    }
}
