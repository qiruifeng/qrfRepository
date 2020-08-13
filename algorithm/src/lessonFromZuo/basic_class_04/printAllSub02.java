package lessonFromZuo.basic_class_04;

public class printAllSub02 {

    public static void printAllSub(String str) {
        process(str.toCharArray(), 0, "");
    }

    public static void process(char[] str, int i, String result) {
        if (i == str.length) {
            System.out.println(result);
            return;
        }
        process(str, i + 1, result + " ");
        process(str, i + 1, result + String.valueOf(str[i]));
    }

    public static void main(String[] args) {
        printAllSub("abc");
    }
}
