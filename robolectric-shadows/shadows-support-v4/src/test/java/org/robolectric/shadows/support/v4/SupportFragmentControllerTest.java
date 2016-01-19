package org.robolectric.shadows.support.v4;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.R;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.util.TestRunnerWithManifest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;

@RunWith(TestRunnerWithManifest.class)
public class SupportFragmentControllerTest {
  @Test
  public void initialNotAttached() {
    final LoginFragment fragment = new LoginFragment();
    SupportFragmentController.of(fragment);

    assertThat(fragment.getView()).isNull();
    assertThat(fragment.getActivity()).isNull();
    assertThat(fragment.isAdded()).isFalse();
  }

  @Test
  public void initialNotAttached_customActivity() {
    final LoginFragment fragment = new LoginFragment();
    SupportFragmentController.of(fragment, LoginActivity.class);

    assertThat(fragment.getView()).isNull();
    assertThat(fragment.getActivity()).isNull();
    assertThat(fragment.isAdded()).isFalse();
  }

  @Test
  public void attachedAfterCreate() {
    final LoginFragment fragment = new LoginFragment();
    SupportFragmentController.of(fragment).create();

    assertThat(fragment.getActivity()).isNotNull();
    assertThat(fragment.isAdded()).isTrue();
    assertThat(fragment.isResumed()).isFalse();
  }

  @Test
  public void attachedAfterCreate_customActivity() {
    final LoginFragment fragment = new LoginFragment();
    SupportFragmentController.of(fragment, LoginActivity.class).create();

    assertThat(fragment.getActivity()).isNotNull();
    assertThat(fragment.getActivity()).isInstanceOf(LoginActivity.class);
    assertThat(fragment.isAdded()).isTrue();
    assertThat(fragment.isResumed()).isFalse();
  }

  @Test
  public void isResumed() {
    final LoginFragment fragment = new LoginFragment();
    SupportFragmentController.of(fragment, LoginActivity.class).create().start().resume();

    assertThat(fragment.getView()).isNotNull();
    assertThat(fragment.getActivity()).isNotNull();
    assertThat(fragment.isAdded()).isTrue();
    assertThat(fragment.isResumed()).isTrue();
    assertThat(fragment.getView().findViewById(R.id.tacos)).isNotNull();
  }

  @Test
  public void isPaused() {
    final LoginFragment fragment = spy(new LoginFragment());
    SupportFragmentController.of(fragment, LoginActivity.class).create().start().resume().pause();

    assertThat(fragment.getView()).isNotNull();
    assertThat(fragment.getActivity()).isNotNull();
    assertThat(fragment.isAdded()).isTrue();
    assertThat(fragment.isResumed()).isFalse();

    verify(fragment).onResume();
    verify(fragment).onPause();
  }

  @Test
  public void isStopped() {
    final LoginFragment fragment = spy(new LoginFragment());
    SupportFragmentController.of(fragment, LoginActivity.class).create().start().resume().pause().stop();

    assertThat(fragment.getView()).isNotNull();
    assertThat(fragment.getActivity()).isNotNull();
    assertThat(fragment.isAdded()).isTrue();
    assertThat(fragment.isResumed()).isFalse();

    verify(fragment).onStart();
    verify(fragment).onResume();
    verify(fragment).onPause();
    verify(fragment).onStop();
  }
  
  @Test
  public void withIntent() {
    final LoginFragment fragment = new LoginFragment();
    final SupportFragmentController<LoginFragment> controller = SupportFragmentController.of(fragment, LoginActivity.class);

    Intent intent = new Intent("test_action");
    intent.putExtra("test_key", "test_value");
    controller.withIntent(intent).create();

    Intent intentInFragment = controller.get().getActivity().getIntent();
    assertThat(intentInFragment.getAction()).isEqualTo("test_action");
    assertThat(intentInFragment.getExtras().getString("test_key")).isEqualTo("test_value");
  }

  @Test
  public void visible() {
    final LoginFragment fragment = new LoginFragment();
    final SupportFragmentController<LoginFragment> controller = SupportFragmentController.of(fragment, LoginActivity.class);

    controller.create().start().resume();
    assertThat(fragment.isVisible()).isFalse();

    controller.visible();
    assertThat(fragment.isVisible()).isTrue();
  }

  @Test
  public void createWithBundle() {
    final LoginFragmentWithState spyFragment = new LoginFragmentWithState();
//    final LoginFragment spyFragment = new LoginFragment();
    final SupportFragmentController<LoginFragmentWithState> controller = SupportFragmentController.of(spyFragment, LoginActivity.class);

    controller.create().start();
    controller.get().setState("some_state");

    FragmentActivity activity = controller.get().getActivity();
    ShadowActivity shadowActivity = shadowOf(activity);
    shadowActivity.recreate();


//    final Bundle bundle = new Bundle();
//    controller.create(bundle);

//    verify(spyFragment).onCreate(bund);
  }

  public static class LoginFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      return inflater.inflate(R.layout.fragment_contents, container, false);
    }
  }

  public static class LoginFragmentWithState extends LoginFragment {
    public static final String STATE_KEY = "STATE_KEY";

    public String state;

    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      if (savedInstanceState != null) {
        state = savedInstanceState.getString(STATE_KEY);
      }
    }

    public void setState(String newState) {
      this.state = newState;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
      super.onSaveInstanceState(outState);
      outState.putString(STATE_KEY, state);
    }
  }

  private static class LoginActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      LinearLayout view = new LinearLayout(this);
      view.setId(1);

      setContentView(view);
    }
  }
}
