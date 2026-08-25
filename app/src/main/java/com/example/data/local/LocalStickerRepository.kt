package com.example.data.local

import android.content.Context
import com.example.data.StickerRepository
import com.example.data.model.Artist
import com.example.data.model.Catalog
import com.example.data.model.Category
import com.example.data.model.Pack
import com.example.data.model.Sticker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class LocalStickerRepository(private val context: Context) : StickerRepository {
    private val _catalog = MutableStateFlow<Catalog?>(null)

    init {
        loadCatalog()
    }

    private fun loadCatalog() {
        try {
            val jsonString = context.assets.open("catalog.json").bufferedReader().use { it.readText() }
            val catalog = Json { ignoreUnknownKeys = true }.decodeFromString<Catalog>(jsonString)
            _catalog.value = catalog
        } catch (e: Exception) {
            e.printStackTrace()
            _catalog.value = Catalog(emptyList(), emptyList(), emptyList())
        }
    }

    override fun getPacks(): Flow<List<Pack>> = _catalog.map { it?.packs ?: emptyList() }

    override fun getPack(id: String): Flow<Pack?> = _catalog.map { cat -> cat?.packs?.find { it.id == id } }

    override fun getPackBySlug(slug: String): Flow<Pack?> = _catalog.map { cat -> cat?.packs?.find { it.slug == slug } }

    override fun getStickersForPack(packId: String): Flow<List<Sticker>> = _catalog.map { cat ->
        cat?.stickers?.filter { it.packId == packId }?.sortedBy { it.index } ?: emptyList()
    }

    override fun getArtist(artistId: String): Flow<Artist?> = _catalog.map { cat ->
        cat?.artists?.find { it.id == artistId }
    }

    override fun getCategories(): Flow<List<Category>> = _catalog.map { cat ->
        val packs = cat?.packs ?: return@map emptyList()
        val tags = packs.flatMap { it.tags }.distinct()
        val types = packs.map { it.type }.distinct()
        val dialects = packs.map { it.dialect }.distinct()
        
        val allCategories = mutableListOf<Category>()
        
        // Tags
        tags.forEach { tag ->
            val matchingPacks = packs.filter { it.tags.contains(tag) }
            if (matchingPacks.isNotEmpty()) {
                allCategories.add(Category(tag, tag, tag, matchingPacks.size, matchingPacks.first().id))
            }
        }
        
        // Types
        types.forEach { type ->
            val matchingPacks = packs.filter { it.type == type }
            if (matchingPacks.isNotEmpty()) {
                val nameAr = if (type == "animated") "متحرك" else "ثابت"
                allCategories.add(Category("type_$type", nameAr, type, matchingPacks.size, matchingPacks.first().id))
            }
        }
        
        // Dialects
        dialects.forEach { dialect ->
            val matchingPacks = packs.filter { it.dialect == dialect }
            if (matchingPacks.isNotEmpty()) {
                val nameAr = when(dialect) {
                    "egyptian" -> "مصري"
                    "gulf" -> "خليجي"
                    "levantine" -> "شامي"
                    "msa" -> "فصحى"
                    else -> dialect
                }
                allCategories.add(Category("dialect_$dialect", nameAr, dialect, matchingPacks.size, matchingPacks.first().id))
            }
        }
        
        allCategories.sortedByDescending { it.packCount }
    }

    override suspend fun refresh() {
        withContext(Dispatchers.IO) {
            loadCatalog()
        }
    }
}
