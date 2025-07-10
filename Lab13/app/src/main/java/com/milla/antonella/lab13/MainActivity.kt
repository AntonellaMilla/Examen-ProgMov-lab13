package com.milla.antonella.lab13

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.common.util.concurrent.ListenableFuture
import com.milla.antonella.lab13.databinding.ActivityMainBinding
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import androidx.core.net.toUri  // Import added for 'toUri()' function

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraSelector: CameraSelector
    private var imageCapture: ImageCapture? = null
    private lateinit var imgCaptureExecutor: ExecutorService

    private val cameraPermissionResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { permissionGranted ->
            if (permissionGranted) {
                startCamera()
            } else {
                Snackbar.make(
                    binding.root,
                    "The camera permission is necessary",
                    Snackbar.LENGTH_INDEFINITE
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar variables
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        imgCaptureExecutor = Executors.newSingleThreadExecutor()

        // Solicitar permisos de cámara
        cameraPermissionResult.launch(Manifest.permission.CAMERA)

        // Configurar listeners de botones
        binding.imgCaptureBtn.setOnClickListener {
            takePhoto()
        }

        binding.switchBtn.setOnClickListener {
            cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                CameraSelector.DEFAULT_FRONT_CAMERA
            } else {
                CameraSelector.DEFAULT_BACK_CAMERA
            }
            startCamera() // Reiniciar cámara después del cambio de cámara
        }

        binding.galleryBtn.setOnClickListener {
            val intent = Intent(this, GalleryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun startCamera() {
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(binding.preview.surfaceProvider)
        }

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, // lifecycleOwner
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (e: Exception) {
                Log.d(TAG, "Use case binding failed", e)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        imageCapture?.let {
            val fileName = "JPEG_${System.currentTimeMillis()}.jpg"

            // Crear directorio si no existe
            val photoDir = File(getExternalFilesDir(null), "Lab13_Images")
            if (!photoDir.exists()) {
                photoDir.mkdirs()
            }

            val file = File(photoDir, fileName)
            val outputFileOptions = ImageCapture.OutputFileOptions.Builder(file).build()

            it.takePicture(
                outputFileOptions,
                imgCaptureExecutor,
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        Log.i(TAG, "The image has been saved in ${file.toUri()}") // Correct usage of 'toUri()'
                        runOnUiThread {
                            Toast.makeText(
                                this@MainActivity,
                                "Photo saved successfully!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onError(exception: ImageCaptureException) {
                        runOnUiThread {
                            Toast.makeText(
                                this@MainActivity,
                                "Error taking photo",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        Log.d(TAG, "Error taking photo: $exception")
                    }
                }
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        imgCaptureExecutor.shutdown()
    }

    companion object {
        const val TAG = "MainActivity"
    }
}