import java.util.LinkedList;
import java.util.Queue;

/**
 * FordFulkerson方法解决最大流问题工具类
 * Created by hongjiyao_2014150120 on 16-11-30.
 */

class FordFulkerson {
    private FlowEdge[] edgeTo;      // s->t路径的边的集合（逆增广路径）
    private double value;           // 流大小

    /**
     * 构造函数，构造的同时就不断寻找增广路径并更新最大流
     *
     * @param G 图
     * @param s 起点（源点）
     * @param t 终点（汇点）
     */
    FordFulkerson(final FlowNetwork G, final int s, final int t) {
        value = 0.0;
        while (hasAugmentingPath(G, s, t)) {
            double bottle = Double.POSITIVE_INFINITY;                       // 最大值
            for (int v = t; v != s; v = edgeTo[v].other(v)) {               // 沿着t->s计算最大流量
                bottle = Math.min(bottle, edgeTo[v].residualCapacityTo(v));
            }
            for (int v = t; v != s; v = edgeTo[v].other(v)) {
                edgeTo[v].addResidualFlowTo(v, bottle);                     // 给边重新计算流量
            }
            value += bottle;                                                // 最大流量上升

            for (int v = t; v != s; v = edgeTo[v].other(v)) {
                int from = edgeTo[v].from() + 1;
                int to = edgeTo[v].to() + 1;
                System.out.println(from + "->" + to + " : " + edgeTo[v].getFlow());
            }
            System.out.println("本次迭代流量上升了" + bottle + "，达到流量" + value);
            System.out.println();
        }
    }

    /**
     * 图的遍历算法，判断是不是有增广路径
     *
     * @param G 图
     * @param s 起点（源点）
     * @param t 终点（汇点）
     * @return 是和否
     */
    private boolean hasAugmentingPath(final FlowNetwork G, final int s, final int t) {
        edgeTo = new FlowEdge[G.V()];
        boolean[] marked = new boolean[G.V()];                              // 记录在残留网络中s（源点）->t是否可达
        Queue<Integer> q = new LinkedList<>();                              // 用队列组织广度优先搜索算法

        q.offer(s);
        marked[s] = true;                                                   // s->s必然可达
        while (!q.isEmpty()) {
            int v = q.poll();                                               // 每次取出队首v（访问之）
            for (FlowEdge e : G.adj(v)) {                                   // v的所有领接边
                int w = e.other(v);                                         // 领接点w
                // 如果还有残余流量并且这条路并未被使用
                if (e.residualCapacityTo(w) > 0 && !marked[w]) {            // e到w的流量大于0且s->w未判断
                    edgeTo[w] = e;                                          // 放入增广路径
                    marked[w] = true;                                       // 判断s->w可达
                    q.offer(w);                                             // w放入队首（发现之）
                }
            }
        }
        // 顺利到达终点的话 marked[t]==true s可达t于是存在增广路径
        return marked[t];
    }

    /**
     * 获取最大流大小
     *
     * @return 大小
     */
    double value() {
        return value;
    }

}
