package com.andy.mataballpoint;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator
 * Time on 2015/12/16.
 * Description
 */
public class MataballPointView extends View{

	private static final int DEFAULT_RADIUS = 35;
	private final float SCALE = 0.2f;
	private final float RATE = 2f;
	private int mPointDivider;
	private int mCount;
	private int mRadius;
	private Paint mPaint;
	private List<Circle> mPoints;
	private Circle mPoint;
	private int mLocation;
	private int mDefaultColor;
	private int mLightColor;


	public MataballPointView(Context context) {
		this(context, null);
	}

	public MataballPointView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MataballPointView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public MataballPointView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}

	private void init(){
		mRadius = DEFAULT_RADIUS;
		mPointDivider = (int) (DEFAULT_RADIUS * 1.8f);
		mDefaultColor = 0xff4db9ff;
		mLightColor = 0xffff0000;
		mPaint = new Paint();
		mPaint.setColor(mDefaultColor);
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setAntiAlias(true);
		initPoints();
	}

	private void initPoints() {
		mPoints = new ArrayList<Circle>();
		Circle point = new Circle();
		point.center = new float[]{mRadius * (1f + SCALE), mRadius * (1f + SCALE)};
		point.radius = mRadius / 5 * 4;
		mPoints.add(point);
		for (int i = 0; i < mCount; i++) {
			point = new Circle();
			point.center = new float[]{(mRadius * 2 + mPointDivider) * i + mRadius * (1f + SCALE), mRadius * (1f + SCALE)};
			point.radius = mRadius;
			mPoints.add(point);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(resolveSizeAndState((int) (mCount * (mRadius * 2 + mPointDivider)), widthMeasureSpec, 0),
				resolveSizeAndState((int) (2 * mRadius * 1.4f), heightMeasureSpec, 0));
	}

	public void setPositionOffset (int position, float offset) {
		mLocation = (int) ((mRadius * 2 + mPointDivider) * (position + offset)) + (int) (mRadius * (1f + SCALE));
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		mPoint = mPoints.get(0);
		mPoint.center[0] = mLocation;

		RectF ball = new RectF();
		ball.left = mPoint.center[0] - mPoint.radius;
		ball.top = mPoint.center[1] - mPoint.radius;
		ball.right = ball.left + mPoint.radius * 2;
		ball.bottom = ball.top + mPoint.radius * 2;
		canvas.drawCircle(ball.centerX(), ball.centerY(), mPoint.radius, mPaint);

		for (int i = 1, l = mPoints.size(); i < l; i++) {
			metaball(canvas, i, 0.5f, (mRadius * 2f + mPointDivider));
		}
	}

	/**
	 * @param canvas 画布
	 * @param i 圆的位置
	 * @param v 控制两个圆连接时候长度，间接控制连接线的粗细，该值为1的时候连接线为直线
	 * @param maxDistance 两球发生贝塞尔曲线最大距离
	 */
	private void metaball(Canvas canvas, int i, float v, float maxDistance) {
		final Circle point1 = mPoints.get(0);
		final Circle point2 = mPoints.get(i);

		RectF ball1 = new RectF();
		ball1.left = point1.center[0] - point1.radius;
		ball1.top = point1.center[1] - point1.radius;
		ball1.right = ball1.left + point1.radius * 2;
		ball1.bottom = ball1.top + point1.radius * 2;

		RectF ball2 = new RectF();
		ball2.left = point2.center[0] - point2.radius;
		ball2.top = point2.center[1] - point2.radius;
		ball2.right = ball2.left + point2.radius * 2;
		ball2.bottom = ball2.top + point2.radius * 2;

		float[] center1 = new float[]{
				ball1.centerX(),
				ball1.centerY()
		};
		float[] center2 = new float[]{
				ball2.centerX(),
				ball2.centerY()
		};
		float d = getDistance(center1, center2); //两点的距离

		float radius1 = ball1.width() / 2;  // 球1半径
		float radius2 = ball2.width() / 2;  // 球2半径
		float pi2 = (float) (Math.PI / 2);  // π / 2
		float u1, u2;

		Log.i("", "MataballPointView d = " + d + " maxDistance = " + maxDistance);
		if (d >= maxDistance) {
			canvas.drawCircle(center2[0], center2[1], radius2, mPaint);
		} else {
			float scale2 = 1 + SCALE * (1 - d / maxDistance); //球2放大倍数
			radius2 *= scale2; //球2放大半径
			if(d == 0) {
				Paint paint = new Paint();
				paint.setColor(mLightColor);
				paint.setStyle(Paint.Style.FILL);
				paint.setAntiAlias(true);
				canvas.drawCircle(center2[0], center2[1], radius2, paint);
			} else {
				canvas.drawCircle(center2[0], center2[1], radius2, mPaint);
			}
		}

		if (radius1 == 0 || radius2 == 0) {
			return;
		}

		if (d >= maxDistance || d <= Math.abs(radius1 - radius2)) {
			return;
		} else if (d < radius1 + radius2) {
			u1 = (float) Math.acos((radius1 * radius1 + d * d - radius2 * radius2) /
					(2 * radius1 * d));
			u2 = (float) Math.acos((radius2 * radius2 + d * d - radius1 * radius1) /
					(2 * radius2 * d));  //球2到球
		} else {
			u1 = 0;
			u2 = 0;
		}
		float[] centerMin = new float[]{center2[0] - center1[0], center2[1] - center1[1]};

		float angle1 = (float) Math.atan2(centerMin[1], centerMin[0]);
		float angle2 = (float) Math.acos((radius1 - radius2) / d);
		float angle1a = angle1 + u1 + (angle2 - u1) * v;
		float angle1b = angle1 - u1 - (angle2 - u1) * v;
		float angle2a = (float) (angle1 + Math.PI - u2 - (Math.PI - u2 - angle2) * v);
		float angle2b = (float) (angle1 - Math.PI + u2 + (Math.PI - u2 - angle2) * v);

		float[] p1a1 = getVector(angle1a, radius1);
		float[] p1b1 = getVector(angle1b, radius1);
		float[] p2a1 = getVector(angle2a, radius2);
		float[] p2b1 = getVector(angle2b, radius2);

		float[] p1a = new float[]{p1a1[0] + center1[0], p1a1[1] + center1[1]};
		float[] p1b = new float[]{p1b1[0] + center1[0], p1b1[1] + center1[1]};
		float[] p2a = new float[]{p2a1[0] + center2[0], p2a1[1] + center2[1]};
		float[] p2b = new float[]{p2b1[0] + center2[0], p2b1[1] + center2[1]};

		float[] p1_p2 = new float[]{p1a[0] - p2a[0], p1a[1] - p2a[1]};

		float totalRadius = (radius1 + radius2);
		float d2 = Math.min(v * RATE, getLength(p1_p2) / totalRadius);
		d2 *= Math.min(1, d * 2 / (radius1 + radius2));
		radius1 *= d2;
		radius2 *= d2;

		float[] sp1 = getVector(angle1a - pi2, radius1);
		float[] sp2 = getVector(angle2a + pi2, radius2);
		float[] sp3 = getVector(angle2b - pi2, radius2);
		float[] sp4 = getVector(angle1b + pi2, radius1);


		Path path1 = new Path();
		path1.moveTo(p1a[0], p1a[1]);
		path1.cubicTo(p1a[0] + sp1[0], p1a[1] + sp1[1], p2a[0] + sp2[0], p2a[1] + sp2[1], p2a[0], p2a[1]);
		path1.lineTo(p2b[0], p2b[1]);
		path1.cubicTo(p2b[0] + sp3[0], p2b[1] + sp3[1], p1b[0] + sp4[0], p1b[1] + sp4[1], p1b[0], p1b[1]);
		path1.lineTo(p1a[0], p1a[1]);
		path1.close();
		canvas.drawPath(path1, mPaint);

	}

	private float getDistance(float[] b1, float[] b2) {
		float x = b1[0] - b2[0];
		float y = b1[1] - b2[1];
		float d = x * x + y * y;
		return (float) Math.sqrt(d);
	}

	private float getLength(float[] b) {
		return (float) Math.sqrt(b[0] * b[0] + b[1] * b[1]);
	}

	private float[] getVector(float radians, float length) {
		float x = (float) (Math.cos(radians) * length);
		float y = (float) (Math.sin(radians) * length);
		return new float[]{
				x, y
		};
	}

	private class Circle {
		float[] center;
		float radius;
	}

	public void setColor(int colorResId) {
		mPaint.setColor(getResources().getColor(colorResId));
		invalidate();
	}

	public void setPaintMode(int mode) {
		mPaint.setStyle(mode == 0 ? Paint.Style.STROKE : Paint.Style.FILL);
		invalidate();
	}

	public void setCount(int count) {
		mCount = count;
		initPoints();
		invalidate();
	}

	public void setRadius(int radius) {
		mRadius = radius;
		mPointDivider = (int) (radius * 1.8f);
		initPoints();
		invalidate();
	}

	public void setDefaultColor(int defaultColor) {
		mDefaultColor = defaultColor;
		mPaint.setColor(defaultColor);
		invalidate();
	}

	public void setLightColor(int lightColor) {
		mLightColor = lightColor;
		invalidate();
	}
}
