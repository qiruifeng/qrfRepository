package cn.qrf.unzip;

public class test {
    public static void main(String[] args) {
        System.out.println("kaishi");
        Object[][] objects=new Object[3][2];

        objects[0][0]=1;
        objects[0][1]=2;
        objects[2][0]=3;
        objects[2][1]=4;

        for (int i = 0; i < objects.length; i++) {
            for (int j = 0; j < objects[0].length; j++) {
                System.out.print(objects[i][j]+" ");
            }
            System.out.println();
        }
    }
}
