package test;

import annotations.di.Autowired;
import annotations.di.Component;
import test.testttt.MultipleInterfaceClass;
import test.testttt.Person;

@Component
public class Test1 {

    @Autowired(verbose = true)
    Person person;

    @Autowired(verbose = true)
    MultipleInterfaceClass multipleInterfaceClass;

    public Test1(){

    }

    public void message(){
        System.out.println("Message from " + this.getClass().getName());
        person.introduce();
        System.out.println(person.getName() + ", " + person.getAge());
    }

}
