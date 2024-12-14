package com.namseox.st086_spranki_music.view.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.namseox.st086_spranki_music.utils.SystemUtils
import com.namseox.st086_spranki_music.utils.showSystemUI


abstract class AbsBaseActivity<V : ViewDataBinding>() : AppCompatActivity() {
    lateinit var binding: V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemUtils.setLocale(this)
        binding = DataBindingUtil.setContentView(this, getLayoutId())

        init()
    }
    override fun onResume() {
        super.onResume()
        showSystemUI(false)
    }

    override fun onRestart() {
        super.onRestart()
        SystemUtils.setLocale(this)
    }
    abstract fun getLayoutId(): Int
    abstract fun init()

}