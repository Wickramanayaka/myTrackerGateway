package com.proactiveants.mytrackergateway

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.ExecutionException


class MainActivity : AppCompatActivity() {
    private val PERMISSION_REQUEST_CAMERA = 0
    private var previewView: PreviewView? = null
    private var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>? = null
    private lateinit var qrCodeFoundButton: Button
    private var qrCode: String? = null
    private var defaultCameraFacing = CameraSelector.LENS_FACING_BACK
    private lateinit var cameraProvider: ProcessCameraProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        previewView = findViewById(R.id.activity_main_previewView)
        qrCodeFoundButton = findViewById(R.id.activity_main_qrCodeFoundButton)
        qrCodeFoundButton.setVisibility(View.INVISIBLE)
        qrCodeFoundButton.setOnClickListener(View.OnClickListener {
            Toast.makeText(applicationContext, qrCode, Toast.LENGTH_SHORT).show()
            Log.i(
                MainActivity::class.java.simpleName,
                "QR Code Found: $qrCode"
            )
        })
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        requestCamera()

        val change : Button = findViewById(R.id.buttonCahnge)
        change.setOnClickListener {
            defaultCameraFacing = if(defaultCameraFacing== CameraSelector.LENS_FACING_BACK){
                CameraSelector.LENS_FACING_FRONT
            }else{
                CameraSelector.LENS_FACING_BACK
            }
            try {
                cameraProvider.unbindAll()
                startCamera()
            } catch (ex: Exception){

            }
        }
    }

    private fun requestCamera() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startCamera()
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.CAMERA
                )
            ) {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.CAMERA),
                    PERMISSION_REQUEST_CAMERA
                )
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    PERMISSION_REQUEST_CAMERA
                )
            }
        }
    }

    private fun startCamera() {
        cameraProviderFuture!!.addListener({
            try {
                cameraProvider = cameraProviderFuture!!.get()
                bindCameraPreview(cameraProvider)
            } catch (e: ExecutionException) {
                Toast.makeText(this, "Error starting camera ", Toast.LENGTH_SHORT)
                    .show()
            } catch (e: InterruptedException) {
                Toast.makeText(this, "Error starting camera ", Toast.LENGTH_SHORT)
                    .show()
            }
        }, ContextCompat.getMainExecutor(this))
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera()
            } else {
                Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun bindCameraPreview(cameraProvider: ProcessCameraProvider) {
        previewView!!.preferredImplementationMode = PreviewView.ImplementationMode.SURFACE_VIEW
        val preview = Preview.Builder()
            .build()
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(defaultCameraFacing)
            .build()
        preview.setSurfaceProvider(previewView!!.createSurfaceProvider())
        val imageAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(Size(1280, 720))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
        imageAnalysis.setAnalyzer(
            ContextCompat.getMainExecutor(this),
            QRCodeImageAnalyzer(object : QRCodeFoundListener {
                override fun onQRCodeFound(_qrCode: String?) {
                    cameraProvider.unbindAll()
                    qrCode = _qrCode
                    qrCodeFoundButton!!.visibility = View.VISIBLE
                    val intent = Intent(this@MainActivity, DetailActivity::class.java)
                    intent.putExtra("qrCode",qrCode);
                    startActivity(intent)
                    finish()
                }

                override fun qrCodeNotFound() {
                    qrCodeFoundButton!!.visibility = View.INVISIBLE
                }
            })
        )
        val camera = cameraProvider.bindToLifecycle(
            (this as LifecycleOwner),
            cameraSelector,
            imageAnalysis,
            preview
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.list -> {
                startActivity(Intent(this, ListActivity::class.java))
                true
            }
            R.id.about -> {
                startActivity(Intent(this, AboutActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}