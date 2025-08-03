package com.monopoly.game.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.monopoly.game.databinding.ItemPlayerInfoBinding
import com.monopoly.game.model.Player

class PlayerInfoAdapter(
    private val players: List<Player>
) : RecyclerView.Adapter<PlayerInfoAdapter.PlayerInfoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerInfoViewHolder {
        val binding = ItemPlayerInfoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlayerInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlayerInfoViewHolder, position: Int) {
        holder.bind(players[position])
    }

    override fun getItemCount(): Int = players.size

    inner class PlayerInfoViewHolder(
        private val binding: ItemPlayerInfoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(player: Player) {
            binding.tvPlayerName.text = player.name
            binding.tvPlayerMoney.text = "$${player.money}"
            binding.tvPlayerProperties.text = "房产: ${player.ownedProperties.size}"
            binding.tvPlayerAssets.text = "总资产: $${player.getTotalAssets()}"
            
            binding.viewPlayerColor.setBackgroundColor(player.color)
            
            // 设置玩家状态
            when {
                player.isBankrupt -> {
                    binding.tvPlayerStatus.text = "破产"
                    binding.root.alpha = 0.5f
                }
                player.isInJail -> {
                    binding.tvPlayerStatus.text = "监狱 (${3 - player.jailTurns}回合)"
                    binding.root.alpha = 0.8f
                }
                else -> {
                    binding.tvPlayerStatus.text = "正常"
                    binding.root.alpha = 1.0f
                }
            }
            
            // 显示拥有的房产
            val propertyNames = player.ownedProperties.take(3).joinToString(", ") { it.name }
            val moreProperties = if (player.ownedProperties.size > 3) "..." else ""
            binding.tvPropertyList.text = if (propertyNames.isNotEmpty()) {
                "$propertyNames$moreProperties"
            } else {
                "无房产"
            }
        }
    }
}