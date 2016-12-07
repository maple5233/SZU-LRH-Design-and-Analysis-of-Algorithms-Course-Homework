/**
 * 图的边的定义
 * Created by hongjiyao_2014150120 on 16-12-5.
 */
class FlowEdge {
    private final int v, w;         // 边的起点和终点
    private final double capacity;  // 容量
    private double flow;            // 流量

    /**
     * 获取流量
     *
     * @return 流量
     */
    double getFlow() {
        return flow;
    }

    /**
     * 构造函数
     *
     * @param v        起点
     * @param w        终点
     * @param capacity 容量
     */
    FlowEdge(final int v, final int w, final double capacity) {
        this.v = v;
        this.w = w;
        this.capacity = capacity;
    }

    /**
     * 获取起点
     *
     * @return 起点
     */
    int from() {
        return v;
    }

    /**
     * 获取终点
     *
     * @return 终点
     */
    int to() {
        return w;
    }

    /**
     * 获取另外一个端点
     *
     * @param vertex 其中一个端点
     * @return 另外一个端点
     */
    int other(final int vertex) {
        if (vertex == v) {
            return w;
        } else if (vertex == w) {
            return v;
        } else {
            throw new RuntimeException("Inconsistent edge");
        }
    }

    /**
     * 获取残留流量
     *
     * @param vertex 流向终点端点
     * @return 残余容量
     */
    double residualCapacityTo(final int vertex) {
        if (vertex == v) {          //反向边
            return flow;
        } else if (vertex == w) {   //正向边
            return capacity - flow;
        } else {
            throw new IllegalArgumentException("点错乱了");
        }
    }

    /**
     * 压入流量delta
     *
     * @param vertex 流向终点端点
     * @param delta  流大小
     */
    void addResidualFlowTo(final int vertex, final double delta) {
        if (vertex == v) {              // 倒流
            flow -= delta;
        } else if (vertex == w) {       // 正流
            flow += delta;
        } else {
            throw new IllegalArgumentException();
        }
    }

}
