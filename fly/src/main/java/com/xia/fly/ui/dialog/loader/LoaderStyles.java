package com.xia.fly.ui.dialog.loader;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author xia
 * @date 2018/7/29.
 */
@SuppressWarnings("WeakerAccess")
public final class LoaderStyles {
    public static final String BALL_PULSE_INDICATOR = "BALL_PULSE_INDICATOR";
    public static final String BALL_GRID_PULSE_INDICATOR = "BALL_GRID_PULSE_INDICATOR";
    public static final String BALL_CLIP_ROTATE_INDICATOR = "BALL_CLIP_ROTATE_INDICATOR";
    public static final String BALL_CLIP_ROTATE_PULSE_INDICATOR = "BALL_CLIP_ROTATE_PULSE_INDICATOR";
    public static final String SQUARE_SPIN_INDICATOR = "SQUARE_SPIN_INDICATOR";
    public static final String BALL_CLIP_ROTATE_MULTIPLE_INDICATOR = "BALL_CLIP_ROTATE_MULTIPLE_INDICATOR";
    public static final String BALL_PULSE_RISE_INDICATOR = "BALL_PULSE_RISE_INDICATOR";
    public static final String BALL_ROTATE_INDICATOR = "BALL_ROTATE_INDICATOR";
    public static final String CUBE_TRANSITION_INDICATOR = "CUBE_TRANSITION_INDICATOR";
    public static final String BALL_ZIG_ZAG_INDICATOR = "BALL_ZIG_ZAG_INDICATOR";
    public static final String BALL_ZIG_ZAG_DEFLECT_INDICATOR = "BALL_ZIG_ZAG_DEFLECT_INDICATOR";
    public static final String BALL_TRIANGLE_PATH_INDICATOR = "BALL_TRIANGLE_PATH_INDICATOR";
    public static final String BALL_SCALE_INDICATOR = "BALL_SCALE_INDICATOR";
    public static final String LINE_SCALE_INDICATOR = "LINE_SCALE_INDICATOR";
    public static final String LINE_SCALE_PARTY_INDICATOR = "LINE_SCALE_PARTY_INDICATOR";
    public static final String BALL_SCALE_MULTIPLE_INDICATOR = "BALL_SCALE_MULTIPLE_INDICATOR";
    public static final String BALL_PULSE_SYNC_INDICATOR = "BALL_PULSE_SYNC_INDICATOR";
    public static final String BALL_BEAT_INDICATOR = "BALL_BEAT_INDICATOR";
    public static final String LINE_SCALE_PULSE_OUT_INDICATOR = "LINE_SCALE_PULSE_OUT_INDICATOR";
    public static final String LINE_SCALE_PULSE_OUT_RAPID_INDICATOR = "LINE_SCALE_PULSE_OUT_RAPID_INDICATOR";
    public static final String BALL_SCALE_RIPPLE_INDICATOR = "BALL_SCALE_RIPPLE_INDICATOR";
    public static final String BALL_SCALE_RIPPLE_MULTIPLE_INDICATOR = "BALL_SCALE_RIPPLE_MULTIPLE_INDICATOR";
    public static final String BALL_SPIN_FADE_LOADER_INDICATOR = "BALL_SPIN_FADE_LOADER_INDICATOR";
    public static final String LINE_SPIN_FADE_LOADER_INDICATOR = "LINE_SPIN_FADE_LOADER_INDICATOR";
    public static final String TRIANGLE_SKEW_SPIN_INDICATOR = "TRIANGLE_SKEW_SPIN_INDICATOR";
    public static final String PACMAN_INDICATOR = "PACMAN_INDICATOR";
    public static final String BALL_GRID_BEAT_INDICATOR = "BALL_GRID_BEAT_INDICATOR";
    public static final String SEMI_CIRCLE_SPIN_INDICATOR = "SEMI_CIRCLE_SPIN_INDICATOR";
    public static final String CUSTOM_INDICATOR = "CUSTOM_INDICATOR";

    @StringDef({BALL_PULSE_INDICATOR, BALL_GRID_PULSE_INDICATOR, BALL_CLIP_ROTATE_INDICATOR,
            BALL_CLIP_ROTATE_PULSE_INDICATOR, SQUARE_SPIN_INDICATOR, BALL_CLIP_ROTATE_MULTIPLE_INDICATOR,
            BALL_PULSE_RISE_INDICATOR, BALL_ROTATE_INDICATOR, CUBE_TRANSITION_INDICATOR,
            BALL_ZIG_ZAG_INDICATOR, BALL_ZIG_ZAG_DEFLECT_INDICATOR, BALL_TRIANGLE_PATH_INDICATOR,
            BALL_SCALE_INDICATOR, LINE_SCALE_INDICATOR, LINE_SCALE_PARTY_INDICATOR,
            BALL_SCALE_MULTIPLE_INDICATOR, BALL_PULSE_SYNC_INDICATOR, BALL_BEAT_INDICATOR,
            LINE_SCALE_PULSE_OUT_INDICATOR, LINE_SCALE_PULSE_OUT_RAPID_INDICATOR, BALL_SCALE_RIPPLE_INDICATOR,
            BALL_SCALE_RIPPLE_MULTIPLE_INDICATOR, BALL_SPIN_FADE_LOADER_INDICATOR, LINE_SPIN_FADE_LOADER_INDICATOR,
            TRIANGLE_SKEW_SPIN_INDICATOR, PACMAN_INDICATOR, BALL_GRID_BEAT_INDICATOR,
            SEMI_CIRCLE_SPIN_INDICATOR, CUSTOM_INDICATOR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LoaderStyle {
    }
}
