package baekjoon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class ConveyorRobot_20055 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int cnt = 0;

        int n = Integer.parseInt(st.nextToken());
        int k = Integer.parseInt(st.nextToken());
        st = new StringTokenizer(br.readLine());

        int[] arr = new int[2 * n];
        boolean[] robots = new boolean[n];

        for (int i = 0; i < 2 * n; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }

        int ans = 0;
        int start = 0;
        while (true) {
            ans++;

            //회전
            start = (start-1 + 2*n) % (2*n);
            for (int i = n - 1; i >= 1; i--) {
                robots[i] = robots[i - 1];
            }
            robots[0] = false;
            robots[n - 1] = false;

            //  로봇 이동
            for (int i = n - 2; i >= 0; i--) {
                if (!robots[i]) continue;
                if (robots[i + 1]) continue;

                int nextBeltIdx = (start + i + 1) % (2 * n);
                if (arr[nextBeltIdx] <= 0) continue;

                // 이동
                robots[i] = false;
                robots[i + 1] = true;

                arr[nextBeltIdx]--;
                if (arr[nextBeltIdx] == 0) cnt++;
            }
            robots[n-1] = false;

            // 3) 로봇 올리기
            if (!robots[0] && arr[start] > 0) {
                robots[0] = true;
                arr[start]--;
                if (arr[start] == 0) cnt++;
            }
            if (cnt >= k) break;
        }

        System.out.println(ans);
    }
}
