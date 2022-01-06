package br.com.luishenrique.detectordefaces.view

import android.os.Bundle
import android.view.View
import androidx.camera.core.ImageCapture
import androidx.fragment.app.Fragment
import br.com.luishenrique.detectordefaces.R
import kotlinx.android.synthetic.main.fragment_camera.*
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

const val TAG_CAMERAFRAGMENT = "camera"

class CameraFragment : Fragment(R.layout.fragment_camera) {

    private var imageCapture: ImageCapture? = null
    private lateinit var outPutDirectory: File
    private lateinit var cameraExecutor: ExecutorService

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requestPermission()

        outPutDirectory = getOutPutDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()

        takePhoto.setOnClickListener { takePhoto() }
    }

    private fun takePhoto() {
        if (!requestPermission()) return

    }

    private fun requestPermission(): Boolean {
        return false
    }

    private fun getOutputDirectory (): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it , resources.getString(R.string.app_name)).apply { mkdirs() }
        }

        return if (mediaDir != null && mediaDir.exists ()) mediaDir
        else filesDir
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}