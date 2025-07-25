package baekjoon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class NumArraySum {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		int n = Integer.parseInt(st.nextToken());
		int target = Integer.parseInt(st.nextToken());
		int[] arr = new int[n];
		st =  new StringTokenizer(br.readLine());
		
		for (int i = 0; i < n; i++) {
			arr[i] = Integer.parseInt(st.nextToken());
		}
		
		int start = 0, end = 0, ans = 0, sum=arr[0];
		while(start <= end && end < n) {

			if (sum == target) {
				ans++;
				if (end + 1 == n)
					break;
				sum += arr[++end];
			} else if (sum < target) {
				if (end + 1 == n)
					break;
				sum += arr[++end];
			} else if (sum > target) {
				if (start == end) {
					if (end + 1 == n)
						break;
					sum += arr[++end];
				} else
					sum -= arr[start++];
			}
		}
		
		System.out.println(ans);
	}

}
