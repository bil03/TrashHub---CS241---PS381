package com.neisha.trashhub.view.Pickup
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.LatLng
import com.neisha.trashhub.databinding.ActivityPickupBinding
import com.neisha.trashhub.view.response.CreatePickupResponse
import java.util.*

class PickupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPickupBinding
    private val pickupViewModel: PickupViewModel by viewModels()

    private var currentImageUri: Uri? = null

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPickupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        setupObservers()

        if (!allPermissionsGranted()) {
            requestCameraPermission()
        }
    }

    private fun setupUI() {
        binding.editButton.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivityForResult(intent, MAP_REQUEST_CODE)
        }

        binding.photoIcon.setOnClickListener {
            openPhotoOptions()
        }

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.saveButton.setOnClickListener {
            val weight = binding.weightInput.text.toString()
            val phone = binding.phoneNumber.text.toString()
            val address = binding.addressInput.text.toString()
            val additionalInfo = binding.additionalInfo.text.toString()

            pickupViewModel.setWeight(weight)
            pickupViewModel.setPhoneNumber(phone)
            pickupViewModel.setAddress(address)
            pickupViewModel.setAdditionalInfo(additionalInfo)

            pickupViewModel.createPickup()
        }

        binding.weightInput.setOnFocusChangeListener { _, _ ->
            pickupViewModel.setWeight(binding.weightInput.text.toString())
        }

        binding.phoneNumber.setOnFocusChangeListener { _, _ ->
            pickupViewModel.setPhoneNumber(binding.phoneNumber.text.toString())
        }

        binding.addressInput.setOnFocusChangeListener { _, _ ->
            pickupViewModel.setAddress(binding.addressInput.text.toString())
        }

        binding.additionalInfo.setOnFocusChangeListener { _, _ ->
            pickupViewModel.setAdditionalInfo(binding.additionalInfo.text.toString())
        }
    }

    private fun setupObservers() {
        pickupViewModel.weight.observe(this, { weight ->
            binding.weightInput.setText(weight)
        })

        pickupViewModel.phoneNumber.observe(this, { phoneNumber ->
            binding.phoneNumber.setText(phoneNumber)
        })

        pickupViewModel.address.observe(this, { address ->
            binding.addressInput.setText(address)
        })

        pickupViewModel.additionalInfo.observe(this, { additionalInfo ->
            binding.additionalInfo.setText(additionalInfo)
        })

        pickupViewModel.apiResponse.observe(this, { response ->
            response?.let {
                if (!it.error!!) {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun openPhotoOptions() {
        val options = arrayOf("Take Photo", "Choose from Gallery")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Option")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> startCamera()
                1 -> startGallery()
            }
        }
        builder.show()
    }

    private fun startGallery() {
        launcherGallery.launch("image/*")
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Toast.makeText(this, "No media selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getImageUri(this))
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MAP_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val selectedLatLng = data?.getParcelableExtra<LatLng>("selected_latlng")
            selectedLatLng?.let {
                val geocoder = Geocoder(this, Locale.getDefault())
                try {
                    val addresses = geocoder.getFromLocation(selectedLatLng.latitude, selectedLatLng.longitude, 1)
                    if (addresses != null && addresses.isNotEmpty()) {
                        val address = addresses[0].getAddressLine(0)
                        pickupViewModel.setAddress(address)
                        binding.addressInput.setText(address)
                    } else {
                        Toast.makeText(this, "Address not found", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "Geocoder error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        } else if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            currentImageUri?.let {
                showImage()
            }
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.photoIcon.setImageURI(it)
        }
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            REQUEST_CAMERA_PERMISSION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 1001
        private const val MAP_REQUEST_CODE = 123
        private const val CAMERA_REQUEST_CODE = 456
    }
}
