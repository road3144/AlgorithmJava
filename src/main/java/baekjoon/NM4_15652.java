package baekjoon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class NM4_15652 {
	static int n, m;
	static int[] arr;
	static StringBuilder sb = new StringBuilder();

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		arr = new int[m];

		bfs(0, 1);
		System.out.println(sb);
	}

	public static void bfs(int depth, int start) {
		if (depth == m) {
			for (int data : arr) {
				sb.append(data).append(" ");
			}
			sb.append("\n");
			return;
		}

		for (int i = start; i < n+1; i++) {
			arr[depth] = i;
			bfs(depth + 1, i);
		}
	}
}
