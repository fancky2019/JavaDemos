package Test;

/**
 * 匿名内部类实现接口
 *
 * 匿名内部类格式：
 * function(new 类名/接口/抽象类(){
 *
 * });
 *
 * 当接口只有一个方法时候（没有实现）匿名内部类可以用lambda表达式代替
 */
public class AnonymousInternalClassTest {
    public  void  test()
    {
        getAnimalInterfaceInfo(new Animal() {
            @Override
            public Integer getAge() {
                return 1;
            }
        });

        getAnimalInterfaceInfo(()->
        {
            System.out.println(54);
            return  2;
        });

        getAnimalInterfaceInfo(()-> 3);


        getPersonInterfaceInfo(new Person() {
            @Override
            public void getName() {

            }

            @Override
            public Integer getAge() {
                return 4;
            }
        });

    }

    private  void getAnimalInterfaceInfo(Animal animal)
    {
        System.out.printf("%d\n",animal.getAge());
    }
    private  void getPersonInterfaceInfo(Person person)
    {
        System.out.printf("%d\n",person.getAge());
    }
}



