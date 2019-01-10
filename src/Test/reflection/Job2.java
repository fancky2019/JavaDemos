package Test.reflection;

@JobAnnotation(value = "2",description = "2")
public class Job2 implements Job {
    @Override
    public void starter() {
        System.out.println("Job2.starter()");
    }
}
