package com.deveco.multiplemediapicker.fraction;

import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.ability.DataAbilityRemoteException;
import ohos.agp.components.ComponentContainer;
import ohos.data.resultset.ResultSet;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.photokit.metadata.AVStorage;
import java.io.File;
import java.util.*;

/**
 * OneFraction represent the media image picker.
 *
 * @since 2021-08-03
 **/

public class ImageSelection {

    public int a = 11;
    ComponentContainer layoutScatter;
    static final HiLogLabel LABEL = new HiLogLabel(HiLog.DEBUG, 0x00201, "ImageSelection");
    private String[] projection = new String[]{
            AVStorage.Images.Media.DISPLAY_NAME, AVStorage.Images.Media.DATA};
    private List<String> bucketNames = new ArrayList<>();
    private List<String> bitmapList = new ArrayList<>();

    public ImageSelection(ComponentContainer layoutScatter) {
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

    public Map<String,List<String>> getPicBuckets() {
     Map<String,List<String>> map =new HashMap();
        HiLog.info(LABEL, "getPicBuckets method called");
        DataAbilityHelper dataAbilityHelper = DataAbilityHelper.creator(layoutScatter.getContext());
        try {
            ResultSet cursor = dataAbilityHelper.query(
                    AVStorage.Images.Media.INTERNAL_DATA_ABILITY_URI, getProjection(), null);
            if (cursor == null) {
                return null;
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
                        return null;
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
            HiLog.info(LABEL, "bitmapList size Sample" + bitmapList.size());
            map.put("bucketNames",bucketNames);
            map.put("bitmapList",bitmapList);
        } catch (DataAbilityRemoteException
                | IllegalStateException | NullPointerException exception) {
            HiLog.info(LABEL, exception.getMessage());
        }
        HiLog.info(LABEL, "Map size " +map.get("bitmapList"));

        return map;
    }

}
