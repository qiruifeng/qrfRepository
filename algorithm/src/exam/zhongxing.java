package exam;

import java.util.Scanner;

public class zhongxing {

    //设置字符数组
    //可以添加任意不重复字符，提高能转换的进制的上限
    public static char chs[] = new char[36];

    static {
        for (int i = 0; i < 10; i++) {
            chs[i] = (char) ('0' + i);
        }
        for (int i = 10; i < chs.length; i++) {
            chs[i] = (char) ('A' + (i - 10));
        }
    }


    /**
     * 转换方法
     *
     * @param num       元数据字符串
     * @param fromRadix 元数据的进制类型
     * @param toRadix   目标进制类型
     * @return
     */
    public static String transRadix(String num, int fromRadix, int toRadix) {
        int number = Integer.valueOf(num, fromRadix);
        StringBuilder sb = new StringBuilder();
        while (number != 0) {
            sb.append(chs[number % toRadix]);
            number = number / toRadix;
        }
        return sb.reverse().toString();

    }


    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String m = scanner.nextLine();
        int n=Integer.valueOf(m);

        String[][] nums = new String[n][3];

        for (int i = 0; i < n; i++) {
            String str = scanner.nextLine();
            String[] strs = str.split(" ");
            for (int j = 0; j < 3; j++) {
                nums[i][j] = strs[j];
            }
        }


        for (int i = 0; i < n; i++) {
            String target=nums[i][0];
            int l=Integer.valueOf(nums[i][1]);
            int r=Integer.valueOf(nums[i][2]);
            int ans=0;
            for (int j=l;j<=r;j++){
                String res=transRadix(target,j,10);
                ans=ans+Integer.valueOf(res);
            }
            if (ans%2==0){
                System.out.println("0");
            }else {
                System.out.println("1");
            }

        }

        //System.out.println(transRadix("YGL", 36, 10));
    }

}
