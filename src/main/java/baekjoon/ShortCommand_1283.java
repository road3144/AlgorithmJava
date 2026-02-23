package baekjoon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class ShortCommand_1283 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int n = Integer.parseInt(st.nextToken());

        Set<Character> set = new HashSet<>();
        Map<String, Character> map = new HashMap<>();
        Map<String, Integer> replaceMap = new HashMap<>();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < n; i++) {
            String option = br.readLine();
            st = new StringTokenizer(option);
            int k = st.countTokens();
            String[] words = new String[k];
            boolean isFind = false;
            int idx = 0;
            // 단어 첫 글자 보기
            for (int j = 0; j < k; j++) {
                words[j] = st.nextToken();
                char lowerCase = Character.toLowerCase(words[j].charAt(0));
                if (!set.contains(lowerCase)) {
                    set.add(lowerCase);
                    map.put(option, words[j].charAt(0));
                    replaceMap.put(option, idx);
                    isFind = true;
                    break;
                }
                idx += words[j].length() + 1;
            }
            if (!isFind) {
                idx = 0;
                for (int j = 0; j < k; j++) {
                    for (int l = 0; l < words[j].length(); l++) {
                        char lowerCase = Character.toLowerCase(words[j].charAt(l));
                        if (!set.contains(lowerCase)) {
                            set.add(lowerCase);
                            map.put(option, words[j].charAt(l));
                            replaceMap.put(option, idx);
                            isFind = true;
                            break;
                        }
                        idx ++;
                    }
                    idx ++;
                    if (isFind) break;
                }
            }

            for (int j = 0; j < option.length(); j++) {
                if (map.containsKey(option) && option.charAt(j) == map.get(option) && replaceMap.get(option) == j) {
                    sb.append("[").append(option.charAt(j)).append("]");
                } else {
                    sb.append(option.charAt(j));
                }
            }
            sb.append("\n");
        }

        System.out.println(sb);
    }
}
