package com.example.android.movieviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class MovieRatingIndicator extends View {

    private int maxRating;

    private Context mContext;

    private int numberOfDisplayItems;
    private int currentRating;

    private float mWidth;
    private float mHeight;
    private int mDisplayItemWidth;
    private int mDisplayItemHeight;
    private float mVerticalPadding;
    private float mHorizontalPadding;

    private Bitmap[] mDisplayItems;

    private Paint mTextPaint;
    private Paint mPaint;


    public MovieRatingIndicator(Context context) {
        this(context, null);
    }

    public MovieRatingIndicator(Context context,  @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);
    }

    public MovieRatingIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs);
    }

    public void init(AttributeSet attrs) {
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(40f);

        mPaint = new Paint();

        // initialize the default attributes
        maxRating = 100;
        currentRating = 50;
        numberOfDisplayItems = 5;

        // Get the custom attributes if available.
        if (attrs != null) {
            TypedArray typedArray = getContext()
                    .obtainStyledAttributes(attrs,
                            R.styleable.MovieRatingIndicator,
                            0, 0);

            maxRating = typedArray.getColor(R.styleable.MovieRatingIndicator_maxRating, maxRating);
            currentRating = typedArray.getColor(R.styleable.MovieRatingIndicator_currentRating,
                    currentRating);
            numberOfDisplayItems= typedArray.getInt(R.styleable.MovieRatingIndicator_numOfPopcorns,
                    numberOfDisplayItems);
            // Must recycle the TypedArray when finished.
            typedArray.recycle();
        }

    }

    private void setUpPopcornDrawables() {
        mDisplayItems = new Bitmap[numberOfDisplayItems];
        int numberOfFullPopcorns = (currentRating*numberOfDisplayItems)/ maxRating;

        // add all the full popcorn images
        for(int currentItem = 0;currentItem < numberOfFullPopcorns; currentItem++ ) {
            mDisplayItems[currentItem] = Bitmap.createScaledBitmap(
                    BitmapFactory.decodeResource(
                    mContext.getResources(), R.drawable.popcorn
                    ),
                    mDisplayItemWidth,
                    mDisplayItemHeight,
                    false
            );
        }

        // if rating is 0.5 or over add a half popcorn
        int emptyStart;
        int halfPopcorn = (currentRating % maxRating) >= 0.5 ? 1 : 0;
        if (halfPopcorn == 1) {
            mDisplayItems[numberOfFullPopcorns] = Bitmap.createScaledBitmap(
                    BitmapFactory.decodeResource(
                    mContext.getResources(), R.drawable.half_popcorn
                    ),
                    mDisplayItemWidth,
                    mDisplayItemHeight,
                    false
            );
            emptyStart = numberOfFullPopcorns + 1;
        }else {
            emptyStart = numberOfFullPopcorns;
        }

        // fill the rest with empty popcorn images
        for(int i = emptyStart; i < numberOfDisplayItems; i++) {
            mDisplayItems[i] = Bitmap.createScaledBitmap(
                    BitmapFactory.decodeResource(
                    mContext.getResources(), R.drawable.empty_popcorn
                    ),
                    mDisplayItemWidth,
                    mDisplayItemHeight,
                    false
            );
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
        mHeight = h;

        mDisplayItemWidth = (w /(numberOfDisplayItems + 1));
        mDisplayItemHeight = h - 16 ;
        mVerticalPadding = 8;
        mHorizontalPadding = mDisplayItemWidth/2;

        setUpPopcornDrawables();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float x = mHorizontalPadding;
        float y = mVerticalPadding;
        for (Bitmap bitmap : mDisplayItems) {
            canvas.drawBitmap(bitmap,x, y, mPaint);
            x+= mDisplayItemWidth;
        }
    }

    public void setMaxRating(int maxRating) {
        this.maxRating = maxRating;
    }

    public void setCurrentRating(int currentRating) {
        this.currentRating = currentRating;
    }

    public void setNumberOfDisplayItems(int numberOfDisplayItems) {
        this.numberOfDisplayItems = numberOfDisplayItems;
    }
}
