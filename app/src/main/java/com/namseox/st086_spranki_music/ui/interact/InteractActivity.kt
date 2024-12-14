package com.namseox.st086_spranki_music.ui.interact

import android.content.Intent
import android.graphics.Color
import androidx.recyclerview.widget.GridLayoutManager
import com.namseox.st086_spranki_music.R
import com.namseox.st086_spranki_music.data.model.InteractModel
import com.namseox.st086_spranki_music.databinding.ActivityInteractBinding
import com.namseox.st086_spranki_music.ui.home.HomeActivity
import com.namseox.st086_spranki_music.utils.Const
import com.namseox.st086_spranki_music.utils.SharedPreferenceUtils
import com.namseox.st086_spranki_music.utils.changeGradientText
import com.namseox.st086_spranki_music.utils.onSingleClick
import com.namseox.st086_spranki_music.view.base.AbsBaseActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class InteractActivity : AbsBaseActivity<ActivityInteractBinding>() {
    @Inject
    lateinit var sharedPreferenceUtils: SharedPreferenceUtils
    var adapter = InteractAdapter()
    var arr=arrayListOf<InteractModel>()
    override fun getLayoutId(): Int = R.layout.activity_interact

    override fun init() {
        arr = arrayListOf(
            InteractModel(R.drawable.imv_interact1, Color.parseColor("#FFD24B"),Color.parseColor("#FFA627"),R.font.butcherman_regular,true),
            InteractModel(R.drawable.imv_interact2, Color.parseColor("#EA6200"),Color.parseColor("#EC4C32"),R.font.boink_drop_shadow_w01),
            InteractModel(R.drawable.imv_interact3, Color.parseColor("#46D640"),Color.parseColor("#2BB426"),R.font.arbuckle_w00_bright),
            InteractModel(R.drawable.imv_interact4, Color.parseColor("#26A2FF"),Color.parseColor("#02599A"),R.font.pixelify_sans_segular),
            InteractModel(R.drawable.imv_interact5, Color.parseColor("#EA6200"),Color.parseColor("#EC4C32"),R.font.nosifer_regular),
            InteractModel(R.drawable.imv_interact6, Color.parseColor("#FFD24B"),Color.parseColor("#FFA627"),R.font.pacifico_regular),
        )
        binding.rcv.adapter = adapter
        binding.rcv.itemAnimator = null
        binding.rcv.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        adapter.submitList(arr)
        adapter.onClick={
            arr.forEach{
                it.check = false
            }
            arr[it].check = true
            adapter.submitList(arr)
        }
        changeGradientText(binding.tvTitle)
        binding.btnContinue.onSingleClick {
            sharedPreferenceUtils.putBooleanValue(Const.PERMISON,true)
            startActivity(Intent(this,HomeActivity::class.java))
            finish()
        }
    }
}