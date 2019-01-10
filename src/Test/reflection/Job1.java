package Test.reflection;

@JobAnnotation(value = "1", description = "1")
public class Job1 implements Job {
    @Override
    public void starter() {
        System.out.println("Job1.starter()");
    }
}
