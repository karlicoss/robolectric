package org.robolectric;

import javax.annotation.Generated;

import org.robolectric.annotation.processing.objects.AnyObject;
import org.robolectric.annotation.processing.objects.Dummy;
import org.robolectric.annotation.processing.shadows.ShadowClassNameOnly;
import org.robolectric.annotation.processing.shadows.ShadowDummy;
import org.robolectric.bytecode.RobolectricInternals;
import org.robolectric.bytecode.ShadowWrangler;

@Generated("org.robolectric.annotation.processing.RoboProcessor")
public class RobolectricBase {

  public static final Class<?>[] DEFAULT_SHADOW_CLASSES = {
    ShadowClassNameOnly.class,
    ShadowDummy.class,
  };
  
  public static ShadowClassNameOnly shadowOf(AnyObject actual) {
    return (ShadowClassNameOnly) shadowOf_(actual);
  }
  
  public static ShadowDummy shadowOf(Dummy actual) {
    return (ShadowDummy) shadowOf_(actual);
  }
  
  public static void reset() {
    ShadowClassNameOnly.anotherResetter();
    ShadowDummy.resetter_method();
  }
  
  public static ShadowWrangler getShadowWrangler() {
    return ((ShadowWrangler) RobolectricInternals.getClassHandler());
  }
  
  @SuppressWarnings({"unchecked"})
  public static <P, R> P shadowOf_(R instance) {
    return (P) getShadowWrangler().shadowOf(instance);
  }
}
