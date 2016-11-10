/**
 * 动态规划的应用-01背包问题
 * Created by hongjiyao_2014150120 on 16-11-8.
 */

import java.util.Date;
import java.util.HashSet;
import java.util.TreeSet;

public class Package {
    private static final int W = 10; // 容量
    private static final int N = 3; // 个数
    private static TreeSet<Item> items = new TreeSet<Item>(); // 物品集合,有序不可重复,如果重复了就不是01背包问题
    private static Item[] itemsArray = new Item[N + 1];
    private static int[][] memo = new int[N + 1][W + 1]; // 自底向上的备忘录,多申请1宽度和1高度是为了叙述方便
    private static HashSet<Item> itemsIn = new HashSet<Item>(); // 被放进去的物品
//    private static final int RUN_TIMES = 1000; //重复次数

    /**
     * 随机生成物品的重量和价值
     * 物品重量不重复
     * 物品重量不超过W
     */
    private static void initItems() {
        while (items.size() < N) {
            items.add(new Item(items.size(), W));
        }
        System.out.println("背包容量为" + W + ",物品个数为" + N + "的生成结果：");
        int i = 1;
        for (Item item : items) {
            itemsArray[i++] = item; // 放入数组
        }
        items.forEach(System.out::println);
    }

    /**
     * 自底向上地填表
     */
    private static void initMemo() {
        for (int i = 1; i <= N; i++) { // 一行一行填，穷举所有物品
            for (int j = 1; j <= W; j++) { // 对于前n个物品，遍历所有重量的情况
                if (j - itemsArray[i].weight >= 0) { // 背包放得下第i个物品，就有两种选择
                    memo[i][j] = Math.max(
                            memo[i - 1][j], // 一种是不放
                            memo[i - 1][j - itemsArray[i].weight] + itemsArray[i].price // 一种是放
                    );
                } else { // 放不下，只能不放了
                    memo[i][j] = memo[i - 1][j];
                }
            }
        }
    }

    /**
     * 根据备忘录的最后一个，回溯地去求背包中放进了什么东西
     */
    private static void getPackages() {
        int j = W; // 从备忘录最右下角开始回溯
        for (int i = N; i >= 1; i--) {
            if (memo[i][j] > memo[i - 1][j]) { // 后面的价值更大，说明第i个物品是被放进去的
                j -= itemsArray[i].weight; // 回溯
                itemsIn.add(itemsArray[i]);
            }
            // 等于的情况就跳过这个i，不会出现小于的情况，因为重量增加了最大价值怎么可能变小啊
        }
    }

    /**
     * 主函数
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {

        initItems();
        long beginInitMemo = new Date().getTime();
        initMemo();
        long finishInitMemo = new Date().getTime();
        getPackages();
        long finishGetPackages = new Date().getTime();

//         被放进去的物品
        System.out.println("最大价值：" + memo[N][W]);
        System.out.println("被放进去的物品：");
        itemsIn.forEach(System.out::println);

        System.out.println("填表耗时:" + (finishInitMemo - beginInitMemo) + "ms");
        System.out.println("求解耗时:" + (finishGetPackages - finishInitMemo) + "ms");
        System.out.println("总耗时:" + (finishGetPackages - beginInitMemo) + "ms");

//        long sumTime = 0;
//        int i = 0;
//        while (i++ < RUN_TIMES) {
//            initItems();
//            long beginInitMemo = new Date().getTime();
//            initMemo();
//            getPackages();
//            long finishGetPackages = new Date().getTime();
//            sumTime += finishGetPackages - beginInitMemo;
//        }
//        System.out.println(RUN_TIMES + "次总耗时:" + sumTime + "ms");
    }
}
