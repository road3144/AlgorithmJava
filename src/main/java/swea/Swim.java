package swea;

import java.util.PriorityQueue;
import java.util.Scanner;

public class Swim {
	static int n, startX, startY, goalX, goalY;
	static PriorityQueue<Node> pq;
	static int[][] distance;
	static int[] dy = {-1, 1, 0, 0};
	static int[] dx = {0, 0, 1, -1};

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int T;
		T=sc.nextInt();
		for(int test_case = 1; test_case <= T; test_case++) {
			n = sc.nextInt();
			int[][] graph = new int[n][n];
			distance = new int[n][n];
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					graph[i][j] = sc.nextInt();
					distance[i][j] = Integer.MAX_VALUE;
				}
			}
			startX = sc.nextInt();
			startY = sc.nextInt();
			goalX = sc.nextInt();
			goalY = sc.nextInt();

			System.out.println("#" + test_case + " " + bfs(graph));
		}
	}

	public static int bfs(int[][] graph) {
		pq = new PriorityQueue<>();
		pq.offer(new Node(startX, startY, 0));

		while (!pq.isEmpty()) {
			Node now = pq.poll();
			int x = now.x;
			int y = now.y;
			int time = now.time;

			if (time > distance[x][y]) {
				continue;
			}

			if (x == goalX && y == goalY) {
				return time;
			}

			for (int i = 0; i < 4; i++) {
				int nx = x + dx[i];
				int ny = y + dy[i];
				if (nx >= n || ny >= n || nx < 0 || ny < 0) {
					continue;
				}
				if (graph[nx][ny] == 1) {
					continue;
				}

				int newTime = -1;
				if (graph[nx][ny] == 0) {
					newTime = time + 1;
				} else if (graph[nx][ny] == 2) {
					int waitTime = 2 - (time % 3);
					newTime = time + waitTime + 1;
				}

				if (newTime != -1 && newTime < distance[nx][ny]) {
					distance[nx][ny] = newTime;
					pq.offer(new Node(nx, ny, newTime));
				}
			}
		}
		return -1;
	}

	static class Node implements Comparable<Node> {
		int x, y, time;

		public Node(int x, int y, int time) {
			this.x = x;
			this.y = y;
			this.time = time;
		}

		@Override
		public int compareTo(Node other) {
			return this.time - other.time; // 시간이 적은 순서대로 정렬
		}
	}
}
