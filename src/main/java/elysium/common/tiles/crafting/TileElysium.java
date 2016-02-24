package elysium.common.tiles.crafting;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * Created by dawar on 2016. 02. 06..
 */
public class TileElysium extends TileEntity {
    public TileElysium() {
    }

    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.readCustomNBT(nbt);
    }

    public void readCustomNBT(NBTTagCompound nbt) {
    }

    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        this.writeCustomNBT(nbt);
    }

    public void writeCustomNBT(NBTTagCompound nbt) {
    }

    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();
        this.writeCustomNBT(nbt);
        return new S35PacketUpdateTileEntity(this.getPos(), -999, nbt);
    }

    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        this.readCustomNBT(pkt.getNbtCompound());
    }

    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    public EnumFacing getFacing() {
        try {
            return EnumFacing.getFront(this.getBlockMetadata() & 7);
        } catch (Exception var2) {
            return EnumFacing.UP;
        }
    }

    public boolean gettingPower() {
        return this.worldObj.isBlockPowered(this.getPos());
    }
}