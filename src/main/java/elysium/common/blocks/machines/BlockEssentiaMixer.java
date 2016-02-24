package elysium.common.blocks.machines;

import elysium.common.blocks.BlockElysiumMachine;
import elysium.common.tiles.TileEssentiaMixer;
import net.minecraft.block.material.Material;

/**
 * Created by dawar on 2016. 02. 07..
 */
public class BlockEssentiaMixer extends BlockElysiumMachine {
    public BlockEssentiaMixer() {
        super(Material.rock, TileEssentiaMixer.class);
        this.setHardness(0.3F);
        this.setStepSound(soundTypeStone);
    }
}
