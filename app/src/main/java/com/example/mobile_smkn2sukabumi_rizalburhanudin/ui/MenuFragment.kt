package com.example.mobile_smkn2sukabumi_rizalburhanudin.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.mobile_smkn2sukabumi_rizalburhanudin.InvoiceActivity
import com.example.mobile_smkn2sukabumi_rizalburhanudin.R
import com.example.mobile_smkn2sukabumi_rizalburhanudin.api.Connect
import com.example.mobile_smkn2sukabumi_rizalburhanudin.contracts.Transaksi.ItemTransaksi
import com.example.mobile_smkn2sukabumi_rizalburhanudin.contracts.Transaksi.TransaksiRequest
import com.example.mobile_smkn2sukabumi_rizalburhanudin.model.EntityMenu
import java.text.DecimalFormat
import kotlin.concurrent.thread

class MenuFragment : Fragment() {
    private lateinit var listMenu : RecyclerView
    private var menu : ArrayList<EntityMenu> = ArrayList()
    private var menuFiltered : ArrayList<EntityMenu> = ArrayList()
    private lateinit var searchView : SearchView
    private lateinit var txtTotal : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_menu, container, false)

        listMenu = view.findViewById(R.id.listMenu)
        searchView = view.findViewById(R.id.searchView)
        txtTotal = view.findViewById(R.id.txtTotalHarga)
        val btnBayar = view.findViewById<LinearLayout>(R.id.btnBayar)

        searchView.setOnClickListener {
            searchView.requestFocus()
        }

        searchView.setOnQueryTextListener(object  : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return  false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterMenu(newText)
                return true
            }

            private fun filterMenu(newText: String) {
                menuFiltered.clear()
                menuFiltered.addAll(menu.filter { it.name.lowercase().startsWith(newText.lowercase()) })
                listMenu.adapter?.notifyDataSetChanged()
            }
        })
        btnBayar.setOnClickListener {
            saveTransaksi()
        }
        getFoods()
        return view
    }

    fun saveTransaksi() {
        if (menu.filter { it.qty > 0 }.size < 1) {
            activity?.runOnUiThread {
                Toast.makeText(activity, "Tidak ada data yang dipilih", Toast.LENGTH_SHORT).show()
            }
            return
        }
        thread {
            try {
                val items : ArrayList<ItemTransaksi> = ArrayList()
                menu.filter { it.qty > 0 }.forEach { it ->
                    items.add(
                        ItemTransaksi(
                            it.id,
                            it.qty,
                            it.qty * it.price
                        )
                    )
                }
                val request = TransaksiRequest(
                    items,
                    menu.filter { it.qty > 0 }.sumOf { it.qty * it.price },
                    menu.filter { it.qty > 0 }.sumOf { it.qty }
                    )
                val status = Connect.saveTransaksi(request)

                if (status != null) {
                    activity?.runOnUiThread {
                        Toast.makeText(activity, "Transaksi berhasil disimpan", Toast.LENGTH_SHORT)
                            .show()
                        startActivity(Intent(activity, InvoiceActivity::class.java).apply {
                            putExtra("data", status)
                        })
                        clear()
                    }
                } else {
                    activity?.runOnUiThread {
                        Toast.makeText(activity, "Gagal mennyimpan transaksi", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            } catch (ex : Exception) {
                Log.d("err-saveTransaksi", ex.toString())
            }
        }
    }

    fun clear() {
        for (i in 0 .. menu.size -1 ) {
            menu.get(i).qty = 0
        }
        txtTotal.text = "Rp. 0"
        menuFiltered.clear()
        menuFiltered.addAll(menu)
        listMenu.adapter?.notifyDataSetChanged()
    }

    fun getFoods() {
        thread {
            try {
                val data = Connect.getFoods()

                if (data.size < 1) {
                    activity?.runOnUiThread {
                        Toast.makeText(
                            activity,
                            "Tidak ada data untuk ditampilkan",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    var data = Connect.getFoods()
                    for (i in 0 .. menu.size - 1) {
                        if (menu.get(i).qty > 0) {
                            val x = data.find { it.id == menu.get(i).id }
                            if (x != null) {
                                x.qty = menu.get(i).qty
                            }
                        }
                    }
                    menu.clear()
                    menu.addAll(data)
                    menuFiltered.clear()
                    menuFiltered.addAll(menu)
                    activity?.runOnUiThread {
                        listMenu.adapter = ListMenuAdapter(this, menu, menuFiltered, txtTotal)
                    }
                }
            } catch (ex : Exception) {
                Log.d("err-getFoods", ex.toString())
            }
        }
    }
}

class ListMenuAdapter(
    val menuFragment: MenuFragment,
    val menu: java.util.ArrayList<EntityMenu>,
    val menuFiltered: java.util.ArrayList<EntityMenu>,
    val txtTotal: TextView
) : RecyclerView.Adapter<ListMenuHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListMenuHolder {
        return ListMenuHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ListMenuHolder, position: Int) {
        val data = menuFiltered.get(position)
        val data_real = menu.find { it.id == data.id  }

        with(holder) {
            txtNamaMenu.text = data.name
            txtPrice.text = "Rp. " + DecimalFormat("#,###").format(data.price)
            txtQty.text = data_real?.qty.toString()

            btnAdd.setOnClickListener {
                if (data_real != null) {
                    data_real.qty++
                    txtQty.text = data_real.qty.toString()
                    txtTotal.text = "Rp. " + DecimalFormat("#,###").format(menu.sumOf { it.qty * it.price })
                }
            }

            btnRemove.setOnClickListener {
                if (data_real != null) {
                    if (data_real.qty > 0) {
                        data_real.qty--
                        txtQty.text = data_real.qty.toString()
                        txtTotal.text = "Rp. " + DecimalFormat("#,###").format(menu.sumOf { it.qty * it.price })
                    }
                }
            }

            btnTambahKeranjang.text = data.rating.toString()

            Glide.with(menuFragment)
                .load(data.image)
                .into(imgMenu)
        }
    }

    override fun getItemCount(): Int {
        return menuFiltered.size
    }

}

class ListMenuHolder(itemView : View) : ViewHolder(itemView) {
    val txtNamaMenu = itemView.findViewById<TextView>(R.id.txtInvoiceNum)
    val txtPrice = itemView.findViewById<TextView>(R.id.txtTotalHarga)
    val txtQty = itemView.findViewById<TextView>(R.id.txtQty)
    val imgMenu = itemView.findViewById<ImageView>(R.id.imgMenu)
    val btnAdd = itemView.findViewById<ImageView>(R.id.btnAdd)
    val btnRemove = itemView.findViewById<ImageView>(R.id.btnRemove)
    val btnTambahKeranjang = itemView.findViewById<TextView>(R.id.btnTambahKeranjang)
}
