package com.fexemu.steamlauncher.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fexemu.steamlauncher.databinding.ItemGameBinding
import com.fexemu.steamlauncher.steam.models.SteamGame

/**
 * RecyclerView adapter for Steam games
 */
class GamesAdapter(
    private val games: List<SteamGame>,
    private val onGameClick: (SteamGame) -> Unit
) : RecyclerView.Adapter<GamesAdapter.GameViewHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val binding = ItemGameBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GameViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        holder.bind(games[position])
    }
    
    override fun getItemCount(): Int = games.size
    
    inner class GameViewHolder(
        private val binding: ItemGameBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(game: SteamGame) {
            binding.gameName.text = game.name
            binding.gamePlaytime.text = "Playtime: ${game.playtimeForever} minutes"
            
            // Set click listener
            binding.root.setOnClickListener {
                onGameClick(game)
            }
            
            binding.btnPlay.setOnClickListener {
                onGameClick(game)
            }
            
            // Show download progress if downloading
            if (game.downloadProgress > 0 && game.downloadProgress < 100) {
                binding.downloadProgress.visibility = android.view.View.VISIBLE
                binding.downloadProgress.progress = game.downloadProgress.toInt()
            } else {
                binding.downloadProgress.visibility = android.view.View.GONE
            }
        }
    }
}
