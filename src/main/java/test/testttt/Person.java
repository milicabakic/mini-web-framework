package test.testttt;

import annotations.di.Autowired;
import annotations.di.Bean;
import annotations.di.Qualifier;
import test.spec.Interface1;

@Bean
public class Person {

    private String name;
    private int age;

    @Autowired(verbose = true)
    @Qualifier(value = "Interface1")
    Interface1 interface1;

    public Person(){
        this.name = "Milica";
        this.age = 22;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void introduce(){
        interface1.introduceYourself();
    }
}
