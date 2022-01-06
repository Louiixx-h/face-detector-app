package br.com.luishenrique.detectordefaces.view

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import br.com.luishenrique.detectordefaces.MLKitVisionImage
import br.com.luishenrique.detectordefaces.R
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.*
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedInputStream
import java.io.InputStream
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    private val mlKitVisionImage = MLKitVisionImage()
    private val detector: FaceDetector
    private val PICK_IMAGE = 5

    init {
        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .setMinFaceSize(0.15f)
            .enableTracking()
            .build()

        detector = FaceDetection.getClient(options)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chooseImage.setOnClickListener {
            intent = Intent(Intent.ACTION_GET_CONTENT);
            intent.type = "image/*";
            startActivityForResult(intent, PICK_IMAGE);
        }

        closeImage.setOnClickListener {
            icon.setImageDrawable(null)
            closeImage.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.menuPhoto -> {
                openCamera()
                true
            }
            else -> true
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun openCamera() {
        with(supportFragmentManager.beginTransaction()) {
            replace(R.id.fragmentContainer, CameraFragment(), TAG_CAMERAFRAGMENT)
            addToBackStack(TAG_CAMERAFRAGMENT)
            commit()
            fragmentContainer.visibility = View.VISIBLE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            if (data == null || data.data == null) {
                return
            } else {
                val inputStream: InputStream? = contentResolver.openInputStream(data.data!!)
                val bitmap = BitmapFactory.decodeStream(BufferedInputStream(inputStream))
                val image = mlKitVisionImage.imageFromBitmap(bitmap, 0)
                processImage(image, bitmap)
            }
        }
    }

    private fun processImage(inputImage: InputImage, bitmap: Bitmap) {
        detector.process(inputImage)
            .addOnSuccessListener { faces ->
                if (faces.isEmpty()) {
                    Toast.makeText(
                        this,
                        "Nenhum rosto identificado.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                drawRectFace(faces, bitmap, icon)
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    "Erro ao reconhecer rosto.",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun drawRectFace(faces: List<Face>, bitmap: Bitmap, imageView: ImageView) {
        val mutableBitmap = bitmap.copy(
            Bitmap.Config.ARGB_8888, true
        )
        for (face in faces) {
            val canvas = Canvas(mutableBitmap)
            val paint = Paint()
            val rect = face.boundingBox
            val decimal = DecimalFormat("###.00")

            paint.style = Paint.Style.STROKE
            paint.color = Color.RED
            paint.strokeWidth = 4f
            paint.isAntiAlias = true
            canvas.drawRect(rect, paint)

            paint.style = Paint.Style.FILL_AND_STROKE
            paint.color = Color.GRAY
            canvas.drawRect(
                Rect(
                    rect.left,
                    rect.bottom + 10,
                    rect.right,
                    rect.bottom + 40
                ),
                paint
            )

            paint.style = Paint.Style.FILL
            paint.color = Color.WHITE
            paint.strokeWidth = 2f
            paint.textSize = 26f
            canvas.drawText(
                "Sorrindo ${decimal.format(face.smilingProbability * 100)}%",
                face.boundingBox.left.toFloat() + 15,
                face.boundingBox.bottom+30f,
                paint
            )
        }
        if (mutableBitmap != null) {
            imageView.setImageBitmap(mutableBitmap)
            closeImage.visibility = View.VISIBLE
        }
    }
}