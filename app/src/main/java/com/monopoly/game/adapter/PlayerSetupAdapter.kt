package com.monopoly.game.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.monopoly.game.databinding.ItemPlayerSetupBinding

class PlayerSetupAdapter(
    private val playerNames: MutableList<String>,
    private val onRemoveClick: (Int) -> Unit
) : RecyclerView.Adapter<PlayerSetupAdapter.PlayerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val binding = ItemPlayerSetupBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlayerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        holder.bind(playerNames[position], position)
    }

    override fun getItemCount(): Int = playerNames.size

    inner class PlayerViewHolder(
        private val binding: ItemPlayerSetupBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(playerName: String, position: Int) {
            binding.tvPlayerName.text = playerName
            binding.tvPlayerNumber.text = "玩家 ${position + 1}"
            
            // 设置玩家颜色指示器
            val colors = listOf(
                android.graphics.Color.RED,
                android.graphics.Color.BLUE,
                android.graphics.Color.GREEN,
                android.graphics.Color.YELLOW
            )
            binding.viewPlayerColor.setBackgroundColor(colors[position % colors.size])
            
            binding.btnRemovePlayer.setOnClickListener {
                onRemoveClick(adapterPosition)
            }
        }
    }
}