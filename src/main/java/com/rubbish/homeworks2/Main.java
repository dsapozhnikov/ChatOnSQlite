package com.rubbish.homeworks2;


import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        firstMethod();
        final int SIZE = 10000000;
        final int THREADS_COUNT = 2;
        final int PART_SIZE = SIZE/THREADS_COUNT;
        float[]mas = new float[SIZE];
        Arrays.fill(mas,1f);
        long a  = System.currentTimeMillis();
        float[][]m = new float[THREADS_COUNT][PART_SIZE];
        Thread[]threads = new Thread[THREADS_COUNT];

        for (int i = 0; i <THREADS_COUNT ; i++) {
            System.arraycopy(mas, PART_SIZE * i, m[i], 0, PART_SIZE);
            final int u = i;
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0, z = u * PART_SIZE; j < PART_SIZE; j++, z++) {
                        m[u][j] = (float) (m[u][j] * Math.sin(0.2f + z / 5) * Math.cos(0.2f + z / 5) * Math.cos(0.4f + z / 2));
                    }
                }
            });
            threads[i].start();
        }
        for (int i = 0; i <THREADS_COUNT ; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
            for (int i = 0; i<THREADS_COUNT; i++) {
                System.arraycopy(m[i],0,mas,i*PART_SIZE,PART_SIZE);
            }
        System.out.println(System.currentTimeMillis()-a+" Time of the secondmethod");

        }
    public static void firstMethod() {
        final int SIZE = 10000000;
        float[]arr = new float[SIZE];
        for (int i = 0; i <arr.length; i++) {
            arr[i]=1; }
        long a =  System.currentTimeMillis(); // check runtime

        for (int i = 0; i <arr.length ; i++) {
            arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }

        System.out.println(System.currentTimeMillis()-a+" -Time of the firstMethod");

    }

            

        }


