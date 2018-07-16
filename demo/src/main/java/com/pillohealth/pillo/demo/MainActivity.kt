package com.pillohealth.pillo.demo

import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import rubikstudio.library.OnItemRotationListener
import rubikstudio.library.OnItemSelectedListener
import rubikstudio.library.model.LuckyItem
import java.util.*









class MainActivity : AppCompatActivity() {

    private var data: ArrayList<LuckyItem> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val item1 = LuckyItem()
        item1.apply {
            title = "SUN"
            subtitle = "08"
            color = Color.WHITE
        }
        data.add(item1)

        val item2 = LuckyItem()
        item2.apply {
            title = "MON"
            subtitle = "09"
            color = Color.WHITE
        }
        data.add(item2)

        val item3 = LuckyItem()
        item3.apply {
            title = "TUE"
            subtitle = "10"
            color = Color.WHITE
        }
        data.add(item3)

        val item4 = LuckyItem()
        item4.apply {
            title = "WED"
            subtitle = "11"
            color = Color.WHITE
        }
        data.add(item4)

        val item5 = LuckyItem()
        item5.apply {
            title = "THU"
            subtitle = "12"
            color = Color.WHITE
        }
        data.add(item5)

        val item6 = LuckyItem()
        item6.apply {
            title = "FRI"
            subtitle = "13"
            color = Color.WHITE
        }
        data.add(item6)

        val item7 = LuckyItem()
        item7.apply {
            title = "SAT"
            subtitle = "14"
            color = Color.WHITE
        }
        data.add(item7)


//        imgBedtime.isDrawingCacheEnabled = true
//        var bitmap : Bitmap?

        val bitmap = (imgBedtime.drawable as BitmapDrawable).bitmap

        val rnd = Random()
        imgBedtime.setOnTouchListener { _, motionEvent ->
//
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
//                    val bitmap = (imgBedtime.drawable as BitmapDrawable).bitmap
//                    bitmap = Bitmap.createBitmap(imgBedtime.drawingCache)
//                    imgBedtime.isDrawingCacheEnabled = false

                    val inverse = Matrix()
                    imgBedtime.imageMatrix.invert(inverse)
                    val touchPoint = floatArrayOf(motionEvent.x, motionEvent.y)
                    inverse.mapPoints(touchPoint)
                    val xCoord = touchPoint[0].toInt()
                    val yCoord = touchPoint[1].toInt()


                    val pixel = bitmap.getPixel(xCoord, yCoord)
                    val a  = Color.alpha(pixel)
                    if (a == 0)
                        return@setOnTouchListener false
                    return@setOnTouchListener true
                }
                MotionEvent.ACTION_UP -> {

                    val inverse = Matrix()
                    imgBedtime.imageMatrix.invert(inverse)
                    val touchPoint = floatArrayOf(motionEvent.x, motionEvent.y)
                    inverse.mapPoints(touchPoint)
                    val xCoord = touchPoint[0].toInt()
                    val yCoord = touchPoint[1].toInt()

//                    val bitmap = (imgBedtime.drawable as BitmapDrawable).bitmap
                    val pixel = bitmap.getPixel(xCoord, yCoord)
                    val a  = Color.alpha(pixel)
                    val r = Color.red(pixel)
                    val g = Color.green(pixel)
                    val b  = Color.blue(pixel)
                    if (a == 0)
                        return@setOnTouchListener false
                    imgBedtime.setColorFilter(Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)))
                    return@setOnTouchListener true
                }
            }
            return@setOnTouchListener false
        }

        luckyWheel.setData(data)

        luckyWheel.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(luckyItem: LuckyItem) {
                luckyWheel.centerItem(luckyItem)
            }
        })
        luckyWheel.setOnItemRotationListener(object : OnItemRotationListener {
            override fun onItemRotated(luckyItem: LuckyItem) {
                Toast.makeText(applicationContext, luckyItem.title.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }
}
