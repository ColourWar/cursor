package com.monopoly.game

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.monopoly.game.adapter.PlayerSetupAdapter
import com.monopoly.game.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var playerSetupAdapter: PlayerSetupAdapter
    private val playerNames = mutableListOf<String>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupUI()
        setupRecyclerView()
        setupListeners()
    }
    
    private fun setupUI() {
        // 初始化界面状态
        updateStartGameButton()
    }
    
    private fun setupRecyclerView() {
        playerSetupAdapter = PlayerSetupAdapter(playerNames) { position ->
            removePlayer(position)
        }
        
        binding.rvPlayers.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = playerSetupAdapter
        }
    }
    
    private fun setupListeners() {
        binding.btnAddPlayer.setOnClickListener {
            addPlayer()
        }
        
        binding.btnStartGame.setOnClickListener {
            startGame()
        }
        
        binding.etPlayerName.setOnEditorActionListener { _, _, _ ->
            addPlayer()
            true
        }
    }
    
    private fun addPlayer() {
        val name = binding.etPlayerName.text.toString().trim()
        
        when {
            name.isEmpty() -> {
                Toast.makeText(this, "请输入玩家姓名", Toast.LENGTH_SHORT).show()
            }
            playerNames.contains(name) -> {
                Toast.makeText(this, "玩家姓名已存在", Toast.LENGTH_SHORT).show()
            }
            playerNames.size >= 4 -> {
                Toast.makeText(this, "最多支持4个玩家", Toast.LENGTH_SHORT).show()
            }
            else -> {
                playerNames.add(name)
                playerSetupAdapter.notifyItemInserted(playerNames.size - 1)
                binding.etPlayerName.text.clear()
                updateStartGameButton()
                
                Toast.makeText(this, "玩家 $name 已添加", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun removePlayer(position: Int) {
        if (position < playerNames.size) {
            val removedName = playerNames.removeAt(position)
            playerSetupAdapter.notifyItemRemoved(position)
            updateStartGameButton()
            
            Toast.makeText(this, "玩家 $removedName 已移除", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun updateStartGameButton() {
        binding.btnStartGame.isEnabled = playerNames.size >= 2
    }
    
    private fun startGame() {
        if (playerNames.size >= 2) {
            val intent = Intent(this, GameActivity::class.java)
            intent.putStringArrayListExtra("player_names", ArrayList(playerNames))
            startActivity(intent)
        } else {
            Toast.makeText(this, "至少需要2个玩家才能开始游戏", Toast.LENGTH_SHORT).show()
        }
    }
}