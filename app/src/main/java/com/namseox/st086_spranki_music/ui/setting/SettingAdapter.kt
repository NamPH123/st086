package com.namseox.st086_spranki_music.ui.setting

import androidx.recyclerview.widget.RecyclerView
import com.namseox.st086_spranki_music.R
import com.namseox.st086_spranki_music.data.model.SettingModel
import com.namseox.st086_spranki_music.databinding.ItemSettingBinding
import com.namseox.st086_spranki_music.view.base.AbsBaseAdapter
import com.namseox.st086_spranki_music.view.base.AbsBaseDiffCallBack

class SettingAdapter : AbsBaseAdapter<SettingModel, ItemSettingBinding>(R.layout.item_setting,DiffCallback()) {
    var onClick : ((SettingModel) -> Unit)? = null
    class DiffCallback : AbsBaseDiffCallBack<SettingModel>(){
        override fun itemsTheSame(oldItem: SettingModel, newItem: SettingModel): Boolean {
            return oldItem.imv == newItem.imv
        }

        override fun contentsTheSame(oldItem: SettingModel, newItem: SettingModel): Boolean {
            return oldItem.imv != newItem.imv
        }

    }

    override fun bind(
        binding: ItemSettingBinding,
        position: Int,
        data: SettingModel,
        holder: RecyclerView.ViewHolder
    ) {
        binding.imv.setImageResource(data.imv)
        binding.tv.text = data.tv
        binding.ll.setBackgroundResource(data.bg)
        binding.root.setOnClickListener {
            onClick?.invoke(data)
        }
    }
}