package com.namseox.st086_spranki_music.ui.setting

import androidx.recyclerview.widget.GridLayoutManager
import com.namseox.st086_spranki_music.R
import com.namseox.st086_spranki_music.data.model.SettingModel
import com.namseox.st086_spranki_music.databinding.ActivitySettingBinding
import com.namseox.st086_spranki_music.ui.language.LanguageActivity
import com.namseox.st086_spranki_music.utils.RATE
import com.namseox.st086_spranki_music.utils.SharedPreferenceUtils
import com.namseox.st086_spranki_music.utils.changeGradientText
import com.namseox.st086_spranki_music.utils.newIntent
import com.namseox.st086_spranki_music.utils.onSingleClick
import com.namseox.st086_spranki_music.utils.policy
import com.namseox.st086_spranki_music.utils.rateUs
import com.namseox.st086_spranki_music.utils.shareApp
import com.namseox.st086_spranki_music.utils.unItem
import com.namseox.st086_spranki_music.view.base.AbsBaseActivity

class SettingActivity : AbsBaseActivity<ActivitySettingBinding>() {
    var adapter = SettingAdapter()
    var arr = arrayListOf<SettingModel>()
    override fun getLayoutId(): Int = R.layout.activity_setting

    override fun init() {
        arr = arrayListOf(
            SettingModel(
                R.drawable.ic_language,
                getString(R.string.language),
                R.drawable.bg_language
            ),
            SettingModel(
                R.drawable.ic_share_app,
                getString(R.string.share_app),
                R.drawable.bg_share_app
            ),
            SettingModel(R.drawable.ic_rate_us, getString(R.string.rate_us), R.drawable.bg_rate_us),
            SettingModel(R.drawable.ic_policy, getString(R.string.policy), R.drawable.bg_policy),
        )
        if (SharedPreferenceUtils.getInstance(application).getBooleanValue(RATE)) {
            arr.removeAt(1)
        }
        unItem = {
            arr.removeAt(1)
            adapter.submitList(arr)
        }
        binding.rcv.adapter = adapter
        binding.rcv.itemAnimator = null
        binding.rcv.layoutManager = GridLayoutManager(this, 1)
        adapter.submitList(arr)
        adapter.onClick = {
            when (it.imv) {
                R.drawable.ic_language -> {
                    startActivity(newIntent(applicationContext, LanguageActivity::class.java))
                }
                R.drawable.ic_rate_us -> {
                    rateUs(0, binding.rcv)
                }
                R.drawable.ic_share_app -> {shareApp()}
                R.drawable.ic_policy -> {policy()}
            }
        }
        binding.btnBack.onSingleClick { finish() }
        changeGradientText(binding.tvTitle)
    }
}