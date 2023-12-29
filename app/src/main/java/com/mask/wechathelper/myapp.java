package com.mask.wechathelper;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.mask.wechathelper.assist.step.StepManager;
import com.mask.wechathelper.assists.simple.step.GestureBottomTab;
import com.mask.wechathelper.assists.simple.step.GestureScrollSocial;
import com.mask.wechathelper.assists.simple.step.OpenWechatSocial;
import com.mask.wechathelper.assists.simple.step.OpenZhuLongSocial;
import com.mask.wechathelper.assists.simple.step.PublishSocial;
import com.mask.wechathelper.assists.simple.step.ScrollContacts;

/**
 * 应用模块:
 * <p>
 * 类描述:
 * <p>
 *
 * @since: clx
 * @date: 2023/12/27
 */
public class myapp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        StepManager.INSTANCE.register(OpenWechatSocial.class);
        StepManager.INSTANCE.register(OpenZhuLongSocial.class);
        StepManager.INSTANCE.register(PublishSocial.class);
        StepManager.INSTANCE.register(ScrollContacts.class);
        StepManager.INSTANCE.register(GestureBottomTab.class);
        StepManager.INSTANCE.register(GestureScrollSocial.class);
    }
}
