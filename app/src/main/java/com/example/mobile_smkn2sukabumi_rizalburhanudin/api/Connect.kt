package com.example.mobile_smkn2sukabumi_rizalburhanudin.api

import com.example.mobile_smkn2sukabumi_rizalburhanudin.contracts.Invoices.TransactionInvoices
import com.example.mobile_smkn2sukabumi_rizalburhanudin.contracts.Transaksi.InvoiceDetail
import com.example.mobile_smkn2sukabumi_rizalburhanudin.contracts.auth.LoginRequest
import com.example.mobile_smkn2sukabumi_rizalburhanudin.contracts.auth.RegisterRequest
import com.example.mobile_smkn2sukabumi_rizalburhanudin.contracts.Transaksi.TransaksiRequest
import com.example.mobile_smkn2sukabumi_rizalburhanudin.contracts.Transaksi.TransaksiResponse
import com.example.mobile_smkn2sukabumi_rizalburhanudin.model.EntityMenu
import com.example.mobile_smkn2sukabumi_rizalburhanudin.model.EntityUserData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class Connect {
    companion object {
        const val BASE_URL = "http://103.67.187.184/"
        var token = ""
        fun login(request : LoginRequest) : Boolean {
            val url_link = URL("${BASE_URL}api/login")

            with(url_link.openConnection() as HttpURLConnection) {
                requestMethod = "POST"
                setRequestProperty("Accept", "application/json")
                setRequestProperty("Content-Type", "application/json")

                val gson = Gson()
                with(outputStream.bufferedWriter()) {
                    write(gson.toJson(request).toString().trim())
                    flush()
                }

                when(responseCode) {
                    200 -> {
                        val response = JSONObject(inputStream.bufferedReader().readText())
                        token = response["data"].toString()
                        return true
                    } else -> {
                        return false
                    }
                }
            }
        }

        fun register(registerRequest: RegisterRequest): Boolean {
            val url_link = URL("${BASE_URL}api/register")

            with(url_link.openConnection() as HttpURLConnection) {
                requestMethod = "POST"
                setRequestProperty("Accept", "application/json")
                setRequestProperty("Content-Type", "application/json")

                val gson = Gson()
                with(outputStream.bufferedWriter()) {
                    write(gson.toJson(registerRequest).toString().trim())
                    flush()
                }

                when(responseCode) {
                    201 -> {
                      return  true
                    } else -> {
                     return  false
                   }
                }
            }
        }

        fun getFoods() : ArrayList<EntityMenu> {
            val url_link = URL("${BASE_URL}api/products")

            with(url_link.openConnection() as HttpURLConnection) {
                requestMethod = "GET"
                setRequestProperty("Accept", "application/json")
                setRequestProperty("Content-Type", "application/json")
                setRequestProperty("Authorization", "Bearer " + token)

                val gson = Gson()
                val entityMenu : ArrayList<EntityMenu> = ArrayList()
                when(responseCode) {
                    200 -> {
                        val response = JSONObject(inputStream.bufferedReader().readText())
                        val data = response.getJSONArray("data")
                        val myType = object : TypeToken<ArrayList<EntityMenu>>() {}.type
                        entityMenu.addAll(gson.fromJson(data.toString(), myType))
                        return  entityMenu
                    } else -> {
                        return entityMenu
                    }
                }
            }
        }

        fun getInvoices() : ArrayList<TransactionInvoices> {
            val url_link = URL("${BASE_URL}api/invoices")

            with(url_link.openConnection() as HttpURLConnection) {
                requestMethod = "GET"
                setRequestProperty("Accept", "application/json")
                setRequestProperty("Content-Type", "application/json")
                setRequestProperty("Authorization", "Bearer " + token)

                val gson = Gson()
                val entity : ArrayList<TransactionInvoices> = ArrayList()
                when(responseCode) {
                    200 -> {
                        val response = JSONObject(inputStream.bufferedReader().readText())
                        val data = response.getJSONArray("data")
                        entity.clear()
                        val myType = object  : TypeToken<ArrayList<TransactionInvoices>>() {}.type
                        entity.addAll(gson.fromJson(data.toString(), myType))
                        return entity
                    } else -> {
                    return entity
                }
                }
            }
        }

        fun logout() : Boolean {
            val url_link = URL("${BASE_URL}api/logout")

            with(url_link.openConnection() as HttpURLConnection) {
                requestMethod = "GET"
                setRequestProperty("Accept", "application/json")
                setRequestProperty("Content-Type", "application/json")
                setRequestProperty("Authorization", "Bearer " + token)

                when(responseCode) {
                    200 -> {
                        return true
                    } else -> {
                    return false
                }
                }
            }
        }

        fun saveTransaksi(request : TransaksiRequest): TransaksiResponse? {
            val url_link = URL("${BASE_URL}api/store-invoice")

            with(url_link.openConnection() as HttpURLConnection) {
                requestMethod = "POST"
                setRequestProperty("Accept", "application/json")
                setRequestProperty("Content-Type", "application/json")
                setRequestProperty("Authorization", "Bearer " + token)

                val gson = Gson()
                with(outputStream.bufferedWriter()) {
                    write(gson.toJson(request).toString().trim())
                    flush()
                }

                when(responseCode) {
                    201 -> {
                        val response = JSONObject(inputStream.bufferedReader().readText())
                        val data = response.getJSONObject("data")
                        val entity = gson.fromJson(data.toString(), TransaksiResponse::class.java)
                        return entity
                    } else -> {
                        return null
                    }
                }
            }
        }

        fun getProfile(): EntityUserData? {
            val url_link = URL("${BASE_URL}api/user")

            with(url_link.openConnection() as HttpURLConnection) {
                requestMethod = "GET"
                setRequestProperty("Accept", "application/json")
                setRequestProperty("Content-Type", "application/json")
                setRequestProperty("Authorization", "Bearer " + token)

                val gson = Gson()
                when(responseCode) {
                    200 -> {
                        val response = JSONObject(inputStream.bufferedReader().readText())
                        val entity = gson.fromJson(response.toString(), EntityUserData::class.java)
                        return entity
                    } else -> {
                        return null
                    }
                }
            }
        }

    }
}