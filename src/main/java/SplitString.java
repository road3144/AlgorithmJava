public class SplitString {
	public int solution(String s) {
		int answer = 1;

		int cnt = 1;
		char x = s.charAt(0);
		for (int i=1; i < s.length(); i++) {
			if (cnt == 0) {
				x = s.charAt(i);
				answer++;
			}

			if (x == s.charAt(i)) {
				cnt++;
			} else {
				cnt--;
			}
		}
		return answer;
	}
}
