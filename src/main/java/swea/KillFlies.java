package swea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class KillFlies {
	static int T;
	static int n, m, ans;
	static int[][] map;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		T = Integer.parseInt(st.nextToken());
		for (int test_case = 1; test_case <= T; test_case++) {
			st = new StringTokenizer(br.readLine());
			n = Integer.parseInt(st.nextToken());
			m = Integer.parseInt(st.nextToken());
			map = new int[n][n];
			ans = 0;
			for (int i = 0; i < n; i++) {
				st = new StringTokenizer(br.readLine());
				for (int j = 0; j < n; j++) {
					map[i][j] = Integer.parseInt(st.nextToken());
				}
			}

			for (int i = 0; i < n-m+1; i++) {
				for (int j = 0; j < n-m+1; j++) {
					int cnt = 0;
					for (int x = 0; x < m; x++) {
						for (int y = 0; y < m; y++) {
							cnt += map[i+x][j+y];
						}
					}
					ans = Math.max(ans, cnt);
				}
			}
			System.out.println("#" + test_case + " " + ans);
		}
	}
}
