package com.fxyan.letterindicatorview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;

/**
 * @author fxYan
 */
public final class LetterIndicatorView extends View {

    private int indicatorBgColor;
    private float indicatorItemWidth;
    private float indicatorItemHeight;
    private float indicatorTextSize;
    private int indicatorSelectedTextColor;
    private int indicatorSelectedBgColor;
    private float indicatorSelectedBgRadius;
    private int indicatorUnSelectTextColor;

    private float zoomUpIndicatorTextSize;
    private int zoomUpIndicatorTextColor;
    private int zoomUpIndicatorBgColor;
    private float zoomUpIndicatorBgRadius;
    private float zoomUpIndicatorMargin;

    private ArrayList<String> titles;

    private Paint paint;
    private Rect textBounds;
    private Path path;
    private RectF zoomUpCircleRect;

    private boolean isOnTouchMode;
    private float zoomUpIndicatorCenterY;

    private int onTouchIndex;
    private int outChangeIndex;
    private OnTitleIndexChangeListener onTitleIndexChangeListener;

    public LetterIndicatorView(Context context) {
        super(context);
        init(null);
    }

    public LetterIndicatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        configDefaultAttrs();
        loadXmlAttrs(attrs);

        titles = new ArrayList<>();

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textBounds = new Rect();
        path = new Path();
        zoomUpCircleRect = new RectF();
    }

    private void configDefaultAttrs() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);

        indicatorBgColor = Color.TRANSPARENT;
        indicatorItemWidth = indicatorItemHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, metrics);
        indicatorTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, metrics);
        indicatorSelectedTextColor = Color.parseColor("#1b8fe6");
        indicatorSelectedBgColor = Color.TRANSPARENT;
        indicatorSelectedBgRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, metrics);
        indicatorUnSelectTextColor = Color.parseColor("#646464");

        zoomUpIndicatorTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, metrics);
        zoomUpIndicatorTextColor = Color.WHITE;
        zoomUpIndicatorBgColor = Color.argb(0x30, 0x00, 0x00, 0x00);
        zoomUpIndicatorBgRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, metrics);
        zoomUpIndicatorMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, metrics);
    }

    private void loadXmlAttrs(AttributeSet attrs){
        if (attrs != null) {
            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.LetterIndicatorView);

            if (array.hasValue(R.styleable.LetterIndicatorView_livIndicatorBgColor)) {
                indicatorBgColor = array.getColor(R.styleable.LetterIndicatorView_livIndicatorBgColor, indicatorBgColor);
            }
            if (array.hasValue(R.styleable.LetterIndicatorView_livIndicatorItemWidth)) {
                indicatorItemWidth = array.getDimension(R.styleable.LetterIndicatorView_livIndicatorItemWidth, indicatorItemWidth);
            }
            if (array.hasValue(R.styleable.LetterIndicatorView_livIndicatorItemHeight)) {
                indicatorItemHeight = array.getDimension(R.styleable.LetterIndicatorView_livIndicatorItemHeight, indicatorItemHeight);
            }
            if (array.hasValue(R.styleable.LetterIndicatorView_livIndicatorTextSize)) {
                indicatorTextSize = array.getDimension(R.styleable.LetterIndicatorView_livIndicatorTextSize, indicatorTextSize);
            }
            if (array.hasValue(R.styleable.LetterIndicatorView_livIndicatorSelectedTextColor)) {
                indicatorSelectedTextColor = array.getColor(R.styleable.LetterIndicatorView_livIndicatorSelectedTextColor, indicatorSelectedTextColor);
            }
            if (array.hasValue(R.styleable.LetterIndicatorView_livIndicatorSelectedBgColor)) {
                indicatorSelectedBgColor = array.getColor(R.styleable.LetterIndicatorView_livIndicatorSelectedBgColor, indicatorSelectedBgColor);
            }
            if (array.hasValue(R.styleable.LetterIndicatorView_livIndicatorSelectedBgRadius)) {
                indicatorSelectedBgRadius = array.getDimension(R.styleable.LetterIndicatorView_livIndicatorSelectedBgRadius, indicatorSelectedBgRadius);
            }
            if (array.hasValue(R.styleable.LetterIndicatorView_livIndicatorUnSelectTextColor)) {
                indicatorUnSelectTextColor = array.getColor(R.styleable.LetterIndicatorView_livIndicatorUnSelectTextColor, indicatorUnSelectTextColor);
            }

            if (array.hasValue(R.styleable.LetterIndicatorView_livZoomUpIndicatorTextSize)) {
                zoomUpIndicatorTextSize = array.getDimension(R.styleable.LetterIndicatorView_livZoomUpIndicatorTextSize, zoomUpIndicatorTextSize);
            }
            if (array.hasValue(R.styleable.LetterIndicatorView_livZoomUpIndicatorBgColor)) {
                zoomUpIndicatorBgColor = array.getColor(R.styleable.LetterIndicatorView_livZoomUpIndicatorBgColor, zoomUpIndicatorBgColor);
            }
            if (array.hasValue(R.styleable.LetterIndicatorView_livZoomUpIndicatorTextColor)) {
                zoomUpIndicatorTextColor = array.getColor(R.styleable.LetterIndicatorView_livZoomUpIndicatorTextColor, zoomUpIndicatorTextColor);
            }
            if (array.hasValue(R.styleable.LetterIndicatorView_livZoomUpIndicatorBgRadius)) {
                zoomUpIndicatorBgRadius = array.getDimension(R.styleable.LetterIndicatorView_livZoomUpIndicatorBgRadius, zoomUpIndicatorBgRadius);
            }
            if (array.hasValue(R.styleable.LetterIndicatorView_livZoomUpIndicatorMargin)) {
                zoomUpIndicatorMargin = array.getDimension(R.styleable.LetterIndicatorView_livZoomUpIndicatorMargin, zoomUpIndicatorMargin);
            }

            array.recycle();
        }
    }

    public void setTitles(ArrayList<String> titles) {
        this.titles.clear();
        this.titles.addAll(titles);
    }

    public void setOutChangeIndex(int index) {
        outChangeIndex = index;
        if (!isOnTouchMode) {
            onTouchIndex = outChangeIndex;
            invalidate();
        }
    }

    public void setOnTitleIndexChangeListener(OnTitleIndexChangeListener listener) {
        onTitleIndexChangeListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                float x = event.getX();
                if (x >= getWidth() - indicatorItemWidth) {
                    isOnTouchMode = true;
                }
            case MotionEvent.ACTION_MOVE:
                if (isOnTouchMode) {
                    float y = event.getY();
                    calculateOnTouchIndex(y);
                    if (onTitleIndexChangeListener != null) {
                        onTitleIndexChangeListener.onTitleIndexChanged(onTouchIndex);
                    }
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                onTouchIndex = outChangeIndex;
                invalidate();
                isOnTouchMode = false;
                break;
            default:
        }
        if (isOnTouchMode) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    private void calculateOnTouchIndex(float y) {
        float firstItemTop = (getHeight() - getTotalItemHeight()) / 2;
        onTouchIndex = (int) ((y - firstItemTop) / indicatorItemHeight);
        if (onTouchIndex < 0) {
            onTouchIndex = 0;
        }
        if (onTouchIndex >= titles.size()) {
            onTouchIndex = titles.size() - 1;
        }
        outChangeIndex = onTouchIndex;

        zoomUpIndicatorCenterY = y;
        if (zoomUpIndicatorCenterY < firstItemTop + indicatorItemHeight / 2) {
            zoomUpIndicatorCenterY = firstItemTop + indicatorItemHeight / 2;
        }
        float lastItemBottom = firstItemTop + getTotalItemHeight();
        if (zoomUpIndicatorCenterY > lastItemBottom - indicatorItemHeight / 2) {
            zoomUpIndicatorCenterY = lastItemBottom - indicatorItemHeight / 2;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        if (wMode == MeasureSpec.AT_MOST) {
            double zoomUpWidth = Math.sqrt(3) * zoomUpIndicatorBgRadius + zoomUpIndicatorBgRadius;
            wSize = (int) Math.floor(indicatorItemWidth + zoomUpWidth + zoomUpIndicatorMargin + 0.5);
        }

        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        if (hMode == MeasureSpec.AT_MOST) {
            hSize = (int) Math.floor(getTotalItemHeight() + 0.5);
        }

        setMeasuredDimension(wSize, hSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBackground(canvas);
        drawTitles(canvas);
        drawZoomText(canvas);
    }

    private void drawBackground(Canvas canvas) {
        float left = getWidth() - indicatorItemWidth;
        float top = 0;
        float right = getWidth();
        float bottom = getHeight();
        paint.setColor(indicatorBgColor);
        canvas.drawRect(left, top, right, bottom, paint);
    }

    private void drawTitles(Canvas canvas) {
        float firstItemTop = (getHeight() - getTotalItemHeight()) / 2;
        for (int i = 0; i < titles.size(); i++) {
            float left = getWidth() - indicatorItemWidth;
            float top = firstItemTop + i * indicatorItemHeight;
            float right = getWidth();
            float bottom = top + indicatorItemHeight;

            if (i == onTouchIndex) {
                paint.setColor(indicatorSelectedBgColor);
                float centerX = (left + right) / 2;
                float centerY = (top + bottom) / 2;
                path.reset();
                path.addCircle(centerX, centerY, indicatorSelectedBgRadius, Path.Direction.CCW);
                canvas.drawPath(path, paint);

                paint.setColor(indicatorSelectedTextColor);
            } else {
                paint.setColor(indicatorUnSelectTextColor);
            }
            paint.setTextSize(indicatorTextSize);
            Paint.FontMetrics fontMetrics = paint.getFontMetrics();
            String title = titles.get(i);
            paint.getTextBounds(title, 0, title.length(), textBounds);
            float xOffset = left + (indicatorItemWidth - textBounds.width()) / 2;
            float yOffset = top + indicatorItemHeight / 2 - fontMetrics.top / 2 - fontMetrics.bottom / 2;
            canvas.drawText(title, xOffset, yOffset, paint);
        }
    }

    public void drawZoomText(Canvas canvas) {
        if (isOnTouchMode) {
            // bg
            float zoomUpRight = getWidth() - indicatorItemWidth - zoomUpIndicatorMargin;
            float circleX = (float) (zoomUpRight - Math.sqrt(3) * zoomUpIndicatorBgRadius);
            float circleY = zoomUpIndicatorCenterY;
            zoomUpCircleRect.left = circleX - zoomUpIndicatorBgRadius;
            zoomUpCircleRect.top = circleY - zoomUpIndicatorBgRadius;
            zoomUpCircleRect.right = circleX + zoomUpIndicatorBgRadius;
            zoomUpCircleRect.bottom = circleY + zoomUpIndicatorBgRadius;
            path.reset();
            double degree = Math.toRadians(60);
            path.moveTo(zoomUpRight, circleY);
            path.lineTo((float) (circleX + Math.cos(degree) * zoomUpIndicatorBgRadius), (float) (circleY - Math.sin(degree) * zoomUpIndicatorBgRadius));
            path.moveTo(zoomUpRight, circleY);
            path.lineTo((float) (circleX + Math.cos(degree) * zoomUpIndicatorBgRadius), (float) (circleY + Math.sin(degree) * zoomUpIndicatorBgRadius));
            path.arcTo(zoomUpCircleRect, 60, 240);
            paint.setColor(zoomUpIndicatorBgColor);
            canvas.drawPath(path, paint);
            // text
            paint.setColor(zoomUpIndicatorTextColor);
            paint.setTextSize(zoomUpIndicatorTextSize);
            Paint.FontMetrics fontMetrics = paint.getFontMetrics();
            String tmp = titles.get(onTouchIndex);
            paint.getTextBounds(tmp, 0, tmp.length(), textBounds);
            float xOffset = zoomUpIndicatorBgRadius - textBounds.width() / 2;
            float yOffset = zoomUpIndicatorCenterY - fontMetrics.top / 2 - fontMetrics.bottom / 2;
            canvas.drawText(tmp, xOffset, yOffset, paint);
        }
    }

    private float getTotalItemHeight() {
        float total = 0;
        total += titles.size() * indicatorItemHeight;
        return total;
    }

    interface OnTitleIndexChangeListener {
        void onTitleIndexChanged(int index);
    }
}
