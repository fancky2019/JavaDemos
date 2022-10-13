package Test.test2022.annotation;

import java.lang.annotation.*;
import java.util.Arrays;


public class AnnotationTest {
    public void test() {
        fun();
    }

    private void fun() {
        AnnotationA a = AnnotationTestA.class.getAnnotation(AnnotationA.class);
        //注解AnnotationA上的注解
        AnnotationB ab = a.annotationType().getAnnotation(AnnotationB.class);

        AnnotationA b = AnnotationTestB.class.getAnnotation(AnnotationA.class);
        //null
        AnnotationB bb = AnnotationTestB.class.getAnnotation(AnnotationB.class);

        AnnotationC c = AnnotationTestC.class.getAnnotation(AnnotationC.class);
        //获取注解上注解
        AnnotationA ca = c.annotationType().getAnnotation(AnnotationA.class);
        //null
        AnnotationB cb = AnnotationTestC.class.getAnnotation(AnnotationB.class);


        Annotation[] annotations = AnnotationTestC.class.getAnnotations();
        for (Annotation annotation : annotations) {
            System.out.println(annotation);
            System.out.println("-----------");
            Arrays.stream(annotation.annotationType().getAnnotations())
                    .forEach(System.out::println);
        }


        int m = 0;
    }
}

@AnnotationA
class AnnotationTestA {

}

@AnnotationA
@AnnotationB
class AnnotationTestB {

}


@AnnotationC
class AnnotationTestC {

}
