package test;

public class Autoever_260405 {
}

// 1번
/*
*  w, h, int[] blocks 주어짐
* w, h <= 100, blocks.length <= 16
* w,h 격자 주어지고 각 칸은 좌상부터 1~w*h 까지 번호 있음
* blocks 에 무조건 들려야하는 칸의 번호가 오름차순으로 주어짐
* 1은 처음에 무조건 고정
* blocks 다 들리는데 필요한 최소 거리 구하기
* 나는 우선  blocks 돌면서 block 끼리 거리 구해서 graph 만듬, graph = new PQ[n+1]
* 거리순으로 정렬해서 그리디 하게 돔
* while(q not empty){
*   int now = q.poll();
*   while(graph[now] not empty){
*       Edge next graph[now].poll();
*       if (next not visited){
*           q.add(next);
*           answer += next.cost;
*       }
*   }
* }
*
* */



//2번
/*
* int[] enemy_power
* 적들의 파워가 주어짐,
* 적들의 파워는 음수가 나올 수 있음.
* 적들의 파워보다 1 이상 커야 무찌르기 가능.
* 적들을 죽이면 그 파워 만큼 체력 흡수, 음수면 감소. 이때 0보다 작아지면 게임 오버
* 꺠는데 순서는 최대한 유리하게 ㅇㅇ
* n까지 깰 수 있는다 했을 때 처음에 필요한 체력 량
*
* 우선 절대값 기준으로 정렬하고 , 같은 절대값이면 양수 먼저로 정렬.
* i 까지 합 계산.
* 그 합이 음수면 전체 깨는데체력이 - 되니까 그합 보다 1 크면됨
* 그게 양수면 최대 차이 보다 1 크면 됨
* answer[i] = Math.max(answer[i-1], max[i]-(pSum[i] - max[i] + 1))
*
* */