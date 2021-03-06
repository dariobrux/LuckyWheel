package rubikstudio.library

import android.animation.Animator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import rubikstudio.library.model.LuckyItem

/**
 * Created by kiennguyen on 11/5/16.
 */

class PieView : View {

    private var mRange = RectF()
    private var mRadius: Int = 0

    private var mArcPaint: Paint? = null
    private var mBackgroundPaint: Paint? = null
    private var mTitlePaint: Paint? = null
    private var mSubtitlePaint: Paint? = null

    private val mStartAngle = 0f
    private var mCenter: Int = 0
    private var mPadding: Int = 0
    private var mTargetIndex: Int = 0
    private var mRoundOfNumber = 4
    private var isRunning = false
    private var defaultBackgroundColor = -1
    private var drawableCenterImage: Drawable? = null
    private var strokeColor: Int? = Color.TRANSPARENT
    private var firstTitleColor: Int? = null
    private var firstSubtitleColor: Int? = null
    private var mLetterSpacing: Float = 1f
    private var mAnimationDuration: Long = 1000
    private var titleColor = -0x1
    private var subtitleColor = -0x1
    private var titleSize = 14
    private var subtitleSize = 14

    private var mLuckyItemList: List<LuckyItem>? = null
    private var mPathList: ArrayList<Path> = ArrayList()
    private var onItemRotationListener: OnItemRotationListener? = null
    private var mOnItemSelectedListener: OnItemSelectedListener? = null

    /**
     * @return
     */
    private val angleOfIndexTarget: Float
        get() {
            val tempIndex = if (mTargetIndex == 0) 1 else mTargetIndex
            return (360 / mLuckyItemList!!.size * tempIndex).toFloat()
        }

//    interface PieRotateListener {
//        fun rotateDone(index: Int)
//    }

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    fun setOnItemRotationListener(listener: OnItemRotationListener) {
        this.onItemRotationListener = listener
    }

    private fun init() {

        mArcPaint = Paint().apply {
            isAntiAlias = true
            isDither = true
        }

        mTitlePaint = Paint().apply {
            color = titleColor
            typeface = Typeface.create("sans-serif", Typeface.NORMAL)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                letterSpacing = mLetterSpacing
            }
            textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, titleSize.toFloat(), resources.displayMetrics)
        }

        mSubtitlePaint = Paint().apply {
            color = subtitleColor
            typeface = Typeface.create("sans-serif", Typeface.NORMAL)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                letterSpacing = mLetterSpacing
            }
            textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, subtitleSize.toFloat(), resources.displayMetrics)
        }

        mRange = RectF(mPadding.toFloat(), mPadding.toFloat(), (mPadding + mRadius).toFloat(), (mPadding + mRadius).toFloat())
    }

    fun setData(luckyItemList: List<LuckyItem>) {
        this.mLuckyItemList = luckyItemList
        invalidate()
    }

    fun setPieBackgroundColor(color: Int) {
        defaultBackgroundColor = color
        invalidate()
    }

    fun setPieCenterImage(drawable: Drawable) {
        drawableCenterImage = drawable
        invalidate()
    }

    fun setStrokeColor(strokeColor: Int) {
        this.strokeColor = strokeColor
    }

    fun setPieTitleColor(color: Int) {
        titleColor = color
        invalidate()
    }

    fun setPieSubtitleColor(color: Int) {
        subtitleColor = color
        invalidate()
    }

    fun setPieTitleSize(size: Int) {
        titleSize = size
        invalidate()
    }

    fun setPieSubtitleSize(size: Int) {
        subtitleSize = size
        invalidate()
    }

    fun setFirstTitleColor(color: Int) {
        firstTitleColor = color
        invalidate()
    }

    fun setFirstSubtitleColor(color: Int) {
        firstSubtitleColor = color
        invalidate()
    }

    fun setLetterSpacing(spacing: Float) {
        mLetterSpacing = spacing
        invalidate()
    }

    fun setAnimationDuration(duration: Long) {
        mAnimationDuration = duration
    }

    private fun drawPieBackgroundWithBitmap(canvas: Canvas, bitmap: Bitmap) {
        canvas.drawBitmap(bitmap, null, Rect(mPadding / 2, mPadding / 2,
                measuredWidth - mPadding / 2, measuredHeight - mPadding / 2), null)
    }

    /**
     *
     * @param canvas
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (mLuckyItemList == null) {
            return
        }

        drawBackgroundColor(canvas, defaultBackgroundColor)

        init()

        var tmpAngle = mStartAngle
        val sweepAngle = (360f / mLuckyItemList!!.size.toFloat())

        mPathList.clear()

        for (i in mLuckyItemList!!.indices) {

            mArcPaint!!.style = Paint.Style.FILL
            mArcPaint!!.color = mLuckyItemList!![i].color
//            canvas.drawArc(mRange, tmpAngle, sweepAngle, true, mArcPaint!!)
            val path = Path()
            path.moveTo(width / 2f, height / 2f)
            path.arcTo(mRange, tmpAngle, sweepAngle, false)
            canvas.drawPath(path, mArcPaint)

            mPathList.add(path)

            strokeColor?.let {
                mArcPaint!!.style = Paint.Style.STROKE
                mArcPaint!!.color = it
                canvas.drawArc(mRange, tmpAngle, sweepAngle, true, mArcPaint!!)
            }

            mTitlePaint!!.color = titleColor
            mSubtitlePaint!!.color = subtitleColor

            if (i == 0) {
                firstTitleColor?.let {
                    mTitlePaint!!.color = it
                }

                firstSubtitleColor?.let {
                    mSubtitlePaint!!.color = it
                }
            }

//            BitmapFactory.decodeResource(resources, mLuckyItemList!![i].indicator)?.let {
//                drawIndicator(canvas, tmpAngle, sweepAngle, it)
//            }

            drawTitle(canvas, tmpAngle, sweepAngle, mLuckyItemList!![i].title!!)
            drawSubtitle(canvas, tmpAngle, sweepAngle, mLuckyItemList!![i].subtitle!!)

            BitmapFactory.decodeResource(resources, mLuckyItemList!![i].icon)?.let {
                drawImage(canvas, tmpAngle, it)
            }

            tmpAngle += sweepAngle
        }

        drawableCenterImage?.let {
            drawCenterImage(canvas, drawableCenterImage)
        }
    }

//    private fun drawIndicator(canvas: Canvas, tmpAngle: Float, sweepAngle: Float, indicator: Bitmap) {
////        val path = Path()
////        path.addArc(mRange, tmpAngle, sweepAngle)
////
////        val hOffset = (mRadius * Math.PI / mLuckyItemList!!.size.toDouble() / 2.0 ).toFloat()
////        val vOffset = mRadius.toFloat()
////
////        val imgWidth = 32
////
////
////        val x = (mCenter + mRadius / 2 / 2 * Math.cos(angle.toDouble())).toInt()
////        val y = mCenter + mRadius / 2
////
////        val rect = Rect(x - imgWidth / 2, y - imgWidth / 2, x + imgWidth / 2, y + imgWidth / 2)
////        canvas.drawBitmap(indicator, null, rect, null)
//
//        val imgWidth = indicator.width
//        val imgHeight = indicator.height
//        val angle = ((tmpAngle + 360 / mLuckyItemList!!.size / 2) * Math.PI / 180).toFloat()
//        val x = mCenter + mRadius / 2f  * Math.cos(angle.toDouble()).toFloat()
//        val y = mCenter + mRadius / 2f  * Math.sin(angle.toDouble()).toFloat()
//        val rect = RectF(x - imgWidth / 2f, y - imgWidth / 2f, x + imgWidth / 2f, y + imgHeight / 2f)
//
//
//
//        val matrix = Matrix()
//        matrix.postTranslate(rect.centerX(), rect.centerY())
//        matrix.postTranslate(-rect.width() / 2.toFloat(), -rect.height() / 2)
////        matrix.postTranslate(-rect.width() / 2f, (-rect.height()).toFloat())
////        matrix.postTranslate(rect.centerX().toFloat(), rect.centerY().toFloat())
//        matrix.postRotate((tmpAngle + 360f / mLuckyItemList!!.size / 2f) - 90, rect.centerX(), rect.centerY())
//
//// to set the position in canvas where the bitmap should be drawn to;
//// NOTE: coords in canvas-space!
//
//        val yellowPaint = Paint()
//        yellowPaint.color = Color.WHITE
//        yellowPaint.style = Paint.Style.FILL_AND_STROKE
//        yellowPaint.strokeWidth = 10f
//        canvas.drawRect(rect, yellowPaint)
////        canvas.drawPoint(rect.left.toFloat(), rect.top.toFloat(), paint)
//
//        canvas.drawBitmap(indicator, matrix, null)
//
//
////        canvas.drawBitmap(indicator, null, rect, null)
//
//
//
//        val cyanPaint = Paint()
//        cyanPaint.color = Color.CYAN
//        cyanPaint.style = Paint.Style.FILL_AND_STROKE
//        cyanPaint.strokeWidth = 30f
//        canvas.drawPoint(rect.centerX(), rect.centerY(), cyanPaint)
//
//
//
////        val paint = Paint()
////        paint.color = Color.GREEN
////        paint.style = Paint.Style.FILL_AND_STROKE
////        paint.strokeWidth = 10f
////        canvas.drawArc(Math.cos(angle.toDouble()).toFloat()/ 2, Math.sin(angle.toDouble()).toFloat()/2, indicator.width.toFloat(), indicator.height.toFloat(), tmpAngle, sweepAngle, false, paint)
//    }

    private fun drawBackgroundColor(canvas: Canvas, color: Int) {
        if (color == -1)
            return
        mBackgroundPaint = Paint()
        mBackgroundPaint!!.color = color
        canvas.drawCircle(mCenter.toFloat(), mCenter.toFloat(), mCenter.toFloat(), mBackgroundPaint!!)
    }

    /**
     * @param canvas
     * @param tmpAngle
     * @param bitmap
     */
    private fun drawImage(canvas: Canvas, tmpAngle: Float, bitmap: Bitmap) {
        val imgWidth = mRadius / mLuckyItemList!!.size

        val angle = ((tmpAngle + 360 / mLuckyItemList!!.size / 2) * Math.PI / 180).toFloat()

        val x = (mCenter + mRadius / 2 / 2 * Math.cos(angle.toDouble())).toInt()
        val y = (mCenter + mRadius / 2 / 2 * Math.sin(angle.toDouble())).toInt()

        val rect = Rect(x - imgWidth / 2, y - imgWidth / 2, x + imgWidth / 2, y + imgWidth / 2)
        canvas.drawBitmap(bitmap, null, rect, null)
    }

    private fun drawCenterImage(canvas: Canvas, drawable: Drawable?) {
        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawable);
        var bitmap = LuckyWheelUtils.drawableToBitmap(drawable)
        bitmap = Bitmap.createScaledBitmap(bitmap, 90, 90, false)
        canvas.drawBitmap(bitmap, (measuredWidth / 2 - bitmap.width / 2).toFloat(), (measuredHeight / 2 - bitmap.height / 2).toFloat(), null)
    }

    /**
     * @param canvas
     * @param tmpAngle
     * @param sweepAngle
     * @param mStr
     */
    private fun drawTitle(canvas: Canvas, tmpAngle: Float, sweepAngle: Float, mStr: String) {
        val path = Path()
        path.addArc(mRange, tmpAngle, sweepAngle)

        val textWidth = mTitlePaint!!.measureText(mStr)
        val hOffset = (mRadius * Math.PI / mLuckyItemList!!.size.toDouble() / 2.0 - textWidth / 2).toInt()

        val vOffset = mRadius / 2 / 4

        canvas.drawTextOnPath(mStr, path, hOffset.toFloat(), vOffset.toFloat(), mTitlePaint!!)
    }

    /**
     * @param canvas
     * @param tmpAngle
     * @param sweepAngle
     * @param mStr
     */
    private fun drawSubtitle(canvas: Canvas, tmpAngle: Float, sweepAngle: Float, mStr: String) {
        val path = Path()
        path.addArc(mRange, tmpAngle, sweepAngle)

        val textWidth = mSubtitlePaint!!.measureText(mStr)
        val hOffset = (mRadius * Math.PI / mLuckyItemList!!.size.toDouble() / 2.0 - textWidth / 2).toInt()

        val vOffset = (mRadius / 2 / 3) + 20

        canvas.drawTextOnPath(mStr, path, hOffset.toFloat(), vOffset.toFloat(), mSubtitlePaint!!)
    }

    /**
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val width = Math.min(measuredWidth, measuredHeight)

        mPadding = if (paddingLeft == 0) 10 else paddingLeft
        mRadius = width - mPadding * 2

        mCenter = width / 2

        setMeasuredDimension(width, width)
    }

//    /**
//     * @param numberOfRound
//     */
//    fun setRound(numberOfRound: Int) {
//        mRoundOfNumber = numberOfRound
//    }

    fun rotateByStep(step: Int) {
        if (isRunning) {
            return
        }

        mTargetIndex += step

        val targetAngle = (360f / mLuckyItemList!!.size.toFloat()) * step
        animate()
                .setInterpolator(DecelerateInterpolator())
//                .setDuration(Math.abs(step) * 1000 + 900L)
                .setDuration(mAnimationDuration)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {
                        isRunning = true
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        isRunning = false

                        var index = mTargetIndex % mLuckyItemList!!.size
                        if (index < 0)
                            index = mLuckyItemList!!.size - Math.abs(index)
                        onItemRotationListener?.onItemRotated(mLuckyItemList!![index])
                    }

                    override fun onAnimationCancel(animation: Animator) {}

                    override fun onAnimationRepeat(animation: Animator) {}
                })
                .rotationBy(-targetAngle)
                .start()
    }

    /**
     * @param index
     */
    fun rotateTo(index: Int, animated: Boolean = true) {
        if (isRunning) {
            return
        }
        mTargetIndex = index
        rotation = 0f
        val targetAngle = 360 * mRoundOfNumber + 270 - angleOfIndexTarget + 360 / mLuckyItemList!!.size / 2
        animate()
                .setInterpolator(DecelerateInterpolator())
//                .setDuration(if (animated) mRoundOfNumber * 1000 + 900L else 0)
                .setDuration(if (animated) mAnimationDuration else 0)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {
                        isRunning = true
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        isRunning = false
                        onItemRotationListener?.onItemRotated(mLuckyItemList!![index])
                    }

                    override fun onAnimationCancel(animation: Animator) {}

                    override fun onAnimationRepeat(animation: Animator) {}
                })
                .rotation(targetAngle)
                .start()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (isRunning) {
            return false
        }
        for (i in 0 until mPathList.size) {

            val path = mPathList[i]

            val rectF = RectF()
            path.computeBounds(rectF, true)
            val region = Region()
            region.setPath(path, Region(rectF.left.toInt(), rectF.top.toInt(), rectF.right.toInt(), rectF.bottom.toInt()))

            val point = Point()
            point.x = event.x.toInt()
            point.y = event.y.toInt()

            if (event.action == MotionEvent.ACTION_DOWN) {
                return true
            }

            if (region.contains(point.x, point.y) && event.action == MotionEvent.ACTION_UP) {
                mOnItemSelectedListener?.onItemSelected(mLuckyItemList!![i])
                return true
            }
        }

        return false
    }

    fun setOnItemSelectedListener(listener: OnItemSelectedListener) {
        mOnItemSelectedListener = listener
    }
}
