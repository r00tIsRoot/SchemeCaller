package com.isroot.schemecaller

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.isroot.schemecaller.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.button.setOnClickListener {
            getTestAppInfo()
        }
    }

    override fun onResume() {
        super.onResume()

        val intent = intent
        if (intent.action.equals(Intent.ACTION_VIEW)) {
            val uri = intent.data
            val responseResult = uri?.getQueryParameter("result")
            binding.tv.setText(responseResult)
        }
    }

    fun getTestAppInfo() {
        val packageName = "com.isroot.schemecallee"
        //앱이 설치되어 있는지 확인
        if (isInstalledApp(packageName)) {
            //설치되어 있으면 실행
            requestTestApp()
        } else {
            //설치되어 있지 않으면 마켓에서 다운.
            startMarket(packageName)
        }
    }

    // 앱 설치 여부 확인.
    private fun isInstalledApp(packageName: String): Boolean {
        val pm = applicationContext.packageManager
        val intent: Intent? = pm.getLaunchIntentForPackage(packageName)
        return intent != null
    }


    // 외부 스키마를 이용한 앱 실행.
    private fun requestTestApp() {
        val data1 = 1
        val data2 = 2
        val schemeUrl = "scheme_test://test_action_add?data1=$data1&data2=$data2"

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(schemeUrl))
//        val intent = Intent.parseUri(schemeUrl, Intent.URI_INTENT_SCHEME)
//        val existPackage = packageManager.getLaunchIntentForPackage(intent.getPackage()!!)
//        if (existPackage != null) startActivity(intent)
        startActivity(intent)
    }


    // 앱이 설치되어 있지 않은경우 마켓으로 이동.
    private fun startMarket(packageName: String): Boolean {
        return try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("market://details?id=$packageName")
            startActivity(intent)
            true
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
            false
        }
    }
}