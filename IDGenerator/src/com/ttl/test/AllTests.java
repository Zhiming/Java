package com.ttl.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ IdGeneratorThreadTest.class, IdPoolTest.class,
		UniqueIdArrayTest.class, UniqueIdTest.class, IDUpdateSectionsTest.class })
public class AllTests {

}
