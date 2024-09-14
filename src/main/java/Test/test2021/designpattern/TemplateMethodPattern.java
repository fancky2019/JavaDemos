package Test.test2021.designpattern;

public class TemplateMethodPattern {
//    模板方法模式是一种软件设计模式，它定义了一个算法的步骤，并允许子类为其中的一些步骤提供具体的实现方式。
    public void  test()
    {
        Worker worker=new Fancky();
        worker.work();
    }
}

abstract class Worker {
    public final void work() {
        walk();
        lunch();
        coding();
    }

    public void walk() {
        System.out.println("default walk");
    }

    public void lunch() {
        System.out.println("default lunch");
    }

    public void coding() {
        System.out.println("default coding");
    }
}

class Fancky extends Worker
{
    @Override
    public void lunch() {
        System.out.println("Fancky  lunch");
    }
}

