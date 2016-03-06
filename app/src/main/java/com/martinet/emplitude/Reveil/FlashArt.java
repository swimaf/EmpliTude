package com.martinet.emplitude.Reveil;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.util.Log;

/**
 * Created by Arnaud on 06/03/2016.
 */


public class FlashArt {
    public static CameraManager cameraManager;


    @TargetApi(Build.VERSION_CODES.M)
    public void turnOnFlash(Context context) {
        cameraManager = (CameraManager) context.getSystemService(context.CAMERA_SERVICE);

        try {
            for (String id : cameraManager.getCameraIdList()) {

                // Turn on the flash if camera has one
                if (cameraManager.getCameraCharacteristics(id)
                        .get(CameraCharacteristics.FLASH_INFO_AVAILABLE)) {
                    cameraManager.setTorchMode(id, true);
                }
            }
        } catch (CameraAccessException e) {
            Log.e("test", "Failed to interact with camera.", e);

        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public  void turnOffFlash(Context context) {

        try {
            for (String id : cameraManager.getCameraIdList()) {

                // Turn on the flash if camera has one
                if (cameraManager.getCameraCharacteristics(id)
                        .get(CameraCharacteristics.FLASH_INFO_AVAILABLE)) {
                    cameraManager.setTorchMode(id, false);
                }
            }
        } catch (CameraAccessException e) {
            Log.e("test", "Failed to interact with camera.", e);


        }
    }
}