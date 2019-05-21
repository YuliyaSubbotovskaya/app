package com.zamkovenko.time4child.widgets;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zamkovenko.time4child.R;

public class MySwipeButton {

    /** создать ресурс drawable bt_swipe.xml - округлый background для swipe_layout и кнопки slidingButton


     <selector xmlns:android="http://schemas.android.com/apk/res/android">
     <item>
     <shape android:shape="rectangle">
     <corners android:radius="50dp" />
     </shape>
     </item>
     </selector>

     */

    /** Наша кастомная View - добавить в layout, отрегулировать произвольно

     <RelativeLayout
     android:id="@+id/swipe_layout"
     android:layout_width="match_parent"
     android:layout_height="wrap_content"
     android:layout_gravity="center"
     android:background="@drawable/bt_swipe"
     android:backgroundTint="@color/colorPrimary">

     <TextView
     android:id="@+id/centerText"
     android:layout_width="match_parent"
     android:layout_height="60dp"
     android:layout_centerHorizontal="true"
     android:layout_centerVertical="true"
     android:background="@drawable/bt_swipe"
     android:backgroundTint="@color/colorPrimaryDark"
     android:gravity="center"
     android:text="Swipe" />

     <ImageView
     android:id="@+id/slidingButton"
     android:layout_width="60dp"
     android:layout_height="60dp"
     android:layout_alignParentLeft="true"
     android:layout_centerVertical="true"
     android:background="@drawable/bt_swipe"
     android:backgroundTint="@color/colorAccent"
     android:padding="8dp"/>

     </RelativeLayout>

     */


    /** Пример кода в активити
     MySwipeButton mySwipeButton = new MySwipeButton(context, null, null);

     mySwipeButton.setOnSwipeButtonListener(new MySwipeButton.SwipeButtonListener() {

    @Override
    public void swipeButtonOn(ImageView slidingButton) {
    Log.i(TAG, "swipeButtonOn");
    }

    @Override
    public void swipeButtonOff(ImageView slidingButton) {
    Log.i(TAG, "swipeButtonOff");
    }
    });

     */

    private final String TAG = "LOG_SwipeButton";
   private RelativeLayout swipe_layout;
   private ImageView slidingButton;
   private TextView centerText;
   private Boolean isActive = false;
   private Drawable enabledDrawable;
   private Drawable disabledDrawable;

    private Boolean ACTION_START = true;
    private float startRawX;
    private float startX;
    private float moveX;
    private float oldMoveX;
    private int defaultSlidingButtonWidth;
    private int parentWidth;
    private int parentPaddingL;
    private int parentPaddingR;

    private final float thresholdRight = 0.85f; // порог срабатывания при перетаскивании вправо - статус активный (при 85%)

    public MySwipeButton(Context context, Drawable disabledDrawableSlidingButton, Drawable enabledDrawableSlidingButton) {
        Activity activity = (Activity) context;
        slidingButton = activity.findViewById(R.id.slidingButton);
        centerText = activity.findViewById(R.id.centerText);
        slidingButton.setImageDrawable(disabledDrawableSlidingButton);

        this.disabledDrawable = disabledDrawableSlidingButton;
        this.enabledDrawable = enabledDrawableSlidingButton;
       /* enabledDrawable = ContextCompat.getDrawable(context, android.R.drawable.presence_online); // берем произвольные
        disabledDrawable = ContextCompat.getDrawable(context, android.R.drawable.presence_offline); // берем произвольные
        */

        swipe_layout = activity.findViewById(R.id.swipe_layout);
        swipe_layout.postDelayed(new Runnable() {
            @Override
            public void run() {
                getParametersViews();
                slidingButton.setOnTouchListener(onTouchListener);
            }
        },500);

    }



   private View.OnTouchListener onTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            return moveView(event);
        }
    };


    private void swipeButtonOn() {

        final ValueAnimator positionAnimator = ValueAnimator.ofFloat(slidingButton.getX(), 0);
        positionAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float x = (Float) positionAnimator.getAnimatedValue();
                //slidingButton.setX(x);
            }
        });


        final ValueAnimator widthAnimator = ValueAnimator.ofInt(slidingButton.getWidth(), parentWidth);

        widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ViewGroup.LayoutParams params = slidingButton.getLayoutParams();
                params.width = (Integer) widthAnimator.getAnimatedValue();
                //slidingButton.setLayoutParams(params);
            }
        });


        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isActive = true;
                slidingButton.setEnabled(true);
                slidingButton.setImageDrawable(enabledDrawable);

                /*звоним в интерфейс*/
                swipeButtonListener.swipeButtonOn(slidingButton);

            }
        });

        animatorSet.playTogether(positionAnimator, widthAnimator);
        animatorSet.start();


    }


    private void swipeButtonOff() {
        final ValueAnimator widthAnimator = ValueAnimator.ofInt(slidingButton.getWidth(), defaultSlidingButtonWidth);

        widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ViewGroup.LayoutParams params =  slidingButton.getLayoutParams();
                params.width = (Integer) widthAnimator.getAnimatedValue();
                slidingButton.setLayoutParams(params);
            }
        });

        widthAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isActive = false;
                slidingButton.setEnabled(true);
                slidingButton.setImageDrawable(disabledDrawable);

                /*звоним в интерфейс*/
                swipeButtonListener.swipeButtonOff(slidingButton);
            }
        });

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(
                centerText, "alpha", 1);

        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet.playTogether(objectAnimator, widthAnimator);
        animatorSet.start();


    }



    private void moveButtonBack() {
        final ValueAnimator positionAnimator = ValueAnimator.ofFloat(slidingButton.getX(), 0);
        positionAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        positionAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float x = (Float) positionAnimator.getAnimatedValue();
                slidingButton.setX(x);
                slidingButton.setEnabled(true);
            }
        });

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(centerText, "alpha", 1);
        positionAnimator.setDuration(200);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimator, positionAnimator);
        animatorSet.start();
    }


    private Boolean moveView(MotionEvent motionEvent){

        switch (motionEvent.getActionMasked()) {

            case MotionEvent.ACTION_DOWN:

                Log.i(TAG, "ACTION_DOWN");
                return true;

            case MotionEvent.ACTION_MOVE:

                if(!isActive) {
                    action_START(motionEvent);
                    action_MOVE(motionEvent);
                }

                return true;

            case MotionEvent.ACTION_UP:

                action_UP(motionEvent);
                return true;

            default:
                return false;

        }

    }

    private void action_START(MotionEvent motionEvent) {

        if (ACTION_START) {

            ACTION_START = false;

            startRawX = motionEvent.getRawX();
//            Log.i(TAG, "startRawX = " + startRawX);

            startX = slidingButton.getX() - startRawX;

            moveX = startRawX + startX;
            oldMoveX = moveX;

        }

    }

    private void action_MOVE(MotionEvent motionEvent){

        centerText.setAlpha(1 - 1.3f * (slidingButton.getX() + slidingButton.getWidth()) / parentWidth);

        limiter(motionEvent);

        slidingButton.animate()
                .x(moveX)
                .setDuration(0)
                .setStartDelay(0)
                .setInterpolator(null)
                .start();
    }


    private void action_UP(MotionEvent motionEvent){

        ACTION_START= true;
        slidingButton.setEnabled(false);

        if (isActive) {
            swipeButtonOff();
        } else {

            if (slidingButton.getX() + defaultSlidingButtonWidth > parentWidth * thresholdRight) {
                swipeButtonOn();
            } else {
                moveButtonBack();
            }
        }

    }

    /*предотвращает выход view из родителя*/
    private void limiter(MotionEvent motionEvent){

        float x = motionEvent.getRawX() + startX;

        int limiterX;

        if(x > oldMoveX) {
            //Log.i(TAG, "Right");
            limiterX = (int) x + defaultSlidingButtonWidth + parentPaddingR;
            if(limiterX < parentWidth){
                moveX = x;
            }

        }else if(x < oldMoveX){
            // Log.i(TAG, "Left");
            limiterX = (int) x;
            if(limiterX > parentPaddingL){
                moveX = x;
            }

        }

        oldMoveX = moveX;

//        Log.i(TAG, "limiterX = " + limiterX);

    }

    private void getParametersViews(){

        defaultSlidingButtonWidth = slidingButton.getWidth();
        parentWidth = swipe_layout.getWidth();
        parentPaddingL = swipe_layout.getPaddingLeft();
        parentPaddingR = swipe_layout.getPaddingRight();

        Log.i(TAG, "parentWidth: " + parentWidth);

//            Log.i(TAG, "viewHeight = " + viewHeight);
//            Log.i(TAG, "parentWidth = " + parentWidth);
//            Log.i(TAG, "parentHeight = " + parentHeight);
//            Log.i(TAG, "parentPaddingL = " + parentPaddingL);
//            Log.i(TAG, "parentPaddingR = " + parentPaddingR);
//            Log.i(TAG, "parentPaddingT = " + parentPaddingT);
//            Log.i(TAG, "parentPaddingB = " + parentPaddingB);

    }


    /**Интерфейс слушатель который используем в активити*/
    public interface SwipeButtonListener{

        void swipeButtonOn(ImageView slidingButton);

        void swipeButtonOff(ImageView slidingButton);

    }

    private SwipeButtonListener swipeButtonListener;

    public void setOnSwipeButtonListener(SwipeButtonListener swipeButtonListener){

        this.swipeButtonListener = swipeButtonListener;

    }


}
