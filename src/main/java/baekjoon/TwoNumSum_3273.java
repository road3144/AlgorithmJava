package baekjoon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class TwoNumSum_3273 {

	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		int n = Integer.parseInt(st.nextToken());
		
		int[] arr = new int[n];
		st =  new StringTokenizer(br.readLine());
		
		for (int i = 0; i < n; i++) {
			arr[i] = Integer.parseInt(st.nextToken());
		}
		
		st =  new StringTokenizer(br.readLine());
		int target = Integer.parseInt(st.nextToken());
		
		Arrays.sort(arr);
		int start = 0, end = n-1, ans = 0;
		while(start < end) {
			int sum = arr[start] + arr[end];
			if (sum == target) {
				ans++;
				start++;
				end--;
			} else if (sum < target)
				start++;
			else
				end--;
		}
		
		System.out.println(ans);
	}

}
