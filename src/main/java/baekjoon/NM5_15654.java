package baekjoon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class NM5_15654 {
	static int n, m;
	static int[] arr;
	static int[] ans;
	static boolean[] visited;
	static StringBuilder sb = new StringBuilder();

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		st = new StringTokenizer(br.readLine());

		arr = new int[n];
		visited = new boolean[n];
		ans = new int[m];
		for (int i = 0; i < n; i++) {
			arr[i] = Integer.parseInt(st.nextToken());
		}
		Arrays.sort(arr);

		bfs(0);
		System.out.println(sb);
	}

	public static void bfs(int depth) {
		if (depth == m) {
			for (int data : ans) {
				sb.append(data + " ");
			}
			sb.append("\n");
			return;
		}

		for (int i = 0; i < n; i++) {
			if (!visited[i]) {
				visited[i] = true;
				ans[depth] = arr[i];
				bfs(depth + 1);
				visited[i] = false;
			}
		}
	}
}
