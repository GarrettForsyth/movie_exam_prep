package com.example.android.movieexamprep;


import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @OpenClassOnDebug
    public class TestClass{
        public int test(String str){return 0;}
    }

    @Test
    public void certainlyTrue() {
        TestClass c = mock(TestClass.class);
        when(c.test("Mockito")).thenReturn(1);
        when(c.test("Eclipse")).thenReturn(2);
        assertEquals(1, c.test("Mockito"));
    }
}