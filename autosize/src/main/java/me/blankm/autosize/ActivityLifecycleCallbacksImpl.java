/*
 * Copyright 2018 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.blankm.autosize;


import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;


/**
 * ================================================
 * {@link ActivityLifecycleCallbacksImpl} 可用来代替在 BaseActivity 中加入适配代码的传统方式
 * {@link ActivityLifecycleCallbacksImpl} 这种方案类似于 AOP, 面向接口, 侵入性低, 方便统一管理, 扩展性强, 并且也支持适配三方库的 {@link Activity}
 * <p>
 * ================================================
 */
public class ActivityLifecycleCallbacksImpl implements Application.ActivityLifecycleCallbacks {
    /**
     * 屏幕适配逻辑策略类
     */
    private AutoAdaptStrategy mAutoAdaptStrategy;
    /**
     * 让 Fragment 支持自定义适配参数
     */
    private FragmentLifecycleCallbacksImplToAndroidx mFragmentLifecycleCallbacksToAndroidx;

    public ActivityLifecycleCallbacksImpl(AutoAdaptStrategy autoAdaptStrategy) {
        mFragmentLifecycleCallbacksToAndroidx = new FragmentLifecycleCallbacksImplToAndroidx(autoAdaptStrategy);
        mAutoAdaptStrategy = autoAdaptStrategy;
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, Bundle savedInstanceState) {
        if (AutoSizeConfig.getInstance().isCustomFragment()) {
            if (mFragmentLifecycleCallbacksToAndroidx != null && activity instanceof androidx.fragment.app.FragmentActivity) {
                ((androidx.fragment.app.FragmentActivity) activity).getSupportFragmentManager().registerFragmentLifecycleCallbacks(mFragmentLifecycleCallbacksToAndroidx, true);
                ((androidx.fragment.app.FragmentActivity) activity).getSupportFragmentManager().registerFragmentLifecycleCallbacks(mFragmentLifecycleCallbacksToAndroidx, true);
            }
        }

        //Activity 中的 setContentView(View) 一定要在 super.onCreate(Bundle); 之后执行
        if (mAutoAdaptStrategy != null) {
            mAutoAdaptStrategy.applyAdapt(activity, activity);
        }
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        if (mAutoAdaptStrategy != null) {
            mAutoAdaptStrategy.applyAdapt(activity, activity);
        }
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }

    /**
     * 设置屏幕适配逻辑策略类
     *
     * @param autoAdaptStrategy {@link AutoAdaptStrategy}
     */
    public void setAutoAdaptStrategy(AutoAdaptStrategy autoAdaptStrategy) {
        mAutoAdaptStrategy = autoAdaptStrategy;
        if (mFragmentLifecycleCallbacksToAndroidx != null) {
            mFragmentLifecycleCallbacksToAndroidx.setAutoAdaptStrategy(autoAdaptStrategy);
        }
    }
}
