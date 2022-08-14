package routes;

import annotations.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RouteContainer {

    private Map<String,Class<?>> controllers;


    public RouteContainer(){
        this.controllers = new HashMap<>();
    }

    public void setValues(List<Class<?>> listClass){
        for(Class<?> classE : listClass){
            if(classE.isAnnotationPresent(Controller.class)){
                controllers.put(classE.getName(), classE);
            }
        }
    }




    public Map<String, Class<?>> getControllers() {
        return controllers;
    }
}
