package com.emirh.yemekkitabi

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PathPermission
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PackageManagerCompat
import com.emirh.yemekkitabi.databinding.FragmentTarifBinding
import com.google.android.material.snackbar.Snackbar
import java.io.IOException
import kotlin.math.sign

// view bindingleri yapmayı unutmuyoruz ve bir tık fragmentlarda farklı oluyor.
class TarifFragment : Fragment() {
    private var _binding: FragmentTarifBinding? = null
    private val binding get() = _binding!!
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    private var secilenGorsel : Uri? = null // urllerde bir uri dır kaynagımızın nerede oldugunu belirtir orn:data/data/media/image.jpg gibi
    private var secilenBitmap : Bitmap?= null //

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerLauncher()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTarifBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageView.setOnClickListener { gorselSec(it) }
        binding.kaydetButton.setOnClickListener { kaydet(it) }
        binding.silButton.setOnClickListener { sil(it) }

        arguments?.let {
            val bilgi = TarifFragmentArgs.fromBundle(it).bilgi

            if (bilgi == "yeni") {
                // yeni tarif eklenecek
                binding.silButton.isEnabled = false
                binding.kaydetButton.isEnabled = true
                binding.isimText.setText("") // isimtextler sadece set text ile duzenliyyebiliyoz
                binding.MalzemeText.setText("")

            } else {
                //eski eklenmemiş tarif gosteriliyor.
                binding.silButton.isEnabled =
                    true // is enabled bunlar butonlara durumlara gore basılabilir veya basılmayabilir yapıyor.etkinlestirme yani
                binding.kaydetButton.isEnabled = false
            }
        }

        // protection level: dangerous yazıyorsa manifest kısımlarında kullanıcaya izinleri sormak zorundayız.


    }

    fun kaydet(view: View) {

    }

    fun sil(view: View) {

    }
    // GERNEL OLRAK BU ASAGIDAKİ KODLARLA İZİNLERLE ALAKALI HERSEYİ HALLETMİŞ OLDUK
    fun gorselSec(view: View) {

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU){
            //!= PackageManager.PERMISSION_GRANTED bu izin verilmediyse demek ,izin istememiz gerekiyor

            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_MEDIA_IMAGES
                ) != PackageManager.PERMISSION_GRANTED
            ){// bu izin daha once alındımı diye kontrol etmemizi saglıyo,PERMİSSİONS GRANTED İZİN VERİLDİ DEMEK
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        Manifest.permission.READ_MEDIA_IMAGES
                    )
                ) {
                    Snackbar.make(
                        view,
                        "Galeriye ulasıp fotograf veya gorsel secmemiz lazım",
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction(
                        "İzin ver",
                        View.OnClickListener {
                            //izin isteyecegiz yine
                            permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)

                        }

                    ).show()                                                                                  // kullanıcı o izni vermediyse, android snack bar ile kullanıcaya izin veriyosan ver artık diyor
                } else {
                    //izin isteyecegiz yine
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                }

            } else{
                // izin verilmis ,galeriye gidebilriz demek zaten
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }


        }else{
            //!= PackageManager.PERMISSION_GRANTED bu izin verilmediyse demek ,izin istememiz gerekiyor

            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ){// bu izin daha once alındımı diye kontrol etmemizi saglıyo,PERMİSSİONS GRANTED İZİN VERİLDİ DEMEK
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    Snackbar.make(
                        view,
                        "Galeriye ulasıp fotograf veya gorsel secmemiz lazım",
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction(
                        "İzin ver",
                        View.OnClickListener {
                            //izin isteyecegiz yine
                            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)

                        }

                    ).show()                                                                                  // kullanıcı o izni vermediyse, android snack bar ile kullanıcaya izin veriyosan ver artık diyor
                } else {
                    //izin isteyecegiz yine
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }

            } else{
                // izin verilmis ,galeriye gidebilriz demek zaten
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }


        }



    }
    private  fun  registerLauncher (){
        // galeriye gidiyoruz onun kodu
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK){
                val intentFromResult = result.data
                if(intentFromResult != null){
                   secilenGorsel= intentFromResult.data // kullanıcının sectigi gorselin nerede kayıtlı oldugunu gosterir.
                    try {
                        if(Build.VERSION.SDK_INT>= 28){ // eski versiyonunda uyumluysa boyle  eskiyse altdaki kod gibi demek yani
                            val source = ImageDecoder.createSource(requireActivity().contentResolver,secilenGorsel!!)
                            secilenBitmap = ImageDecoder.decodeBitmap(source)
                            binding.imageView.setImageBitmap(secilenBitmap)

                        }else{
                            //asagıdaki bitmap yontemi eski yonten yukarıdaki yeni yontem
                            secilenBitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver,secilenGorsel)
                            binding.imageView.setImageBitmap(secilenBitmap)
                        }
                    } catch (e: IOException){ // yukarıdaki islemleri dene eger bir sıkıntı cıkarsa hatayı yakala uygulama cokmesin
                        println(e.localizedMessage) // yerellestirlmis mesaj
                    }
                }



            }
        }
        // izin verme asaması
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ result ->
            if (result) {
                //izin verildi
                //galeriye gidebiliriz
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }else{
                //izin verilmedi
                Toast.makeText(requireContext(),"izin verilmedi!", Toast.LENGTH_LONG).show()

            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}







