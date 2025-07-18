package swea;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.StringTokenizer;

public class JanghoonHighShelf {
	static int T;
	static int n, m, ans;
	static int[] arr;

	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		T = Integer.parseInt(st.nextToken());
		for (int test_case = 1; test_case <= T; test_case++) {
			st = new StringTokenizer(br.readLine());
			n = Integer.parseInt(st.nextToken());
			m = Integer.parseInt(st.nextToken());
			arr = new int[n];
			ans = Integer.MAX_VALUE;
			st = new StringTokenizer(br.readLine());
			for (int i = 0; i < n; i++) {
				arr[i] = Integer.parseInt(st.nextToken());
			}
			Arrays.sort(arr);
			reverse(arr);

			dfs(0, 0);
			System.out.println("#" + test_case + " " + (ans - m));
		}
	}

	static void dfs(int depth, int total) {
		if (total >= m) {
			ans = Math.min(total, ans);
			return;
		}
		if (depth == n || total >= ans) {
			return;
		}
		dfs(depth + 1, total + arr[depth]);
		dfs(depth + 1, total);

	}

	static void reverse(int[] arr) {
		for (int i = 0, j = arr.length - 1; i < j; i++, j--) {
			int tmp = arr[i];
			arr[i] = arr[j];
			arr[j] = tmp;
		}
	}
}
