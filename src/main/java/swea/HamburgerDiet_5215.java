package swea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class HamburgerDiet_5215 {
	static int T;
	static int n, l, ans;
	static int[][] data;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		T = Integer.parseInt(st.nextToken());
		for (int test_case = 1; test_case <= T; test_case++) {
			st = new StringTokenizer(br.readLine());
			n = Integer.parseInt(st.nextToken());
			l = Integer.parseInt(st.nextToken());
			data = new int[n][2];
			ans = 0;

			for (int i = 0; i < n; i++) {
				st = new StringTokenizer(br.readLine());
				data[i][0] = Integer.parseInt(st.nextToken());
				data[i][1] = Integer.parseInt(st.nextToken());
			}
			dfs(0, 0, 0);
			System.out.println("#" + test_case + " " + ans);
		}
	}

	static void dfs(int depth, int kal, int tot) {
		if (kal > l) return;
		ans = Math.max(tot, ans);
		if (depth == n) return;
		dfs(depth+1, kal + data[depth][1], tot + data[depth][0]);
		dfs(depth+1, kal, tot);
	}
}
