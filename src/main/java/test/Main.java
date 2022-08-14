package test;

import ioc.DIEngine;


public class Main {

    public static void main(String[] args) {
        try {
            DIEngine diEngine = DIEngine.getInstance();

            Test1 test1 = (Test1) diEngine.inject(Test1.class);
            test1.message();

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
