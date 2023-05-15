package com.example.mobile_smkn2sukabumi_rizalburhanudin.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.mobile_smkn2sukabumi_rizalburhanudin.R
import com.example.mobile_smkn2sukabumi_rizalburhanudin.api.Connect
import com.example.mobile_smkn2sukabumi_rizalburhanudin.contracts.Invoices.TransactionInvoices
import java.text.DecimalFormat
import kotlin.concurrent.thread


class TransactionFragment : Fragment() {
    private lateinit var listInvoices : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_transaction, container, false)
        listInvoices = view.findViewById(R.id.listInvoiceTransactions)
        getInvoices()
        return view
    }

    private fun getInvoices() {
        thread {
            try {
                val data = Connect.getInvoices()

                if (data.size < 1) {
                    activity?.runOnUiThread {
                        Toast.makeText(
                            activity,
                            "TIdak ada data untuk ditampilkan",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    activity?.runOnUiThread {
                        listInvoices.adapter = ListInvoicesTransactionAdapter(this, data)
                    }
                }
            } catch (ex : Exception) {
                Log.d("err-getInvoices", ex.toString())
            }
        }
    }
}

class ListInvoicesTransactionAdapter(
    val transactionFragment:
     TransactionFragment,
    val data: ArrayList<TransactionInvoices>
) : RecyclerView.Adapter<ListInvoicesTransactionHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListInvoicesTransactionHolder {
        return ListInvoicesTransactionHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_transactins, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ListInvoicesTransactionHolder, position: Int) {
        val myData = data.get(position)

        with(holder) {
            txtInvoiceNum.text = myData.invoice_num
            txtTanggal.text = "Tanggal transaksi : " + myData.created_at.split('T')[0]
            txtTotalPembayaran.text = "Total pembayaran Rp. " + DecimalFormat("#,###").format(myData.price_total)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

}

class ListInvoicesTransactionHolder(itemView : View) : ViewHolder(itemView) {
    val txtInvoiceNum : TextView = itemView.findViewById(R.id.txtInvoiceNum)
    val txtTotalPembayaran : TextView = itemView.findViewById(R.id.txtTotalHarga)
    val txtTanggal : TextView = itemView.findViewById(R.id.txtTanggal)
}
