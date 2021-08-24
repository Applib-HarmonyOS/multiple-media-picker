package com.erikagtierrez.multiplemediapicker.slice;

import com.erikagtierrez.multiplemediapicker.ResourceTable;
import com.erikagtierrez.multiplemediapicker.adapter.BucketAdapter;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.Image;
import ohos.agp.components.ListContainer;
import ohos.agp.components.TableLayoutManager;
import ohos.agp.components.Text;
import ohos.agp.components.element.Element;
import ohos.agp.components.element.ElementScatter;
import ohos.agp.utils.Color;
import java.util.ArrayList;
import java.util.List;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

/**
 * OpenGalleryAbilitySlice represent the selected media picker.
 */
public class OpenGalleryAbilitySlice extends AbilitySlice {

    private int maxSelection = 3;
    static final HiLogLabel LABEL =
            new HiLogLabel(HiLog.DEBUG, 0x00201, "Multiple_media_picker_open_gallery");
    private ListContainer listContainer;
    protected static List<Boolean> selected = new ArrayList<>();
    protected static List<String> imagesSelected = new ArrayList<>();
    Text appBarTitle;
    String parent;
    private List<String> bucketNames = new ArrayList<>();
    private List<String> bitmapList = new ArrayList<>();

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_open_gallery);
        initAppBar();
        appBarTitle = (Text) findComponentById(ResourceTable.Id_appBar_title);
        listContainer = (ListContainer) findComponentById(ResourceTable.Id_list);
        bitmapList.clear();
        selected.clear();

        if (imagesSelected.isEmpty() && imagesSelected.size() == 0) {
            HiLog.info(LABEL, "  List is empty");
        } else {
            appBarTitle.setText(String.valueOf(imagesSelected.size()));
            HiLog.info(LABEL, "  imagesSelected.size() :" + imagesSelected.size());
        }
        HiLog.info(LABEL, "bitmapList size :" + bitmapList.size() + " : " + selected.size());
        parent = intent.getStringParam("FROM");
        bucketNames = intent.getStringArrayListParam("bucketNames");
        bitmapList = intent.getStringArrayListParam("bitmapList");
       if(!bitmapList.isEmpty()) {
           for (int i = 0; i < bitmapList.size(); i++) {
               selected.add(false);
           }
           populateListContainer();
       }
    }

    public void populateListContainer() {
        HiLog.info(LABEL, " bitmapList.size() :"   + bitmapList.size());
        for (int i = 0; i < selected.size(); i++) {
            boolean result = imagesSelected.contains(bitmapList.get(i)) ? true : false;
            selected.set(i,result);
        }
        HiLog.info(LABEL, "BucketAdapter pass :"  + "bucketNames :" + bucketNames +
                "bitmapList :" + bitmapList);
        BucketAdapter mAdapter = new BucketAdapter(bitmapList, bucketNames, selected, getContext());
        HiLog.info(LABEL, "BucketAdapter after pass :"
                + "bucketNames :" + bucketNames + "bitmapList :" + bitmapList);

        listContainer.setBoundaryColor(new Color(0xffdddddd));
        listContainer.setBoundaryThickness(2);
        TableLayoutManager tableLayoutManager = new TableLayoutManager();
        tableLayoutManager.setColumnCount(3);
        listContainer.setLayoutManager(tableLayoutManager);
        listContainer.setItemProvider(mAdapter);

        listContainer.setItemClickedListener
                ((ListContainer container, Component component1, int position, long l) -> {
            if (!selected.get(position).equals(true)
                    && imagesSelected.size() < maxSelection) {
                imagesSelected.add(bitmapList.get(position));
                selected.set(position, !selected.get(position));
                mAdapter.notifyDataChanged();
            } else if (selected.get(position).equals(true)
                    &&  imagesSelected.contains(bitmapList.get(position))) {
                    imagesSelected.remove(bitmapList.get(position));
                    selected.set(position, !selected.get(position));
                    mAdapter.notifyDataChanged();
            }
            HomeAbilitySlice.selectionTitle = imagesSelected.size();
            if (imagesSelected.size() != 0) {
                appBarTitle.setText(String.valueOf(imagesSelected.size()));
                HiLog.info(LABEL, " String.valueOf(imagesSelected.size()) :" + imagesSelected.size());
            } else {
                appBarTitle.setText(HomeAbilitySlice.title);
            }
        });

    }

    private void initAppBar() {
        DirectionalLayout backButton = (DirectionalLayout)
                findComponentById(ResourceTable.Id_appBar_backButton_touchTarget);
        Image backButtonImage = (Image) findComponentById(ResourceTable.Id_appBar_backButton);
        if (backButtonImage.getLayoutDirectionResolved()
                == Component.LayoutDirection.RTL) {
            Element buttonImage = ElementScatter.getInstance(this).parse(
                    ResourceTable.Graphic_ic_back_mirror);
            backButtonImage.setImageElement(buttonImage);
        }
        backButton.setClickedListener(component -> onBackPressed());
    }
}
