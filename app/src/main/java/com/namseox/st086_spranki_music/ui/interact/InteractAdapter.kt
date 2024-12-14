package com.namseox.st086_spranki_music.ui.interact

import android.os.Build
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.namseox.st086_spranki_music.R
import com.namseox.st086_spranki_music.data.model.InteractModel
import com.namseox.st086_spranki_music.databinding.ItemChooseInteractBinding
import com.namseox.st086_spranki_music.utils.changeGradientText2
import com.namseox.st086_spranki_music.utils.dpToPx
import com.namseox.st086_spranki_music.utils.onSingleClick
import com.namseox.st086_spranki_music.view.base.AbsBaseAdapter
import com.namseox.st086_spranki_music.view.base.AbsBaseDiffCallBack

class InteractAdapter : AbsBaseAdapter<InteractModel, ItemChooseInteractBinding>(
    R.layout.item_choose_interact,
    DiffCallback()
) {
    var onClick: ((Int) -> Unit)? = null

    class DiffCallback : AbsBaseDiffCallBack<InteractModel>() {
        override fun itemsTheSame(oldItem: InteractModel, newItem: InteractModel): Boolean {
            return oldItem.imv == newItem.imv
        }

        override fun contentsTheSame(oldItem: InteractModel, newItem: InteractModel): Boolean {
            return oldItem.imv != newItem.imv || oldItem.check != newItem.check
        }

    }

    override fun bind(
        binding: ItemChooseInteractBinding,
        position: Int,
        data: InteractModel,
        holder: RecyclerView.ViewHolder
    ) {
        if (data.check) {
            binding.cv.strokeWidth = dpToPx(4f, binding.root.context).toInt()
        } else {
            binding.cv.strokeWidth = 0
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.tv.typeface = binding.root.context.resources.getFont(data.font)
        }
        Glide.with(binding.root).load(data.imv).into(binding.imv)
        changeGradientText2(binding.tv, data.color1, data.color2)
        binding.root.onSingleClick {
            onClick?.invoke(position)
        }
    }
}