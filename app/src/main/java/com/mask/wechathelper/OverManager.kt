package com.mask.wechathelper

import android.annotation.SuppressLint
import android.view.LayoutInflater
import androidx.core.widget.NestedScrollView
import com.blankj.utilcode.util.ThreadUtils
import com.blankj.utilcode.util.TimeUtils
import com.mask.wechathelper.assist.Assists
import androidx.core.view.isVisible
import com.mask.wechathelper.assist.step.StepManager
import com.mask.wechathelper.assist.ui.UIOver
import com.mask.wechathelper.assists.simple.step.*
import com.mask.wechathelper.databinding.ViewMainOverBinding

object OverManager : Assists.ListenerManager.StepListener, Assists.ListenerManager.GestureListener {
    @SuppressLint("StaticFieldLeak")
    private var viewMainOver: ViewMainOverBinding? = null
        get() {
            if (field == null) {
                field = Assists.service?.let {
                    Assists.ListenerManager.stepListener.add(this)
                    ViewMainOverBinding.inflate(LayoutInflater.from(it)).apply {
                        llOption.isVisible = true
                        llLog.isVisible = false
                        btnCloseLog.isVisible = false
                        ivClose.setOnClickListener {
                            stop()
                            showOption()
                            mainOver?.hide()

                        }
                        btnOpenZhulongSocial.setOnClickListener {
                            beginStart(this)
                            StepManager.execute(OpenZhuLongSocial::class.java, Step.STEP_1, isBegin = true)
                        }
                        btnOpenSocial.setOnClickListener {
                            beginStart(this)
                            StepManager.execute(OpenWechatSocial::class.java, Step.STEP_1, isBegin = true)
                        }
                        btnPublishSocial.setOnClickListener {
                            beginStart(this)
                            StepManager.execute(PublishSocial::class.java, Step.STEP_1, isBegin = true)
                        }
                        btnStop.setOnClickListener {
                            stop()
                        }
                        btnCloseLog.setOnClickListener { showOption() }
                        btnStopScrollLog.setOnClickListener {
                            isAutoScrollLog = !isAutoScrollLog
                        }
                        btnLog.setOnClickListener {
                            showLog()
                            btnCloseLog.isVisible = true
                            btnStop.isVisible = false
                        }
                        btnScrollContacts.setOnClickListener {
                            beginStart(this)
                            StepManager.execute(ScrollContacts::class.java, Step.STEP_1, isBegin = true)
                        }
                        btnClickBottomTab.setOnClickListener {
                            beginStart(this)
                            StepManager.execute(GestureBottomTab::class.java, Step.STEP_1, isBegin = true)
                        }
                        btnScrollSocial.setOnClickListener {
                            beginStart(this)
                            StepManager.execute(GestureScrollSocial::class.java, Step.STEP_1, isBegin = true)
                        }
                    }
                }
            }
            return field
        }

    private fun beginStart(view: ViewMainOverBinding) {
        with(view) {
            clearLog()
            showLog()
            isAutoScrollLog = true
            btnCloseLog.isVisible = false
            btnStop.isVisible = true
        }
    }

    override fun onGestureBegin(startLocation: FloatArray, endLocation: FloatArray): Long {
        mainOver?.view?.let {
            val viewXY = IntArray(2)
            it.getLocationOnScreen(viewXY)
            if (startLocation[0] >= viewXY[0] &&
                startLocation[0] <= viewXY[0] + it.measuredWidth &&
                startLocation[1] >= viewXY[1] &&
                startLocation[1] <= viewXY[1] + it.measuredHeight
            ) {
                mainOver?.hide()
                return 1000
            }
        }
        return 0
    }

    override fun onGestureEnd() {
        mainOver?.show()
    }

    override fun onStepStop() {
        log("已停止")
    }

    private fun stop() {
        if (StepManager.isStop) {
            showOption()
            return
        }
        StepManager.isStop=true
        isAutoScrollLog = false
        viewMainOver?.btnStop?.isVisible = false
        viewMainOver?.btnCloseLog?.isVisible = true
    }

    fun showLog() {
        viewMainOver?.llOption?.isVisible = false
        viewMainOver?.llLog?.isVisible = true
    }

    fun showOption() {
        viewMainOver?.llOption?.isVisible = true
        viewMainOver?.llLog?.isVisible = false
    }

    @SuppressLint("StaticFieldLeak")
    var mainOver: UIOver? = null
        get() {
            if (field == null) {
                field = viewMainOver?.let {
                    Assists.ListenerManager.gestureListener = this
                    UIOver.Builder(it.root.context, it.root)
                        .setModality(false)
                        .setMoveAble(true)
                        .setAutoAlign(false)
                        .isFirstCenterShow(true)
                        .build()
                }
            }
            return field
        }

    fun clear() {
        mainOver?.remove()
        mainOver = null
        viewMainOver = null
    }

    private val logStr: StringBuilder = StringBuilder()
    fun log(value: Any) {
        if (logStr.length > 1000) logStr.delete(0, 50)
        if (logStr.isNotEmpty()) logStr.append("\n")
        logStr.append(TimeUtils.getNowString())
        logStr.append("\n")
        logStr.append(value.toString())
        viewMainOver?.tvLog?.text = logStr
    }

    fun clearLog() {
        logStr.delete(0, logStr.length)
        viewMainOver?.tvLog?.text = ""
    }

    var isAutoScrollLog = true
        set(value) {
            if (value) onAutoScrollLog()
            viewMainOver?.btnStopScrollLog?.text = if (value) "停止滚动" else "继续滚动"
            field = value
        }

    private fun onAutoScrollLog() {
        viewMainOver?.scrollView?.fullScroll(NestedScrollView.FOCUS_DOWN)
        ThreadUtils.runOnUiThreadDelayed({
            if (!isAutoScrollLog) return@runOnUiThreadDelayed
            onAutoScrollLog()
        }, 250)
    }
}