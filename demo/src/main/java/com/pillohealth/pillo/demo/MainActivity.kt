package com.pillohealth.pillo.demo

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
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
