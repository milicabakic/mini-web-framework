package ioc;

import annotations.di.Qualifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DependencyContainer {

    private Map<String, Class<?>> map;


    public DependencyContainer(){
        map = new HashMap<>();
    }

    public void setValues(List<Class<?>> listClass) {
        for(Class classE : listClass){
            if(classE.isAnnotationPresent(Qualifier.class)){
                String specName = ((Qualifier)classE.getAnnotation(Qualifier.class)).value();

                if(map.containsKey(specName)){
                    throw new RuntimeException("There are multiple @Bean-s with @Qualifier of the same value!");
                }

                map.put(specName, classE);
            }
        }
    }

    public Class<?> getImplementation(String specName){
        if(map.containsKey(specName)){
            return map.get(specName);
        }
        else{
            throw new RuntimeException("@Qualifier with value " + specName + " not found!");
        }
    }

}
