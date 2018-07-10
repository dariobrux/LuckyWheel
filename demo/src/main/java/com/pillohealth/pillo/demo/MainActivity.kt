package com.pillohealth.pillo.demo

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import rubikstudio.library.model.LuckyItem
import java.util.*

class MainActivity : AppCompatActivity() {

    private var data: MutableList<LuckyItem> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val item1 = LuckyItem()
        item1.text = "SUN"
        item1.color = Color.WHITE
        data.add(item1)

        val item2 = LuckyItem()
        item2.text = "MON"
        item2.color = Color.WHITE
        data.add(item2)

        val item3 = LuckyItem()
        item3.text = "TUE"
        item3.color = Color.WHITE
        data.add(item3)

        val item4 = LuckyItem()
        item4.text = "WED"
        item4.color = Color.WHITE
        data.add(item4)

        val item5 = LuckyItem()
        item5.text = "THU"
        item5.color = Color.WHITE
        data.add(item5)

        val item6 = LuckyItem()
        item6.text = "FRI"
        item6.color = Color.WHITE
        data.add(item6)

        val item7 = LuckyItem()
        item7.text = "SAT"
        item7.color = Color.WHITE
        data.add(item7)

        luckyWheel.setData(data)
    }
}
