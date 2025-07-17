package baekjoon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class NM12_15666 {
	static int n, m;
	static int[] arr, ans;
	static StringBuilder sb = new StringBuilder();

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		arr = new int[n];
		ans = new int[m];
		st = new StringTokenizer(br.readLine());
		for (int i = 0; i < n; i++) {
			arr[i] = Integer.parseInt(st.nextToken());
		}
		Arrays.sort(arr);
		dfs(0, 0);
		System.out.println(sb);
	}

	public static void dfs(int depth, int start) {
		if (depth == m) {
			for (int data : ans) {
				sb.append(data).append(" ");
			}
			sb.append("\n");
			return;
		}
		int prev = 0;
		for (int i = start; i < n; i++) {
			if (prev != arr[i]) {
				ans[depth] = arr[i];
				prev = arr[i];
				dfs(depth + 1, i);
			}
		}
	}
}
