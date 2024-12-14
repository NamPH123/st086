package com.namseox.st086_spranki_music.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.namseox.st086_spranki_music.R
import com.namseox.st086_spranki_music.databinding.ActivitySplashBinding
import com.namseox.st086_spranki_music.ui.language.LanguageActivity
import com.namseox.st086_spranki_music.ui.tutorial.TutorialActivity
import com.namseox.st086_spranki_music.utils.Const
import com.namseox.st086_spranki_music.utils.SharedPreferenceUtils
import com.namseox.st086_spranki_music.view.base.AbsBaseActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class SplashActivity : AbsBaseActivity<ActivitySplashBinding>() {
    @Inject
    lateinit var sharedPreferenceUtils: SharedPreferenceUtils
    var handler = Handler(Looper.myLooper()!!)
    var runnable = Runnable {
        if (!sharedPreferenceUtils.getBooleanValue(Const.LANGUAGE)
        ) {
            startActivity(Intent(this@SplashActivity, LanguageActivity::class.java))
        } else {
            startActivity(Intent(this@SplashActivity, TutorialActivity::class.java))
        }
        finish()
    }

    override fun getLayoutId(): Int = R.layout.activity_splash

    override fun init() {

        if (!isTaskRoot() && getIntent().hasCategory(Intent.CATEGORY_LAUNCHER)
            && getIntent().getAction() != null && getIntent().getAction().equals(Intent.ACTION_MAIN)
        ) {
            finish(); return;
        }
        handler.postDelayed(runnable, 2000)

    }

    override fun onRestart() {
        super.onRestart()
        handler.postDelayed(runnable, 2000)

    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(runnable)
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
    }
}