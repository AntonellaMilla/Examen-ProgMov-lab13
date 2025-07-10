package com.milla.antonella.lab13



import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.milla.antonella.lab13.databinding.ActivityGalleryBinding
import java.io.File

class GalleryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGalleryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener lista de imágenes
        val imageList = getImageList()

        // Crear y configurar el adaptador
        val adapter = GalleryAdapter(imageList)
        binding.viewPager.adapter = adapter
    }

    private fun getImageList(): List<File> {
        // Usar el mismo directorio que se usa en MainActivity para guardar las imágenes
        val directory = File(getExternalFilesDir(null), "Lab13_Images")

        return if (directory.exists()) {
            // Filtrar solo archivos de imagen y ordenar por fecha de modificación (más reciente primero)
            directory.listFiles()
                ?.filter { it.isFile && it.name.endsWith(".jpg", ignoreCase = true) }
                ?.sortedByDescending { it.lastModified() }
                ?: emptyList()
        } else {
            emptyList()
        }
    }
}