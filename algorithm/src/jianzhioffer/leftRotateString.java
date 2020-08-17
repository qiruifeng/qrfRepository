package jianzhioffer;

public class leftRotateString {
    public static String LeftRotateString(String str,int n) {

        if (str.length()==0||str==null){
            return "";
        }

        int N=n%(str.length());

        StringBuffer stringBuffer=new StringBuffer(str);

        String preString=str.substring(0,N);

        stringBuffer.replace(0,str.length(),str.substring(N));

        stringBuffer.replace(str.length()-N,str.length(),preString);

        return stringBuffer.toString();

    }

    public static void main(String[] args) {
        String a="";
        System.out.println(LeftRotateString(a,6));
    }
}
