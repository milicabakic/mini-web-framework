package test.testttt;

import annotations.di.Autowired;
import annotations.di.Qualifier;
import annotations.di.Service;
import test.spec.Interface2;
import test.spec.Interface3;

@Service
public class MultipleInterfaceClass {

    @Autowired(verbose =  true)
    @Qualifier(value = "Interface2")
    Interface2 interface2;

    @Autowired(verbose =  true)
    @Qualifier(value = "Interface3")
    Interface3 interface3;


    public MultipleInterfaceClass(){

    }
}
