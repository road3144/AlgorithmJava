package baekjoon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class ColorPaper_2630 {

	static int n, nor, err;
	static int[][] map;
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		map = new int[n][n];
		for (int i = 0; i < n; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < n; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		nor = NorDfs(n, 0, 0);
		err = ErrDfs(n, 0, 0);
		
		System.out.println(nor);
		System.out.println(err);
	}
	
	static int NorDfs(int size, int x, int y) {
		if (size == 1) {
			if (map[x][y] == 0) return 1;
			else return 0;
		}
		boolean flag = true;
		for (int i=x; i < x+size; i++) {
			if (!flag) break;
			for (int j=y; j < y+size; j++) {
				if (map[i][j] == 1) {
					flag = false;
					break;
				}
			}
		}
		if (flag) {
			return 1;
		}
		else {
			int total = 0;
			total += NorDfs(size/2, x, y);
			total += NorDfs(size/2, x + size/2, y);
			total += NorDfs(size/2, x, y + size/2);
			total += NorDfs(size/2, x + size/2, y + size/2);
			return total;
		}
	}
	
	static int ErrDfs(int size, int x, int y) {
		if (size == 1) {
			if (map[x][y] == 1) return 1;
			else return 0;
		}
		boolean flag = true;
		for (int i=x; i < x+size; i++) {
			if (!flag) break;
			for (int j=y; j < y+size; j++) {
				if (map[i][j] == 0) {
					flag = false;
					break;
				}
			}
		}
		if (flag) {
			return 1;
		}
		else {
			int total = 0;
			total += ErrDfs(size/2, x, y);
			total += ErrDfs(size/2, x + size/2, y);
			total += ErrDfs(size/2, x, y + size/2);
			total += ErrDfs(size/2, x + size/2, y + size/2);
			return total;
		}
	}
}
