package com.Mod_Ores.Dimension.FrozenHearth;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

import com.Mod_Ores.soul_forest;
import com.Mod_Ores.Dimension.GenLayer.GenLayerFrozenHearth;
import com.Mod_Ores.Dimension.GenLayer.GenLayerSoulForest;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WorldChunkManagerFrozenHearth extends WorldChunkManager{
    //public static ArrayList<BiomeGenBase> allowedBiomes = new ArrayList<BiomeGenBase>(Arrays.asList(mod_Ores.SoulForest, mod_Ores.FrostCaves));
    private GenLayer myGenBiomes;
    private GenLayer myBiomeIndexLayer;
    private BiomeCache myBiomeCache;
    private List<BiomeGenBase> myBiomesToSpawnIn;

    public WorldChunkManagerFrozenHearth(){
	this.myBiomeCache = new BiomeCache(this);
	this.myBiomesToSpawnIn = new ArrayList<BiomeGenBase>();
	this.myBiomesToSpawnIn.add(soul_forest.FrozenPlains);
	this.myBiomesToSpawnIn.add(soul_forest.ShinaiForest);
    }

    public WorldChunkManagerFrozenHearth(long par1, WorldType par3WorldType){
	this();
	//GenLayer[] agenlayer = GenLayerMarona.initializeAllBiomeGenerators(par1, par3WorldType);
	//agenlayer = getModdedBiomeGenerators(par3WorldType, par1, agenlayer);
	GenLayer[] agenlayer = GenLayerFrozenHearth.makeTheWorld(par1);
	this.myGenBiomes = agenlayer[0];
	this.myBiomeIndexLayer = agenlayer[1];

    }

    public WorldChunkManagerFrozenHearth(World par1World){
	this(par1World.getSeed(), par1World.getWorldInfo().getTerrainType());
    }

    public List<BiomeGenBase> getBiomesToSpawnIn(){
	return this.myBiomesToSpawnIn;
    }

    public BiomeGenBase getBiomeGenAt(int par1, int par2){
	BiomeGenBase biome = this.myBiomeCache.getBiomeGenAt(par1, par2);
	if (biome == null){
	    return soul_forest.ShinaiForest;
	}

	return biome;
    }

    public float[] getRainfall(float[] par1ArrayOfFloat, int par2, int par3, int par4, int par5){
	IntCache.resetIntCache();

	if (par1ArrayOfFloat == null || par1ArrayOfFloat.length < par4 * par5){
	    par1ArrayOfFloat = new float[par4 * par5];
	}

	int[] aint = this.myBiomeIndexLayer.getInts(par2, par3, par4, par5);

	for (int i1 = 0; i1 < par4 * par5; ++i1){
	    float f = (float)BiomeGenBase.getBiomeGenArray()[aint[i1]].getIntRainfall() / 65536.0F;

	    if (f > 1.0F)
	    {
		f = 1.0F;
	    }

	    par1ArrayOfFloat[i1] = f;
	}

	return par1ArrayOfFloat;
    }

    @SideOnly(Side.CLIENT)

    public float getTemperatureAtHeight(float par1, int par2){
	return par1;
    }

    public float[] getTemperatures(float[] par1ArrayOfFloat, int par2, int par3, int par4, int par5){
	IntCache.resetIntCache();

	if (par1ArrayOfFloat == null || par1ArrayOfFloat.length < par4 * par5){
	    par1ArrayOfFloat = new float[par4 * par5];
	}

	int[] aint = this.myBiomeIndexLayer.getInts(par2, par3, par4, par5);

	for (int i1 = 0; i1 < par4 * par5; ++i1){
	    float f = (float)BiomeGenBase.getBiomeGenArray()[aint[i1]].getFloatTemperature(par2, par3, par4) / 65536.0F;

	    if (f > 1.0F){
		f = 1.0F;
	    }

	    par1ArrayOfFloat[i1] = f;
	}

	return par1ArrayOfFloat;
    }

    public BiomeGenBase[] getBiomesForGeneration(BiomeGenBase[] par1ArrayOfBiomeGenBase, int par2, int par3, int par4, int par5){
	IntCache.resetIntCache();

	if (par1ArrayOfBiomeGenBase == null || par1ArrayOfBiomeGenBase.length < par4 * par5){
	    par1ArrayOfBiomeGenBase = new BiomeGenBase[par4 * par5];
	}

	int[] aint = this.myGenBiomes.getInts(par2, par3, par4, par5);

	for (int i1 = 0; i1 < par4 * par5; ++i1){
	    if (aint[i1] >= 0) {
		par1ArrayOfBiomeGenBase[i1] = BiomeGenBase.getBiomeGenArray()[aint[i1]];
	    } 
	    else {
		par1ArrayOfBiomeGenBase[i1] = soul_forest.SoulForest;
	    }
	}

	return par1ArrayOfBiomeGenBase;
    }

    public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase[] par1ArrayOfBiomeGenBase, int par2, int par3, int par4, int par5){
	return this.getBiomeGenAt(par1ArrayOfBiomeGenBase, par2, par3, par4, par5, true);
    }

    public BiomeGenBase[] getBiomeGenAt(BiomeGenBase[] par1ArrayOfBiomeGenBase, int x, int y, int width, int length, boolean cacheFlag){
	IntCache.resetIntCache();

	if (par1ArrayOfBiomeGenBase == null || par1ArrayOfBiomeGenBase.length < width * length) {
	    par1ArrayOfBiomeGenBase = new BiomeGenBase[width * length];
	}

	if (cacheFlag && width == 16 && length == 16 && (x & 15) == 0 && (y & 15) == 0) {
	    BiomeGenBase[] abiomegenbase1 = this.myBiomeCache.getCachedBiomes(x, y);
	    System.arraycopy(abiomegenbase1, 0, par1ArrayOfBiomeGenBase, 0, width * length);
	    return par1ArrayOfBiomeGenBase;
	} 
	else {
	    int[] aint = this.myBiomeIndexLayer.getInts(x, y, width, length);

	    for (int i = 0; i < width * length; ++i) {
		if (aint[i] >= 0) {
		    par1ArrayOfBiomeGenBase[i] = BiomeGenBase.getBiomeGenArray()[aint[i]];
		} 
		else {
		    par1ArrayOfBiomeGenBase[i] = soul_forest.SoulForest;
		}
	    }

	    return par1ArrayOfBiomeGenBase;
	}
    }

    public boolean areBiomesViable(int par1, int par2, int par3, List par4List){
	IntCache.resetIntCache();
	int l = par1 - par3 >> 2;
	int i1 = par2 - par3 >> 2;
	int j1 = par1 + par3 >> 2;
	int k1 = par2 + par3 >> 2;
	int l1 = j1 - l + 1;
	int i2 = k1 - i1 + 1;
	int[] aint = this.myGenBiomes.getInts(l, i1, l1, i2);

	for (int j2 = 0; j2 < l1 * i2; ++j2){
	    BiomeGenBase biomegenbase = BiomeGenBase.getBiomeGenArray()[aint[j2]];

	    if (!par4List.contains(biomegenbase)){
		return false;
	    }
	}

	return true;
    }

    public ChunkPosition findBiomePosition(int par1, int par2, int par3, List par4List, Random par5Random){
	IntCache.resetIntCache();
	int l = par1 - par3 >> 2;
	int i1 = par2 - par3 >> 2;
	int j1 = par1 + par3 >> 2;
	int k1 = par2 + par3 >> 2;
	int l1 = j1 - l + 1;
	int i2 = k1 - i1 + 1;
	int[] aint = this.myGenBiomes.getInts(l, i1, l1, i2);
	ChunkPosition chunkposition = null;
	int j2 = 0;

	for (int k2 = 0; k2 < l1 * i2; ++k2){
	    int l2 = l + k2 % l1 << 2;
	    int i3 = i1 + k2 / l1 << 2;
	    BiomeGenBase biomegenbase = BiomeGenBase.getBiomeGenArray()[aint[k2]];

	    if (par4List.contains(biomegenbase) && (chunkposition == null || par5Random.nextInt(j2 + 1) == 0))
	    {
		chunkposition = new ChunkPosition(l2, 0, i3);
		++j2;
	    }
	}

	return chunkposition;
    }

    public void cleanupCache(){
	this.myBiomeCache.cleanupCache();
    }

    /*public GenLayer[] getModdedBiomeGenerators(WorldType worldType, long seed, GenLayer[] original)
    {
    	WorldTypeEvent.InitBiomeGens event = new WorldTypeEvent.InitBiomeGens(worldType, seed, original);
        MinecraftForge.TERRAIN_GEN_BUS.post(event);
        return event.newBiomeGens;
    }*/
}
