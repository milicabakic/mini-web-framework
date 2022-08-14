package routes;

import java.lang.reflect.Method;

public class ControllerPair {

    Class<?> classE;
    Method method;


    public ControllerPair(){}

    public ControllerPair(Class<?> classE, Method method){
        this.classE = classE;
        this.method = method;
    }

    public Class<?> getClassE() {
        return classE;
    }

    public Method getMethod() {
        return method;
    }
}
