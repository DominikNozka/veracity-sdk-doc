package com.veracity.sdk.sample

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.text.Html
import android.text.method.ScrollingMovementMethod
import com.google.gson.Gson
import com.koushikdutta.async.future.FutureCallback
import com.koushikdutta.ion.Ion
import com.veracity.sdk.R
import com.veracity.sdk.VeracitySdk
import com.veracity.sdk.apiAdd.addVariables.ProtectAdd
import com.veracity.sdk.apiAdd.addVariables.SearchAdd
import com.veracity.sdk.apiAdd.addVariables.VerifyAdd
import com.veracity.sdk.apiGet.ProtectGet
import com.veracity.sdk.apiGet.ProtectGetList
import com.veracity.sdk.apiGet.VerifyGet
import com.veracity.sdk.apiGet.VerifyGetList
import com.veracity.sdk.auth.Authenticate
import com.veracity.sdk.detail.DetailActivity
import com.veracity.sdk.detail.DetailConfig
import com.veracity.sdk.detail.DetailLocation
import com.veracity.sdk.event.ProtectEvent
import com.veracity.sdk.event.SearchEvent
import com.veracity.sdk.event.VerifyEvent
import kotlinx.android.synthetic.main.sample_activity.*
import java.io.File

class SampleActivity:Activity(),
        Authenticate.LoginListener,
        VerifyEvent.EventListener, ProtectEvent.EventListener,SearchEvent.EventListener {

    lateinit var protectEvent:ProtectEvent
    lateinit var searchEvent:SearchEvent
    lateinit var verifyEvent:VerifyEvent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sample_activity)

        val m_path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM).path

        val fp1 = File(m_path+"/FP2300348882928829.png")
        val fp2 = File(m_path+"/FP132983990808329329.png") //FP132983990808329329
        val overview = File(m_path+"/OV983299929920033.jpg")
        val thumbnail = File(m_path+"/TH12332109832918931.jpg")

        writeLog("downloading/checking 4 test files","Please wait")

        downloadFile("https://oneprove-dev.s3.amazonaws.com/FP2300348882928829.png",fp1)
        downloadFile("https://oneprove-dev.s3.amazonaws.com/FP132983990808329329.png",fp2)
        downloadFile("https://oneprove-dev.s3.amazonaws.com/OV983299929920033.jpg",overview)
        downloadFile("https://oneprove-dev.s3.amazonaws.com/TH12332109832918931.jpg",thumbnail)

        log_text.movementMethod=ScrollingMovementMethod()

        button_protect.setOnClickListener{

            val protectAdd = ProtectAdd.Builder()
                    .setArtistFirstname("Dominik")
                    .setArtistLastname("Nožka")
                    .setYear(1993)
                    .setName("Test")
                    .setWidth(30.0f)
                    .setHeight(40.0f)
                    .setThumbnail(thumbnail)
                    .setOverview(overview)
                    .setFingerprint1(fp1)
                    .setFingerprint2(fp2)
                    .setFingerprintLocation( " {\n" +
                            "        \"x\" : 260,\n" +
                            "        \"y\" : 166,\n" +
                            "        \"width\" : 139,\n" +
                            "        \"height\" : 184\n" +
                            "    }")
                    .create()

            VeracitySdk(this).upload(protectAdd)

            writeLog("protectAdd",Gson().toJson(protectAdd))
        }

        button_verify.setOnClickListener{

            val verifyAdd = VerifyAdd.Builder()
                    .setFingerprint(fp1)
                    .setThumbnailImage(thumbnail.path)
                    .setOverviewImage(overview.path)
                    .setArtworkId("5c30b27a507d460004bd8059")
                    .setArtworkPublicId("artPublicId")
                    .setName("name")
                    .setAuthorFirstName("authorFirstname")
                    .setAuthorLastName("authorLastname")
                    .setAdmin(false)
                    .setOriginal(false)
                    .setCreatedBy("")
                    .create()

            VeracitySdk(this).upload(verifyAdd)
            writeLog("verifyAdd",Gson().toJson(verifyAdd))
        }

        button_search.setOnClickListener{
            //OneproveSdk(this).upload()
            val searchAdd = SearchAdd.Builder()
                    .setOverviewImg(overview)
                    .setThumbnailImg(thumbnail)
                    .create()

            VeracitySdk(this).upload(searchAdd)
            writeLog("searchAdd",Gson().toJson(searchAdd))
        }

        get_verify.setOnClickListener {

            VerifyGetList(this, false).Query {
                var i=0

                it.forEach {
                    if(i<10)writeLog("item"+i,Gson().toJson(it))
                    i++
                }
            }
        }

        get_protect.setOnClickListener{

            ProtectGetList(this, false).Query {
                var i=0

                it.forEach{
                    if(i<10)writeLog("item"+i,Gson().toJson(it))
                    i++
                }
            }
        }

        capture_protect.setOnClickListener {
            val detailConfigProtect = DetailConfig(captureType = DetailConfig.typeProtect,
                    folder = File(m_path),jpegPath=overview.path,overviewWidth = 40,overviewHeight = 30)

            DetailActivity.launch(detailConfigProtect,this)
        }

        capture_verify.setOnClickListener {

            val detailLocation = DetailLocation(100,100,1500,2000)
            val detailConfigVerify = DetailConfig(captureType = DetailConfig.typeVerify,
                    folder = File(m_path),jpegPath=overview.path,overviewWidth = 40,overviewHeight = 30,detailLocation = detailLocation)

            DetailActivity.launch(detailConfigVerify,this)
        }

        capture_documents.setOnClickListener{

            val detailConfigProtectDoc = DetailConfig(captureType = DetailConfig.typeProtectDoc,
                    folder = File(m_path),jpegPath=overview.path,overviewWidth = 40,overviewHeight = 30)

            DetailActivity.launch(detailConfigProtectDoc,this)
        }

        protectEvent = ProtectEvent(this,this)
        verifyEvent = VerifyEvent(this,this)
        searchEvent = SearchEvent(this,this)

        protectEvent.registerReceiver()
        verifyEvent.registerReceiver()
        searchEvent.registerReceiver()

        //Authenticate(this).logIn("oneprove.two@seznam.cz","dominik",this)
    }

    override fun onDestroy() {
        super.onDestroy()
        protectEvent.unregisterReceiver()
        verifyEvent.unregisterReceiver()
        searchEvent.unregisterReceiver()
    }

    //detail capture activity result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                val result = data.getStringExtra("result")
                writeLog("onActivityResult",result)
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                writeLog("onActivityResult","resultCancelled")
            }
        }
    }

    //login feedback
    override fun onLogInSuccess() {
        writeLog("Login"," Success")
    }
    override fun onLogInFailure(error: String) {
        writeLog("Login"," Failure "+error)
    }

    //search events
    override fun onSearchUploadingStarted(searchAdd: SearchAdd) {
        writeLog("onSearchUploadingStarted",Gson().toJson(searchAdd))
    }
    override fun onSearchUploadingFailed(failReason: String, searchAdd: SearchAdd) {
        writeLog("onSearchUploadingFailed",Gson().toJson(searchAdd))
    }

    override fun onSearchUploadingProgress(progress: Int, uploadSpeed: String, searchAdd: SearchAdd) {
        writeLog("onSearchUploadingProgress: speed:"+uploadSpeed +" progress: "+progress ,"\nsearchAddObject")
    }
    override fun onSearchUploadingFinished(searchAdd: SearchAdd) {
        writeLog("onSearchUploadingFinished",Gson().toJson(searchAdd))
    }
    override fun onSearchAnalyzingFinished(verifyGet: VerifyGet) {
        writeLog("onSearchAnalyzingFinished",Gson().toJson(verifyGet))
    }

    //verify events
    override fun onVerifyUploadingStarted(verifyAdd: VerifyAdd) {
        writeLog("onVerifyUploadingStarted",Gson().toJson(verifyAdd))
    }
    override fun onVerifyUploadingFailed(failReason: String, verifyAdd: VerifyAdd) {

        writeLog("onVerifyUploadingFailed failReason:"+failReason,Gson().toJson(verifyAdd))
    }
    override fun onVerifyUploadingProgress(progress: Int, uploadSpeed: String, verifyAdd: VerifyAdd) {
        writeLog("onVerifyUploadingProgress: speed:"+uploadSpeed +" progress: "+progress ,"\nverifyAddObject")
    }
    override fun onVerifyUploadingFinished(verifyAdd: VerifyAdd) {
        writeLog("onVerifyUploadingFinished",Gson().toJson(verifyAdd))
    }
    override fun onVerifyAnalyzingFinished(verifyGet: VerifyGet) {
        writeLog("onVerifyAnalyzingFinished",Gson().toJson(verifyGet))
    }

    //protect events
    override fun onProtectUploadingFinished(protectAdd: ProtectAdd){
        writeLog("onProtectUploadingFinished",Gson().toJson(protectAdd))
    }
    override fun onProtectUploadingStarted(protectAdd: ProtectAdd){
        writeLog("onProtectUploadingStarted",Gson().toJson(protectAdd))
    }
    override fun onProtectUploadingProgress(progress: Int, uploadSpeed: String, protectAdd: ProtectAdd) {
        writeLog("onProtectUploadingProgress: speed:"+uploadSpeed +" progress: "+progress ,"\nProtectAddObject")
    }
    override fun onProtectUploadingFailed(failReason:String,protectAdd: ProtectAdd){
        writeLog("onProtectUploadingFailed: failReason:"+failReason,Gson().toJson(protectAdd))
    }
    override fun onProtectAnalyzingFinished(protectGet: ProtectGet) {
        writeLog("onProtectAnalyzingFinished",Gson().toJson(protectGet))
    }

    fun writeLog(tag:String,msg:String){
        log_text.append("\n")
        log_text.append(Html.fromHtml("<b>"+tag+":</b> "+msg))
        log_text.append("\n")
    }

    fun downloadFile(url:String,file:File){
        if(file.exists()){
            writeLog("Download ",file.name+" Already downloaded")
            return
        }
        Ion.with(this)
                .load(url)
                .write(file)
                .setCallback(object : FutureCallback<File>{
                    override fun onCompleted(e: Exception?, file: File) {
                        if(e==null)writeLog("Downloaded ",file.name)
                        else writeLog("Download ",e.localizedMessage)
                    }
                })
    }


}
