package com.namseox.st086_spranki_music.utils

import android.graphics.Color
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.namseox.st086_spranki_music.R
import com.namseox.st086_spranki_music.data.model.LanguageModel

@BindingAdapter("setBGCV")
fun ConstraintLayout.setBGCV(check: LanguageModel) {
    if (check.active) {
        this.setBackgroundResource(R.drawable.bg_card_border2)
    } else {
        this.setBackgroundResource(R.drawable.bg_card_border)
    }
}

@BindingAdapter("setSrcCheckLanguage")
fun AppCompatImageView.setSrcCheckLanguage(check: Boolean) {
    if (check) {
        this.setImageResource(R.drawable.ic_check_language_true)
    } else {
        this.setImageResource(R.drawable.ic_check_language_false)
    }
}
@BindingAdapter("setBG")
fun AppCompatImageView.setBG(id: Int) {
    Glide.with(this).load(id).into(this)
}
@BindingAdapter("setTextColor")
fun AppCompatTextView.setTextColor(check: Boolean){
    if(check){
       this.setTextColor(Color.parseColor("#ffffff"))
    }else{
        this.setTextColor(Color.parseColor("#CC484848"))
    }

}