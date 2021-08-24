package com.erikagtierrez.multiplemediapicker.fraction;

import com.erikagtierrez.multiplemediapicker.ResourceTable;
import com.erikagtierrez.multiplemediapicker.adapter.CustomAdapter;
import com.erikagtierrez.multiplemediapicker.interfaces.FractionClickListner;
import ohos.aafwk.ability.fraction.Fraction;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.ListContainer;
import ohos.agp.components.TableLayoutManager;
import ohos.agp.render.layoutboost.LayoutBoost;
import ohos.agp.utils.Color;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OneFraction represent the media image picker.
 */
public class OneFraction extends Fraction {

    ComponentContainer layoutScatter;
    Context context;
    Component component;
    FractionClickListner fractionClickListner;
    ImageSelection imageSelect;
    Map<String, List<String>> map ;
    static final HiLogLabel LABEL = new HiLogLabel(HiLog.DEBUG, 0x00201, "one_fragment");
    private List<String> bucketNames = new ArrayList<>();
    private List<String> bitmapList = new ArrayList<>();
    protected static final List<String> imagesList = new ArrayList<>();
    protected static final List<Boolean> selected = new ArrayList<>();

    @Override
    protected Component onComponentAttached(
            LayoutScatter scatter, ComponentContainer container, Intent intent) {
        component = scatter.parse(ResourceTable.Layout_fraction_one, container, false);
        return component;
    }

    public OneFraction(Context context, ComponentContainer layoutScatter) {
        this.context = context;
        this.layoutScatter = layoutScatter;
    }

    @Override
    public Component getComponent() {

        HiLog.info(LABEL, "layoutScatter " + this.layoutScatter);
        HiLog.info(LABEL, "context " + this.context);
        component = LayoutBoost.inflate(
                context, ResourceTable.Layout_fraction_one, this.layoutScatter, false);
        ListContainer listContainer = (ListContainer)
                component.findComponentById(ResourceTable.Id_list);
        bitmapList.clear();
        imagesList.clear();
        bucketNames.clear();
        map = new HashMap<String,List<String>>();
        imageSelect = new ImageSelection(this.layoutScatter);
        map = imageSelect.getPicBuckets();
        bucketNames.addAll(map.get("bucketNames"));
        bitmapList.addAll(map.get("bitmapList"));

        HiLog.info(LABEL, "bitmapList image path :  " + bitmapList.size());
        CustomAdapter mAdapter = new CustomAdapter(
                bitmapList, bucketNames, layoutScatter.getContext());
        listContainer.setBoundaryColor(new Color(0xffdddddd));
        listContainer.setBoundaryThickness(2);
        TableLayoutManager tableLayoutManager = new TableLayoutManager();
        tableLayoutManager.setColumnCount(3);
        listContainer.setLayoutManager(tableLayoutManager);
        listContainer.setItemProvider(mAdapter);
        fractionClickListner = (FractionClickListner) context;
        listContainer.setItemClickedListener((
                ListContainer listContainer1, Component component1, int pos, long l) -> {
                    fractionClickListner.itemClicked("IMAGES", bitmapList, bucketNames);
                });
        return component;
    }
}
