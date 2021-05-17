package Test.test2021;

public class SwitchTest {
    public  void  test()
    {
        fun();
    }

    private  void  fun()
    {
        Integer num=null;
        switch (num)
        {
            //需要常亮表达式，不能为null
//            case null:
//                break;
            case  1:
                break;
            default:
                break;
        }

    }
}
