package me.loc2.loc2me.ui.graphics;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

import me.loc2.loc2me.util.Ln;

public final class Animations {

    private static final Integer CROSS_FADE_ANIMATION = 1000;

    public static void crossFade(final View viewToShow, final View viewToHide) {
        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        viewToShow.setAlpha(0f);
        viewToShow.setVisibility(View.VISIBLE);

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        viewToShow.animate()
                .alpha(1f)
                .setDuration(CROSS_FADE_ANIMATION)
                .setListener(null);

        // Animate the loading view to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        viewToHide.animate()
                .alpha(0f)
                .setDuration(CROSS_FADE_ANIMATION)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        viewToHide.setVisibility(View.GONE);
                    }
                });

    }

    private static String visibilityToString(int view) {
        switch (view) {
            case View.VISIBLE:
                return "VISIBLE";
            case View.INVISIBLE:
                return "INVISIBLE";
            case View.GONE:
                return "GONE";
            default:
                return "UNKNOWN";
        }
    }


    private Animations() {

    }


}
