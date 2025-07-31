package baekjoon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;

public class HideSeek4_13913 {
	static int n, k;
	static int ans;
	static int[] dx = {-1, 1, 0};
	static int[] dist, prev;
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());
		dist = new int[100001];
		prev = new int[100001];
		Queue<Integer> q = new LinkedList<>();
		q.add(n);
		
		while (!q.isEmpty()) {
			int now = q.poll();
			if (now == k) {
				ans = dist[now];
				break;
			}
			for (int i=0; i <3; i++) {
				int ns = 0;
				if (dx[i] == 0)
					ns = now * 2;
				else
					ns = now + dx[i];
				
				if (ns < 0 || ns > 100000)
					continue;
				if (dist[ns] == 0) {
					dist[ns] = dist[now] + 1;
					prev[ns] = now;
					q.add(ns);
				}
			}
		}
		prev[n] = n;
		System.out.println(ans);
		
		int cur = k;
		List<Integer> list = new ArrayList<>();

		while (cur != n) {
			list.add(cur);
			cur = prev[cur];
		}
		list.add(cur);
		Collections.reverse(list);
		for (int c : list) {
			System.out.print(c + " ");
		}
		System.out.print("\n");
	}

}
