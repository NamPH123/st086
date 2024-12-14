package com.namseox.st086_spranki_music.ui.playing

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.namseox.st086_spranki_music.R
import com.namseox.st086_spranki_music.data.model.ImvPlayingModel
import com.namseox.st086_spranki_music.databinding.ItemPlayingBinding
import com.namseox.st086_spranki_music.view.base.AbsBaseAdapter
import com.namseox.st086_spranki_music.view.base.AbsBaseDiffCallBack

class PlayingAdapter :
    AbsBaseAdapter<ImvPlayingModel, ItemPlayingBinding>(R.layout.item_playing, DiffCallBack()) {
    class DiffCallBack() : AbsBaseDiffCallBack<ImvPlayingModel>() {
        override fun itemsTheSame(oldItem: ImvPlayingModel, newItem: ImvPlayingModel): Boolean {
            return oldItem.imv == newItem.imv
        }

        override fun contentsTheSame(oldItem: ImvPlayingModel, newItem: ImvPlayingModel): Boolean {
            return oldItem.imv != newItem.imv || oldItem.check != newItem.check
        }

    }

    override fun bind(
        binding: ItemPlayingBinding,
        position: Int,
        data: ImvPlayingModel,
        holder: RecyclerView.ViewHolder
    ) {
        Glide.with(binding.root).load(data).into(binding.imv)
        if (data.check) {
            binding.imv1.visibility = View.VISIBLE
        } else {
            binding.imv1.visibility = View.VISIBLE
        }
    }
}