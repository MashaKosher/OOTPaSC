import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public int[] arr;
    int num =10000;


    public static void main(String[] args) throws InterruptedException {
        new Main();
    }

    public Main() throws InterruptedException {
        arr=new int[num];

//        System.out.println("Start");
        ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        CountDownLatch latch = new CountDownLatch(num);
        long start = System.nanoTime();



        for(int i=0; i<=num-1; i++){
            final int index = i;
            final int value = i;
            service.submit(new Runnable() {

                @Override
                public void run() {
                    putValue(index, value);
                    latch.countDown();
                }
            });



        }
        latch.await();
        service.shutdown();
        long end = System.nanoTime()-start;

        System.out.println("End with time " + end);

        start = System.nanoTime();

        for(int i=0; i<=num-1; i++){
            putValue(i, i);
        }
        end = System.nanoTime()-start;

        System.out.println("End with time " + end);

    }
    public void putValue(int value, int index){
        arr[index] = value;
//        System.out.println(index + "Thread added with success value" + value);
    }

    public void putValue( ){
        System.out.println("Thread added with success value");
    }
}