package com.emirh.yemekkitabi.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.room.Room
import com.emirh.yemekkitabi.ListeFragmentDirections
import com.emirh.yemekkitabi.databinding.FragmentListe2Binding
import com.emirh.yemekkitabi.roomdb.TarifDAO
import com.emirh.yemekkitabi.roomdb.TarifDatabase

// recycler view bize vertical dikey bir sekilde yeni bir tarif eklendigi zaman geridonmeli
// bir sekilde satır haline getiren id si verilmis lazım olan bir kod layout resourcesflie dan ekliyoruz
class ListeFragment : Fragment() {

    private var _binding: FragmentListe2Binding? = null
    private val binding get() = _binding!!
    private lateinit var  db: TarifDatabase
    private lateinit var  tarifDAO: TarifDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = Room.databaseBuilder(requireContext(), TarifDatabase::class.java,"Tarifler").build()
        tarifDAO = db.tarifDao()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListe2Binding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { // her zaman sonradan fragmentlarda calısırken onviewcreated sonradan yazılır.
        super.onViewCreated(view, savedInstanceState)
        binding.floatingActionButton.setOnClickListener { yeniEkle(it) }


    }

    fun yeniEkle(view:View){ // bunu yapsakda dahi onclick metodunda onviewcreated kısmına floating action vermemiz lazım
        val action = ListeFragmentDirections.actionListeFragmentToTarifFragment(bilgi="yeni",id=0) // fragmentda arg vermistikya onları istedi bak
        Navigation.findNavController(view).navigate(action)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
