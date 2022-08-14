package test.impl;

import annotations.di.Component;
import annotations.di.Qualifier;
import test.spec.Interface1;

@Qualifier(value = "Interface1")
@Component
public class Interface1Impl implements Interface1 {

    @Override
    public void introduceYourself() {
        System.out.println("Hello, my name is...");
    }
}
