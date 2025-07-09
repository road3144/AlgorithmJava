package baekjoon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class NM2_15650 {
	static int n, m;
	static int[] arr;
	static StringBuilder sb = new StringBuilder();

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		arr = new int[m];
		bfs(0, 0);
		System.out.println(sb);
	}

	static void bfs(int depth, int start) {
		if (depth == m) {
			for (int data : arr) {
				sb.append(data).append(" ");
			}
			sb.append("\n");
			return;
		}

		for(int i = start; i < n; i++){
			arr[depth] = i+1;
			bfs(depth+1, i + 1);
		}
	}
}
