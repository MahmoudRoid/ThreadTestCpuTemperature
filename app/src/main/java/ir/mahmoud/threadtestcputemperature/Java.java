package ir.mahmoud.threadtestcputemperature;

import android.widget.Toast;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Java {
    private static final Java ourInstance = new Java();

    public static Java getInstance() {
        return ourInstance;
    }

    private Java() {
    }

    // test commit

    ////////////////////////////////////

    List<Long> list = new ArrayList<>();
    int count = 1;
    public boolean isStopped = false;
    ThreadPoolExecutor executor ;
//    int i = 0;


    public void execute(){

        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(count);


        for (int i = 0; i < count; i++) {
            createThreads(i);
//            testThreads();
        }

        executor.shutdown();
    }

    private void createThreads(int i) {
        executor.submit(()->{
            while (true){
                if(isStopped)
                    break;
                else
                    list.set(i, list.get(i)+1 );

            }
        });
    }

    public void initialList(int threadCount) {
        this.count = threadCount;

        list.clear();
        for (int j = 0; j < count; j++) {
            list.add(0L);
        }


        execute();
    }

    public void stop(){
        isStopped = true;
    }

    public String getResult(){

        String result = "";

        for (int j = 0; j < count; j++) {
            result += String.valueOf(list.get(j)) + "\n";
        }

        return result;
    }

    //////////////////////////////////////////   execute

    public void testThreads(){
        executor.submit(()->{
                while (true){
                    if(isStopped)
                        break;
                    else
                        list.set(0, list.get(0)+1 );
                }
            });

        executor.submit(()->{
            while (true){
                if(isStopped)
                    break;
                else
                    list.set(1, list.get(1)+1 );
            }
        });

        executor.submit(()->{
            while (true){
                if(isStopped)
                    break;
                else
                    list.set(2, list.get(2)+1 );
            }
        });

        executor.submit(()->{
            while (true){
                if(isStopped)
                    break;
                else
                    list.set(3, list.get(3)+1 );
            }
        });


        executor.submit(()->{
            while (true){
                if(isStopped)
                    break;
                else
                    list.set(4, list.get(4)+1 );
            }
        });

        executor.submit(()->{
            while (true){
                if(isStopped)
                    break;
                else
                    list.set(5, list.get(5)+1 );
            }
        });

        executor.submit(()->{
            while (true){
                if(isStopped)
                    break;
                else
                    list.set(6, list.get(6)+1 );
            }
        });

        executor.submit(()->{
            while (true){
                if(isStopped)
                    break;
                else
                    list.set(7, list.get(7)+1 );
            }
        });
    }


}
