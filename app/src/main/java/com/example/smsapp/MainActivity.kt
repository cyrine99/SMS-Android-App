package com.example.smsapp

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.smsapp.R

class MainActivity : AppCompatActivity() {
    lateinit var button: Button
    lateinit var editTextNumber: EditText
    lateinit var editTextMessage: EditText

    private val permissionRequest = 101

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = "ارسال رسالة"
        editTextNumber = findViewById(R.id.editTextPhone)
        editTextMessage = findViewById(R.id.editTextMessage)
        button = findViewById(R.id.btnSendSMS)
    }
    fun sendMessage(view: View)
    {
        val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)

        if (permissionCheck == PackageManager.PERMISSION_GRANTED)
        {
            myMessage()
        }
        else
        {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS),permissionRequest)
        }
    }
    private fun myMessage()
    {
        val myNumber: String = editTextNumber.text.toString().trim()
        val myMsg: String = editTextMessage.text.toString().trim()
        if (myNumber.isEmpty())
        {
            editTextNumber.setError("لايمكن ان يكون خاليا !");
        }
        else if( myMsg.isEmpty())
        {
            editTextMessage.setError("لايمكن ان يكون خاليا !");
        }
        else
        {
            if (TextUtils.isDigitsOnly(myNumber))
            {
                try
                {

                    val smsManager: SmsManager = SmsManager.getDefault()
                    smsManager.sendTextMessage(myNumber, null, myMsg, null, null)
                    customDialog("تم ارسال الرسالة بنجاح ")
                }
                catch ( e:Exception)
                {
                    customDialog("تأكد من وجود تغطية للشبكة !")
                }

            }
            else
            {
                editTextNumber.setError("هناك حطأ ما , تأكد من ادخال الرقم بشكل صحيح !");
            }
        }
    }


    private fun customDialog(text:String)
    {
        val alert = AlertDialog.Builder(this)
        val inflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.send_message,null)


        alert.setView(view)
        alert.setCancelable(true)

        val alertDialog = alert.create()
        alertDialog.show()

        val tv:TextView=view.findViewById(R.id.textView)
        tv.setText(text)

        val but_ok:Button= view.findViewById(R.id.but_ok)

        but_ok.setOnClickListener {
            alertDialog.dismiss()
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults:IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionRequest)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                myMessage();
            } else {
                Toast.makeText(this, "لا تملك تصريح لإرسال الرسالة !",Toast.LENGTH_SHORT).show();
            }
        }
    }
}