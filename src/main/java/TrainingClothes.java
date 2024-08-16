import java.util.Arrays;

public class TrainingClothes {
	public int solution(int n, int[] lost, int[] reserve) {
		int answer = n;
		int[] status = new int[n+2];
		for (int i=0; i < lost.length; i++) {
			status[lost[i]] += 1;
		}

		for (int i=0; i < reserve.length; i++) {
			if (status[reserve[i]] == 1) {
				status[reserve[i]] = 0;
				reserve[i] = -1;
			}
		}
		Arrays.sort(reserve);
		for (int i=0; i < reserve.length; i++) {
			if (reserve[i] == -1) {
				continue;
			} else if (status[reserve[i] - 1] == 1) {
				status[reserve[i] - 1] = 0;
			} else if (status[reserve[i] + 1] == 1) {
				status[reserve[i] + 1] = 0;
			}
		}

		for (int i=0; i < n+1; i++) {
			if (status[i] == 1) {
				answer -= 1;
			}
		}

		return answer;
	}
}
