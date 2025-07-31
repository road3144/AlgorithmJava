package baekjoon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.StringTokenizer;

public class Exercise_1956 {
	
	static int[][] map;
	static int n, m;
	static final int INF = 8000000;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		map = new int[n+1][n+1];
		int ans = INF;

		for (int i=0; i < n+1; i++) {
			Arrays.fill(map[i], INF);
		}
		
		for (int i = 0; i < m; i++) {
			st = new StringTokenizer(br.readLine());
			int s = Integer.parseInt(st.nextToken()); 
			int e = Integer.parseInt(st.nextToken()); 
			int c = Integer.parseInt(st.nextToken());
			map[s][e] = c;
		}

		for (int k=1; k < n+1; k++) {
			for (int i=1; i < n+1; i++) {
				for (int j=1; j < n+1; j++) {
					if (i == j)
						continue;
					if (map[i][j] > map[i][k] + map[k][j])
						map[i][j] = map[i][k] + map[k][j];

				}
			}
		}
		
		for (int i=1; i <n+1; i++) {
			for (int j=1; j<n+1; j++) {
				if (i == j)
					continue;
				if (map[i][j] != INF && map[j][i] != INF)
					ans = Math.min(ans, map[i][j] + map[j][i]);
			}
		}
		if (ans == INF) {
			System.out.println(-1);
		} else {
			System.out.println(ans);
		}
		
	}

}
