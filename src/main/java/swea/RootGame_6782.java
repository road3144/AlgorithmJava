package swea;

import java.io.*;
import java.util.StringTokenizer;

public class RootGame_6782 {
    static long T, n, ans;

    static StringBuilder sb = new StringBuilder();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        T = Integer.parseInt(st.nextToken());
        for (int test_case = 1; test_case <= T; test_case++) {
            st = new StringTokenizer(br.readLine());
            n = Long.parseLong(st.nextToken());
            long cnt = 0;
            while (n != 2) {

                long tmp = (long) Math.sqrt(n);
                if (tmp * tmp == n){
                    n = tmp;
                    cnt++;
                }
                else{
                   cnt += (tmp+1) * (tmp+1) - n;
                   n = (tmp+1) * (tmp+1);
                }
            }
            bw.write("#" + test_case + " " + cnt + "\n");
        }
        bw.flush();
        bw.close();
    }

}
