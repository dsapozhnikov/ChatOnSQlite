package com.rubbish.homeworks;


public class TestThreads {
    static final int SIZE =  10000000;
    static final int h = SIZE/2;

    public static void firstMethod() {
        float[]arr = new float[SIZE];
        for (int i = 0; i <arr.length; i++) {
            arr[i]=1; }
        long a =  System.currentTimeMillis(); // check runtime

        for (int i = 0; i <arr.length ; i++) {
            arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }

        System.out.println(System.currentTimeMillis()-a+" -Time of the firstMethod");

    }
    public static void secondMethod() {
        float[]arr = new float[SIZE];
        float[]a1 = new float[h];
        float[]a2 = new float[h];

        for (int i = 0; i <arr.length; i++) {
            arr[i]=1; }
        long a = System.currentTimeMillis();   //check runtime

        System.arraycopy(arr,0,a1,0,h);
        System.arraycopy(arr,h,a2,0,h);
      Thread t1 =  new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0,j = h; i <h; i++) {
                    a1[i] = (float)(a1[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
                    a2[j] = (float) (a2[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));

                }

            }
        });
       Thread t2 =  new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = h; i <a2.length; i++) {

                }
            }
        });

        t1.start();
       t2.start();

        try {
            t1.join();

            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        System.arraycopy(a1,0,arr,0,h);
        System.arraycopy(a2,0,arr,h,h);

        System.out.println(System.currentTimeMillis()-a+" -Time of the second method");
    }


}
