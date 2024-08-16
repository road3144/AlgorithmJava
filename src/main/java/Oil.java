import java.util.*;
public class Oil {

	int n;
	int m;
	int num = 1;
	int[] dx = {1, -1, 0, 0};
	int[] dy = {0, 0, -1, 1};
	Map<Integer, Integer> oil = new HashMap<>();

	public int solution(int[][] land) {
		int answer = 0;
		n = land.length;
		m = land[0].length;
		for (int i=0; i < n; i++) {
			for (int j=0; j < m; j++) {
				if (land[i][j] == 1) {
					num++;
					bfs(i, j, land);
				}
			}
		}
		for (int j=0; j < m; j++) {
			int total = 0;
			Set<Integer> visited = new HashSet<>();
			for (int i=0; i < n; i++) {
				if (land[i][j] != 0) {
					visited.add(land[i][j]);
				}
			}
			for (int i : visited) {
				total += oil.get(i);
			}
			answer = Math.max(answer, total);
		}
		return answer;
	}

	private void bfs(int sx, int sy, int[][] land) {
		Queue<Node> queue = new LinkedList<>();
		queue.offer(new Node(sx, sy));
		land[sx][sy] = num;
		int area = 1;
		while (!queue.isEmpty()) {
			Node now = queue.poll();
			int x = now.x;
			int y = now.y;
			for (int i=0; i < 4; i++) {
				int nx = x + dx[i];
				int ny = y + dy[i];
				if (nx < 0 || ny < 0 || nx >= n || ny >= m) {
					continue;
				}
				if (land[nx][ny] != 1) {
					continue;
				}
				land[nx][ny] = num;
				queue.offer(new Node(nx, ny));
				area++;
			}
		}
		oil.put(num, area);
	}

	static class Node{
		int x;
		int y;

		public Node(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
}
