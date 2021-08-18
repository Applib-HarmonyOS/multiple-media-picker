/*
 * Copyright (C) 2020-21 Application Library Engineering Group
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

package com.deveco.multiplemediapicker.slice;

import com.deveco.multiplemediapicker.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.bundle.IBundleManager;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.security.SystemPermission;

/**
 *Shows the main page of unified Media picker.
 */

public class MainAbilitySlice extends AbilitySlice {

  Button button;
  static final HiLogLabel LABEL = new HiLogLabel(HiLog.DEBUG, 0x00201, "Multiple_media_picker");

  /**
   *Start Main Choose Media page.
   */

  @Override
    public void onStart(Intent intent) {
    super.onStart(intent);
    super.setUIContent(ResourceTable.Layout_ability_main);

    if (verifySelfPermission(
          SystemPermission.READ_USER_STORAGE) != IBundleManager.PERMISSION_GRANTED) {
      HiLog.info(LABEL, "Media permission getting 1");
      if (canRequestPermission(SystemPermission.READ_USER_STORAGE)) {
        requestPermissionsFromUser(new String[]{SystemPermission.READ_USER_STORAGE}, 0);
        HiLog.info(LABEL, "Media permission getting 2");
      }
    }

    button = (Button) findComponentById(ResourceTable.Id_btn_select_image);
    button.setClickedListener(listener ->
                openGallery());
  }

  public void openGallery() {
    present(new HomeAbilitySlice(), new Intent());
  }

  @Override
    public void onActive() {
    super.onActive();
  }

  @Override
    public void onForeground(Intent intent) {
    super.onForeground(intent);
  }
}
