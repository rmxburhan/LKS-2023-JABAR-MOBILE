package com.example.mobile_smkn2sukabumi_rizalburhanudin

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.mobile_smkn2sukabumi_rizalburhanudin.contracts.Transaksi.TransaksiResponse
import com.example.mobile_smkn2sukabumi_rizalburhanudin.databinding.ActivityInvoiceBinding
import java.io.File
import java.text.DecimalFormat
import java.time.LocalDateTime

class InvoiceActivity : AppCompatActivity() {
    private lateinit var binding : ActivityInvoiceBinding
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invoice)

        binding = ActivityInvoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.getParcelableExtra<TransaksiResponse>("data")

        if (data != null) {
            binding.listInvoice.adapter = ListInvoiceAdapter(this, data)
        }

        binding.btnSave.setOnClickListener {
            try {
                val bitmap = viewToBitmap(findViewById(R.id.invoicePng))
                val file = bitmapToFIle(bitmap, (data?.invoice_num ?: LocalDateTime.now().toString()) + ".png")
               startActivity(Intent().apply {
                   action = Intent.ACTION_VIEW
                   addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                   setDataAndType(FileProvider.getUriForFile(this@InvoiceActivity, "lks.provider", file), "image/*")
               })
            }catch (ex : Exception) {
                Log.d("err-save", ex.toString())
            }
        }

        binding.btnShare.setOnClickListener {
            try {
                val bitmap = viewToBitmap(findViewById(R.id.invoicePng))
                val file = bitmapToFIle(bitmap, (data?.invoice_num ?: LocalDateTime.now().toString()) + ".png")
                startActivity(Intent().apply {
                    action = Intent.ACTION_SEND
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(this@InvoiceActivity, "lks.provider", file))
                    type = "image/*"
                })
            }catch (ex : Exception) {
                Log.d("err-share", ex.toString())
            }
        }

        binding.btnSelesai.setOnClickListener {
            finish()
        }

        if (data != null) {
            binding.txtTotalHarga.text = "Rp. " + DecimalFormat("#,###").format(data.price_total)
        }
    }

    fun viewToBitmap(view : View) : Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    fun bitmapToFIle(bitmap: Bitmap, filename : String) : File {
        val file : File = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), filename)
        Log.d("file-path", file.path.toString())
        try {
            file.createNewFile()
            file.outputStream().run {
                bitmap.compress(Bitmap.CompressFormat.PNG, 85, this)
                flush()
                close()
            }
            return  file
        } catch (ex : Exception) {
            throw ex
        }
    }
}

class ListInvoiceAdapter(val invoiceActivity: InvoiceActivity,val data: TransaksiResponse) : RecyclerView.Adapter<ListInvoiceHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListInvoiceHolder {
        return ListInvoiceHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_invoice, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ListInvoiceHolder, position: Int) {
        val x = data.invoice_details.get(position)

        with(holder) {
            txtNama.text = x.product.name
            txtHarga.text = "Rp. " + DecimalFormat("#,###").format(x.product.price)
            txtQty.text = x.qty.toString()
            txtSubtotal.text = DecimalFormat("#,###").format(x.qty * x.product.price)
        }
    }

    override fun getItemCount(): Int {
        return data.invoice_details.size
    }

}

class ListInvoiceHolder(itemView : View) : ViewHolder(itemView){
    val txtNama = itemView.findViewById<TextView>(R.id.txtNama)
    val txtHarga = itemView.findViewById<TextView>(R.id.txtTotalHarga)
    val txtQty = itemView.findViewById<TextView>(R.id.txtQty)
    val txtSubtotal = itemView.findViewById<TextView>(R.id.txtSubtotal)
}
