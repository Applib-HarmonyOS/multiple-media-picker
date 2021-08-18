package com.deveco.multiplemediapicker.adapter;

import com.bumptech.glide.Glide;
import com.deveco.multiplemediapicker.ResourceTable;
import ohos.agp.components.*;
import ohos.agp.render.layoutboost.LayoutBoost;
import ohos.app.Context;
import java.util.ArrayList;
import java.util.List;

/**
 * CustomAdapter represent the media adapter.
 *
 * @since 2021-08-03
 **/

public class CustomAdapter extends BaseItemProvider {
    Context context;
    Image thumbnail;
    Component componentLayout;
    private List<String> imagePath =new ArrayList<>();
    private List<String> imageName =new ArrayList<>();

    public CustomAdapter(List<String>image, List<String> title, Context context) {
        this.imagePath = image;
        this.imageName = title;
        this.context = context;
    }

    @Override
    public int getCount() {
        return imagePath.size();
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
            int i, Component component, ComponentContainer componentContainer) {
        componentLayout = LayoutBoost.inflate(
                context, ResourceTable.Layout_custom_list, null, true);
        Text text = (Text) componentLayout.findComponentById(ResourceTable.Id_text_title);
        text.setText(imageName.get(i));
        thumbnail = (Image) componentLayout.findComponentById(ResourceTable.Id_image);
        Glide.with(context)
                .load(imagePath.get(i))
                .into(thumbnail);
        return componentLayout;
    }
}
