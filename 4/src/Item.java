/**
 * 背包中的物品的数据结构
 * Created by hongjiyao_2014150120 on 16-11-8.
 */
class Item implements Comparable<Item> {
    private static final int MAX_PRICE = 10; // 最大价值
    private int num;    // 物品编号
    int weight;     // 物品重量
    int price;      // 物品价值

    /**
     * 构造函数
     *
     * @param num       物品标号
     * @param maxWeight 物品最大重量
     */
    Item(int num, int maxWeight) {
        this.num = num;
        // this.weight = (int) (Math.random() * (maxWeight)); // 保证质量在 0 ~ maxWeight 之间
        this.weight = (int) (Math.random() * (maxWeight - 1)) + 1; // 保证质量在 1 ~ maxWeight 之间
        // 为什么允许质量为0？
        // 因为不允许质量为0且质量不可重复，则最多有maxWeight-1种物品 当物品的数量N>=M的时候，无法构造足够数量的重量不同的物品
        this.price = (int) (Math.random() * MAX_PRICE);
    }

    public Item(int num, int weight, int price) {
        this.weight = weight;
        this.num = num;
        this.price = price;
    }

    /**
     * 比较接口
     *
     * @param o 被比较的Item元素
     * @return 比较结果 0表示相等 this大则是1 o大则是-1
     */
    @Override
    public int compareTo(Item o) {
        if (this.weight == o.weight && this.price == o.price) {
            return 0;
        }
        if (this.weight > o.weight) {
            return 1;
        }
        if (this.weight < o.weight) {
            return -1;
        }
        return 1;
    }

    @Override
    public String toString() {
        return "编号：" + this.num + " 重量：" + this.weight + " 价值：" + this.price;
    }
}
