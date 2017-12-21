package com.jimi.smt.eps_appclient.Views;

import android.view.View;

/**
 * 类名:ForegroundToBackgroundTransformer
 * 创建人:Liang GuoChang
 * 创建时间:2017/12/20 15:16
 * 描述:
 * 版本号:
 * 修改记录:
 */

public class ForegroundToBackgroundTransformer extends ABaseTransformer{
    @Override
    protected void onTransform(View page, float position) {
        /*
        page.setPivotX(position < 0 ? 0 : page.getWidth());
        page.setPivotY(page.getHeight() / 2f);
        float scale = position < 0 ? 1f + position : 1f - position;
        page.setScaleX(scale);
        page.setScaleY(scale);
        */
        final float height = page.getHeight();
        final float width = page.getWidth();
        final float scale = min(position > 0 ? 1f : Math.abs(1f + position), 0.5f);

        page.setScaleX(scale);
        page.setScaleY(scale);
        page.setPivotX(width * 0.5f);
        page.setPivotY(height * 0.5f);
        page.setTranslationX(position > 0 ? width * position : -width * position * 0.25f);
    }
}
