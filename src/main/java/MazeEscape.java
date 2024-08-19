import java.util.*;

public class MazeEscape {
	int[][] graph;
	int n, m;
	int[] dx = {0, 0, 1, -1};
	int[] dy = {1, -1, 0, 0};
	int sx, sy, lx, ly, gx, gy;
	public int solution(String[] maps) {
		int answer = 0;
		n = maps.length;
		m = maps[0].length();
		graph = new int[n][m];
		for (int i=0; i < n; i++) {
			for (int j=0; j < m; j++) {
				if (maps[i].charAt(j) == 'S') {
					sx = i;
					sy = j;
				} else if (maps[i].charAt(j) == 'L') {
					lx = i;
					ly = j;
				} else if (maps[i].charAt(j) == 'E') {
					gx = i;
					gy = j;
				}
			}
		}

		bfs(maps, sx, sy);
		if (graph[lx][ly] == 0) {
			return -1;
		} else {
			answer += graph[lx][ly] - 1;
		}

		graph = new int[n][m];
		bfs(maps, lx, ly);
		if (graph[gx][gy] == 0) {
			return -1;
		} else {
			answer += graph[gx][gy] - 1;
		}

		return answer;
	}

	private void bfs(String[] maps, int tx, int ty) {
		Queue<Node> q = new LinkedList<>();
		q.offer(new Node(tx, ty));
		graph[tx][ty] = 1;
		while (!q.isEmpty()) {
			Node node = q.poll();
			int x = node.x;
			int y = node.y;

			for (int i=0; i < 4; i++) {
				int nx = x + dx[i];
				int ny = y + dy[i];
				if (nx < 0 || nx >= n || ny < 0 || ny >= m) {
					continue;
				}
				if (graph[nx][ny] != 0 || maps[nx].charAt(ny) == 'X') {
					continue;
				}
				graph[nx][ny] = graph[x][y] + 1;
				q.offer(new Node(nx, ny));
			}
		}
	}

	static class Node{
		int x;
		int y;
		public Node(int x, int y){
			this.x = x;
			this.y = y;
		}
	}
}
