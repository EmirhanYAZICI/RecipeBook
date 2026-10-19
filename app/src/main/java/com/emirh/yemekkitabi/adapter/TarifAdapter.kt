package com.emirh.yemekkitabi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.emirh.yemekkitabi.databinding.RecyclerRowBinding
import com.emirh.yemekkitabi.model.Tarif
import com.emirh.yemekkitabi.view.ListeFragmentDirections

class TarifAdapter(val tarifListesi : List<Tarif>) : RecyclerView.Adapter<TarifAdapter.TarifHolder>() {
    class TarifHolder(val binding: RecyclerRowBinding): RecyclerView.ViewHolder(binding.root){ // item view istiyordu


    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TarifHolder {
        val recylerRowBinding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TarifHolder(recylerRowBinding)
    }

    override fun onBindViewHolder(
        holder: TarifHolder,
        position: Int
    ) {
        holder.binding.recyclerViewTextView.text = tarifListesi[position].isim
        holder.itemView.setOnClickListener {
            val action = ListeFragmentDirections.actionListeFragmentToTarifFragment(bilgi = "eski",id = tarifListesi[position].id)
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return tarifListesi.size // kac tane tarif listemiz varsa ona gore olusacak sonucta
    }


}






/*
Adapter, Android geliştirmede veri kaynakları (liste, veri tabanı, API'den gelen veriler) ile bu verileri ekranda gösterecek olan görsel bileşenler (RecyclerView, ListView gibi) arasında bir köprü (aracı) görevi görür.
Temelde şu iki şeyi sağlar:
• Veriyi Dönüştürme: Ham verileri (örneğin bir Product veya User sınıfı listesini), kullanıcının ekranda görebileceği görsel kutucuklara (Layout/View) dönüştürür.
• Bellek ve Performans Yönetimi: Özellikle RecyclerView ile birlikte kullanıldığında, sadece ekranda görünen ögeleri oluşturur. Ekrandan kaybolan ögeleri hafızadan silmek yerine yeni gelen veriler için yeniden kullanır (recycle eder). Bu sayede binlerce satırlık listeler bile telefonu kastırmadan akıcı bir şekilde listelenir.

🛠️ Kotlin'de Nasıl Çalışır? (Özet Örnek)

Kotlin'de en sık kullanılan türü RecyclerView.Adapter'dır. Bir adapter temelde 3 ana fonksiyonu doldurmamızı ister:
1. onCreateViewHolder: Listenin her bir satırı için kullanılacak görsel tasarımı (XML dosyasını) belleğe yükler ve hazırlar.
2. onBindViewHolder: Hazırlanan bu görsel tasarıma, listenin o anki sırasındaki (position) veriyi yerleştirir (Örn: Yazıyı değiştirir, görseli yükler).
3. getItemCount: Listede toplam kaç adet veri olduğunu sisteme söyler
 */