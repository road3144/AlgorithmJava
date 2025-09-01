package baekjoon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;
import java.util.StringTokenizer;

public class Run_16930 {

	static int n, m, k;
	static int[] dx = {1, -1, 0, 0};
	static int[] dy = {0, 0, 1, -1};
	static char[][] map;
	static int[][] dist;
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());
		map = new char[n][];
		dist = new int[n][m];
		
		for(int i=0; i < n; i++) {
			st = new StringTokenizer(br.readLine());
			map[i] = st.nextToken().toCharArray();
		}
		st = new StringTokenizer(br.readLine());
		int sX = Integer.parseInt(st.nextToken()) - 1;
		int sY = Integer.parseInt(st.nextToken()) - 1;
		int gX = Integer.parseInt(st.nextToken()) - 1;
		int gY = Integer.parseInt(st.nextToken()) - 1;
        for (int i = 0; i < n; i++) Arrays.fill(dist[i], -1);
		bfs(sX, sY);
        System.out.println(dist[gX][gY]);
		
	}
	
	static void bfs(int sX, int sY) {
		Queue<int[]> q = new ArrayDeque<>();
		q.offer(new int[] {sX, sY});
        dist[sX][sY] = 0;
		
		while(!q.isEmpty()) {
			int[] now = q.poll();
			int x = now[0], y = now[1];
			for (int i=0; i<4; i++) {
				for (int j=1; j<=k; j++) {
					int nx = x + dx[i] * j, ny = y + dy[i] * j;
					
					if (nx<0 || nx>=n || ny<0 || ny>=m) break;
					if(map[nx][ny] == '#') break;
                    if (dist[nx][ny] == -1) {
                        dist[nx][ny] = dist[x][y] + 1;
                        q.offer(new int[]{nx, ny});
                    }
                    // 더 짧은 거리로 이미 방문한 칸을 만나면 그 뒤도 볼 필요 없음
                    else if (dist[nx][ny] < dist[x][y] + 1) {
                        break;
                    }
                    // 같은 레벨(dist[x][y]+1)로 이미 방문한 칸은 스킵만 하고 계속 더 멀리 뻗기
                    else if (dist[nx][ny] == dist[x][y] + 1) {
                        continue;
                    }
				}
			}
		}
	}
}
