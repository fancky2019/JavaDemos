package Test.test2018;


/**
 *
 * 当一个方法参数是   接口、抽象类对象时候。可以使用匿名内部类作为参数。直接 new  接口、抽象类。
 *                    注意：new 后的名称必须是方法参数对应的接口、抽象类。名字不能改。
 *
 *
 *
 * 匿名内部类实现接口
 * <p>
 * 匿名内部类格式：
 * function(new 类名/接口/抽象类(){
 *
 * });
 *
 * 当接口只有一个方法时候（没有实现）匿名内部类可以用lambda表达式代替
 */
public class AnonymousInternalClassTest {
    public void test() {
        getAnimalInterfaceInfo(new Animal() {
            @Override
            public Integer getAge() {
                return 1;
            }
        });

        getAnimalInterfaceInfo(() ->
        {
            System.out.println(54);
            return 2;
        });

        getAnimalInterfaceInfo(() -> 3);


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

    private void getAnimalInterfaceInfo(Animal animal) {
        System.out.printf("%d\n", animal.getAge());
    }

    private void getPersonInterfaceInfo(Person person) {
        System.out.printf("%d\n", person.getAge());
    }
}



