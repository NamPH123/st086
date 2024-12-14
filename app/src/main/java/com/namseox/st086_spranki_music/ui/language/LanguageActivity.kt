package com.namseox.st086_spranki_music.ui.language

import android.content.Intent
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.namseox.st086_spranki_music.R
import com.namseox.st086_spranki_music.data.model.LanguageModel
import com.namseox.st086_spranki_music.databinding.ActivityLanguageBinding
import com.namseox.st086_spranki_music.ui.home.HomeActivity
import com.namseox.st086_spranki_music.ui.tutorial.TutorialActivity
import com.namseox.st086_spranki_music.utils.Const.LANGUAGE
import com.namseox.st086_spranki_music.utils.Const.listLanguage
import com.namseox.st086_spranki_music.utils.Const.positionLanguageOld
import com.namseox.st086_spranki_music.utils.SharedPreferenceUtils
import com.namseox.st086_spranki_music.utils.SystemUtils
import com.namseox.st086_spranki_music.utils.changeGradientText
import com.namseox.st086_spranki_music.utils.onSingleClick
import com.namseox.st086_spranki_music.view.base.AbsBaseActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LanguageActivity : AbsBaseActivity<ActivityLanguageBinding>(){
    lateinit var adapter: LanguageAdapter
    var codeLang: String? = null
    @Inject
    lateinit var sharedPreferenceUtils: SharedPreferenceUtils

    override fun getLayoutId(): Int = R.layout.activity_language

    override fun init() {
        codeLang = sharedPreferenceUtils.getStringValue("language")
        if (codeLang.equals("")) {
            sharedPreferenceUtils.putStringValue("language", "en")
            codeLang = "en"
        }
        binding.rclLanguage.itemAnimator = null
        adapter = LanguageAdapter()
        setRecycleView()
        setClick()

        changeGradientText(binding.tvLanguage)
    }

    private fun setClick() {
        binding.imvDone.onSingleClick {
            if(codeLang.equals("")){
                Toast.makeText(this, getString(R.string.you_have_not_selected_anything_yet), Toast.LENGTH_SHORT).show()
            }else{
                SystemUtils.setPreLanguage(applicationContext, codeLang)
                sharedPreferenceUtils.putStringValue("language", codeLang)
                if (sharedPreferenceUtils.getBooleanValue(LANGUAGE)
                ) {
                    var intent = Intent(
                        applicationContext,
                        HomeActivity::class.java
                    )
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    finishAffinity()
                    startActivity(intent)
                } else {
                    SharedPreferenceUtils.getInstance(applicationContext)
                        .putBooleanValue(LANGUAGE, true)
                    var intent = Intent(applicationContext, TutorialActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun setRecycleView() {
        var i = 0
        lateinit var x: LanguageModel
        if (!codeLang.equals("")) {
            listLanguage.forEach {
                listLanguage[i].active = false
                if (codeLang.equals(it.code)) {
                    x = listLanguage[i]
                    x.active = true
                }
                i++
            }

            listLanguage.remove(x)
            listLanguage.add(0, x)
        }
        adapter.getData(listLanguage)
        binding.rclLanguage.adapter = adapter
        val manager = GridLayoutManager(applicationContext, 1, RecyclerView.VERTICAL, false)
        binding.rclLanguage.layoutManager = manager

        adapter.onClick = {
            codeLang = listLanguage[it].code
        }
    }

    override fun onResume() {
        super.onResume()

    }
    override fun onBackPressed() {
        listLanguage[positionLanguageOld].active = false
        positionLanguageOld = 0
        super.onBackPressed()
    }
}