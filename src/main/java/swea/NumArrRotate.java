package swea;

import java.util.Scanner;

public class NumArrRotate {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int T;
		T=sc.nextInt();

		for(int test_case = 1; test_case <= T; test_case++) {
			int n = sc.nextInt();
			int[][] arr = new int[n][n];
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					arr[i][j] = sc.nextInt();
				}
			}

			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					sb.append(arr[n-1-j][i]);
				}
				sb.append(" ");
				for (int j = 0; j < n; j++) {
					sb.append(arr[n-1-i][n-1-j]);
				}
				sb.append(" ");
				for (int j = 0; j < n; j++) {
					sb.append(arr[j][n-1-i]);
				}
				sb.append("\n");
			}
			System.out.println("#" + test_case);
			System.out.println(sb);
		}
	}
}
