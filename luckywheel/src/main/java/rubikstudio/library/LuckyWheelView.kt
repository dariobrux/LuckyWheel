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
import java.util.*

/**
 * Created by kiennguyen on 11/5/16.
 */

class LuckyWheelView : RelativeLayout {

    private var mBackgroundColor: Int = 0
    private var mTitleColor: Int = 0
    private var mTitleSize: Int = 14
    private var mSubtitleSize: Int = 14
    private var mSubtitleColor: Int = 0
    private var mCenterImage: Drawable? = null
    private var mCursorImage: Drawable? = null
    private var mStrokeColor: Int? = Color.TRANSPARENT

    private var mFirstTitleColor: Int? = null
    private var mFirstSubtitleColor: Int? = null

    private var mLetterSpacing: Float = 1f

    private var mAnimationDuration: Long = 1000

    private var mDirectionFirstItem: Int = DirectionFirstItem.BOTTOM.value

    private lateinit var pieView: PieView
    private var ivCursorView: ImageView? = null

    private var step = 0
    private lateinit var itemListTemp: ArrayList<LuckyItem>

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
            mBackgroundColor = typedArray.getColor(R.styleable.LuckyWheelView_lkw_backgroundColor, -0x340000)
            mTitleColor = typedArray.getColor(R.styleable.LuckyWheelView_lkw_titleColor, -0x1)
            mTitleSize = typedArray.getDimensionPixelOffset(R.styleable.LuckyWheelView_lkw_titleSize, mTitleSize)
            mSubtitleSize = typedArray.getDimensionPixelOffset(R.styleable.LuckyWheelView_lkw_subtitleSize, mSubtitleSize)
            mSubtitleColor = typedArray.getColor(R.styleable.LuckyWheelView_lkw_subtitleColor, -0x1)
            mCursorImage = typedArray.getDrawable(R.styleable.LuckyWheelView_lkw_cursor)
            mCenterImage = typedArray.getDrawable(R.styleable.LuckyWheelView_lkw_centerImage)
            mStrokeColor = typedArray.getColor(R.styleable.LuckyWheelView_lkw_strokeColor, Color.TRANSPARENT)
            mFirstTitleColor = typedArray.getColor(R.styleable.LuckyWheelView_lkw_firstTitleColor, mTitleColor)
            mFirstSubtitleColor = typedArray.getColor(R.styleable.LuckyWheelView_lkw_firstSubtitleColor, mSubtitleColor)
            mLetterSpacing = typedArray.getFloat(R.styleable.LuckyWheelView_lkw_letterSpacing, 1f)
            mAnimationDuration = typedArray.getInt(R.styleable.LuckyWheelView_lkw_animationDuration, 1000).toLong()
            mDirectionFirstItem = typedArray.getInt(R.styleable.LuckyWheelView_lkw_directionFirstItem, 0)
            typedArray.recycle()
        }

        val inflater = LayoutInflater.from(context)
        val frameLayout = inflater.inflate(R.layout.lucky_wheel_layout, this, false) as FrameLayout

        pieView = frameLayout.findViewById(R.id.pieView)
        ivCursorView = frameLayout.findViewById(R.id.cursorView)

        pieView.setPieBackgroundColor(mBackgroundColor)

        mCenterImage?.let {
            pieView.setPieCenterImage(it)
        }

        mStrokeColor?.let {
            pieView.setStrokeColor(it)
        }

        pieView.setPieTitleColor(mTitleColor)
        pieView.setPieSubtitleColor(mSubtitleColor)

        pieView.setPieTitleSize(mTitleSize)
        pieView.setPieSubtitleSize(mSubtitleSize)

        pieView.setLetterSpacing(mLetterSpacing)

        pieView.setAnimationDuration(mAnimationDuration)

        rotation = when (DirectionFirstItem.values()[mDirectionFirstItem]) {
            DirectionFirstItem.TOP -> 0f
            DirectionFirstItem.BOTTOM -> 180f
        }

        mFirstTitleColor?.let {
            pieView.setFirstTitleColor(it)
        }

        mFirstSubtitleColor?.let {
            pieView.setFirstSubtitleColor(it)
        }

        mCursorImage?.let {
            ivCursorView!!.setImageDrawable(it)
        }

        addView(frameLayout)
    }

//    fun setLuckyWheelBackgrouldColor(color: Int) {
//        pieView.setPieBackgroundColor(color)
//    }
//
//    fun setLuckyWheelCursorImage(drawable: Int) {
//        ivCursorView!!.setBackgroundResource(drawable)
//    }
//
//    fun setLuckyWheelCenterImage(drawable: Drawable) {
//        pieView.setPieCenterImage(drawable)
//    }
//
//    fun setLuckyWheelTitleColor(color: Int) {
//        pieView.setPieTitleColor(color)
//    }

    /**
     *
     * @param data
     */
    fun setData(data: List<LuckyItem>) {
        pieView.setData(data)
        itemListTemp = ArrayList(data)
        startTo(0)
    }

//    /**
//     *
//     * @param numberOfRound
//     */
//    fun setRound(numberOfRound: Int) {
//        pieView.setRound(numberOfRound)
//    }
//
//    fun startLuckyWheelWithTargetIndex(index: Int) {
//        pieView.rotateTo(index)
//    }

    fun setOnItemSelectedListener(listener: OnItemSelectedListener) {
        pieView.setOnItemSelectedListener(listener)
    }

    fun setOnItemRotationListener(listener: OnItemRotationListener) {
        pieView.setOnItemRotationListener(listener)
    }

    private fun rotateByStep(step: Int) {
        pieView.rotateByStep(step)
    }

    private fun startTo(position: Int) {
        pieView.rotateTo(position, false)
    }

//    fun rotateTo(position: Int) {
//        pieView.rotateTo(position, true)
//    }

    fun centerItem(item : LuckyItem) {
        val index = itemListTemp.indexOf(item)
        step = if (index <= itemListTemp.size / 2) {
            // clockwise
            index
        } else {
            // counter clockwise
            index - itemListTemp.size
        }
        rotateByStep(step)
        Collections.rotate(itemListTemp, -step)
    }
}
