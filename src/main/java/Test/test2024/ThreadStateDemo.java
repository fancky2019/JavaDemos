package Test.test2024;

public class ThreadStateDemo implements Runnable {
    public void run() {
        try {
            Thread.sleep(1000);
            System.out.println("当前线程状态：" + Thread.currentThread().getState());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void test() {
        ThreadStateDemo demo = new ThreadStateDemo();
        Thread thread = new Thread(demo);
        System.out.println("new Thread() 新建线程状态：" + thread.getState());

        thread.start();
        System.out.println("thread.start()可运行线程状态：" + thread.getState());
        Object obj = new Object();

        try {
            obj.wait();
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(" Thread.sleep阻塞线程状态：" + thread.getState());

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("终止线程状态：" + thread.getState());
        int m = 0;
    }
}
