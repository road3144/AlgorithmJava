package baekjoon;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.Iterator;

public class ChickenDelivery_15686 {
	static int n, m, ans = Integer.MAX_VALUE;
	static boolean[] visited;
	static ArrayList<int[]> chicken = new ArrayList<>();
	static ArrayList<int[]> house = new ArrayList<>();

	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		
		for (int i = 0; i < n; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < n; j++) {
				int k = Integer.parseInt(st.nextToken());
				if (k == 1)
					house.add(new int[] {i, j});
				if (k == 2)
					chicken.add(new int[] {i, j});

			}
		}
		visited = new boolean[chicken.size()];
		dfs(0, 0);
		System.out.println(ans);
	}
	
	static void dfs(int depth, int start) {
		if (depth == m) {
			int total = getChickenDist();
			ans = Math.min(ans, total);
		}
		
		for (int i = start; i < chicken.size(); i++) {
			if (!visited[i]) {
				visited[i] = true;
				dfs(depth + 1, i);
				visited[i] = false;
			}
		}
	}
	
	static int getChickenDist() {
		int total = 0;
		for (int[] h : house) {
			int min = Integer.MAX_VALUE;
			for (int i = 0; i < chicken.size(); i++) {
				if (visited[i]) {
					int[] c = chicken.get(i);
					min = Math.min(min, Math.abs(h[0] - c[0]) + Math.abs(h[1] - c[1]));
				}
			}
			total += min;
		}
		return total;
	}

}
