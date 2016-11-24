import java.io.*;
import java.nio.file.Files;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * 赫夫曼编码
 * Created by hongjiyao_2014150120 on 16-11-18.
 */
public class Huffman {
    private static HashMap<Character, Integer> codeStatistics = new HashMap<>();        // 字母频率表
    private static TrieNode root = null;                                                // 赫夫曼树的树根
    private static LinkedList<TrieNode> leafs = new LinkedList<>();                     // 可能的叶子集合
    private static HashMap<Character, String> codeTable = new HashMap<>();              // 字母编码字典
    private static final int BITS_OF_BYTE = 8;                                          // 一个字节8bits

    /**
     * 读取原始文件并转化为字节数组
     *
     * @return 字符数组
     */
    private static char[] readOriginalFile() {
        File file = new File("data/cacm.all");
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            StringBuilder stringBuilder = new StringBuilder();
            while ((tempString = reader.readLine()) != null) {
                stringBuilder.append(tempString).append('\n');
            }
            reader.close();
            return stringBuilder.toString().toCharArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过NIO的方式读取整个文件的字节数组
     *
     * @return 字节数组
     * @throws IOException 读写异常
     */
    private static byte[] readOriginalFile2() throws IOException {
        return Files.readAllBytes(new File("data/cacm.all").toPath());
    }

    /**
     * 读取字符数组并统计频率
     *
     * @param charArray 读入的字符数组
     */
    private static void statistics(final char[] charArray) {
        for (char c : charArray) {
            Character _c = c;
            if (codeStatistics.containsKey(_c)) {
                codeStatistics.put(_c, codeStatistics.get(_c) + 1);                     // 覆盖前面的
            } else {
                codeStatistics.put(_c, 1);
            }
        }
        System.out.println("频率表如下:");
        for (Map.Entry<Character, Integer> entry : codeStatistics.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
        System.out.println();
    }

    private static void statistics2(final byte[] bytes) {
        for (byte b : bytes) {
            Character _c = (char) b;
            if (codeStatistics.containsKey(_c)) {
                codeStatistics.put(_c, codeStatistics.get(_c) + 1);                     // 覆盖前面的
            } else {
                codeStatistics.put(_c, 1);
            }
        }
        System.out.println("频率表如下:");
        for (Map.Entry<Character, Integer> entry : codeStatistics.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
        System.out.println();
    }

    /**
     * 遍历频率表，生成PFC森林，再生成树
     */
    private static void initTree() {
        PriorityQueue<TrieNode> PFCForest = new PriorityQueue<>();                      // 森林 优先级队列
        for (Map.Entry<Character, Integer> entry : codeStatistics.entrySet()) {
            TrieNode node = new TrieNode(entry.getKey(), entry.getValue(), null, null, null);
            PFCForest.add(node);
            leafs.add(node);
        }
        while (PFCForest.size() > 1) {
            // 每次拿出两棵频率最低的树
            TrieNode node1 = PFCForest.poll();
            TrieNode node2 = PFCForest.poll();
            // 构造新的树
            TrieNode sumNode = new TrieNode('0', node1.freq + node2.freq, node1, node2, null);
            node1.parent = sumNode;
            node2.parent = sumNode;
            // 放回去
            PFCForest.add(sumNode);
        }
        root = PFCForest.poll();
    }

    /**
     * 构造编码字典
     * 某个字符对应的编码为：从该字符所在的叶子节点向上搜索，
     * 如果该字符节点是父节点的左节点，编码字符之前加0，反之如果是右节点，加1，直到根节点
     */
    private static void initCodeTable() {
        for (TrieNode leafNode : leafs) {
            Character character = leafNode.ch;
            String codeword = "";
            TrieNode currentNode = leafNode;

            do {
                if (currentNode.isLeftChild()) {
                    codeword = "0" + codeword;
                } else {
                    codeword = "1" + codeword;
                }
                currentNode = currentNode.parent;
            } while (!currentNode.isRoot());

            codeTable.put(character, codeword);
        }
        System.out.println("编码表如下:");
        for (Map.Entry<Character, String> entry : codeTable.entrySet()) {
            System.out.println(entry.getKey() + "：" + entry.getValue());
        }
        System.out.println();
    }

    /**
     * 对字符串进行编码
     *
     * @param originalStr 原始字符串
     * @return 编码结果
     */
    private static String encode(final String originalStr) {

        if (originalStr == null || originalStr.equals("")) {
            return "";
        }

        char[] charArray = originalStr.toCharArray();
        StringBuilder builder = new StringBuilder();
        for (char c : charArray) {
            Character character = c;
            builder.append(codeTable.get(character));
        }
        return builder.toString();
    }

    /**
     * 将编码的01字符串转化为byte
     *
     * @param bitString 8位的01字符串 高位在右 低位在左
     * @return 字节
     */
    private static int bitStringToInt(final String bitString) {
        char[] bits = bitString.toCharArray();
        int b = 0;
        int n = 1; // 每一位的基数
        for (char bit : bits) {
            b += (bit - '0') * n;
            n *= 2;
        }
        return b;
    }

    /**
     * 将编码字符串转化为01流写入压缩文件
     *
     * @param codeStr 编码字符串
     * @throws Exception 文件读写异常
     */
    private static void writeHuffmanFile(final String codeStr) throws Exception {
        StringBuilder stringBuilder = new StringBuilder(codeStr);
        File file = new File("data/cacm.huffman.all");
        if (!file.exists()) {
            boolean fileNewFile = file.createNewFile();
            if (!fileNewFile) {
                throw new Exception("文件创建失败");
            }
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file);

        /* 编码字符串的长度不是8的倍数的话在后面补充0 */
        int remainder = codeStr.length() % BITS_OF_BYTE; // 余数
        if (remainder != 0) {
            while (remainder < BITS_OF_BYTE) {
                stringBuilder.append("0");
                remainder++;
            }
        }

        String bitString;
        String s = stringBuilder.toString();
        for (int i = 0; i < s.length(); i += BITS_OF_BYTE) {
            bitString = s.substring(i, i + BITS_OF_BYTE);                   // 截取8位 注意这里不用i + BITS_OF_BYTE - 1
            int b = bitStringToInt(bitString);
            fileOutputStream.write(b);
        }
    }

    /**
     * int转为01字符串以供解码
     *
     * @param b 字节
     * @return 8位的01字符串
     */
    private static String intToZeroOneString(int b) {
        StringBuilder stringBuilder = new StringBuilder();
        while (b != 0) {
            stringBuilder.append(b % 2 == 0 ? '0' : '1'); // 低位在左，高位在右
            b /= 2;
        }
        int lengthAdd = BITS_OF_BYTE - stringBuilder.length(); // 需要补多少个0
        for (int i = 1; i <= lengthAdd; i++) {
            stringBuilder.append('0');
        }
        return stringBuilder.toString();
    }

    /**
     * 读取压缩文件，转化为01字符串
     *
     * @return 01字符串
     * @throws Exception 文件读写异常
     */
    private static String readHuffmanFile() throws Exception {
        File file = new File("data/cacm.huffman.all");
        FileInputStream fileInputStream = new FileInputStream(file);
        int read;
        StringBuilder stringBuilder = new StringBuilder();
        while ((read = fileInputStream.read()) != -1) {
            stringBuilder.append(intToZeroOneString(read));
        }
        return stringBuilder.toString();
    }

    /**
     * 根据huffman编码树将01字符串转为字符
     * 依次取出二进制的每一位，从树根向下搜索，1向右，0向左，到了叶子节点(命中)，退回根节点继续重复以上动作
     *
     * @param huffmanStr 压缩完的字符串
     * @return 原文字符串
     * @throws Exception 叶子节点异常
     */
    private static String decode(final String huffmanStr) throws Exception {

        if (huffmanStr == null || huffmanStr.equals("")) {
            return "";
        }

        char[] binaryCharArray = huffmanStr.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();

        TrieNode node = root;
        for (int i = 0; i < binaryCharArray.length - 1; ) {
            do {
                node = binaryCharArray[i] == '0' ? node.left : node.right;
                i++;
            } while (!node.isLeaf());
            stringBuilder.append(node.ch);
            node = root;
        }

        return stringBuilder.toString();
    }

    /**
     * 将解压缩的字符串写入文件
     *
     * @param result 解压缩后的字符串
     * @throws Exception 文件读写异常
     */
    private static void writeResultFile(final String result) throws Exception {
        File file = new File("data/cacm.new.all");
        if (!file.exists()) {
            boolean fileNewFile = file.createNewFile();
            if (!fileNewFile) {
                throw new Exception("文件创建失败");
            }
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(result.getBytes());
    }

    public static void main(final String[] args) {
        char[] charArray = readOriginalFile();
        statistics(charArray);
//        byte[] charArray = new byte[0];
//        try {
//            charArray = readOriginalFile2();
//            statistics2(charArray);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        initTree();
        initCodeTable();

        try {
            String originalStr = charArray != null ? new String(charArray) : "";
            long beginToEncode = new Date().getTime();
            String codeStr = encode(originalStr);
            long endToEncode = new Date().getTime();
            System.out.println("压缩消耗：" + (endToEncode - beginToEncode) + "ms");

            System.out.println("总字符长度：" + (charArray != null ? charArray.length : 0));
            System.out.println("编码字节数：" + (codeStr.length() / BITS_OF_BYTE
                    + (codeStr.length() % BITS_OF_BYTE == 0 ? 0 : 1)));
            System.out.println("平均字符长度（压缩率）："
                    + (double) (codeStr.length() / (charArray != null ? charArray.length : 1)) / BITS_OF_BYTE);

            writeHuffmanFile(codeStr);

            String codeStr2 = readHuffmanFile();

            long beginToDecode = new Date().getTime();
            String _originalStr = decode(codeStr2);
            long endToDecode = new Date().getTime();
            System.out.println("解压缩消耗：" + (endToDecode - beginToDecode) + "ms");

            writeResultFile(originalStr.substring(0, _originalStr.length() - 1));        // 注意去掉最后一个换行符
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
