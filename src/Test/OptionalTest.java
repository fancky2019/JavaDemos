package Test;

import Model.Student;

import java.util.Optional;

public class OptionalTest {
    public void tetst() {
        Student stu = new Student("fancky", 1);
        Student student = null;
        Optional<Student> optionalStudent = Optional.ofNullable(stu);
        //如果值存在，执行函数接口consumer，和C#的?.
        optionalStudent.ifPresent(p -> System.out.println(p.getName()));

        //取值
        Student val = null;
        if (optionalStudent.isPresent()) {
            val = optionalStudent.get();
        }
        //如果值不存在就用Null
        Student st = optionalStudent.orElse(null);
        optionalStudent = Optional.ofNullable(null);
        //  Student st1 = optionalStudent.orElse(null);


//      Optional<Student> fla=  optionalStudent.flatMap(p->Optional.of(p));
        Optional<String> fla = optionalStudent.flatMap(p -> Optional.of(p.getName()));
        Integer m = 0;
    }


}
