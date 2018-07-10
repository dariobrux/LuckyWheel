package rubikstudio.library

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

class LuckyWheelView : RelativeLayout, PielView.PieRotateListener {

    private var mBackgroundColor: Int = 0
    private var mTextColor: Int = 0
    private var mCenterImage: Drawable? = null
    private var mCursorImage: Drawable? = null
    private var mStrokeColor: Int? = Color.TRANSPARENT

    private lateinit var pielView: PielView
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
    private fun init(ctx: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val typedArray = ctx.obtainStyledAttributes(attrs, R.styleable.LuckyWheelView)
            mBackgroundColor = typedArray.getColor(R.styleable.LuckyWheelView_lkwBackgroundColor, -0x340000)
            mTextColor = typedArray.getColor(R.styleable.LuckyWheelView_lkwTextColor, -0x1)
            mCursorImage = typedArray.getDrawable(R.styleable.LuckyWheelView_lkwCursor)
            mCenterImage = typedArray.getDrawable(R.styleable.LuckyWheelView_lkwCenterImage)
            mStrokeColor = typedArray.getColor(R.styleable.LuckyWheelView_lkwStrokeColor, Color.TRANSPARENT)
            typedArray.recycle()
        }

        val inflater = LayoutInflater.from(context)
        val frameLayout = inflater.inflate(R.layout.lucky_wheel_layout, this, false) as FrameLayout

        pielView = frameLayout.findViewById(R.id.pieView)
        ivCursorView = frameLayout.findViewById(R.id.cursorView)

        pielView.setPieRotateListener(this)
        pielView.setPieBackgroundColor(mBackgroundColor)

        mCenterImage?.let {
            pielView.setPieCenterImage(it)
        }

        mStrokeColor?.let {
            pielView.setStrokeColor(it)
        }

        pielView.setPieTextColor(mTextColor)

        mCursorImage?.let {
            ivCursorView!!.setImageDrawable(it)
        }

        addView(frameLayout)
    }

    fun setLuckyWheelBackgrouldColor(color: Int) {
        pielView.setPieBackgroundColor(color)
    }

    fun setLuckyWheelCursorImage(drawable: Int) {
        ivCursorView!!.setBackgroundResource(drawable)
    }

    fun setLuckyWheelCenterImage(drawable: Drawable) {
        pielView!!.setPieCenterImage(drawable)
    }

    fun setLuckyWheelTextColor(color: Int) {
        pielView!!.setPieTextColor(color)
    }

    /**
     *
     * @param data
     */
    fun setData(data: List<LuckyItem>) {
        pielView!!.setData(data)
    }

    /**
     *
     * @param numberOfRound
     */
    fun setRound(numberOfRound: Int) {
        pielView!!.setRound(numberOfRound)
    }

    fun startLuckyWheelWithTargetIndex(index: Int) {
        pielView!!.rotateTo(index)
    }
}
