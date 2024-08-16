public class NumberMate {
	public String solution(String X, String Y) {
		StringBuilder answer = new StringBuilder();
		int[] x = new int[10];
		int[] y = new int[10];

		for (int i=0; i < X.length(); i++) {
			x[X.charAt(i) - 48] += 1;
		}
		for (int i=0; i < Y.length(); i++) {
			y[Y.charAt(i) - 48] += 1;
		}

		for (int i=9; i >= 0; i--) {
			for (int j=0; j < Math.min(x[i], y[i]); j++) {
				answer.append(i);
			}
		}

		if (answer.toString().equals("")) {
			return "-1";
		} else if (answer.toString().charAt(0) == 48) {
			return "0";
		}
		return answer.toString();
	}
}
