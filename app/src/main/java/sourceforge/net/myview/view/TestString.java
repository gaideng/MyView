package sourceforge.net.myview.view;


/**
 * Created：2018/4/16 on 16:03
 * Author:gaideng on dg
 * Description:
 */

public class TestString {
    public static void main(String args[]) {
        test1();
//        test2();
    }

    public static void test1(){
        String value = "abcdefghi";
        String value1 = "ABC";
        String value2 = "abc";
        String str1 = new String(new char[]{'a','b','c','d'},1,3);
        str1.length();
        str1.charAt(0);
        System.out.println("str1="+str1);
        System.out.println(value1.compareToIgnoreCase(value2));

    }
}
