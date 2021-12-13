package br.com.luishenrique.detectordefaces

import android.app.Activity
import android.content.Context
import android.content.Context.CAMERA_SERVICE
import android.graphics.Bitmap
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.media.Image
import android.net.Uri
import android.os.Build
import android.util.SparseIntArray
import android.view.Surface
import androidx.annotation.RequiresApi
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class MLKitVisionImage {

    private val ORIENTATIONS = SparseIntArray()

    init {
        ORIENTATIONS.append(Surface.ROTATION_0, 0)
        ORIENTATIONS.append(Surface.ROTATION_90, 90)
        ORIENTATIONS.append(Surface.ROTATION_180, 180)
        ORIENTATIONS.append(Surface.ROTATION_270, 270)
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    fun imageFromMediaImage(mediaImage: Image, rotation: Int) {
        val image = InputImage.fromMediaImage(mediaImage, rotation)
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    fun imageFromBitmap(bitmap: Bitmap, rotation: Int): InputImage {
        return InputImage.fromBitmap(bitmap, rotation)
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    suspend fun imageFromUri(context: Context, imageUri: Uri): InputImage? {
        return withContext(Dispatchers.IO) {
            try {
                InputImage.fromFilePath(context, imageUri)
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Throws(CameraAccessException::class)
    private fun getRotationCompensation(
        cameraId: String,
        activity: Activity,
        isFrontFacing: Boolean
    ): Int {

        val deviceRotation = activity.windowManager.defaultDisplay.rotation
        val rotationCompensation = ORIENTATIONS.get(deviceRotation)

        val cameraManager = activity.getSystemService(CAMERA_SERVICE) as CameraManager
        val sensorOrientation = cameraManager
            .getCameraCharacteristics(cameraId)
            .get(CameraCharacteristics.SENSOR_ORIENTATION)!!

        return if (isFrontFacing) {
            (sensorOrientation + rotationCompensation) % 360
        } else {
            (sensorOrientation - rotationCompensation + 360) % 360
        }
    }
}