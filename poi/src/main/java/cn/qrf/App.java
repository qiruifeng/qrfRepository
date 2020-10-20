package cn.qrf;

import java.util.Date;

import static cn.qrf.tools.tools.printArr;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Date d1=new Date(2020,1,20,3,45);
        Date d2=new Date(2020,1,20,3,45);
//        System.out.println( d );

        String str="1.2.3.4.5";
        String[] temp=str.split("\\.");
        printArr(temp);
    }
}
