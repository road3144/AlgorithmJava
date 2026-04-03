package codetree.samsung;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class PirateCaptain {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int T = Integer.parseInt(st.nextToken());
        for (int i = 0; i < T; i++) {
            st = new StringTokenizer(br.readLine());
            int comm = Integer.parseInt(st.nextToken());
            if (comm == 100){
                int n = Integer.parseInt(st.nextToken());
                int[] ids = new int[n];
                int[] powers = new int[n];
                int[] reloads = new int[n];
                for (int j = 0; j < n; j++) {
                    ids[j] = Integer.parseInt(st.nextToken());
                    powers[j] = Integer.parseInt(st.nextToken());
                    reloads[j] = Integer.parseInt(st.nextToken());
                }
                init(n, ids, powers, reloads);
            } else if (comm == 200){
                int id = Integer.parseInt(st.nextToken());
                int power = Integer.parseInt(st.nextToken());
                int reload = Integer.parseInt(st.nextToken());
                add(id, power, reload);
            } else if (comm == 300){
                int id = Integer.parseInt(st.nextToken());
                int power = Integer.parseInt(st.nextToken());
                change(id, power);
            } else if (comm == 400){
                attack();
            }
        }
    }

    static class Ship implements Comparable<Ship>{
        int id, power, reload;

        Ship(int id, int power, int reload){
            this.id = id;
            this.power = power;
            this.reload = reload;
        }

        public int compareTo(Ship o){
            if(this.power != o.power) return o.power - this.power;
            return this.id - o.id;
        }
    }

    static TreeSet<Ship> ready;
    static Map<Integer, Ship> map;
    static List<Ship>[] empty;

    static void init(int n, int[] ids, int[] powers, int[] reloads){
        ready = new TreeSet<>();
        map = new HashMap<>();
        empty = new List[10];
        for (int i = 0; i < 10; i++) {
            empty[i] = new ArrayList<>();
        }

        for (int i = 0; i < n; i++) {
            Ship s = new Ship(ids[i], powers[i], reloads[i]);
            map.put(s.id, s);
            ready.add(s);
        }
    }

    static void add(int id, int power, int reload){
        touch();
        Ship s = new Ship(id, power, reload);
        map.put(s.id, s);
        ready.add(s);
    }

    static void change(int id, int power){
        touch();
        Ship s = map.get(id);
        if (ready.contains(s)) {
            ready.remove(s);
            s.power = power;
            ready.add(s);
        } else {
            s.power = power;
        }
    }

    static void touch(){
        ready.addAll(empty[0]);
        for (int i = 1; i < 10; i++) {
            empty[i-1] = empty[i];
        }
        empty[9] = new ArrayList<>();
    }

    static void attack(){
        touch();
        List<Ship> attacked = new ArrayList<>();
        int total = 0, cnt = 0;
        for (int i = 0; i < 5; i++) {
            if (!ready.isEmpty()) {
                Ship s = ready.pollFirst();
                attacked.add(s);
                cnt++;
                total += s.power;
                empty[s.reload-1].add(s);
            }
            else break;
        }
        System.out.print(total + " " + cnt + " ");
        for (int i = 0; i < cnt; i++) {
            System.out.print(attacked.get(i).id + " ");
        }
        System.out.println();
    }
}
