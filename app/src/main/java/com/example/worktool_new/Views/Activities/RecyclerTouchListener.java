package com.example.worktool_new.Views.Activities;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Rect;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import androidx.recyclerview.widget.RecyclerView;
import com.example.worktool_new.Util.OnActivityTouchListner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener, OnActivityTouchListner {
    private static final String TAG = "RecyclerTouchListener";
    private long ANIMATION_CLOSE = 150;
    private long ANIMATION_STANDARD = 300;
    private int LONG_CLICK_DELAY = 800;
    Activity act;
    private View bgView;
    private int bgViewID;
    private int bgViewIDLeft;
    /* access modifiers changed from: private */
    public boolean bgVisible;
    private int bgVisiblePosition;
    private View bgVisibleView;
    private int bgWidth = 1;
    private int bgWidthLeft = 1;
    private boolean clickable = false;
    private ArrayList<Integer> fadeViews;
    private boolean fgPartialViewClicked;
    private View fgView;
    private int fgViewID;
    final Handler handler = new Handler();
    private int heightOutsideRView;
    Set<Integer> ignoredViewTypes;
    List<Integer> independentViews;
    private boolean isFgSwiping;
    /* access modifiers changed from: private */
    public boolean isRViewScrolling;
    /* access modifiers changed from: private */
    public boolean longClickVibrate;
    /* access modifiers changed from: private */
    public boolean longClickable = false;
    /* access modifiers changed from: private */
    public OnSwipeOptionsClickListener mBgClickListener;
    private OnSwipeOptionsClickListener mBgClickListenerLeft;
    private int mDismissAnimationRefCount = 0;
    /* access modifiers changed from: private */
    public boolean mLongClickPerformed;
    Runnable mLongPressed = new Runnable() {
        public void run() {
            if (RecyclerTouchListener.this.longClickable) {
                boolean unused = RecyclerTouchListener.this.mLongClickPerformed = true;
                if (!RecyclerTouchListener.this.bgVisible && RecyclerTouchListener.this.touchedPosition >= 0 && !RecyclerTouchListener.this.unClickableRows.contains(Integer.valueOf(RecyclerTouchListener.this.touchedPosition)) && !RecyclerTouchListener.this.isRViewScrolling) {
                    boolean unused2 = RecyclerTouchListener.this.longClickVibrate;
                    RecyclerTouchListener.this.mRowLongClickListener.onRowLongClicked(RecyclerTouchListener.this.touchedPosition);
                }
            }
        }
    };
    private boolean mPaused;
    private OnRowClickListener mRowClickListener;
    /* access modifiers changed from: private */
    public OnRowLongClickListener mRowLongClickListener;
    private int mSwipingSlop;
    private VelocityTracker mVelocityTracker;
    private int maxFlingVel;
    private int minFlingVel;
    List<Integer> optionViews;
    private RecyclerView rView;
    private int screenHeight;
    private boolean swipeable = false;
    private boolean swipeableLeftOptions = false;
    private int touchSlop;
    /* access modifiers changed from: private */
    public int touchedPosition;
    private View touchedView;
    private float touchedX;
    private float touchedY;
    List<Integer> unClickableRows;
    List<Integer> unSwipeableRows;

    private enum Animation {
        OPEN,
        CLOSE
    }

    public interface OnRowClickListener {
        void onIndependentViewClicked(int i, int i2);

        void onRowClicked(int i);
    }

    public interface OnRowLongClickListener {
        void onRowLongClicked(int i);
    }

    public interface OnSwipeListener {
        void onSwipeOptionsClosed();

        void onSwipeOptionsOpened();
    }

    public interface OnSwipeOptionsClickListener {
        void onSwipeOptionClicked(int i, int i2);
    }

    public interface RecyclerTouchListenerHelper {
        void setOnActivityTouchListener(OnActivityTouchListner onActivityTouchListner);
    }

    public RecyclerTouchListener(Activity a, RecyclerView recyclerView) {
        this.act = a;
        ViewConfiguration vc = ViewConfiguration.get(recyclerView.getContext());
        this.touchSlop = vc.getScaledTouchSlop();
        this.minFlingVel = vc.getScaledMinimumFlingVelocity() * 16;
        this.maxFlingVel = vc.getScaledMaximumFlingVelocity();
        this.rView = recyclerView;
        this.bgVisible = false;
        this.bgVisiblePosition = -1;
        this.bgVisibleView = null;
        this.fgPartialViewClicked = false;
        this.unSwipeableRows = new ArrayList();
        this.unClickableRows = new ArrayList();
        this.ignoredViewTypes = new HashSet();
        this.independentViews = new ArrayList();
        this.optionViews = new ArrayList();
        this.fadeViews = new ArrayList<>();
        this.isRViewScrolling = false;
        this.rView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                boolean z = false;
                RecyclerTouchListener.this.setEnabled(newState != 1);
                RecyclerTouchListener recyclerTouchListener = RecyclerTouchListener.this;
                if (newState != 0) {
                    z = true;
                }
                boolean unused = recyclerTouchListener.isRViewScrolling = z;
            }

            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            }
        });
    }

    public void setEnabled(boolean enabled) {
        this.mPaused = !enabled;
    }

    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent motionEvent) {
        return handleTouchEvent(motionEvent);
    }

    public void onTouchEvent(RecyclerView rv, MotionEvent motionEvent) {
        handleTouchEvent(motionEvent);
    }

    public RecyclerTouchListener setClickable(OnRowClickListener listener) {
        this.clickable = true;
        this.mRowClickListener = listener;
        return this;
    }

    public RecyclerTouchListener setClickable(boolean clickable2) {
        this.clickable = clickable2;
        return this;
    }

    public RecyclerTouchListener setLongClickable(boolean vibrate, OnRowLongClickListener listener) {
        this.longClickable = true;
        this.mRowLongClickListener = listener;
        this.longClickVibrate = vibrate;
        return this;
    }

    public RecyclerTouchListener setLongClickable(boolean longClickable2) {
        this.longClickable = longClickable2;
        return this;
    }

    public RecyclerTouchListener setIndependentViews(Integer... viewIds) {
        this.independentViews = new ArrayList(Arrays.asList(viewIds));
        return this;
    }

    public RecyclerTouchListener setUnClickableRows(Integer... rows) {
        this.unClickableRows = new ArrayList(Arrays.asList(rows));
        return this;
    }

    public RecyclerTouchListener setIgnoredViewTypes(Integer... viewTypes) {
        this.ignoredViewTypes.clear();
        this.ignoredViewTypes.addAll(Arrays.asList(viewTypes));
        return this;
    }

    public RecyclerTouchListener setSwipeable(int foregroundID, int backgroundID, OnSwipeOptionsClickListener listener) {
        this.swipeable = true;
        int i = this.fgViewID;
        if (i == 0 || foregroundID == i) {
            this.fgViewID = foregroundID;
            this.bgViewID = backgroundID;
            this.mBgClickListener = listener;
            Activity activity = this.act;
            if (activity instanceof RecyclerTouchListenerHelper) {
                ((RecyclerTouchListenerHelper) activity).setOnActivityTouchListener(this);
            }
            DisplayMetrics displaymetrics = new DisplayMetrics();
            this.act.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            this.screenHeight = displaymetrics.heightPixels;
            return this;
        }
        throw new IllegalArgumentException("foregroundID does not match previously set ID");
    }

    public RecyclerTouchListener setSwipeable(boolean value) {
        this.swipeable = value;
        if (!value) {
            invalidateSwipeOptions();
        }
        return this;
    }

    public RecyclerTouchListener setSwipeOptionViews(Integer... viewIds) {
        this.optionViews = new ArrayList(Arrays.asList(viewIds));
        return this;
    }

    public RecyclerTouchListener setUnSwipeableRows(Integer... rows) {
        this.unSwipeableRows = new ArrayList(Arrays.asList(rows));
        return this;
    }

    public RecyclerTouchListener setViewsToFade(Integer... viewIds) {
        this.fadeViews = new ArrayList<>(Arrays.asList(viewIds));
        return this;
    }

    public RecyclerTouchListener setFgFade() {
        if (!this.fadeViews.contains(Integer.valueOf(this.fgViewID))) {
            this.fadeViews.add(Integer.valueOf(this.fgViewID));
        }
        return this;
    }

    private boolean isIndependentViewClicked(MotionEvent motionEvent) {
        for (int i = 0; i < this.independentViews.size(); i++) {
            if (this.touchedView != null) {
                Rect rect = new Rect();
                this.touchedView.findViewById(this.independentViews.get(i).intValue()).getGlobalVisibleRect(rect);
                if (rect.contains((int) motionEvent.getRawX(), (int) motionEvent.getRawY())) {
                    return false;
                }
            }
        }
        return true;
    }

    private int getOptionViewID(MotionEvent motionEvent) {
        for (int i = 0; i < this.optionViews.size(); i++) {
            if (this.touchedView != null) {
                Rect rect = new Rect();
                this.touchedView.findViewById(this.optionViews.get(i).intValue()).getGlobalVisibleRect(rect);
                if (rect.contains((int) motionEvent.getRawX(), (int) motionEvent.getRawY())) {
                    return this.optionViews.get(i).intValue();
                }
            }
        }
        return -1;
    }

    private int getIndependentViewID(MotionEvent motionEvent) {
        for (int i = 0; i < this.independentViews.size(); i++) {
            if (this.touchedView != null) {
                Rect rect = new Rect();
                this.touchedView.findViewById(this.independentViews.get(i).intValue()).getGlobalVisibleRect(rect);
                if (rect.contains((int) motionEvent.getRawX(), (int) motionEvent.getRawY())) {
                    return this.independentViews.get(i).intValue();
                }
            }
        }
        return -1;
    }

    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }

    public void invalidateSwipeOptions() {
        this.bgWidth = 1;
    }

    public void openSwipeOptions(int position) {
        if (this.swipeable && this.rView.getChildAt(position) != null && !this.unSwipeableRows.contains(Integer.valueOf(position)) && !shouldIgnoreAction(position)) {
            if (this.bgWidth < 2) {
                if (this.act.findViewById(this.bgViewID) != null) {
                    this.bgWidth = this.act.findViewById(this.bgViewID).getWidth();
                }
                this.heightOutsideRView = this.screenHeight - this.rView.getHeight();
            }
            this.touchedPosition = position;
            View childAt = this.rView.getChildAt(position);
            this.touchedView = childAt;
            this.fgView = childAt.findViewById(this.fgViewID);
            View findViewById = this.touchedView.findViewById(this.bgViewID);
            this.bgView = findViewById;
            findViewById.setMinimumHeight(this.fgView.getHeight());
            closeVisibleBG((OnSwipeListener) null);
            animateFG(this.touchedView, Animation.OPEN, this.ANIMATION_STANDARD);
            this.bgVisible = true;
            this.bgVisibleView = this.fgView;
            this.bgVisiblePosition = this.touchedPosition;
        }
    }

    @Deprecated
    public void closeVisibleBG() {
        View view = this.bgVisibleView;
        if (view == null) {
            Log.e(TAG, "No rows found for which background options are visible");
            return;
        }
        view.animate().translationX(0.0f).setDuration(this.ANIMATION_CLOSE).setListener((Animator.AnimatorListener) null);
        animateFadeViews(this.bgVisibleView, 1.0f, this.ANIMATION_CLOSE);
        this.bgVisible = false;
        this.bgVisibleView = null;
        this.bgVisiblePosition = -1;
    }

    public void closeVisibleBG(final OnSwipeListener mSwipeCloseListener) {
        View view = this.bgVisibleView;
        if (view == null) {
            Log.e(TAG, "No rows found for which background options are visible");
            return;
        }
        final ObjectAnimator translateAnimator = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, new float[]{0.0f});
        translateAnimator.setDuration(this.ANIMATION_CLOSE);
        translateAnimator.addListener(new Animator.AnimatorListener() {
            public void onAnimationStart(Animator animation) {
            }

            public void onAnimationEnd(Animator animation) {
                OnSwipeListener onSwipeListener = mSwipeCloseListener;
                if (onSwipeListener != null) {
                    onSwipeListener.onSwipeOptionsClosed();
                }
                translateAnimator.removeAllListeners();
            }

            public void onAnimationCancel(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
            }
        });
        translateAnimator.start();
        animateFadeViews(this.bgVisibleView, 1.0f, this.ANIMATION_CLOSE);
        this.bgVisible = false;
        this.bgVisibleView = null;
        this.bgVisiblePosition = -1;
    }

    private void animateFadeViews(View downView, float alpha, long duration) {
        ArrayList<Integer> arrayList = this.fadeViews;
        if (arrayList != null) {
            Iterator<Integer> it = arrayList.iterator();
            while (it.hasNext()) {
                downView.findViewById(it.next().intValue()).animate().alpha(alpha).setDuration(duration);
            }
        }
    }

    private void animateFG(View downView, Animation animateType, long duration) {
        if (animateType == Animation.OPEN) {
            ObjectAnimator translateAnimator = ObjectAnimator.ofFloat(this.fgView, View.TRANSLATION_X, new float[]{(float) (-this.bgWidth)});
            translateAnimator.setDuration(duration);
            translateAnimator.setInterpolator(new DecelerateInterpolator(1.5f));
            translateAnimator.start();
            animateFadeViews(downView, 0.0f, duration);
        } else if (animateType == Animation.CLOSE) {
            ObjectAnimator translateAnimator2 = ObjectAnimator.ofFloat(this.fgView, View.TRANSLATION_X, new float[]{0.0f});
            translateAnimator2.setDuration(duration);
            translateAnimator2.setInterpolator(new DecelerateInterpolator(1.5f));
            translateAnimator2.start();
            animateFadeViews(downView, 1.0f, duration);
        }
    }

    private void animateFG(View downView, final Animation animateType, long duration, final OnSwipeListener mSwipeCloseListener) {
        final ObjectAnimator translateAnimator;
        if (animateType == Animation.OPEN) {
            translateAnimator = ObjectAnimator.ofFloat(this.fgView, View.TRANSLATION_X, new float[]{(float) (-this.bgWidth)});
            translateAnimator.setDuration(duration);
            translateAnimator.setInterpolator(new DecelerateInterpolator(1.5f));
            translateAnimator.start();
            animateFadeViews(downView, 0.0f, duration);
        } else {
            translateAnimator = ObjectAnimator.ofFloat(this.fgView, View.TRANSLATION_X, new float[]{0.0f});
            translateAnimator.setDuration(duration);
            translateAnimator.setInterpolator(new DecelerateInterpolator(1.5f));
            translateAnimator.start();
            animateFadeViews(downView, 1.0f, duration);
        }
        translateAnimator.addListener(new Animator.AnimatorListener() {
            public void onAnimationStart(Animator animation) {
            }

            public void onAnimationEnd(Animator animation) {
                if (mSwipeCloseListener != null) {
                    if (animateType == Animation.OPEN) {
                        mSwipeCloseListener.onSwipeOptionsOpened();
                    } else if (animateType == Animation.CLOSE) {
                        mSwipeCloseListener.onSwipeOptionsClosed();
                    }
                }
                translateAnimator.removeAllListeners();
            }

            public void onAnimationCancel(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    /* JADX WARNING: Code restructure failed: missing block: B:273:0x0488, code lost:
        r0 = getOptionViewID(r19);
     */
    /* JADX WARNING: Removed duplicated region for block: B:280:0x04a0  */
    /* JADX WARNING: Removed duplicated region for block: B:281:0x04a9  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean handleTouchEvent(android.view.MotionEvent r19) {
        /*
            r18 = this;
            r6 = r18
            r7 = r19
            boolean r0 = r6.swipeable
            r1 = 2
            if (r0 == 0) goto L_0x0030
            int r0 = r6.bgWidth
            if (r0 >= r1) goto L_0x0030
            android.app.Activity r0 = r6.act
            int r2 = r6.bgViewID
            android.view.View r0 = r0.findViewById(r2)
            if (r0 == 0) goto L_0x0025
            android.app.Activity r0 = r6.act
            int r2 = r6.bgViewID
            android.view.View r0 = r0.findViewById(r2)
            int r0 = r0.getWidth()
            r6.bgWidth = r0
        L_0x0025:
            int r0 = r6.screenHeight
            androidx.recyclerview.widget.RecyclerView r2 = r6.rView
            int r2 = r2.getHeight()
            int r0 = r0 - r2
            r6.heightOutsideRView = r0
        L_0x0030:
            int r0 = r19.getActionMasked()
            r8 = -1
            r9 = 0
            r2 = 1
            r10 = 0
            if (r0 == 0) goto L_0x04b9
            r11 = 0
            if (r0 == r2) goto L_0x0243
            if (r0 == r1) goto L_0x007f
            r1 = 3
            if (r0 == r1) goto L_0x0044
            goto L_0x05a5
        L_0x0044:
            android.os.Handler r0 = r6.handler
            java.lang.Runnable r1 = r6.mLongPressed
            r0.removeCallbacks(r1)
            boolean r0 = r6.mLongClickPerformed
            if (r0 == 0) goto L_0x0051
            goto L_0x05a5
        L_0x0051:
            android.view.VelocityTracker r0 = r6.mVelocityTracker
            if (r0 != 0) goto L_0x0057
            goto L_0x05a5
        L_0x0057:
            boolean r0 = r6.swipeable
            if (r0 == 0) goto L_0x0075
            android.view.View r0 = r6.touchedView
            if (r0 == 0) goto L_0x006a
            boolean r1 = r6.isFgSwiping
            if (r1 == 0) goto L_0x006a
            com.example.worktool_new.Views.Activities.RecyclerTouchListener$Animation r1 = com.example.worktool_new.Views.Activities.RecyclerTouchListener.Animation.CLOSE
            long r2 = r6.ANIMATION_STANDARD
            r6.animateFG(r0, r1, r2)
        L_0x006a:
            android.view.VelocityTracker r0 = r6.mVelocityTracker
            r0.recycle()
            r6.mVelocityTracker = r9
            r6.isFgSwiping = r10
            r6.bgView = r9
        L_0x0075:
            r6.touchedX = r11
            r6.touchedY = r11
            r6.touchedView = r9
            r6.touchedPosition = r8
            goto L_0x05a5
        L_0x007f:
            boolean r0 = r6.mLongClickPerformed
            if (r0 == 0) goto L_0x0085
            goto L_0x05a5
        L_0x0085:
            android.view.VelocityTracker r0 = r6.mVelocityTracker
            if (r0 == 0) goto L_0x05a5
            boolean r1 = r6.mPaused
            if (r1 != 0) goto L_0x05a5
            boolean r1 = r6.swipeable
            if (r1 != 0) goto L_0x0093
            goto L_0x05a5
        L_0x0093:
            r0.addMovement(r7)
            float r0 = r19.getRawX()
            float r1 = r6.touchedX
            float r0 = r0 - r1
            float r1 = r19.getRawY()
            float r3 = r6.touchedY
            float r1 = r1 - r3
            boolean r3 = r6.isFgSwiping
            if (r3 != 0) goto L_0x00d7
            float r3 = java.lang.Math.abs(r0)
            int r4 = r6.touchSlop
            float r4 = (float) r4
            int r3 = (r3 > r4 ? 1 : (r3 == r4 ? 0 : -1))
            if (r3 <= 0) goto L_0x00d7
            float r3 = java.lang.Math.abs(r1)
            float r4 = java.lang.Math.abs(r0)
            r5 = 1073741824(0x40000000, float:2.0)
            float r4 = r4 / r5
            int r3 = (r3 > r4 ? 1 : (r3 == r4 ? 0 : -1))
            if (r3 >= 0) goto L_0x00d7
            android.os.Handler r3 = r6.handler
            java.lang.Runnable r4 = r6.mLongPressed
            r3.removeCallbacks(r4)
            r6.isFgSwiping = r2
            int r3 = (r0 > r11 ? 1 : (r0 == r11 ? 0 : -1))
            if (r3 <= 0) goto L_0x00d2
            int r3 = r6.touchSlop
            goto L_0x00d5
        L_0x00d2:
            int r3 = r6.touchSlop
            int r3 = -r3
        L_0x00d5:
            r6.mSwipingSlop = r3
        L_0x00d7:
            boolean r3 = r6.swipeable
            if (r3 == 0) goto L_0x01ed
            boolean r3 = r6.isFgSwiping
            if (r3 == 0) goto L_0x01ed
            java.util.List<java.lang.Integer> r3 = r6.unSwipeableRows
            int r4 = r6.touchedPosition
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)
            boolean r3 = r3.contains(r4)
            if (r3 != 0) goto L_0x01ed
            android.view.View r3 = r6.bgView
            if (r3 != 0) goto L_0x00fe
            android.view.View r3 = r6.touchedView
            int r4 = r6.bgViewID
            android.view.View r3 = r3.findViewById(r4)
            r6.bgView = r3
            r3.setVisibility(r10)
        L_0x00fe:
            int r3 = r6.touchSlop
            float r3 = (float) r3
            r4 = 1065353216(0x3f800000, float:1.0)
            int r3 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1))
            if (r3 >= 0) goto L_0x0161
            boolean r3 = r6.bgVisible
            if (r3 != 0) goto L_0x0161
            int r3 = r6.mSwipingSlop
            float r3 = (float) r3
            float r3 = r0 - r3
            android.view.View r5 = r6.fgView
            float r8 = java.lang.Math.abs(r3)
            int r9 = r6.bgWidth
            float r10 = (float) r9
            int r8 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r8 <= 0) goto L_0x0120
            int r8 = -r9
            float r8 = (float) r8
            goto L_0x0121
        L_0x0120:
            r8 = r3
        L_0x0121:
            r5.setTranslationX(r8)
            android.view.View r5 = r6.fgView
            float r5 = r5.getTranslationX()
            int r5 = (r5 > r11 ? 1 : (r5 == r11 ? 0 : -1))
            if (r5 <= 0) goto L_0x0133
            android.view.View r5 = r6.fgView
            r5.setTranslationX(r11)
        L_0x0133:
            java.util.ArrayList<java.lang.Integer> r5 = r6.fadeViews
            if (r5 == 0) goto L_0x015f
            java.util.Iterator r5 = r5.iterator()
        L_0x013b:
            boolean r8 = r5.hasNext()
            if (r8 == 0) goto L_0x015f
            java.lang.Object r8 = r5.next()
            java.lang.Integer r8 = (java.lang.Integer) r8
            int r8 = r8.intValue()
            android.view.View r9 = r6.touchedView
            android.view.View r9 = r9.findViewById(r8)
            float r10 = java.lang.Math.abs(r3)
            int r11 = r6.bgWidth
            float r11 = (float) r11
            float r10 = r10 / r11
            float r10 = r4 - r10
            r9.setAlpha(r10)
            goto L_0x013b
        L_0x015f:
            goto L_0x01ec
        L_0x0161:
            int r3 = (r0 > r11 ? 1 : (r0 == r11 ? 0 : -1))
            if (r3 <= 0) goto L_0x01ec
            boolean r3 = r6.bgVisible
            if (r3 == 0) goto L_0x01ec
            if (r3 == 0) goto L_0x01ac
            int r3 = r6.mSwipingSlop
            float r3 = (float) r3
            float r3 = r0 - r3
            int r5 = r6.bgWidth
            float r5 = (float) r5
            float r3 = r3 - r5
            android.view.View r5 = r6.fgView
            int r8 = (r3 > r11 ? 1 : (r3 == r11 ? 0 : -1))
            if (r8 <= 0) goto L_0x017b
            goto L_0x017c
        L_0x017b:
            r11 = r3
        L_0x017c:
            r5.setTranslationX(r11)
            java.util.ArrayList<java.lang.Integer> r5 = r6.fadeViews
            if (r5 == 0) goto L_0x01ab
            java.util.Iterator r5 = r5.iterator()
        L_0x0187:
            boolean r8 = r5.hasNext()
            if (r8 == 0) goto L_0x01ab
            java.lang.Object r8 = r5.next()
            java.lang.Integer r8 = (java.lang.Integer) r8
            int r8 = r8.intValue()
            android.view.View r9 = r6.touchedView
            android.view.View r9 = r9.findViewById(r8)
            float r10 = java.lang.Math.abs(r3)
            int r11 = r6.bgWidth
            float r11 = (float) r11
            float r10 = r10 / r11
            float r10 = r4 - r10
            r9.setAlpha(r10)
            goto L_0x0187
        L_0x01ab:
            goto L_0x01ec
        L_0x01ac:
            int r3 = r6.mSwipingSlop
            float r3 = (float) r3
            float r3 = r0 - r3
            int r5 = r6.bgWidth
            float r5 = (float) r5
            float r3 = r3 - r5
            android.view.View r5 = r6.fgView
            int r8 = (r3 > r11 ? 1 : (r3 == r11 ? 0 : -1))
            if (r8 <= 0) goto L_0x01bc
            goto L_0x01bd
        L_0x01bc:
            r11 = r3
        L_0x01bd:
            r5.setTranslationX(r11)
            java.util.ArrayList<java.lang.Integer> r5 = r6.fadeViews
            if (r5 == 0) goto L_0x01ec
            java.util.Iterator r5 = r5.iterator()
        L_0x01c8:
            boolean r8 = r5.hasNext()
            if (r8 == 0) goto L_0x01ec
            java.lang.Object r8 = r5.next()
            java.lang.Integer r8 = (java.lang.Integer) r8
            int r8 = r8.intValue()
            android.view.View r9 = r6.touchedView
            android.view.View r9 = r9.findViewById(r8)
            float r10 = java.lang.Math.abs(r3)
            int r11 = r6.bgWidth
            float r11 = (float) r11
            float r10 = r10 / r11
            float r10 = r4 - r10
            r9.setAlpha(r10)
            goto L_0x01c8
        L_0x01ec:
            return r2
        L_0x01ed:
            boolean r3 = r6.swipeable
            if (r3 == 0) goto L_0x05a5
            boolean r3 = r6.isFgSwiping
            if (r3 == 0) goto L_0x05a5
            java.util.List<java.lang.Integer> r3 = r6.unSwipeableRows
            int r4 = r6.touchedPosition
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)
            boolean r3 = r3.contains(r4)
            if (r3 == 0) goto L_0x05a5
            int r3 = r6.touchSlop
            float r3 = (float) r3
            int r3 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1))
            if (r3 >= 0) goto L_0x0242
            boolean r3 = r6.bgVisible
            if (r3 != 0) goto L_0x0242
            int r3 = r6.mSwipingSlop
            float r3 = (float) r3
            float r3 = r0 - r3
            android.view.View r4 = r6.bgView
            if (r4 != 0) goto L_0x0221
            android.view.View r4 = r6.touchedView
            int r5 = r6.bgViewID
            android.view.View r4 = r4.findViewById(r5)
            r6.bgView = r4
        L_0x0221:
            android.view.View r4 = r6.bgView
            if (r4 == 0) goto L_0x022a
            r5 = 8
            r4.setVisibility(r5)
        L_0x022a:
            android.view.View r4 = r6.fgView
            r5 = 1084227584(0x40a00000, float:5.0)
            float r5 = r3 / r5
            r4.setTranslationX(r5)
            android.view.View r4 = r6.fgView
            float r4 = r4.getTranslationX()
            int r4 = (r4 > r11 ? 1 : (r4 == r11 ? 0 : -1))
            if (r4 <= 0) goto L_0x0242
            android.view.View r4 = r6.fgView
            r4.setTranslationX(r11)
        L_0x0242:
            return r2
        L_0x0243:
            android.os.Handler r0 = r6.handler
            java.lang.Runnable r3 = r6.mLongPressed
            r0.removeCallbacks(r3)
            boolean r0 = r6.mLongClickPerformed
            if (r0 == 0) goto L_0x0250
            goto L_0x05a5
        L_0x0250:
            android.view.VelocityTracker r0 = r6.mVelocityTracker
            if (r0 != 0) goto L_0x025a
            boolean r0 = r6.swipeable
            if (r0 == 0) goto L_0x025a
            goto L_0x05a5
        L_0x025a:
            int r0 = r6.touchedPosition
            if (r0 >= 0) goto L_0x0260
            goto L_0x05a5
        L_0x0260:
            r0 = 0
            r3 = 0
            r4 = 0
            r5 = 0
            float r12 = r19.getRawX()
            float r13 = r6.touchedX
            float r12 = r12 - r13
            boolean r13 = r6.isFgSwiping
            if (r13 == 0) goto L_0x0282
            int r13 = (r12 > r11 ? 1 : (r12 == r11 ? 0 : -1))
            if (r13 >= 0) goto L_0x0275
            r13 = 1
            goto L_0x0276
        L_0x0275:
            r13 = 0
        L_0x0276:
            r0 = r13
            int r13 = (r12 > r11 ? 1 : (r12 == r11 ? 0 : -1))
            if (r13 <= 0) goto L_0x027d
            r13 = 1
            goto L_0x027e
        L_0x027d:
            r13 = 0
        L_0x027e:
            r3 = r13
            r13 = r0
            r14 = r3
            goto L_0x0284
        L_0x0282:
            r13 = r0
            r14 = r3
        L_0x0284:
            float r0 = java.lang.Math.abs(r12)
            int r3 = r6.bgWidth
            int r3 = r3 / r1
            float r1 = (float) r3
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 <= 0) goto L_0x02a8
            boolean r0 = r6.isFgSwiping
            if (r0 == 0) goto L_0x02a8
            int r0 = (r12 > r11 ? 1 : (r12 == r11 ? 0 : -1))
            if (r0 >= 0) goto L_0x029a
            r0 = 1
            goto L_0x029b
        L_0x029a:
            r0 = 0
        L_0x029b:
            r4 = r0
            int r0 = (r12 > r11 ? 1 : (r12 == r11 ? 0 : -1))
            if (r0 <= 0) goto L_0x02a2
            r0 = 1
            goto L_0x02a3
        L_0x02a2:
            r0 = 0
        L_0x02a3:
            r5 = r0
            r9 = r4
            r15 = r5
            goto L_0x030f
        L_0x02a8:
            boolean r0 = r6.swipeable
            if (r0 == 0) goto L_0x030d
            android.view.VelocityTracker r0 = r6.mVelocityTracker
            r0.addMovement(r7)
            android.view.VelocityTracker r0 = r6.mVelocityTracker
            r1 = 1000(0x3e8, float:1.401E-42)
            r0.computeCurrentVelocity(r1)
            android.view.VelocityTracker r0 = r6.mVelocityTracker
            float r0 = r0.getXVelocity()
            float r1 = java.lang.Math.abs(r0)
            android.view.VelocityTracker r3 = r6.mVelocityTracker
            float r3 = r3.getYVelocity()
            float r3 = java.lang.Math.abs(r3)
            int r15 = r6.minFlingVel
            float r15 = (float) r15
            int r15 = (r15 > r1 ? 1 : (r15 == r1 ? 0 : -1))
            if (r15 > 0) goto L_0x030d
            int r15 = r6.maxFlingVel
            float r15 = (float) r15
            int r15 = (r1 > r15 ? 1 : (r1 == r15 ? 0 : -1))
            if (r15 > 0) goto L_0x030d
            int r15 = (r3 > r1 ? 1 : (r3 == r1 ? 0 : -1))
            if (r15 >= 0) goto L_0x030d
            boolean r15 = r6.isFgSwiping
            if (r15 == 0) goto L_0x030d
            int r15 = (r0 > r11 ? 1 : (r0 == r11 ? 0 : -1))
            if (r15 >= 0) goto L_0x02e8
            r15 = 1
            goto L_0x02e9
        L_0x02e8:
            r15 = 0
        L_0x02e9:
            int r16 = (r12 > r11 ? 1 : (r12 == r11 ? 0 : -1))
            if (r16 >= 0) goto L_0x02ef
            r9 = 1
            goto L_0x02f0
        L_0x02ef:
            r9 = 0
        L_0x02f0:
            if (r15 != r9) goto L_0x02f4
            r9 = 1
            goto L_0x02f5
        L_0x02f4:
            r9 = 0
        L_0x02f5:
            r4 = r9
            int r9 = (r0 > r11 ? 1 : (r0 == r11 ? 0 : -1))
            if (r9 <= 0) goto L_0x02fc
            r9 = 1
            goto L_0x02fd
        L_0x02fc:
            r9 = 0
        L_0x02fd:
            int r15 = (r12 > r11 ? 1 : (r12 == r11 ? 0 : -1))
            if (r15 <= 0) goto L_0x0303
            r15 = 1
            goto L_0x0304
        L_0x0303:
            r15 = 0
        L_0x0304:
            if (r9 != r15) goto L_0x0308
            r9 = 1
            goto L_0x0309
        L_0x0308:
            r9 = 0
        L_0x0309:
            r5 = r9
            r9 = r4
            r15 = r5
            goto L_0x030f
        L_0x030d:
            r9 = r4
            r15 = r5
        L_0x030f:
            boolean r0 = r6.swipeable
            if (r0 == 0) goto L_0x034c
            if (r14 != 0) goto L_0x034c
            if (r9 == 0) goto L_0x034c
            int r0 = r6.touchedPosition
            if (r0 == r8) goto L_0x034c
            java.util.List<java.lang.Integer> r1 = r6.unSwipeableRows
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)
            boolean r0 = r1.contains(r0)
            if (r0 != 0) goto L_0x0349
            boolean r0 = r6.bgVisible
            if (r0 != 0) goto L_0x0349
            android.view.View r0 = r6.touchedView
            int r1 = r6.touchedPosition
            int r3 = r6.mDismissAnimationRefCount
            int r3 = r3 + r2
            r6.mDismissAnimationRefCount = r3
            android.view.View r3 = r6.touchedView
            com.example.worktool_new.Views.Activities.RecyclerTouchListener$Animation r4 = com.example.worktool_new.Views.Activities.RecyclerTouchListener.Animation.OPEN
            r17 = r12
            long r11 = r6.ANIMATION_STANDARD
            r6.animateFG(r3, r4, r11)
            r6.bgVisible = r2
            android.view.View r2 = r6.fgView
            r6.bgVisibleView = r2
            r6.bgVisiblePosition = r1
            goto L_0x049c
        L_0x0349:
            r17 = r12
            goto L_0x034e
        L_0x034c:
            r17 = r12
        L_0x034e:
            boolean r0 = r6.swipeable
            if (r0 == 0) goto L_0x0385
            if (r13 != 0) goto L_0x0385
            if (r15 == 0) goto L_0x0385
            int r0 = r6.touchedPosition
            if (r0 == r8) goto L_0x0385
            java.util.List<java.lang.Integer> r1 = r6.unSwipeableRows
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)
            boolean r0 = r1.contains(r0)
            if (r0 != 0) goto L_0x0385
            boolean r0 = r6.bgVisible
            if (r0 == 0) goto L_0x0385
            android.view.View r0 = r6.touchedView
            int r1 = r6.touchedPosition
            int r3 = r6.mDismissAnimationRefCount
            int r3 = r3 + r2
            r6.mDismissAnimationRefCount = r3
            android.view.View r2 = r6.touchedView
            com.example.worktool_new.Views.Activities.RecyclerTouchListener$Animation r3 = com.example.worktool_new.Views.Activities.RecyclerTouchListener.Animation.CLOSE
            long r4 = r6.ANIMATION_STANDARD
            r6.animateFG(r2, r3, r4)
            r6.bgVisible = r10
            r2 = 0
            r6.bgVisibleView = r2
            r6.bgVisiblePosition = r8
            goto L_0x049c
        L_0x0385:
            boolean r0 = r6.swipeable
            if (r0 == 0) goto L_0x03aa
            if (r13 == 0) goto L_0x03aa
            boolean r0 = r6.bgVisible
            if (r0 != 0) goto L_0x03aa
            android.view.View r11 = r6.bgView
            android.view.View r1 = r6.touchedView
            com.example.worktool_new.Views.Activities.RecyclerTouchListener$Animation r2 = com.example.worktool_new.Views.Activities.RecyclerTouchListener.Animation.CLOSE
            long r3 = r6.ANIMATION_STANDARD
            com.example.worktool_new.Views.Activities.RecyclerTouchListener$5 r5 = new com.example.worktool_new.Views.Activities.RecyclerTouchListener$5
            r5.<init>(r11)
            r0 = r18
            r0.animateFG(r1, r2, r3, r5)
            r6.bgVisible = r10
            r0 = 0
            r6.bgVisibleView = r0
            r6.bgVisiblePosition = r8
            goto L_0x049c
        L_0x03aa:
            boolean r0 = r6.swipeable
            if (r0 == 0) goto L_0x03c9
            if (r14 == 0) goto L_0x03c9
            boolean r0 = r6.bgVisible
            if (r0 == 0) goto L_0x03c9
            android.view.View r0 = r6.touchedView
            com.example.worktool_new.Views.Activities.RecyclerTouchListener$Animation r1 = com.example.worktool_new.Views.Activities.RecyclerTouchListener.Animation.OPEN
            long r3 = r6.ANIMATION_STANDARD
            r6.animateFG(r0, r1, r3)
            r6.bgVisible = r2
            android.view.View r0 = r6.fgView
            r6.bgVisibleView = r0
            int r0 = r6.touchedPosition
            r6.bgVisiblePosition = r0
            goto L_0x049c
        L_0x03c9:
            boolean r0 = r6.swipeable
            if (r0 == 0) goto L_0x03e5
            if (r14 == 0) goto L_0x03e5
            boolean r0 = r6.bgVisible
            if (r0 != 0) goto L_0x03e5
            android.view.View r0 = r6.touchedView
            com.example.worktool_new.Views.Activities.RecyclerTouchListener$Animation r1 = com.example.worktool_new.Views.Activities.RecyclerTouchListener.Animation.CLOSE
            long r2 = r6.ANIMATION_STANDARD
            r6.animateFG(r0, r1, r2)
            r6.bgVisible = r10
            r0 = 0
            r6.bgVisibleView = r0
            r6.bgVisiblePosition = r8
            goto L_0x049c
        L_0x03e5:
            boolean r0 = r6.swipeable
            if (r0 == 0) goto L_0x0404
            if (r13 == 0) goto L_0x0404
            boolean r0 = r6.bgVisible
            if (r0 == 0) goto L_0x0404
            android.view.View r0 = r6.touchedView
            com.example.worktool_new.Views.Activities.RecyclerTouchListener$Animation r1 = com.example.worktool_new.Views.Activities.RecyclerTouchListener.Animation.OPEN
            long r3 = r6.ANIMATION_STANDARD
            r6.animateFG(r0, r1, r3)
            r6.bgVisible = r2
            android.view.View r0 = r6.fgView
            r6.bgVisibleView = r0
            int r0 = r6.touchedPosition
            r6.bgVisiblePosition = r0
            goto L_0x049c
        L_0x0404:
            if (r14 != 0) goto L_0x049c
            if (r13 != 0) goto L_0x049c
            boolean r0 = r6.swipeable
            if (r0 == 0) goto L_0x0422
            boolean r0 = r6.fgPartialViewClicked
            if (r0 == 0) goto L_0x0422
            android.view.View r0 = r6.touchedView
            com.example.worktool_new.Views.Activities.RecyclerTouchListener$Animation r1 = com.example.worktool_new.Views.Activities.RecyclerTouchListener.Animation.CLOSE
            long r2 = r6.ANIMATION_STANDARD
            r6.animateFG(r0, r1, r2)
            r6.bgVisible = r10
            r0 = 0
            r6.bgVisibleView = r0
            r6.bgVisiblePosition = r8
            goto L_0x049c
        L_0x0422:
            boolean r0 = r6.clickable
            if (r0 == 0) goto L_0x044c
            boolean r0 = r6.bgVisible
            if (r0 != 0) goto L_0x044c
            int r0 = r6.touchedPosition
            if (r0 < 0) goto L_0x044c
            java.util.List<java.lang.Integer> r1 = r6.unClickableRows
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)
            boolean r0 = r1.contains(r0)
            if (r0 != 0) goto L_0x044c
            boolean r0 = r18.isIndependentViewClicked(r19)
            if (r0 == 0) goto L_0x044c
            boolean r0 = r6.isRViewScrolling
            if (r0 != 0) goto L_0x044c
            com.example.worktool_new.Views.Activities.RecyclerTouchListener$OnRowClickListener r0 = r6.mRowClickListener
            int r1 = r6.touchedPosition
            r0.onRowClicked(r1)
            goto L_0x049c
        L_0x044c:
            boolean r0 = r6.clickable
            if (r0 == 0) goto L_0x047c
            boolean r0 = r6.bgVisible
            if (r0 != 0) goto L_0x047c
            int r0 = r6.touchedPosition
            if (r0 < 0) goto L_0x047c
            java.util.List<java.lang.Integer> r1 = r6.unClickableRows
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)
            boolean r0 = r1.contains(r0)
            if (r0 != 0) goto L_0x047c
            boolean r0 = r18.isIndependentViewClicked(r19)
            if (r0 != 0) goto L_0x047c
            boolean r0 = r6.isRViewScrolling
            if (r0 != 0) goto L_0x047c
            int r0 = r18.getIndependentViewID(r19)
            if (r0 < 0) goto L_0x047b
            com.example.worktool_new.Views.Activities.RecyclerTouchListener$OnRowClickListener r1 = r6.mRowClickListener
            int r2 = r6.touchedPosition
            r1.onIndependentViewClicked(r0, r2)
        L_0x047b:
            goto L_0x049c
        L_0x047c:
            boolean r0 = r6.swipeable
            if (r0 == 0) goto L_0x049c
            boolean r0 = r6.bgVisible
            if (r0 == 0) goto L_0x049c
            boolean r0 = r6.fgPartialViewClicked
            if (r0 != 0) goto L_0x049c
            int r0 = r18.getOptionViewID(r19)
            if (r0 < 0) goto L_0x049c
            int r1 = r6.touchedPosition
            if (r1 < 0) goto L_0x049c
            int r1 = r6.touchedPosition
            com.example.worktool_new.Views.Activities.RecyclerTouchListener$6 r2 = new com.example.worktool_new.Views.Activities.RecyclerTouchListener$6
            r2.<init>(r0, r1)
            r6.closeVisibleBG(r2)
        L_0x049c:
            boolean r0 = r6.swipeable
            if (r0 == 0) goto L_0x04a9
            android.view.VelocityTracker r0 = r6.mVelocityTracker
            r0.recycle()
            r0 = 0
            r6.mVelocityTracker = r0
            goto L_0x04aa
        L_0x04a9:
            r0 = 0
        L_0x04aa:
            r1 = 0
            r6.touchedX = r1
            r6.touchedY = r1
            r6.touchedView = r0
            r6.touchedPosition = r8
            r6.isFgSwiping = r10
            r6.bgView = r0
            goto L_0x05a5
        L_0x04b9:
            boolean r0 = r6.mPaused
            if (r0 == 0) goto L_0x04bf
            goto L_0x05a5
        L_0x04bf:
            android.graphics.Rect r0 = new android.graphics.Rect
            r0.<init>()
            androidx.recyclerview.widget.RecyclerView r3 = r6.rView
            int r3 = r3.getChildCount()
            int[] r1 = new int[r1]
            androidx.recyclerview.widget.RecyclerView r4 = r6.rView
            r4.getLocationOnScreen(r1)
            float r4 = r19.getRawX()
            int r4 = (int) r4
            r5 = r1[r10]
            int r4 = r4 - r5
            float r5 = r19.getRawY()
            int r5 = (int) r5
            r2 = r1[r2]
            int r5 = r5 - r2
            r2 = 0
        L_0x04e2:
            if (r2 >= r3) goto L_0x04f9
            androidx.recyclerview.widget.RecyclerView r9 = r6.rView
            android.view.View r9 = r9.getChildAt(r2)
            r9.getHitRect(r0)
            boolean r11 = r0.contains(r4, r5)
            if (r11 == 0) goto L_0x04f6
            r6.touchedView = r9
            goto L_0x04f9
        L_0x04f6:
            int r2 = r2 + 1
            goto L_0x04e2
        L_0x04f9:
            android.view.View r2 = r6.touchedView
            if (r2 == 0) goto L_0x057d
            float r2 = r19.getRawX()
            r6.touchedX = r2
            float r2 = r19.getRawY()
            r6.touchedY = r2
            androidx.recyclerview.widget.RecyclerView r2 = r6.rView
            android.view.View r9 = r6.touchedView
            int r2 = r2.getChildAdapterPosition(r9)
            r6.touchedPosition = r2
            boolean r2 = r6.shouldIgnoreAction(r2)
            if (r2 == 0) goto L_0x051c
            r6.touchedPosition = r8
            return r10
        L_0x051c:
            boolean r2 = r6.longClickable
            if (r2 == 0) goto L_0x052c
            r6.mLongClickPerformed = r10
            android.os.Handler r2 = r6.handler
            java.lang.Runnable r8 = r6.mLongPressed
            int r9 = r6.LONG_CLICK_DELAY
            long r11 = (long) r9
            r2.postDelayed(r8, r11)
        L_0x052c:
            boolean r2 = r6.swipeable
            if (r2 == 0) goto L_0x057d
            android.view.VelocityTracker r2 = android.view.VelocityTracker.obtain()
            r6.mVelocityTracker = r2
            r2.addMovement(r7)
            android.view.View r2 = r6.touchedView
            int r8 = r6.fgViewID
            android.view.View r2 = r2.findViewById(r8)
            r6.fgView = r2
            android.view.View r2 = r6.touchedView
            int r8 = r6.bgViewID
            android.view.View r2 = r2.findViewById(r8)
            r6.bgView = r2
            android.view.View r8 = r6.fgView
            int r8 = r8.getHeight()
            r2.setMinimumHeight(r8)
            boolean r2 = r6.bgVisible
            if (r2 == 0) goto L_0x057b
            android.view.View r2 = r6.fgView
            if (r2 == 0) goto L_0x057b
            android.os.Handler r2 = r6.handler
            java.lang.Runnable r8 = r6.mLongPressed
            r2.removeCallbacks(r8)
            float r2 = r19.getRawX()
            int r4 = (int) r2
            float r2 = r19.getRawY()
            int r5 = (int) r2
            android.view.View r2 = r6.fgView
            r2.getGlobalVisibleRect(r0)
            boolean r2 = r0.contains(r4, r5)
            r6.fgPartialViewClicked = r2
            goto L_0x057d
        L_0x057b:
            r6.fgPartialViewClicked = r10
        L_0x057d:
            float r2 = r19.getRawX()
            int r2 = (int) r2
            float r4 = r19.getRawY()
            int r4 = (int) r4
            androidx.recyclerview.widget.RecyclerView r5 = r6.rView
            r5.getHitRect(r0)
            boolean r5 = r6.swipeable
            if (r5 == 0) goto L_0x05a5
            boolean r5 = r6.bgVisible
            if (r5 == 0) goto L_0x05a5
            int r5 = r6.touchedPosition
            int r8 = r6.bgVisiblePosition
            if (r5 == r8) goto L_0x05a5
            android.os.Handler r5 = r6.handler
            java.lang.Runnable r8 = r6.mLongPressed
            r5.removeCallbacks(r8)
            r5 = 0
            r6.closeVisibleBG(r5)
        L_0x05a5:
            return r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.worktool_new.Views.Activities.RecyclerTouchListener.handleTouchEvent(android.view.MotionEvent):boolean");
    }

    public void getTouchCoordinates(MotionEvent ev) {
        int y = (int) ev.getRawY();
        if (this.swipeable && this.bgVisible && ev.getActionMasked() == 0 && y < this.heightOutsideRView) {
            closeVisibleBG((OnSwipeListener) null);
        }
    }

    private boolean shouldIgnoreAction(int touchedPosition2) {
        RecyclerView recyclerView = this.rView;
        return recyclerView == null || this.ignoredViewTypes.contains(Integer.valueOf(recyclerView.getAdapter().getItemViewType(touchedPosition2)));
    }
}
