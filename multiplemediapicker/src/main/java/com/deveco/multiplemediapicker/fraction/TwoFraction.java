package com.deveco.multiplemediapicker.fraction;

import com.deveco.multiplemediapicker.ResourceTable;
import com.deveco.multiplemediapicker.interfaces.FractionClickListner;
import ohos.aafwk.ability.fraction.Fraction;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.render.layoutboost.LayoutBoost;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TwoFraction  represent the media Video picker.
 *
 * @since 2021-08-03
 */

public class TwoFraction extends Fraction {

    ComponentContainer layoutScatter;
    Context context;
    Component component;
    ImageSelection imageSelect;
    ImageSelection imagePic;
    static final HiLogLabel LABEL = new HiLogLabel(HiLog.DEBUG, 0x00201, "Multiple_media_picker");
    private List<String> bucketNames = new ArrayList<>();
    private List<String> bitmapList = new ArrayList<>();
    protected static final List<String> imagesList = new ArrayList<>();
    protected static final List<Boolean> selected = new ArrayList<>();
    FractionClickListner fractionClickListner;

    @Override
    protected Component onComponentAttached(
            LayoutScatter scatter, ComponentContainer container, Intent intent) {
        component = scatter.parse(ResourceTable.Layout_fraction_one, container, false);
        return component;
    }

    public TwoFraction(Context context, ComponentContainer layoutScatter) {
        this.context = context;
        this.layoutScatter = layoutScatter;
    }

    @Override
    public Component getComponent() {
        imagePic = new ImageSelection(this.layoutScatter);
        HiLog.info(LABEL, "layoutScatter " + this.layoutScatter);
        HiLog.info(LABEL, "context " + this.context);
        component = LayoutBoost.inflate(
                context, ResourceTable.Layout_fraction_one, this.layoutScatter, false);
        bitmapList.clear();
        imagesList.clear();
        bucketNames.clear();
        HashMap<String, List<String>> map;
        imageSelect = new ImageSelection(this.layoutScatter);
        map = (HashMap<String, List<String>>) imageSelect.getPicBuckets();
        if (map != null) {
            bucketNames.addAll(map.get("bucketNames"));
            bitmapList.addAll(map.get("bitmapList"));
        }
        return component;
    }
}
