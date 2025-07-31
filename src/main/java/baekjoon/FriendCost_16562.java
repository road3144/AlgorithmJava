package baekjoon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class FriendCost_16562 {

	static int n, m, k;
	static int[] parent, cost;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());
		parent = new int[n + 1];
		cost = new int[n + 1];
		for (int i = 1; i < n + 1; i++) {
			parent[i] = i;
		}
		st = new StringTokenizer(br.readLine());
		for (int i = 1; i < n + 1; i++) {
			cost[i] = Integer.parseInt(st.nextToken());
			;
		}
		for (int i = 0; i < m; i++) {
			st = new StringTokenizer(br.readLine());
			int a = Integer.parseInt(st.nextToken());
			int b = Integer.parseInt(st.nextToken());
			union(a, b);
		}

		Map<Integer, Integer> friends = new HashMap<>();
		int ans = 0;
		for (int i = 1; i < n + 1; i++) {
			int pa = find(i);
			if (!friends.containsKey(pa)) {
				ans += cost[i];
				friends.put(pa, cost[i]);
			} else {
				if (cost[i] < friends.get(pa)) {
					ans -= friends.get(pa) - cost[i];
					friends.put(pa, cost[i]);
				}
			}
		}

		if (ans <= k)
			System.out.println(ans);
		else
			System.out.println("Oh no");

	}

	static int find(int a) {
		if (a == parent[a])
			return a;
		return parent[a] = find(parent[a]);
	}

	static void union(int a, int b) {
		a = find(a);
		b = find(b);
		if (a <= b) {
			parent[b] = a;
		} else {
			parent[a] = b;
		}
	}
}
