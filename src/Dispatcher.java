import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.Random;

public class Dispatcher {
    public static void main(String[] args){
        int[] array = new int[1000000];
        Random rand = new Random();
        ForkJoinPool fp = ForkJoinPool.commonPool();
        long t = System.currentTimeMillis();
        for (int i = 0; i < array.length; i++){
            array[i] = rand.nextInt(101);
        }
        t = System.currentTimeMillis() - t;
        System.out.println(t);
        TaskForCalc tfc = new TaskForCalc(array, 0, 1000000);
        System.out.println(fp.invoke(tfc));
    }
}

/* class TaskForInit extends RecursiveAction {
    int[] array;
    int startInd, finishInd;
    Random rand;

    TaskForInit(int[] array, int startInd, int finishInd, Random rand){
        this.array = array;
        this.startInd = startInd;
        this.finishInd = finishInd;
        this.rand = rand;
    }

     @Override
     protected void compute() {
        if ((finishInd - startInd) < 21){
            //System.out.println(startInd + " " + finishInd);
            long t = System.currentTimeMillis();
            for (int i = startInd; i < finishInd; i++){
                array[i] = rand.nextInt(101);
              //  System.out.println(i + " " + array[i]);
            }
        } else {
            //System.out.println();
            //System.out.println(Thread.currentThread().getName() + " " + startInd + " " + finishInd + " no");
            TaskForInit left = new TaskForInit(array, startInd, startInd + (int) Math.ceil((finishInd - startInd) / 2), rand);
            TaskForInit right = new TaskForInit(array, startInd + (int) Math.ceil((finishInd - startInd) / 2), finishInd, rand);
            left.fork();
            right.fork();
            left.join();
            right.join();
        }
     }
 }*/

class TaskForCalc extends RecursiveTask<Integer> {
    int[] array;
    int startInd, finishInd;

    TaskForCalc(int[] array, int startInd, int finishInd){
        this.array = array;
        this.startInd = startInd;
        this.finishInd = finishInd;
    }

    @Override
    protected Integer compute() {
        int sum = 0;
        if (finishInd - startInd < 21){
            for (int i = startInd; i < finishInd; i++){
                sum += array[i];
            }
            return sum;
        }
        TaskForCalc left = new TaskForCalc(array, startInd, startInd + (int)Math.ceil((finishInd - startInd)/2));
        TaskForCalc right = new TaskForCalc(array, startInd + (int)Math.ceil((finishInd - startInd)/2), finishInd);
        left.fork();
        right.fork();
        sum = left.join() + right.join();
        return sum;
    }
}