package baekjoon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class FineDust_17144 {
	static int r, c, t, ans;
	static int[][] map;
	static int[] dx = {1, -1, 0, 0};
	static int[] dy = {0, 0, 1, -1};
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		r = Integer.parseInt(st.nextToken());
		c = Integer.parseInt(st.nextToken());
		t = Integer.parseInt(st.nextToken());
		int x = 0;
		map = new int[r][c];
		for(int i=0; i<r; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j=0; j<c; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
				if (map[i][j] == -1)
					x = i-1;
			}
		}
		
		for (int i=0; i < t; i++) {
			spread();
			filter(x);
		}
		
		for(int i=0; i<r; i++) {
			for(int j=0; j<c; j++) {
				if (map[i][j] != -1)
					ans += map[i][j];
			}
		}
		System.out.println(ans);
	}
	
	//미세먼지 확산
	static void spread() {
		// 각 칸이 독립적으로 확산해 확산 되어야 할 리스트에 담았다가 한번에 적용
		List<int[]> toAdd = new ArrayList<>();
		for(int x=0; x<r; x++) {
			for(int y=0; y<c; y++) {
				// 5가 안되면 확산 안된다
				if (map[x][y] < 5)
					continue;
				int cnt = 0;
				for (int i=0; i<4; i++) {
					int nx = x + dx[i];
					int ny = y + dy[i];
					if (nx < 0 || nx >= r || ny < 0 || ny >= c)
						continue;
					if (map[nx][ny] == -1)
						continue;
					// 확산 되어야 할 미세먼지 위치, 양
					toAdd.add(new int[] {nx, ny, map[x][y] / 5});
					cnt++;
				}
				// 확산 하고 남은 양
				map[x][y] = map[x][y] - ((map[x][y] / 5) * cnt);
			}
		}
		// 리스트 돌면서 추가
		for (int[]t : toAdd) {
			int x = t[0], y = t[1], d = t[2];
			map[x][y] += d;
		}
	}
	
	static void filter(int x) {
		int utu = map[x][c-1];
		int ltu = map[0][c-1];
		int dtu = map[0][0];
		int dtd = map[x+1][c-1];
		int ltd = map[r-1][c-1];
		int utd = map[r-1][0];
		right(x);
		up(x, utu);
		left(0, ltu);
		downFilter(x, dtu);
		right(x+1);
		down(x+1, dtd);
		left(r-1, ltd);
		upFilter(x+1, utd);
	}
	
	static void right(int x) {
		int tmp = map[x][c-1];
		for (int i=c-1; i > 1; i--) {
			map[x][i] = map[x][i-1];
		}
		map[x][1] = 0;
	}
	
	static void left(int x, int tmp) {
		for (int i=0; i < c-1; i++) {
			map[x][i] = map[x][i+1];
		}
		map[x][c-2] = tmp;
	}
	
	static void up(int x, int tmp) {
		for (int i=0; i < x; i++) {
			map[i][c-1] = map[i+1][c-1];
		}
		map[x-1][c-1] = tmp;
	}
	
	static void upFilter(int x, int tmp) {
		for (int i=x+1; i < r-2; i++) {
			map[i][0] = map[i+1][0];
		}
		map[r-2][0] = tmp;
	}
	
	static void down(int x, int tmp) {
		for (int i=r-1; i > x; i--) {
			map[i][c-1] = map[i-1][c-1];
		}
		map[x+1][c-1] = tmp;
	}
	static void downFilter(int x, int tmp) {
		for (int i=x-1; i > 0; i--) {
			map[i][0] = map[i-1][0];
		}
		map[1][0] = tmp;
	}

}
