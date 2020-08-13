package lessonFromZuo.basic_class_04;

public class hanoi {
    public static void hanoi(int n) {
        process(n, "左", "右", "中");
    }

    /**
     * 表示1~N的问题
     *
     * @param N
     * @param from
     * @param to
     * @param help
     */
    public static void process(int N, String from, String to, String help) {
        if (N <= 0) {
            System.out.println("请输入正整数");
        } else if (N == 1) {
            System.out.println("将1从 " + from + "移动到 " + to + " 上");
        } else if (N > 1 && N < 20) {
            process(N - 1, from, help, to);
            System.out.println("将" + N + "从 " + from + "移动到 " + to + " 上");
            process(N - 1, help, to, from);
        } else {
            System.out.println("输入值过大，计算时间过长");
        }
    }

    public static void main(String[] args) {
        hanoi(18);
    }
}
