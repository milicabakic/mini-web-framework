package test.impl;

import annotations.di.Qualifier;
import annotations.di.Service;
import test.spec.Interface2;

@Qualifier(value = "Interface2")
@Service
public class Interface2Impl implements Interface2 {
}
