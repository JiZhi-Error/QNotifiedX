package com.qnotifiedx.app.hook.base.moduleinit

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.core.view.size
import com.qnotifiedx.app.BuildConfig
import com.qnotifiedx.app.util.*

//模块入口Hook
object ModuleEntry {
    fun init() {
        findMethodByCondition("com.tencent.mobileqq.activity.QQSettingSettingActivity") {
            it.name == "doOnCreate"
        }.also { m ->
            m.hookAfter(100) {
                val thisObject = it.thisObject
                //加载QQ的设置物件类
                val cFormSimpleItem = loadClass("com.tencent.mobileqq.widget.FormSimpleItem")
                //获取所在的ViewGroup
                val vg =
                    (thisObject.getObjectOrNull("a", cFormSimpleItem) as View).parent as ViewGroup
                //创建入口View
                val entry = cFormSimpleItem.newInstance(
                    arrayOf(thisObject as Context),
                    arrayOf(Context::class.java)
                ) as View
                //设置入口属性
                entry.apply {
                    invokeMethod(
                        "setLeftText",
                        arrayOf("QNotifiedX"),
                        arrayOf(CharSequence::class.java)
                    )
                    invokeMethod(
                        "setRightText",
                        arrayOf(BuildConfig.VERSION_NAME),
                        arrayOf(CharSequence::class.java)
                    )
                    setOnClickListener {
                        appContext?.showToast("还没有准备好哦~")
                    }
                }
                //添加入口
                vg.addView(entry, (vg.size / 2) - 4)
            }
        }
    }
}