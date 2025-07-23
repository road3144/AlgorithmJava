package baekjoon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;

public class Puyo_11559 {
	static char[][] map = new char[12][];
	static boolean isPop = true;
	static int ans = 0;
	static int[] dx = { -1, 0, 1, 0 };
	static int[] dy = { 0, 1, 0, -1 };
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		for (int i = 0; i < 12; i++) {
			st = new StringTokenizer(br.readLine());
			map[i] = st.nextToken().toCharArray();
		}
		
		while(isPop) {
			bfs();
			gravity();
			if (!isPop)
				break;
			ans++;
		}
		
		System.out.println(ans);
	}
	
	static void bfs() {
		Queue<Puyo> q = new LinkedList<>();
		boolean[][] visited = new boolean[12][6];
		isPop = false;
		
		for (int i = 0; i < 12; i++) {
			for (int j = 0; j < 6; j++) {
				//뿌요면 bfs 실행 (같은 뿌요만)
				if (!visited[i][j] && map[i][j] != '.') {
					List<int[]> popList = new ArrayList<>();
					q.add(new Puyo(i, j, map[i][j]));
					popList.add(new int[] {i, j});
					
					while (!q.isEmpty()) {
						Puyo now = q.poll();
						visited[now.x][now.y] = true; 
						for(int k = 0; k < 4; k++) {
							int nx = now.x + dx[k], ny = now.y + dy[k];
							if (nx < 0 || nx >= 12 || ny < 0 || ny >= 6) continue;
							
							if (!visited[nx][ny] && map[nx][ny] == now.color) {
								popList.add(new int[] {nx, ny});
								q.add(new Puyo(nx, ny, map[nx][ny]));
							}
						}
					}
					// 4넘으면 삭제
					if (popList.size() >= 4) {
						isPop = true; // 한번이라도 삭제 일어나면 터졌다.
						for(int[] now : popList) {
							map[now[0]][now[1]] = '.';
						}
					}
				}
			}
		}
	}
	
	static void gravity() {
		for (int j = 0; j < 6; j++) {
			int cnt = 0;
			for (int i = 11; i >= 0; i--) {
				if (map[i][j] != '.') {
					char tmp = map[i][j];
					map[i][j] = '.';
					map[i+cnt][j] = tmp;
				} else cnt++;
				
			}
		}
	}
}

class Puyo {
	int x, y;
	char color;
	
	Puyo(int x, int y, char color) {
		this.x = x;
		this.y = y;
		this.color = color;
	}
}
