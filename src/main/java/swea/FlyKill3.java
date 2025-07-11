package swea;

import java.util.Scanner;

public class FlyKill3 {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int T;
		T=sc.nextInt();

		for(int test_case = 1; test_case <= T; test_case++) {
			int n = sc.nextInt();
			int m = sc.nextInt();
			int ans = 0;
			int[][] graph = new int[n][n];

			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					graph[i][j] = sc.nextInt();
				}
			}

			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					int p = graph[i][j];
					int x = graph[i][j];
					for (int k = 1; k < m; k++) {
						if (i+k < n)
							p += graph[i + k][j];
						if (i-k >= 0)
							p += graph[i - k][j];
						if (j+k < n)
							p += graph[i][j + k];
						if (j-k >= 0)
							p += graph[i][j - k];
						if (i-k >= 0 && j-k >= 0)
							x += graph[i - k][j - k];
						if (i-k >= 0 && j+k < n)
							x += graph[i - k][j + k];
						if (i+k < n && j-k >= 0)
							x += graph[i + k][j - k];
						if (i+k < n && j+k < n)
							x += graph[i + k][j + k];
					}
					ans = Math.max(ans, p);
					ans = Math.max(ans, x);
				}
			}
			System.out.println("#" + test_case + " " + ans);
		}
	}
}
