package com.deveco.multiplemediapicker.adapter;

import com.bumptech.glide.Glide;
import com.deveco.multiplemediapicker.ResourceTable;
import ohos.agp.components.BaseItemProvider;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.Image;
import ohos.agp.render.layoutboost.LayoutBoost;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import java.util.List;

/**
 * BucketAdapter represent the media adapter.
 *
 * @since 2021-08-03
 **/

public class BucketAdapter extends BaseItemProvider {
    private  Context context;
     Image thumbnail;
     Image selectIcon;
     Component componentLayout;
    private List<String> bitmapList;
    private List<String> bitmapName;
    private List<Boolean> selected;
    static final HiLogLabel LABEL = new HiLogLabel(
            HiLog.DEBUG, 0x00201, "BucketAdapter");
    public BucketAdapter(
            List<String> image, List<String> bitName, List<Boolean> select, Context context) {
        this.bitmapList = bitName;
        this.bitmapName = image;
        this.selected = select;
        this.context = context;
    }

    @Override
    public int getCount() {
        return bitmapList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Component getComponent(
            int position, Component component, ComponentContainer componentContainer) {
        componentLayout = LayoutBoost.inflate(
                context, ResourceTable.Layout_bucket_list, null, true);
        thumbnail = (Image) componentLayout.findComponentById(ResourceTable.Id_image);
        selectIcon = (Image) componentLayout.findComponentById(ResourceTable.Id_image2);
        HiLog.info(LABEL, "bitmapList :" + bitmapList.get(position) + " : " + bitmapName);
        Glide.with(context)
                .load(bitmapName.get(position))
                .into(thumbnail);
        if (selected.get(position).equals(true)) {
            selectIcon.setVisibility(Component.VISIBLE);
        } else {
            selectIcon.setVisibility(Component.INVISIBLE);
        }
        return componentLayout;
    }
}
