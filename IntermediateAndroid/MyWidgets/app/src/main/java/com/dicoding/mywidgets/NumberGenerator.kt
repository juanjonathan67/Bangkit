package com.dicoding.mywidgets

import android.appwidget.AppWidgetManager
import android.content.Context
import android.widget.RemoteViews
import java.util.*

internal object NumberGenerator {
    fun generate(max: Int): Int {
        val random = Random()
        return random.nextInt(max)
    }
}
