package com.mask.wechathelper.assists.simple.step

import android.content.ComponentName
import android.content.Intent
import com.mask.wechathelper.assist.Assists
import com.mask.wechathelper.assist.ext.click
import com.mask.wechathelper.assist.ext.getBoundsInScreen
import com.mask.wechathelper.assist.ext.logToText
import com.mask.wechathelper.assist.step.StepCollector
import com.mask.wechathelper.assist.step.StepImpl
import com.mask.wechathelper.assist.step.StepManager
import com.mask.wechathelper.assist.ui.UIOperate
import com.mask.wechathelper.OverManager

class OpenZhuLongSocial : StepImpl {
    override fun onImpl(collector: StepCollector) {
        collector.next(Step.STEP_1) {
            OverManager.log("启动注考帮Pro")
            Intent().apply {
                addCategory(Intent.CATEGORY_LAUNCHER)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                component = ComponentName("com.zhulong.newtikupro", "com.zhulong.newtikupro.main.ui.main.MainActivity")
                Assists.service?.startActivity(this)
            }
            StepManager.execute(this::class.java, Step.STEP_2)
        }.nextLoop(Step.STEP_2) {
            OverManager.log("检查是否已打开主页：\n剩余时间=${it.loopSurplusSecond}秒")
            UIOperate.findByText("我的").forEach {
                val screen = it.getBoundsInScreen()
                if ("我的".equals(it.text)) {
                    OverManager.log("已打开主页，点击【我的】")
                    it.parent.parent.click()
                    StepManager.execute(this::class.java, Step.STEP_3)
                    return@nextLoop true
                }
            }

            if (0f==it.loopSurplusSecond){
                StepManager.execute(this::class.java, Step.STEP_1)
            }

            false
        }.next(Step.STEP_3) {
            UIOperate.findByText("已购课程").forEach {
                it.logToText()
                val screen = it.getBoundsInScreen()
                /*if (screen.left > 140 && screen.top > 240) {

                }*/
                    OverManager.log("点击已购课程")
                    UIOperate.findParentClickable(it) {
                        it.click()
                        StepManager.execute(this::class.java, Step.STEP_4)
                    }

                return@next
            }
        }.nextLoop(Step.STEP_4) {
            OverManager.log("检查是否进入点击已购课程：剩余时间=${it.loopSurplusSecond}秒")
            UIOperate.findByText("训练营").forEach {
                OverManager.log("已进入已购课程")
                return@nextLoop true
            }
            UIOperate.findByText("全部").forEach {
                OverManager.log("已进入已购课程")
                return@nextLoop true
            }
            if (it.loopSurplusSecond == 0f) {
                OverManager.log("未进入已购课程")
            }
            false
        }
    }
}