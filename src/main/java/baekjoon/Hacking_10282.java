package baekjoon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Hacking_10282 {

	static List<List<int[]>> map;
	static int t, n, d, c;
	static int[] distance;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		t = Integer.parseInt(st.nextToken());
		for (int test = 0; test < t; test++) {
			int ans = 0;
			int cnt = 0;
			st = new StringTokenizer(br.readLine());
			n = Integer.parseInt(st.nextToken());
			d = Integer.parseInt(st.nextToken());
			c = Integer.parseInt(st.nextToken());
			map = new ArrayList<>();
			distance = new int[n+1];
			for (int i = 0; i < n+1; i++) {
				map.add(new ArrayList<>());
			}
			Arrays.fill(distance, Integer.MAX_VALUE);
			distance[c] = 0;

			for (int i = 0; i < d; i++) {
				st = new StringTokenizer(br.readLine());
				int a = Integer.parseInt(st.nextToken());
				int b = Integer.parseInt(st.nextToken());
				int s = Integer.parseInt(st.nextToken());
				map.get(b).add(new int[] {s, a});
			}
			dijkstra(c);
			for (int i = 0; i < n + 1; i++) {
				if (distance[i] != Integer.MAX_VALUE) {
					ans = Math.max(ans, distance[i]);
					cnt++;
				}
			}
			System.out.println(cnt + " " + ans);
		}
	}

	static void dijkstra(int start) {
		PriorityQueue<int[]> pq = new PriorityQueue<>((Comparator.comparingInt(o -> o[0])));
		pq.add(new int[] {0, start});
		while (!pq.isEmpty()) {
			int[] t = pq.poll();
			int dist = t[0], now = t[1];

			if (dist > distance[now])
				continue;
			for (int[] nt : map.get(now)) {
				int cost = nt[0], next = nt[1];
				if (dist + cost < distance[next]) {
					distance[next] = dist + cost;
					pq.add(new int[] {distance[next], next});
				}
			}
		}
	}
}
