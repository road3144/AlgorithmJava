package baekjoon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.BufferUnderflowException;
import java.util.StringTokenizer;

public class CycleGame_20040{

	static int n, m;
	static int[] parent;
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		int ans = 0;
		parent = new int[n];
		for (int i=0; i < n; i++) {
			parent[i] = i;
		}
		
		for (int i=0; i < m; i++) {
			st = new StringTokenizer(br.readLine());
			int x = Integer.parseInt(st.nextToken());
			int y = Integer.parseInt(st.nextToken());
			if (find(x) != find(y)) {
				union(x, y);
				ans++;
			} else {
				ans++;
                System.out.println(ans);
                return;
			}
		}
        System.out.println(0);

	}
	
	static int find(int a) {
		if (a == parent[a]) return a;
		return parent[a] = find(parent[a]);
	}
	
	static void union(int a, int b) {
		a = find(a);
		b = find(b);
		if (a < b) parent[b] = a;
		if (a > b) parent[a] = b;
	}
}
