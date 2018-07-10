package rubikstudio.library

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import rubikstudio.library.model.LuckyItem

/**
 * Created by kiennguyen on 11/5/16.
 */

class LuckyWheelView : RelativeLayout, PieView.PieRotateListener {

    private var mBackgroundColor: Int = 0
    private var mTitleColor: Int = 0
    private var mSubtitleColor: Int = 0
    private var mCenterImage: Drawable? = null
    private var mCursorImage: Drawable? = null
    private var mStrokeColor: Int? = Color.TRANSPARENT

    private lateinit var pieView: PieView
    private var ivCursorView: ImageView? = null

    private var mLuckyRoundItemSelectedListener: LuckyRoundItemSelectedListener? = null

    override fun rotateDone(index: Int) {
        if (mLuckyRoundItemSelectedListener != null) {
            mLuckyRoundItemSelectedListener!!.LuckyRoundItemSelected(index)
        }
    }

    interface LuckyRoundItemSelectedListener {
        fun LuckyRoundItemSelected(index: Int)
    }

    fun setLuckyRoundItemSelectedListener(listener: LuckyRoundItemSelectedListener) {
        this.mLuckyRoundItemSelectedListener = listener
    }

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    /**
     *
     * @param ctx
     * @param attrs
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun init(ctx: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val typedArray = ctx.obtainStyledAttributes(attrs, R.styleable.LuckyWheelView)
            mBackgroundColor = typedArray.getColor(R.styleable.LuckyWheelView_lkwBackgroundColor, -0x340000)
            mTitleColor = typedArray.getColor(R.styleable.LuckyWheelView_lkwTitleColor, -0x1)
            mSubtitleColor = typedArray.getColor(R.styleable.LuckyWheelView_lkwSubtitleColor, -0x1)
            mCursorImage = typedArray.getDrawable(R.styleable.LuckyWheelView_lkwCursor)
            mCenterImage = typedArray.getDrawable(R.styleable.LuckyWheelView_lkwCenterImage)
            mStrokeColor = typedArray.getColor(R.styleable.LuckyWheelView_lkwStrokeColor, Color.TRANSPARENT)
            typedArray.recycle()
        }

        val inflater = LayoutInflater.from(context)
        val frameLayout = inflater.inflate(R.layout.lucky_wheel_layout, this, false) as FrameLayout

        pieView = frameLayout.findViewById(R.id.pieView)
        ivCursorView = frameLayout.findViewById(R.id.cursorView)

        pieView.setPieRotateListener(this)
        pieView.setPieBackgroundColor(mBackgroundColor)
//        pieView.setOnTouchListener { view, motionEvent ->
//            Toast.makeText(context, pieView.angleOfIndexTarget.toString(), Toast.LENGTH_SHORT).show()
//            return@setOnTouchListener true
//        }

        mCenterImage?.let {
            pieView.setPieCenterImage(it)
        }

        mStrokeColor?.let {
            pieView.setStrokeColor(it)
        }

        pieView.setPieTitleColor(mTitleColor)
        pieView.setPieSubtitleColor(mSubtitleColor)

        mCursorImage?.let {
            ivCursorView!!.setImageDrawable(it)
        }

        addView(frameLayout)
    }

    fun setLuckyWheelBackgrouldColor(color: Int) {
        pieView.setPieBackgroundColor(color)
    }

    fun setLuckyWheelCursorImage(drawable: Int) {
        ivCursorView!!.setBackgroundResource(drawable)
    }

    fun setLuckyWheelCenterImage(drawable: Drawable) {
        pieView.setPieCenterImage(drawable)
    }

    fun setLuckyWheelTitleColor(color: Int) {
        pieView.setPieTitleColor(color)
    }

    /**
     *
     * @param data
     */
    fun setData(data: List<LuckyItem>) {
        pieView.setData(data)
    }

    /**
     *
     * @param numberOfRound
     */
    fun setRound(numberOfRound: Int) {
        pieView.setRound(numberOfRound)
    }

    fun startLuckyWheelWithTargetIndex(index: Int) {
        pieView.rotateTo(index)
    }
}
