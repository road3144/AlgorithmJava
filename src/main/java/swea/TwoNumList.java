package swea;

import java.util.Scanner;

public class TwoNumList {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int T;
		T=sc.nextInt();

		for(int test_case = 1; test_case <= T; test_case++) {
			int n = sc.nextInt();
			int m = sc.nextInt();
			int[] a = new int[n];
			int[] b = new int[m];
			int ans = Integer.MIN_VALUE;

			for (int i = 0; i < n; i++) {
				a[i] = sc.nextInt();
			}
			for (int i = 0; i < m; i++) {
				b[i] = sc.nextInt();
			}
			int gap = Math.abs(n - m);

			if (n < m) {
				for (int i = 0; i < gap + 1; i++) {
					int total = 0;
					for (int j = 0; j < n; j++) {
						total += a[j] * b[j + i];
					}
					ans = Math.max(ans, total);
				}
			} else {
				for (int i = 0; i < gap + 1; i++) {
					int total = 0;
					for (int j = 0; j < m; j++) {
						total += a[j+i] * b[j];
					}
					ans = Math.max(ans, total);
				}
			}

			System.out.println("#" + test_case + " " + ans);
		}
	}
}
