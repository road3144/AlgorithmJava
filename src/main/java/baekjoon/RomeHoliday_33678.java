package baekjoon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class RomeHoliday_33678 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int n = Integer.parseInt(st.nextToken());
        int k = Integer.parseInt(st.nextToken());
        int x = Integer.parseInt(st.nextToken());

        int[] salaries = new int[n];
        st = new StringTokenizer(br.readLine());
        int sum = 0;
        for (int i = 0; i < n; i++) {
            salaries[i] = Integer.parseInt(st.nextToken());
            sum += salaries[i] * x;
        }
        int ans = 0;
        sum -= salaries[n - 1] * x;
        int start = n-1, end = n-1;
        while (start <= end) {
            if (sum >= k && start > 0) {
                ans = Math.max(ans, end - start + 1);
                start--;
                sum -= salaries[start] * x;
                if (sum >= k)
                    ans = Math.max(ans, end - start + 1);
            } else {
                if (start == end && start > 0) {
                    start--;
                    sum -= salaries[start] * x;
                    sum += salaries[end];
                    end--;
                    if (sum >= k)
                        ans = Math.max(ans, end - start + 1);
                    continue;
                }
                sum += salaries[end];
                end--;
                if (sum >= k)
                    ans = Math.max(ans, end - start + 1);
            }
        }
        if (ans == 0)
            System.out.println(-1);
        else
            System.out.println(ans);
    }
}
