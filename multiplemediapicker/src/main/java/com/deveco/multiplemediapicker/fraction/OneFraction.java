package com.deveco.multiplemediapicker.fraction;

import com.deveco.multiplemediapicker.ResourceTable;
import com.deveco.multiplemediapicker.adapter.CustomAdapter;
import com.deveco.multiplemediapicker.interfaces.FractionClickListner;
import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.ability.DataAbilityRemoteException;
import ohos.aafwk.ability.fraction.Fraction;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.render.layoutboost.LayoutBoost;
import ohos.agp.utils.Color;
import ohos.app.Context;
import ohos.data.resultset.ResultSet;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.photokit.metadata.AVStorage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * OneFraction represent the media image picker.
 *
 * @since 2021-08-03
 **/

public class OneFraction extends Fraction {

    ComponentContainer layoutScatter;
    Context context;
    Component component;
    FractionClickListner fractionClickListner;

    static final HiLogLabel LABEL = new HiLogLabel(HiLog.DEBUG, 0x00201, "one_fragment");
    private String[] projection = new String[]{
            AVStorage.Images.Media.DISPLAY_NAME, AVStorage.Images.Media.DATA};
    private String[] projection2 = new String[]{
            AVStorage.Images.Media.DISPLAY_NAME, AVStorage.Images.Media.DATA};
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

    private String[] getProjection() {
        return new String[]{
                AVStorage.Images.Media.DATA,
                AVStorage.Images.Media.MIME_TYPE,
                AVStorage.Images.Media.ID,
                AVStorage.Images.Media.DISPLAY_NAME,
                AVStorage.Images.Media.DATE_ADDED
        };
    }

    private void getPicBuckets() {
        HiLog.info(LABEL, "getPicBuckets method called");
        DataAbilityHelper dataAbilityHelper = DataAbilityHelper.creator(layoutScatter.getContext());
        try {
            ResultSet cursor = dataAbilityHelper.query(
                    AVStorage.Images.Media.INTERNAL_DATA_ABILITY_URI, getProjection(), null);
            HiLog.info(LABEL, "getPicBuckets cursor" + cursor);
            if (cursor == null) {

                return;
            }
            if (cursor.getRowCount() == 0) {
                HiLog.info(LABEL, "query: No result found ");
            }

            List<String> bucketNamestemp = new ArrayList<>(cursor.getRowCount());
            List<String> bitmapListtemp = new ArrayList<>(cursor.getRowCount());
            HashSet<String> albumSet = new HashSet<>();
            File file;
            HiLog.info(LABEL, " getPicBuckets " + "getRowCount() :" + cursor.getRowCount());
            if (cursor.goToLastRow()) {
                do {
                    if (Thread.interrupted()) {
                        return;
                    }
                    String album = cursor.getString(cursor.getColumnIndexForName(projection[0]));
                    String image = cursor.getString(cursor.getColumnIndexForName(projection[1]));
                    HiLog.info(LABEL, "getPicBuckets cursor " + "album " + album);
                    HiLog.info(LABEL, "getPicBuckets cursor " + "image " + image);

                    file = new File(image);
                    if (file.exists() && !albumSet.contains(album)) {
                        bucketNamestemp.add(album);
                        bitmapListtemp.add(image);
                        albumSet.add(album);
                    }
                } while (cursor.goToPreviousRow());
            }

            cursor.close();
            bucketNames.clear();
            bitmapList.clear();
            bucketNames.addAll(bucketNamestemp);
            bitmapList.addAll(bitmapListtemp);
            HiLog.info(LABEL, "bucketNamestemp size " + bucketNamestemp.size());
            HiLog.info(LABEL, "bitmapListtemp size " + bitmapListtemp.size());
            HiLog.info(LABEL, "bucketNames size " + bucketNames.size());
            HiLog.info(LABEL, "bitmapList size " + bitmapList.size());
        } catch (DataAbilityRemoteException
                | IllegalStateException | NullPointerException exception) {
            HiLog.info(LABEL, exception.getMessage());
        }
    }

    public void getPictures(String bucketName) {
        selected.clear();
        DataAbilityHelper dataAbilityHelper =
                DataAbilityHelper.creator(layoutScatter.getContext());
        try {
            ResultSet cursor = dataAbilityHelper.query(
                    AVStorage.Images.Media.INTERNAL_DATA_ABILITY_URI, getProjection(), null);
            List<String> imagesTemp = new ArrayList<>(cursor.getRowCount());
            HashSet<String> albumSet = new HashSet<>();
            File file;
            if (cursor.goToLastRow()) {
                do {
                    if (Thread.interrupted()) {
                        return;
                    }
                    String path = cursor.getString(cursor.getColumnIndexForName(projection2[1]));
                    file = new File(path);
                    if (file.exists() && !albumSet.contains(path)) {
                        imagesTemp.add(path);
                        albumSet.add(path);
                        selected.add(false);
                    }
                } while (cursor.goToPreviousRow());
            }
            cursor.close();
            imagesList.clear();
            imagesList.addAll(imagesTemp);
            HiLog.info(LABEL, "imagesTemp size " + imagesTemp.size());
            HiLog.info(LABEL, "imagesList size " + imagesList.size());
        } catch (DataAbilityRemoteException
                | IllegalStateException | NullPointerException exception) {
            HiLog.info(LABEL, "Exception " + exception.getMessage());
        }
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
        getPicBuckets();
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
                    getPictures(bucketNames.get(pos));
                    fractionClickListner.itemClicked("IMAGES", bitmapList, bucketNames);
                });
        return component;
    }
}
