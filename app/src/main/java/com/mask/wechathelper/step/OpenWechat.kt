package com.mask.wechathelper.assists.simple.step

import android.content.ComponentName
import android.content.Intent
import com.mask.wechathelper.assist.Assists
import com.mask.wechathelper.assist.ext.click
import com.mask.wechathelper.assist.ext.getBoundsInScreen
import com.mask.wechathelper.assist.step.StepCollector
import com.mask.wechathelper.assist.step.StepImpl
import com.mask.wechathelper.assist.step.StepManager
import com.mask.wechathelper.assist.ui.UIOperate
import com.mask.wechathelper.OverManager

class OpenWechat:StepImpl {
    override fun onImpl(collector: StepCollector) {
        collector.next(Step.STEP_1) {
            OverManager.log("启动微信")
            Intent().apply {
                addCategory(Intent.CATEGORY_LAUNCHER)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                component = ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI")
                Assists.service?.startActivity(this)
            }
            StepManager.execute(this::class.java, Step.STEP_2)
        }.nextLoop(Step.STEP_2) {
            OverManager.log("检查是否已打开微信主页：\n剩余时间=${it.loopSurplusSecond}秒")
            UIOperate.findByText("通讯录").forEach {
                val screen = it.getBoundsInScreen()
                if (screen.left > UIOperate.getX(1080, 340) &&
                    screen.top > UIOperate.getX(1920, 1850)
                ) {
                    OverManager.log("已打开微信主页")
                    it.parent.parent.click()
                    StepManager.execute(this::class.java, Step.STEP_3)
                    return@nextLoop true
                }
            }

            if (0f==it.loopSurplusSecond){
                StepManager.execute(this::class.java, Step.STEP_1)
            }

            false
        }
    }
}