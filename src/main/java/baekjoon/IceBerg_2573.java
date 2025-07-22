package baekjoon;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

class IceBerg {
	int x;
	int y;

	IceBerg(int x, int y) {
		this.x = x;
		this.y = y;
	}
}

public class IceBerg_2573 {
	static int[] dx = { -1, 0, 1, 0 };
	static int[] dy = { 0, 1, 0, -1 };

	static int n, m;
	static int[][] map;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());

		map = new int[n][m];
		for (int i = 0; i < n; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < m; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}

		int ans = 0;
		int cnt = 0;

		// 빙하가 2개 이상 분리될 경우 반복문을 종료.
		// 빙하가 다 녹아버렸을 경우, 0을 출력.
		while ((cnt = countIce()) < 2) {
			if (cnt == 0) {
				ans = 0;
				break;
			}

			Melt();
			ans++;
		}

		System.out.println(ans);
	}

	// 빙하가 분리된 개수
	public static int countIce() {
		boolean[][] visited = new boolean[n][m];

		int cnt = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				if (map[i][j] != 0 && !visited[i][j]) {
					dfs(i, j, visited); // DFS 방식을 통해 총 몇개의 빙하로 나누어졌는지 구할 수 있음.
					cnt++;
				}
			}
		}
		return cnt;
	}

	public static void dfs(int x, int y, boolean[][] visited) {
		visited[x][y] = true;

		for (int i = 0; i < 4; i++) {
			int nx = x + dx[i];
			int ny = y + dy[i];

			if (nx < 0 || ny < 0 || nx >= n || ny >= m) {
				continue;
			}

			if (map[nx][ny] != 0 && !visited[nx][ny]) {
				dfs(nx, ny, visited);
			}
		}
	}

	// 빙하를 녹이는 함수.
	public static void Melt() {
		Queue<IceBerg> q = new LinkedList<>();

		boolean[][] visited = new boolean[n][m];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				if (map[i][j] != 0) {
					q.offer(new IceBerg(i, j));
					visited[i][j] = true;
				}
			}
		}

		while (!q.isEmpty()) {
			IceBerg ice = q.poll();

			int seaNum = 0; // 빙하 상하좌우에 존재하는 바다 영역의 수.

			for (int i = 0; i < 4; i++) {
				int nx = ice.x + dx[i];
				int ny = ice.y + dy[i];

				if (nx < 0 || ny < 0 || nx >= n || ny >= m) {
					continue;
				}

				if (!visited[nx][ny] && map[nx][ny] == 0) {
					seaNum++;
				}
			}

			map[ice.x][ice.y] = Math.max(0, map[ice.x][ice.y] - seaNum);
		}
	}
}
