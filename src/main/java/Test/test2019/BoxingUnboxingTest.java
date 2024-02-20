package Test.test2019;

/**
 * primitive类型数据的包装器只有Float、Double装箱不用缓存是直接new,其他缓存值范围：-128---127
 * string 字符串内存驻留
 *
 * test2021.PrimitiveTypeCacheTest
 * 包装类型初始值为null
 * 泛型只能用包装类型、数组基本数据类型。 方法内部使用基本数据类型  其他地方使用包装类
 */
public class BoxingUnboxingTest {
    public void test() {
        equalsOperate();
        equal();
    }

    private void equalsOperate() {
        Integer[] a=new Integer[10];
        Integer a1 = new Integer(2);
        Integer a11 = 2;
        Integer a12 = 150;
        Integer a13 = 150;//不在缓存范围内，创建实例
        Integer a14 = 15;//装箱，在缓存范围内，从缓存取
        Integer a15 = Integer.valueOf(15);//装箱:实际调用的是此方法：valueOf。
        Integer a16 = a15.intValue();//拆箱：intValue

        Boolean r1 = a1 == a11;//false ==比较引用类型，比较的是同一个对象
        Boolean r11 = a12 == a13;//false 不在缓存-128---127，则创建实例
        Boolean r12 = a14 == a15;//true  在缓存-128---127，从缓存取

        //primitive类型数据只有float、double装箱不用缓存
        Float f1 = 5f;
        Double d1 = 5d;//带小数的默认double,不带小数默认int
        Double d11 = 5d;
        Double d12 = new Double(5d);
        Double d13 = Double.valueOf(5d);//装箱：直接new Double对象
        Boolean r2 = d1 == d11;
        Integer m = 0;
    }

    private void equal() {
        Integer a1 = new Integer(2);
        Integer a11 = 2;
        Integer a12 = 150;
        Integer a13 = 150;//不在缓存范围内，创建实例
        Integer a14 = 15;//装箱，在缓存范围内，从缓存取
        Integer a15 = 15;//装箱

        Boolean r1 = a1.equals(a11);//true  拆箱比较intValue
        Boolean r11 = a12.equals(a13);//true
        Boolean r12 = a14.equals(a15);//true


        //缓存装箱
        Short sh = Short.valueOf((short) 5);
        // 装箱 return (b ? TRUE : FALSE);
        Boolean bo = Boolean.valueOf(true);
        //缓存装箱
        Long lon = Long.valueOf(5l);
        //primitive类型数据的包装器只有Float、Double装箱不用缓存是直接new,其他缓存值范围：-128---127

        Float f1 = 5f;
        Float f12 = Float.valueOf(5f);//装箱 new 新的实例
        Float f13 = f12.floatValue();//先拆箱，然后装箱
        Float f14 = new Float(5f);
        //equal比较值，相等
        Boolean rf1 = f1.equals(f12);//true
        Boolean rf12 = f1.equals(f13);//true
        Boolean rf13 = f1.equals(f14);//true

        //==比较同一个对象，装箱new不相等
        Boolean rf11 = f1 == f12;//false
        Boolean rf121 = f1 == f13;//false
        Boolean rf131 = f1 == f14;//false


        Double d1 = 5d;//带小数的默认double,不带小数默认int
        Double d11 = 5d;
        Double d12 = Double.valueOf(5d);//装箱 new 新的实例
        Double d13 = d12.doubleValue();//先拆箱，然后装箱
        Double d14 = new Double(5d);
        //equal比较值，相等
        Boolean rd1 = d1.equals(d12);//true
        Boolean rd12 = d1.equals(d13);//true
        Boolean rd13 = d1.equals(d14);//true
        Boolean r2 = d1.equals(d11);

        //==比较同一个对象，装箱new不相等
        Boolean rd11 = d1 == d12;//false
        Boolean rd121 = d1 == d13;//false
        Boolean rd131 = d1 == d14;//false


        //
        String str = "abc";
        String str1 = String.valueOf("abc");
        String str12 = "abcd";
        String str13 = new String("abc");
        Boolean bs1 = str == str1;//true //字符串驻留
        Boolean bs12 = str == str12;//false
        Boolean bs13 = str == str13;//false


        //
        Boolean bs11 = str.equals(str1);//true
        Boolean bs121 = str.equals(str12);//false
        Boolean bs131 = str.equals(str13);//true

        Integer m = 0;
    }

    private void numberDeclare() {
        Short s1 = 5;
        Character ch = '5';
        Long l2 = 3L;
        Float f1 = 5f;
        Double d1 = 5d;//带小数的默认double,不带小数默认int
        Double d11 = 5d;

        short sh = 1;
        char ch11 = 5;
        char cha = 'd';
        long l21 = 3L;
        float f11 = 5f;
        double d111 = 5d;//带小数的默认double,不带小数默认int

    }

}
