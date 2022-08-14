package ioc;

import annotations.Controller;
import annotations.GET;
import annotations.POST;
import annotations.Path;
import annotations.di.*;
import framework.request.Request;
import routes.ControllerPair;
import routes.HttpPair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.*;

public class DIEngine {

    private static DIEngine instance = null;
    private static String packageName = "test";

    private List<Class<?>> listClass;
    private DependencyContainer dependencyContainer;
    private Map<String,Object> listSingleton;
    private Map<String,Object> initializedControllers;
    private Map<HttpPair, ControllerPair> routes;


    private DIEngine() {
        try {
            this.listClass = new ArrayList<>(findAllClasses(packageName));
            this.dependencyContainer = new DependencyContainer();
            this.dependencyContainer.setValues(this.listClass);
            this.listSingleton = new HashMap<>();
            this.initializedControllers = new HashMap<>();
            this.routes = new HashMap<>();
            goThroughControllers();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public Set<Class<?>> findAllClasses(String packageName) throws IOException {
        Set<Class<?>> toReturn = new HashSet<>();

        InputStream stream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(packageName.replaceAll("[.]", "/"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        for(String line = reader.readLine(); line != null; line = reader.readLine()){
            if(!line.endsWith(".class")){
                toReturn.addAll(findAllClasses(packageName + "." + line ));
            }
            else{
                toReturn.add(getClass(line, packageName));
            }
        }
        return toReturn;
    }

    private Class<?> getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "." + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            // handle the exception
        }
        return null;
    }

    private void goThroughControllers(){
        for(Class<?> classE : this.listClass){
            if(classE.isAnnotationPresent(Controller.class)){
                Method[] methods = classE.getDeclaredMethods();
                for(Method method : methods){
                    if(method.isAnnotationPresent(Path.class) && (method.isAnnotationPresent(POST.class) || method.isAnnotationPresent(GET.class))){
                        String location = method.getAnnotation(Path.class).location();
                        String httpMethod = "";
                        if(method.isAnnotationPresent(GET.class)){
                            httpMethod = framework.request.enums.Method.GET.toString();
                        }
                        else{
                            httpMethod = framework.request.enums.Method.POST.toString();
                        }
                        routes.put(new HttpPair(location, httpMethod), new ControllerPair(classE, method));
                    }
                }
            }
        }
    }

    public Object inject(Class<?> classE) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if(!classE.isAnnotationPresent(Bean.class) && !classE.isAnnotationPresent(Component.class) && !classE.isAnnotationPresent(Service.class)
           && !classE.isAnnotationPresent(Controller.class)){
            throw new RuntimeException("Class doesn't have required annotation!");
        }
        else if(listSingleton.containsKey(classE.getName())){
            System.out.println(classE.getName() + " singleton");
            //ukoliko je zatrazen singleton koji je vec inicijalizovan, vracamo ga
            return listSingleton.get(classE.getName());
        }
        else if(initializedControllers.containsKey(classE.getName())){
            return initializedControllers.get(classE.getName());
        }
        else{
            Field[] fields = classE.getDeclaredFields();
            Constructor constructor = classE.getDeclaredConstructor();
            Object object = constructor.newInstance();
            //inicijalizacija atributa
            for(Field field : fields){
                if(field.isAnnotationPresent(Autowired.class)){
                    field.setAccessible(true);
                    Class<?> fieldClass = field.getType();
                    //ukoliko je interface, injectujemo impl iz dependency container-a
                    if(fieldClass.isInterface()){
                        if(!field.isAnnotationPresent(Qualifier.class)){
                            throw new RuntimeException("Class doesn't have required annotation!");
                        }
                        String specName = field.getAnnotation(Qualifier.class).value();
                        field.set(object, inject(this.dependencyContainer.getImplementation(specName)));
                    }
                    else{
                        field.set(object, inject(fieldClass));
                    }

                    if(field.getAnnotation(Autowired.class).verbose()){
                        System.out.println("Initialized "+fieldClass+" in "+field.getDeclaringClass()+" on " +
                                LocalDate.now()+" with " + field.hashCode());
                    }
                }
            }

            if(classE.isAnnotationPresent(Service.class) || (classE.isAnnotationPresent(Bean.class) &&
                    classE.getAnnotation(Bean.class).scope().equals(Scope.SINGLETON))){
                this.listSingleton.put(classE.getName(), object);
            }
            return object;
        }
    }

    public static DIEngine getInstance(){
        if(instance == null){
            instance = new DIEngine();
        }
        return instance;
    }

    public List<Class<?>> getListClass() {
        return listClass;
    }

    public Map<HttpPair, ControllerPair> getRoutes() {
        return routes;
    }

    public Map<String, Object> getInitializedControllers() {
        return initializedControllers;
    }
}
