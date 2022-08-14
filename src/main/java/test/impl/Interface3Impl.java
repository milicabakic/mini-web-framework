package test.impl;

import annotations.di.Qualifier;
import annotations.di.Service;
import test.spec.Interface3;

@Qualifier(value = "Interface3")
@Service
public class Interface3Impl implements Interface3 {
}
