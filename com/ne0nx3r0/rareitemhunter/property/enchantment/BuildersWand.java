package com.ne0nx3r0.rareitemhunter.property.enchantment;

import com.ne0nx3r0.rareitemhunter.property.ItemProperty;
import com.ne0nx3r0.rareitemhunter.property.ItemPropertyTypes;
import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class BuildersWand extends ItemProperty
{
    public BuildersWand()
    {
        super(ItemPropertyTypes.ENCHANTMENT,"Builder's Wand","",2,1);
    }
    
    @Override
    public boolean onInteract(PlayerInteractEvent e,int level)
    {
        int maxCopied = level * 5;
        
        if(e.getClickedBlock() != null)
        {
            Block clickedBlock = e.getClickedBlock();
            BlockFace baseFace = e.getBlockFace();
            Material type = clickedBlock.getType();
            
            ArrayList<Block> blocksToBuildOn = this.addBlocksToBuildOn(new ArrayList<Block>(), clickedBlock, baseFace,maxCopied);
            
            PlayerInventory inventory = e.getPlayer().getInventory();
            
            int changedBlocks = 0;
            
            for(Block b : blocksToBuildOn) {
                ItemStack is = new ItemStack(type,1,(short) 0,b.getData());
                
                HashMap<Integer, ItemStack> leftovers = inventory.removeItem(is);
                
                System.out.println(leftovers);
                    
                if(leftovers.isEmpty()) {
                    Block bFace = b.getRelative(baseFace);
                    
                    bFace.setType(is.getType());
                    bFace.setData(is.getData().getData());
                    
                    changedBlocks++;
                }        
                else {
                    break;
                }
            }
            
            return changedBlocks > 0;
        }
        
        return false;
    }
    
    public ArrayList<Block> addBlocksToBuildOn(ArrayList<Block> blocksToChange,Block startingPoint,BlockFace baseFace,int maxCopied) {
        BlockFace[] affectedFaces = this.getAffectedBlockFaces(baseFace);
        Material baseMaterial = startingPoint.getType();
        
        for(BlockFace face : affectedFaces) {
            if(blocksToChange.size() >= maxCopied) {
                break;
            }
            
            Block b = startingPoint.getRelative(face);
            
            if(!blocksToChange.contains(b) 
             && b.getType().equals(baseMaterial)
             && b.getRelative(baseFace).getType().equals(Material.AIR)) {
                blocksToChange.add(b);
                
                blocksToChange = this.addBlocksToBuildOn(blocksToChange, b, baseFace, maxCopied);
            }
        }
        
        return blocksToChange;
    }
    
    
    
    public BlockFace[] getAffectedBlockFaces(BlockFace bf) {
        switch(bf) {
            case DOWN:
            case UP:
                return new BlockFace[]{BlockFace.NORTH,BlockFace.EAST,BlockFace.SOUTH,BlockFace.WEST};
            case NORTH:
            case SOUTH:
                return new BlockFace[]{BlockFace.UP,BlockFace.EAST,BlockFace.DOWN,BlockFace.WEST};
            case EAST:
            case WEST:
                return new BlockFace[]{BlockFace.UP,BlockFace.NORTH,BlockFace.DOWN,BlockFace.SOUTH};
        }
        return null;
    }
}