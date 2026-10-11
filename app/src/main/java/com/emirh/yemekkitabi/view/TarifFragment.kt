package com.emirh.yemekkitabi.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
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
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.emirh.yemekkitabi.databinding.FragmentTarifBinding
import com.emirh.yemekkitabi.model.Tarif
import com.emirh.yemekkitabi.roomdb.TarifDAO
import com.emirh.yemekkitabi.roomdb.TarifDatabase
import com.google.android.material.snackbar.Snackbar
import java.io.ByteArrayOutputStream
import java.io.IOException

// view bindingleri yapmayı unutmuyoruz ve bir tık fragmentlarda farklı oluyor.
class TarifFragment : Fragment() {
    private var _binding: FragmentTarifBinding? = null
    private val binding get() = _binding!!
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    private var secilenGorsel : Uri? = null // urllerde bir uri dır kaynagımızın nerede oldugunu belirtir orn:data/data/media/image.jpg gibi
    private var secilenBitmap : Bitmap?= null //

    // asagıda database build etmeyi gosteriyor
    private lateinit var  db: TarifDatabase
    private lateinit var  tarifDAO: TarifDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerLauncher()

        // database build ediliyor.
        db = Room.databaseBuilder(requireContext(), TarifDatabase::class.java,"Tarifler").build()
        tarifDAO = db.tarifDao()

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
    // bitmap gorsel kucultme dersi
    fun kaydet(view: View) {
        val isim = binding.isimText.text.toString()
        val malzeme = binding.MalzemeText.toString()
        // asagıdaki kod yuklenen gorselleri dusurmeyecek kadar normal boyutlara getiriyor.
        if(secilenBitmap!=null){
            val kucukBitmap = kucukBitMapOlustur(secilenBitmap!!,300)
            val outputStream = ByteArrayOutputStream()
            kucukBitmap.compress(Bitmap.CompressFormat.PNG,50,outputStream)// cıktıyı nasıl alacagız onu belirliyor
            val byteDizisi = outputStream.toByteArray()

            val tarif = Tarif(isim, malzeme, byteDizisi)
            tarifDAO.insert(tarif)

            // data base kaydetmeye calısıcaz.



        }

    }

    fun sil(view: View) {

    }
    // GENEL OLRAK BU ASAGIDAKİ KODLARLA İZİNLERLE ALAKALI HERSEYİ HALLETMİŞ OLDUK
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
    private fun kucukBitMapOlustur(kullanicininSectigiBitmap: Bitmap,maximumBoyut: Int): Bitmap{
        var widht = kullanicininSectigiBitmap.width
        var height = kullanicininSectigiBitmap.height
        // neden double sectik cunku kusuratlarında hesaplanmasını istiyoruz int secseydik onu atacak kalanı
        val bitmapOrani : Double = widht.toDouble() / height.toDouble() // 1.5 sa 1.5 oranı vermke icin.

        if(bitmapOrani>1 ){
            //gorsel yataysa
            widht= maximumBoyut
            val kisaltilmisYukseklik = widht/bitmapOrani
            height = kisaltilmisYukseklik.toInt()

        }else{
            //görsel Dikey
            height = maximumBoyut
            val kisaltimisGenislik = height * bitmapOrani
            widht = kisaltimisGenislik.toInt()
        }


        return Bitmap.createScaledBitmap(kullanicininSectigiBitmap,  100,  100, true)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}







