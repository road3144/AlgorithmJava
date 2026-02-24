package swea;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class TwoBigDecimalEditor_24750 {

    private static BufferedReader br;
    private static TwoBigDecimalEditor_UserSolution usersolution = new TwoBigDecimalEditor_UserSolution();

    private final static int CMD_INIT = 1;
    private final static int CMD_APPEND = 2;
    private final static int CMD_COUNT = 3;

    private static int mDigitList1[] = new int[30000];
    private static int mDigitList2[] = new int[30000];

    private static boolean run() throws Exception {

        StringTokenizer stdin = new StringTokenizer(br.readLine(), " ");
        int query_num = Integer.parseInt(stdin.nextToken());
        boolean ok = false;

        for (int q = 0; q < query_num; q++) {
            stdin = new StringTokenizer(br.readLine(), " ");
            int query = Integer.parseInt(stdin.nextToken());

            if (query == CMD_INIT) {
                stdin = new StringTokenizer(br.readLine(), " ");
                int mCnt1 = Integer.parseInt(stdin.nextToken());
                String temp1 = stdin.nextToken();
                for (int i = 0; i < mCnt1; i++) {
                    mDigitList1[i] = temp1.charAt(i) - '0';
                }
                for (int i = mCnt1; i < 30000; i++){
                    mDigitList1[i] = 0;
                }
                stdin = new StringTokenizer(br.readLine(), " ");
                int mCnt2 = Integer.parseInt(stdin.nextToken());
                String temp2 = stdin.nextToken();
                for (int i = 0; i < mCnt2; i++) {
                    mDigitList2[i] = temp2.charAt(i) - '0';
                }
                for (int i = mCnt2; i < 30000; i++){
                    mDigitList2[i] = 0;
                }
                usersolution.init(mCnt1, mDigitList1, mCnt2, mDigitList2);
                ok = true;
            } else if (query == CMD_APPEND) {
                int mDir = Integer.parseInt(stdin.nextToken());
                int mNum1 = Integer.parseInt(stdin.nextToken());
                int mNum2 = Integer.parseInt(stdin.nextToken());
                usersolution.append(mDir, mNum1, mNum2);
            } else if (query == CMD_COUNT) {
                int mNum = Integer.parseInt(stdin.nextToken());
                int ret = usersolution.countNum(mNum);
                int ans = Integer.parseInt(stdin.nextToken());
                if (ans != ret) {
                    ok = false;
                }
            }
        }
        return ok;
    }

    public static void main(String[] args) throws Exception {
        int T, MARK;
         System.setIn(new java.io.FileInputStream("./sample_input.txt"));
        br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stinit = new StringTokenizer(br.readLine(), " ");

        T = Integer.parseInt(stinit.nextToken());
        MARK = Integer.parseInt(stinit.nextToken());

        for (int tc = 1; tc <= T; tc++) {
            int score = run() ? MARK : 0;
            System.out.println("#" + tc + " " + score);
        }
        br.close();
    }
}
class TwoBigDecimalEditor_UserSolution {

    static int[] arr1, arr2, map;

    public void init(int mCnt1, int[] mDigitList1, int mCnt2, int[] mDigitList2){
        arr1 = new int[4];
        arr2 = new int[4];
        map = new int[1000];

        for (int i = 0; i < mCnt1-2; i++) {
            map[mDigitList1[i] * 100 + mDigitList1[i+1] * 10 + mDigitList1[i+2]] += 1;
        }
        for (int i = 0; i < mCnt2-2; i++) {
            map[mDigitList2[i] * 100 + mDigitList2[i+1] * 10 + mDigitList2[i+2]] += 1;
        }
        arr1[0] = mDigitList1[0];
        arr1[1] = mDigitList1[1];
        arr1[2] = mDigitList1[mCnt1-2];
        arr1[3] = mDigitList1[mCnt1-1];
        arr2[0] = mDigitList2[0];
        arr2[1] = mDigitList2[1];
        arr2[2] = mDigitList2[mCnt2-2];
        arr2[3] = mDigitList2[mCnt2-1];
    }

    public void append(int mDir, int mNum1, int mNum2) {
        char[] str1 = Integer.toString(mNum1).toCharArray();
        int n1 = str1.length;
        char[] str2 = Integer.toString(mNum2).toCharArray();
        int n2 = str2.length;
        int[] num1 = new int[n1 + 2];
        int[] num2 = new int[n2 + 2];

        if (mDir == 0) {
            for (int i = 0; i < n1; i++)
                num1[i] = str1[i] - '0';
            num1[n1] = arr1[0];
            num1[n1+1] = arr1[1];

            for (int i = 0; i < n1; i++) {
                map[num1[i] * 100 + num1[i+1] * 10 + num1[i+2]] += 1;
            }
            arr1[0] = num1[0];
            arr1[1] = num1[1];

            for (int i = 0; i < n2; i++)
                num2[i] = str2[i] - '0';
            num2[n2] = arr2[0];
            num2[n2+1] = arr2[1];

            for (int i = 0; i < n2; i++) {
                map[num2[i] * 100 + num2[i+1] * 10 + num2[i+2]] += 1;
            }
            arr2[0] = num2[0];
            arr2[1] = num2[1];
        } else {
            for (int i = 0; i < n1; i++)
                num1[i+2] = str1[i] - '0';
            num1[0] = arr1[2];
            num1[1] = arr1[3];

            for (int i = 0; i < n1; i++) {
                map[num1[i] * 100 + num1[i+1] * 10 + num1[i+2]] += 1;
            }
            arr1[2] = num1[n1];
            arr1[3] = num1[n1+1];

            for (int i = 0; i < n2; i++)
                num2[i+2] = str2[i] - '0';
            num2[0] = arr2[2];
            num2[1] = arr2[3];

            for (int i = 0; i < n2; i++) {
                map[num2[i] * 100 + num2[i+1] * 10 + num2[i+2]] += 1;
            }
            arr2[2] = num2[n2];
            arr2[3] = num2[n2+1];
        }

    }

    public int countNum(int mNum) {
        int tmp = 0;
        if (mNum == arr1[2] * 100 + arr1[3] * 10 + arr2[0]) tmp++;
        if (mNum == arr1[3] * 100 + arr2[0] * 10 + arr2[1]) tmp++;

        return map[mNum] + tmp;
    }
}
