package sourceforge.net.myview.view;

/**
 * Created：2018/4/14 on 8:16
 * Author:gaideng on dg
 * Description:
 */

public class MyMath {
    public static void main(String args[]) {
//        test1();
//        test2();
        test3();
    }

    private static void test3() {
        System.out.println("5.5/16:9 == " + getS(5.5f,9,16) + ";width=" + getWidth(5.5f,9,16) + ";height=" + getHeight(5.5f,9,16));
        System.out.println("5.99/18:9 == " + getS(5.99f,9,18) + ";width=" + getWidth(5.99f,9,18) + ";height=" + getHeight(5.99f,9,18));
        System.out.println("5.99/18:9 == " + getS(5.99f,1,2) + ";width=" + getWidth(5.99f,1,2) + ";height=" + getHeight(5.99f,1,2));
        System.out.println("6.44/18:9 == " + getS(6.44f,1,2) + ";width=" + getWidth(6.44f,1,2) + ";height=" + getHeight(6.44f,1,2));
        System.out.println("6.44/16:9 == " + getS(6.44f,9,16) + ";width=" + getWidth(6.44f,9,16) + ";height=" + getHeight(6.44f,9,16));
    }

    /**
     * 根据尺寸和长宽比求面积
     * @return
     */
    public static double getS(float c ,int widthRate,int heightRate){
        return widthRate * heightRate * c * c / (Math.pow(widthRate,2) + Math.pow(heightRate,2));
    }
    /**
     * 根据尺寸和长宽比求宽度
     * @return
     */
    public static double getWidth(float c ,int widthRate,int heightRate){
        return widthRate * Math.sqrt(c * c / (Math.pow(widthRate,2) + Math.pow(heightRate,2)));
    }
    /**
     * 根据尺寸和长宽比求高度
     * @return
     */
    public static double getHeight(float c ,int widthRate,int heightRate){
        return heightRate * Math.sqrt(c * c / (Math.pow(widthRate,2) + Math.pow(heightRate,2)));
    }

    private static void test2() {
        System.out.println(Math.ceil(2.3));
        System.out.println(Math.ceil(-2.3));
        System.out.println(Math.floor(2.3));
        System.out.println(Math.floor(-2.3));
        System.out.println(Math.round(-2.3));
        System.out.println(Math.round(2.3));

        System.out.println(Math.sqrt(4));
        System.out.println(Math.cbrt(8));
        System.out.println(Math.pow(2,3));
        System.out.println(Math.signum(2));
        System.out.println(Math.max(2,3));
        System.out.println(Math.nextUp(1.2f));
        System.out.println(Math.nextAfter(1.2f,1.3f));
        System.out.println(Math.copySign(1.2,-3));



    }

    public static void test1(){
        System.out.println(Math.sin(Math.toRadians(30)));
        System.out.println(Math.toRadians(180));
        System.out.println(Math.toDegrees(Math.PI));
        System.out.println(Math.tan(Math.toRadians(45)));
        System.out.println(Math.asin(Math.sin(30)));
        System.out.println(Math.asin(-0.5));
        System.out.println(Math.toDegrees(Math.asin(Math.sin(-30))));
        System.out.println(Math.sin(Math.toRadians(30)));
        System.out.println(Math.sin(Math.toRadians(-30)));
        System.out.println(Math.sin(Math.toRadians(90)));
        System.out.println(Math.sin(Math.toRadians(120)));
        System.out.println(Math.toDegrees(Math.atan2(-1,1)));
        System.out.println(Math.toDegrees(Math.atan2(1,-1)));
        System.out.println(Math.sin(Math.toRadians(330)));
        System.out.println(Math.sin(Math.toRadians(150)));
        System.out.println(Math.sin(Math.toRadians(30)));

        System.out.println(Math.asin(-1));
        System.out.println(Math.sin(Math.toRadians(89)));
    }
}
