import java.util.ArrayList;

/**
 * 流的图的定义
 * Created by hongjiyao_2014150120 on 16-12-5.
 */

class FlowNetwork {
    private final int V;                    // 顶点个数
    private ArrayList<FlowEdge>[] adj;

    /**
     * 构造函数
     *
     * @param V 顶点的个数
     */
    FlowNetwork(final int V) {
        this.V = V;
        adj = new ArrayList[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new ArrayList<>();
        }
    }

    /**
     * 增加边
     *
     * @param e 边
     */
    void addEdge(final FlowEdge e) {
        int v = e.from();
        int w = e.to();
        adj[v].add(e);                      // 正向边v->w
        adj[w].add(e);                      // 反向边w->e
    }

    /**
     * 返回顶点数量
     *
     * @return 顶点数量
     */
    int V() {
        return V;
    }

    /**
     * 返回邻接边
     *
     * @param v 某个顶点
     * @return 它的领接边们
     */
    Iterable<FlowEdge> adj(final int v) {
        return adj[v];
    }
}
