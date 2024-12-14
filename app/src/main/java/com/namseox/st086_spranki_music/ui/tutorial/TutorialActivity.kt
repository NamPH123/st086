package com.namseox.st086_spranki_music.ui.tutorial

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.namseox.st086_spranki_music.R
import com.namseox.st086_spranki_music.data.model.TutorialModel
import com.namseox.st086_spranki_music.databinding.ActivityTutorialBinding
import com.namseox.st086_spranki_music.ui.home.HomeActivity
import com.namseox.st086_spranki_music.ui.interact.InteractActivity
import com.namseox.st086_spranki_music.utils.Const
import com.namseox.st086_spranki_music.utils.SharedPreferenceUtils
import com.namseox.st086_spranki_music.utils.changeGradientText
import com.namseox.st086_spranki_music.utils.dpToPx
import com.namseox.st086_spranki_music.view.base.AbsBaseActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TutorialActivity : AbsBaseActivity<ActivityTutorialBinding>() {
    @Inject
    lateinit var sharedPreferenceUtils: SharedPreferenceUtils
    var listFragment = 3
    private var viewPagerAdapter: ViewPagerAdapter? = null

    override fun getLayoutId(): Int = R.layout.activity_tutorial

    override fun init() {
        var data = arrayListOf<TutorialModel>(
            TutorialModel(
                R.drawable.photo_intro1,
                getString(R.string.intro1)
            ),
            TutorialModel(
                R.drawable.photo_intro2,
                getString(R.string.intro2)
            ),
            TutorialModel(
                R.drawable.photo_intro3,
                getString(R.string.intro3)
            )
        )
        viewPagerAdapter = ViewPagerAdapter()
        viewPagerAdapter!!.getData(data)
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(100))
        compositePageTransformer.addTransformer { page: View, position: Float ->
            val r = 1 - Math.abs(position)
            page.scaleY = 0.8f + r * 0.2f
            val absPosition = Math.abs(position)
            page.alpha = 1.0f - (1.0f - 0.3f) * absPosition
        }
        binding.viewPager.setPageTransformer(compositePageTransformer)
        bindViewModel()
        binding.viewPager.currentItem = 1
        binding.viewPager.currentItem = 0
changeGradientText(binding.btnNext)
    }
    private fun bindViewModel() {
        binding.viewPager.adapter = viewPagerAdapter
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                addBottomDots(position)
            }
        })
        binding.btnNext.setOnClickListener {

            if (binding.viewPager.currentItem < listFragment - 1) {
                binding.viewPager.setCurrentItem(binding.viewPager.currentItem + 1)
            } else {

                if (!sharedPreferenceUtils.getBooleanValue(Const.PERMISON)) {
                    val intent = Intent(this@TutorialActivity, InteractActivity::class.java).addFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                    finish()
                } else {
                    val intent = Intent(this@TutorialActivity, HomeActivity::class.java).addFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
    private lateinit var dots: Array<ImageView>
    private fun addBottomDots(currentPage: Int) {
        binding.apply {
            linearDots.removeAllViews()
            dots = Array(3) { ImageView(applicationContext) }
            for (i in 0..listFragment - 1) {
                dots[i] = ImageView(applicationContext)
                if (i == currentPage) {
                    dots[i]
                        .setImageDrawable(resources.getDrawable(R.drawable.ic_bg_select))
                    val params = LinearLayout.LayoutParams(
                        dpToPx(20f, applicationContext).toInt(),
                        dpToPx(8f, applicationContext).toInt()
                    )
                    params.setMargins(8, 0, 8, 0)
                    linearDots.addView(dots[i], params)
                } else {
                    dots[i]
                        .setImageDrawable(resources.getDrawable(R.drawable.ic_bg_not_select))
                    val params = LinearLayout.LayoutParams(
                        dpToPx(8f, applicationContext).toInt(),
                        dpToPx(8f, applicationContext).toInt()
                    )
                    params.setMargins(8, 0, 8, 0)
                    linearDots.addView(dots[i], params)
                }
            }
        }

    }
}