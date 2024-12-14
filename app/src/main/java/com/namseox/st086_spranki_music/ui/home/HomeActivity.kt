package com.namseox.st086_spranki_music.ui.home

import android.content.Intent
import android.graphics.Color
import androidx.recyclerview.widget.GridLayoutManager
import com.namseox.st086_spranki_music.R
import com.namseox.st086_spranki_music.data.model.InteractModel
import com.namseox.st086_spranki_music.databinding.ActivityHomeBinding
import com.namseox.st086_spranki_music.ui.setting.SettingActivity
import com.namseox.st086_spranki_music.utils.changeGradientText
import com.namseox.st086_spranki_music.utils.onSingleClick
import com.namseox.st086_spranki_music.view.base.AbsBaseActivity

class HomeActivity : AbsBaseActivity<ActivityHomeBinding>() {
    var adapter = HomeAdapter()
    var arr = arrayListOf<InteractModel>()
    override fun getLayoutId(): Int = R.layout.activity_home

    override fun init() {
        arr = arrayListOf(
            InteractModel(
                R.drawable.imv_interact1, Color.parseColor("#FFD24B"),
                Color.parseColor("#FFA627"), R.font.butcherman_regular
            ),
            InteractModel(
                R.drawable.imv_interact2, Color.parseColor("#EA6200"),
                Color.parseColor("#EC4C32"), R.font.boink_drop_shadow_w01
            ),
            InteractModel(
                R.drawable.imv_interact3, Color.parseColor("#46D640"),
                Color.parseColor("#2BB426"), R.font.arbuckle_w00_bright
            ),
            InteractModel(
                R.drawable.imv_interact4, Color.parseColor("#26A2FF"),
                Color.parseColor("#02599A"), R.font.pixelify_sans_segular
            ),
            InteractModel(
                R.drawable.imv_interact5, Color.parseColor("#EA6200"),
                Color.parseColor("#EC4C32"), R.font.nosifer_regular
            ),
            InteractModel(
                R.drawable.imv_interact6, Color.parseColor("#FFD24B"),
                Color.parseColor("#FFA627"), R.font.pacifico_regular
            ),
        )
        binding.rcv.adapter = adapter
        binding.rcv.itemAnimator = null
        binding.rcv.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        adapter.submitList(arr)
        adapter.onClick = {

        }
        binding.btnSetting.onSingleClick {
            startActivity(Intent(this, SettingActivity::class.java))
        }
        changeGradientText(binding.tvTitle)
    }
}