package com.namseox.st086_spranki_music.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Parcelable
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.TypefaceSpan
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManagerFactory
import com.namseox.st086_spranki_music.dialog.DialogRate
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream


var RATE = "rate"

fun Activity.shareApp() {
    ShareCompat.IntentBuilder.from(this)
        .setType("text/plain")
        .setChooserTitle("Chooser title")
        .setText("http://play.google.com/store/apps/details?id=" + (this).getPackageName())
        .startChooser()
}

fun Activity.policy() {
    val url = "https://sites.google.com/view/lvt-studio-video-editor/home"
    val i = Intent(Intent.ACTION_VIEW)
    i.data = Uri.parse(url)
    startActivity(i)
}

fun newIntent(context: Context, cls: Class<*>): Intent {
    return Intent(
        context,
        cls
    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
}

var unItem: (() -> Unit)? = null
fun Activity.rateUs(i: Int, view: View?) {
    var dialog = DialogRate(this)
    dialog.init(object : DialogRate.OnPress {
        override fun send(rate: Float) {
        }

        override fun rating() {
            if (view != null) {
                unItem?.invoke()
            }
            val manager = ReviewManagerFactory.create(this@rateUs!!)
            var request: Task<ReviewInfo> = manager.requestReviewFlow();
            request.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    var reviewInfo = task.result;
                    val flow: Task<Void> = manager.launchReviewFlow((this@rateUs)!!, reviewInfo)
                    dialog.dismiss()
                    flow.addOnCompleteListener { task2 ->
                        if (i == 1) {
                            finishAffinity()
                        }
                    }
                } else {
                    dialog.dismiss()
                    if (i == 1) {
                        finishAffinity()
                    }
                }
            }
        }

        override fun cancel() {
            if (i == 1) {
                finishAffinity()
            }
        }

        override fun later() {
            if (view != null) {
                unItem?.invoke()
            }
            if (i == 1) {
                finishAffinity()
            }
        }
    })
    if (!SharedPreferenceUtils.getInstance(this).getBooleanValue("rate")) {
        dialog.show()
    }
}

fun Activity.showSystemUI(white: Boolean) {
    if (white) {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    } else {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//        WindowCompat.setDecorFitsSystemWindows(window, false);
    } else {
//        getWindow().setFlags(
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//        );
//        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                )
    }
}

fun Activity.backPress(providerSharedPreference: SharedPreferenceUtils) {
    var a = providerSharedPreference.getNumber("rate2")
    a += 1
    providerSharedPreference.putNumber("rate2", a)
    if (a % 2 == 0) {
        if (!providerSharedPreference.getBooleanValue("rate")
        ) {
            rateUs(1, null)
        } else {
            finishAffinity()
        }
    } else {
        finishAffinity()
    }
}

fun showToast(context: Context, id: Int) {
    SystemUtils.setLocale(context)
    Toast.makeText(context, context.resources.getText(id), Toast.LENGTH_SHORT).show()
}

fun changeColor(
    context: Context,
    text: String,
    color: Int,
    fontfamily: Int,
    textSize: Float
): SpannableString {
    val spannableString = SpannableString(text)
    spannableString.setSpan(
        ForegroundColorSpan(ContextCompat.getColor(context, color)),
        0,
        text.length,
        SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    val font = ResourcesCompat.getFont(context, fontfamily)
    val typefaceSpan = CustomTypefaceSpan("", font)
    spannableString.setSpan(
        typefaceSpan,
        0,
        text.length,
        SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    spannableString.setSpan(
        RelativeSizeSpan(textSize),
        0,
        text.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return spannableString
}

class CustomTypefaceSpan(private val family: String, private val typeface: Typeface?) :
    TypefaceSpan(family) {

    override fun updateDrawState(ds: TextPaint) {
        applyCustomTypeFace(ds, typeface)
    }

    override fun updateMeasureState(paint: TextPaint) {
        applyCustomTypeFace(paint, typeface)
    }

    private fun applyCustomTypeFace(paint: Paint, tf: Typeface?) {
        if (tf != null) {
            paint.typeface = tf
        } else {
            paint.typeface = Typeface.DEFAULT
        }
    }
}

fun isServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
    val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val services = activityManager.getRunningServices(Integer.MAX_VALUE)
    for (service in services) {
        if (serviceClass.name == service.service.className) {
            return true
        }
    }
    return false
}

fun dpToPx(dp: Float, context: Context): Float {
    val metrics = context.resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics)
}

fun dpToSp(sp: Float, context: Context): Float {
    val floatSize =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.resources.displayMetrics)
    return floatSize
}

fun Activity.setupWindow() {
    requestWindowFeature(Window.FEATURE_NO_TITLE)
    getWindow().setFlags(
        WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN
    )
//    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
}

fun AppCompatActivity.changeFragment(fragment: Fragment, tag: String?, id: Int) {
    var fragmentManager: FragmentManager? = null
    var transaction: FragmentTransaction? = null
    fragmentManager = supportFragmentManager
    transaction = fragmentManager!!.beginTransaction()
    val existingFragment: Fragment? = fragmentManager!!.findFragmentByTag(tag)
    if (existingFragment != null) {
        transaction!!.replace(id, existingFragment)
    } else {
        transaction!!.replace(id, fragment, tag)
        transaction!!.addToBackStack(tag)
    }
    transaction!!.commit()
}

fun shareFile(context: Context, file: File) {
    val fileUri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "video/*"
    intent.putExtra(Intent.EXTRA_STREAM, fileUri)
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(Intent.createChooser(intent, "Share Video"))
    }
}

fun getAllUriInFileAsset(context: Context, filePaths: ArrayList<String>): ArrayList<Uri> {
    val assetUris = ArrayList<Uri>()
    for (filePath in filePaths) {

        var uri = Uri.parse(
            "content://com.keyboard.fonts.emojikeyboard.theme.Provider/" + filePath.replace(
                "file:///android_asset/",
                ""
            )
        )
        assetUris.add(uri)
    }
    return assetUris
}

fun shareVideoOnFacebook(context: Context, file: File) {
    // Tạo URI cho file video
    val fileUri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)

    // Tạo intent gửi file với kiểu video
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "video/*"
    intent.putExtra(Intent.EXTRA_STREAM, fileUri)
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

    // Đặt gói ứng dụng là Facebook để chia sẻ trực tiếp lên Facebook
    intent.setPackage("com.facebook.katana")

    if (isAppInstalled(context, "com.facebook.katana")) {
        context.startActivity(Intent.createChooser(intent, "Share Video on Facebook"))

    } else {
//        Toast.makeText(context, context.getString(R.string.facebook_app_is_not_installed), Toast.LENGTH_SHORT).show()
    }


}

fun shareVideoOnTikTok(context: Context, file: File) {
    // Tạo URI cho file video
    val fileUri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)

    // Tạo intent gửi file với kiểu video
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "video/*"
    intent.putExtra(Intent.EXTRA_STREAM, fileUri)
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

    intent.setPackage("com.zhiliaoapp.musically")


    if (isAppInstalled(context, "com.zhiliaoapp.musically")) {
        context.startActivity(Intent.createChooser(intent, "Share Video on TikTok"))
    } else {
//        Toast.makeText(context, context.getString(R.string.tikTok_app_is_not_installed), Toast.LENGTH_SHORT).show()
    }
}

fun shareVideoOnTele(context: Context, file: File) {
    // Tạo URI cho file video
    val fileUri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)

    // Tạo intent gửi file với kiểu video
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "video/*"
    intent.putExtra(Intent.EXTRA_STREAM, fileUri)
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

    // Chỉ định gói ứng dụng Instagram
    intent.setPackage("org.telegram.messenger")
    if (isAppInstalled(context, "org.telegram.messenger")) {
        context.startActivity(Intent.createChooser(intent, "Share Video on Telegram"))
    } else {
//        Toast.makeText(context, context.getString(R.string.telegram_app_is_not_installed), Toast.LENGTH_SHORT).show()
    }
}

fun shareVideoOnWhatsApp(context: Context, file: File) {
    // Tạo URI cho file video
    val fileUri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)

    // Tạo intent gửi file với kiểu video
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "video/*"
    intent.putExtra(Intent.EXTRA_STREAM, fileUri)
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

    // Kiểm tra nếu WhatsApp đã được cài đặt trên thiết bị
    val whatsappIntent = Intent(intent).apply {
        setPackage("com.whatsapp")
    }
    if (isAppInstalled(context, "com.whatsapp")) {
        context.startActivity(Intent.createChooser(whatsappIntent, "Share Video on WhatsApp"))
    } else {
//        Toast.makeText(context, context.getString(R.string.whatsApp_app_is_not_installed), Toast.LENGTH_SHORT).show()
    }
}

fun isAppInstalled(context: Context, packageName: String): Boolean {
    return try {
        context.packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA)
        true // Ứng dụng tồn tại
    } catch (e: PackageManager.NameNotFoundException) {
        Log.d(TAG, "isAppInstalled: $e")
        false // Ứng dụng không tồn tại
    }
}

fun shareAllFile(context: Context, imageUris: ArrayList<Uri>) {
    val intent = Intent(Intent.ACTION_SEND_MULTIPLE).apply {
        type = "video/*"
        putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(intent, "Share Video"))
}

fun Context.showKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
    inputMethodManager!!.toggleSoftInputFromWindow(
        view.getApplicationWindowToken(),
        InputMethodManager.SHOW_FORCED, 0
    )
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun getAllUriInFIleAsset(context: Context, path: String): ArrayList<String> {
    val pathList = arrayListOf<String>()
    try {
        val files = context.assets.list(path) ?: arrayOf()
        for (file in files) {
            val fullPath = "file:///android_asset/$path/$file"
            pathList.add(fullPath)
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    Log.d(TAG, "getAllPathInFileAsset: ${pathList.size}")
    return pathList
}

fun getAllFile(folder: File): ArrayList<String> {
    var arr = arrayListOf<String>()
    if (folder.exists()) {
        val files = folder.listFiles()
        files.forEach {
            arr.add(it.path)
        }
    }
    return arr
}

fun pickImage(fragment: Fragment?, activity: Activity?) {
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
    intent.addCategory(Intent.CATEGORY_OPENABLE)
    intent.setType("image/*")
    intent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/*"))
    if (fragment == null) {
        activity!!.startActivityForResult(intent, 1102)
    } else {
        fragment!!.startActivityForResult(intent, 1103)
    }
}

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
fun fileToDrawable(context: Context, filePath: String?): Drawable? {
    val file = File(filePath)
    if (!file.exists()) {
        return null
    }
    val bitmap = BitmapFactory.decodeFile(filePath)
    return BitmapDrawable(context.resources, bitmap)
}

fun Intent.putParcelableExtra(key: String, value: Parcelable): Intent {
    return this.putExtra(key, value)
}


fun setWidthHeight(view: View, width: Int, height: Int) {
    val params = view.layoutParams
    if (width != 0) {
        params.width = width
    }
    if (height != 0) {
        params.height = height
    }
    view.layoutParams = params
}

fun setLayoutParamParent(view: View, top: Float, right: Float, bottom: Float, left: Float) {
    val params = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT, // width
        LinearLayout.LayoutParams.WRAP_CONTENT // height
    )
    val marginTopInPixels = dpToPx(top, view.context).toInt()
    params.setMargins(0, marginTopInPixels, 0, 0)
    view.layoutParams = params
}

fun setLayoutParam(view: View, top: Float, right: Float, bottom: Float, left: Float) {
    val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.setMargins(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
    view.layoutParams = layoutParams
}

fun changeText(context: Context, text: String, color: Int, fontfamily: Int): SpannableString {
    val spannableString = SpannableString(text)
    spannableString.setSpan(
        ForegroundColorSpan(color),
        0,
        text.length,
        SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    val font = ResourcesCompat.getFont(context, fontfamily)
    val typefaceSpan = CustomTypefaceSpan("", font)
    spannableString.setSpan(
        typefaceSpan,
        0,
        text.length,
        SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return spannableString
}

var lastClickTime = 0L
fun View.onSingleClick(action: () -> Unit) {
    this.setOnClickListener {
        if (System.currentTimeMillis() - lastClickTime >= 500) {
            action()
            lastClickTime = System.currentTimeMillis()
        }
    }
}

fun onClick(action: () -> Unit) {
    if (System.currentTimeMillis() - lastClickTime >= 1000) {
        action()
        lastClickTime = System.currentTimeMillis()
    }
}

fun View.onClick(action: () -> Unit) {
    this.setOnClickListener {
        if (System.currentTimeMillis() - lastClickTime >= 3000) {
            action()
            lastClickTime = System.currentTimeMillis()
        }
    }
}

lateinit var documentFile: DocumentFile


fun getFilePathFromURI(context: Context, uri: Uri): String? {
    var filePath: String? = null
    if (DocumentsContract.isDocumentUri(context, uri)) {
        // DocumentProvider
        val docId = DocumentsContract.getDocumentId(uri)
        val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        val type = split[0]

        var contentUri: Uri? = null
        if ("image" == type) {
            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        } else if ("video" == type) {
            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        } else if ("audio" == type) {
            contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }

        val selection = "_id=?"
        val selectionArgs = arrayOf(
            split[1]
        )

        filePath = getDataColumn(context, contentUri, selection, selectionArgs)
    } else if ("content".equals(uri.scheme, ignoreCase = true)) {
        // MediaStore (and general)
        filePath = getDataColumn(context, uri, null, null)
    } else if ("file".equals(uri.scheme, ignoreCase = true)) {
        filePath = uri.path
    }

    return filePath
}

fun getDataColumn(
    context: Context,
    uri: Uri?,
    selection: String?,
    selectionArgs: Array<String>?
): String? {
    var cursor: Cursor? = null
    val column = "_data"
    val projection = arrayOf(
        column
    )

    try {
        cursor = context.contentResolver.query(
            uri!!, projection, selection, selectionArgs,
            null
        )
        if (cursor != null && cursor.moveToFirst()) {
            val column_index = cursor.getColumnIndexOrThrow(column)
            return cursor.getString(column_index)
        }
    } finally {
        cursor?.close()
    }
    return null
}


fun Activity.pathOfFileCreateFromUri(uri: Uri, endFile: String): String? {

    var filePath: String? = null
    var inputStream: InputStream? = null
    try {
        documentFile = DocumentFile.fromSingleUri(applicationContext, uri)!!
        val contentResolver = contentResolver
        inputStream = contentResolver.openInputStream(uri)
        if (inputStream != null) {
            val file = createTemporalFileFrom(inputStream, documentFile.name!!, endFile)
            filePath = file?.path
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        try {
            inputStream?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    return filePath
}

val buffer = ByteArray(8 * 1024)
var outputDirectory: File? = null
var outputDir: File? = null
var uploadSuccess = MutableLiveData<Int>()

@Throws(IOException::class)
fun Activity.createTemporalFileFrom(
    inputStream: InputStream,
    name: String,
    endFile: String
): File? {
    outputDirectory = File(applicationContext.filesDir, "Ringtone")
    if (!outputDirectory!!.exists()) {
        outputDirectory!!.mkdirs()
    }
    var outputDir: File? = null
    outputDir = File(outputDirectory, "$name.$endFile")
    if (outputDir!!.exists()) {

    } else {
        try {
            inputStream.use { input ->
                FileOutputStream(outputDir).use { output ->
                    var bytesRead: Int
                    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                        Log.d(TAG, "createTemporalFileFrom: ${bytesRead}")
                        output.write(buffer, 0, bytesRead)
                    }
                    output.flush()

                }


            }
        } catch (e: Exception) {
            Log.d(TAG, "createTemporalFileFrom: ${e}")
            e.printStackTrace()
            outputDir = null
        } finally {
            try {
                inputStream.close()
            } catch (e: IOException) {
                Log.d(TAG, "createTemporalFileFrom: Close stream error")
                e.printStackTrace()
            }
        }
    }

    uploadSuccess.postValue(1)
    return outputDir
}

fun Activity.setNotificationSound(uri: Uri) {
    RingtoneManager.setActualDefaultRingtoneUri(
        this,
        RingtoneManager.TYPE_NOTIFICATION,
        uri
    )
}

fun Activity.setAlarmSound(uri: Uri) {
    RingtoneManager.setActualDefaultRingtoneUri(
        this,
        RingtoneManager.TYPE_ALARM,
        uri
    )
}

fun startNewActivity(context: Context, cls: Class<*>): Intent {
    return Intent(
        context,
        cls
    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
}

fun changeGradientText(textView: AppCompatTextView) {
    textView.viewTreeObserver.addOnGlobalLayoutListener(object :
        ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (textView.width > 0 && textView.height > 0) {
                val textShader: Shader = LinearGradient(
                    0f, 0f, textView.width.toFloat(), textView.textSize.toFloat(),
                    intArrayOf(
                        Color.parseColor("#FFD24B"),
                        Color.parseColor("#FFA627")
                    ), floatArrayOf(0.25f, 1f), Shader.TileMode.CLAMP
                )
                textView.paint.setShader(textShader)
            }
            textView.viewTreeObserver.removeOnGlobalLayoutListener(this)
        }
    })
}

fun changeGradientText2(textView: AppCompatTextView, color1: Int, color2: Int) {
    textView.viewTreeObserver.addOnGlobalLayoutListener(object :
        ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (textView.width > 0 && textView.height > 0) {
                val textShader: Shader = LinearGradient(
                    0f, 0f, textView.width.toFloat(), textView.textSize.toFloat(),
                    intArrayOf(
                        color1,
                        color2
                    ), floatArrayOf(0.25f, 1f), Shader.TileMode.CLAMP
                )
                textView.paint.setShader(textShader)
            }
            textView.viewTreeObserver.removeOnGlobalLayoutListener(this)
        }
    })
}
//fun Activity.showInter(func: () -> Unit){
//    Admob.getInstance().showInterAll(this,object : InterCallback(){
//        override fun onNextAction() {
//            super.onNextAction()
//            func()
//        }
//    })
//}