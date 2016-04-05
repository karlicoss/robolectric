package org.robolectric.shadows;

import android.view.View;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.TestRunners;

import static org.mockito.Mockito.mock;

@RunWith(TestRunners.MultiApiWithDefaults.class)
public class AlalaTest {

    @Test
    public void shouldTriggerTheImeListener() {
        mock(View.class);
    }
}