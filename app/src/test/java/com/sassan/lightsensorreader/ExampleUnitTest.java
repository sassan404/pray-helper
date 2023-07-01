package com.sassan.lightsensorreader;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    // todo fix
//    @Test
//    public void checkLowerThreshold() {
//        LightChangeCount.maxLightValue = 30;
//        assertEquals(LightChangeCount.lowerThreshold(), 5, 0.5);
//    }
}