package baekjoon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

class Edge implements Comparable<Edge>{
	int node, price;
	
	Edge(int b, int p){
		node = b;
		price = p;
	}
	

	@Override
	public int compareTo(Edge o) {
		// TODO Auto-generated method stub
		return this.price - o.price;
	}
}

public class LeastCost_1916 {
	static int n, m, start, goal;
	static PriorityQueue<Edge> pq = new PriorityQueue<>();
	static int[] distance;
	static List<Edge>[] graph;
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		distance = new int[n+1];
		Arrays.fill(distance, Integer.MAX_VALUE);
		graph = new ArrayList[n+1];
		for (int i = 1; i <= n; i++) {
			graph[i] = new ArrayList<Edge>();
		}
		st = new StringTokenizer(br.readLine());
		m = Integer.parseInt(st.nextToken());
		for (int i = 0; i < m; i++) {
			st = new StringTokenizer(br.readLine());
			int a = Integer.parseInt(st.nextToken());
			int b = Integer.parseInt(st.nextToken());
			int c = Integer.parseInt(st.nextToken());
			graph[a].add(new Edge(b, c));
		}
		st = new StringTokenizer(br.readLine());
		start = Integer.parseInt(st.nextToken());
		goal = Integer.parseInt(st.nextToken());
		
		pq.offer(new Edge(start, 0));
		
		while(!pq.isEmpty()) {
			Edge e = pq.poll();
			int now = e.node, dist = e.price;
			if (distance[now] < dist)
				continue;
			for (Edge next : graph[now]) {
				int cost = dist + next.price;
				if (cost < distance[next.node]) {
					distance[next.node] = cost;
					pq.offer(new Edge(next.node, cost));
				}
			}
		}
		System.out.println(distance[goal]);
	}
}
