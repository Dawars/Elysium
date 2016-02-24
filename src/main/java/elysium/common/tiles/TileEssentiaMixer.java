package elysium.common.tiles;

import elysium.common.Elysium;
import elysium.common.tiles.crafting.TileElysium;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import thaumcraft.api.aspects.*;


/**
 * Created by dawar on 2016. 02. 07..
 */
public class TileEssentiaMixer extends TileElysium implements IAspectSource, IEssentiaTransport, ITickable {

    public Aspect aspectOut = null;
    public Aspect aspectIn1 = null;
    public Aspect aspectIn2 = null;
    int count = 0;
    int process = 0;

    public Aspect currentSuction = null;
    public Aspect currentRecipe = null;

    float rotationSpeed = 0.0F;
    public float rotation = 0.0F;

    public void readCustomNBT(NBTTagCompound nbttagcompound) {
        this.aspectIn1 = Aspect.getAspect(nbttagcompound.getString("aspectIn1"));
        this.aspectIn2 = Aspect.getAspect(nbttagcompound.getString("aspectIn2"));
        this.aspectOut = Aspect.getAspect(nbttagcompound.getString("aspectOut"));
    }

    public void writeCustomNBT(NBTTagCompound nbttagcompound) {
        if(this.aspectIn1 != null) {
            nbttagcompound.setString("aspectIn1", this.aspectIn1.getTag());
        }
        if(this.aspectIn2 != null) {
            nbttagcompound.setString("aspectIn2", this.aspectIn2.getTag());
        }

        if(this.aspectOut != null) {
            nbttagcompound.setString("aspectOut", this.aspectOut.getTag());
        }
    }

    public AspectList getAspects() {
        AspectList al = new AspectList();
        if(this.aspectOut != null) {
            al.add(this.aspectOut, 1);
        }

        return al;
    }

    public void setAspects(AspectList aspects) {
        Elysium.log.info("setAspects: " + aspects.toString());

    }

    public boolean doesContainerAccept(Aspect tag) {
        boolean flag = tag.isPrimal();
        Elysium.log.info("doesContainerAccept: " + tag.getName());
        return false;
    }

    public int addToContainer(Aspect tag, int am) {

        if(am > 0 && this.aspectOut == null) {
            this.aspectOut = tag;
            this.markDirty();
            this.worldObj.markBlockForUpdate(this.getPos());
            --am;
        }
        Elysium.log.info("addToContainer: " + tag.getName() + " amount: " + am);

        return am;
    }

    public boolean doesContainerContainAmount(Aspect tag, int amount) {//other emptying calls
        boolean flag = tag == aspectOut && amount == 1;

//        if(!tag.isPrimal()){//compound
//            this.currentRecipe = tag;
//
//            Aspect aspect1 = tag.getComponents()[0];
//            Aspect aspect2 = tag.getComponents()[1];
//
//            //check components
////            AspectSourceHelper.findEssentia(this, aspect1, 1)
//
//            //set suction
//            if(aspect1 == this.aspectIn1 || aspect1 == this.aspectIn2){//needs 1 ingredient
//                this.setSuction(aspect2, 1);
//            } else if(aspect2 == this.aspectIn1 || aspect2 == this.aspectIn2){// 0 needed
//                this.setSuction(null, 0);
//                flag = true;
//            } else {//2 needed
//                this.setSuction(aspect1, 1);
//            }
//        }


        Elysium.log.info("doesContainerContainAmount: " + tag.getName() + " amount: " + amount + " > " + flag);
        return flag;
    }

    public boolean takeFromContainer(Aspect tag, int amount) {//other emptying calls after doesContainerContainAmount
        //collect components (wait)
        boolean flag = aspectIn1 != null && aspectIn2 != null;
        if(amount <= 1) {
            if(flag){
                aspectOut = currentRecipe;
                aspectIn1 = aspectIn2 = currentRecipe = null;
                flag = true;
            }
        }
        Elysium.log.info("takeFromContainer: " + tag.getName() + " amount: " + amount + " > " + flag);

        return flag;
    }

    public boolean takeFromContainer(AspectList ot) {
        boolean flag = aspectIn1 != null && aspectIn2 != null;
        Elysium.log.info("takeFromContainer: " + ot.getAspects().toString() + " > " + flag);

        return flag;
    }

    public boolean doesContainerContain(AspectList ot) {
        boolean flag = false;

        for(Aspect tt : ot.getAspects()) {
            if(tt == this.aspectOut) {
                flag = true;
                break;
            }
        }

        Elysium.log.info("doesContainerContain: " + ot.toString() + " > ");
        return flag;
    }

    public int containerContains(Aspect tag) {
        Elysium.log.info("containerContains: " + tag.getName());
        return tag == aspectOut ? 1 : 0;
    }

    @Override
    public void update() {
        if(!this.worldObj.isRemote) {
            if(!this.gettingPower()) {
//                if(this.aspectOut == null && this.aspectIn == null && ++this.count % 5 == 0) {
//                    this.drawEssentia();
//                }
//
//                if(this.process > 0) {
//                    --this.process;
//                }
//
//                if(this.aspectOut == null && this.aspectIn != null && this.process == 0) {
//                    this.processEssentia();
//                }
            }
        } else {
//            if(this.aspectIn != null && !this.gettingPower() && this.rotationSpeed < 20.0F) {
//                this.rotationSpeed += 2.0F;
//            }
//
//            if((this.aspectIn == null || this.gettingPower()) && this.rotationSpeed > 0.0F) {
//                this.rotationSpeed -= 0.5F;
//            }
//
//            int pr = (int)this.rotation;
//            this.rotation += this.rotationSpeed;
//            if(this.rotation % 180.0F <= 20.0F && pr % 180 >= 160 && this.rotationSpeed > 0.0F) {
//                this.worldObj.playSound((double)this.getPos().getX() + 0.5D, (double)this.getPos().getY() + 0.5D, (double)this.getPos().getZ() + 0.5D, "thaumcraft:pump", 1.0F, 1.0F, false);
//            }
        }

    }

    @Override
    public boolean isConnectable(EnumFacing enumFacing) {//adjecent emptying 1
        boolean flag = true;

        Elysium.log.info("isConnectable: " + enumFacing.getName() + " > " + flag);

        return flag;
    }

    @Override
    public boolean canInputFrom(EnumFacing enumFacing) {//adjecent emptying 2
        boolean flag = enumFacing.getAxis() != EnumFacing.Axis.Y;

        Elysium.log.info("canInputFrom: " + enumFacing.getName() + " > " + flag);

        return flag;
    }

    @Override
    public boolean canOutputTo(EnumFacing enumFacing) {
        boolean flag = enumFacing.getAxis() == EnumFacing.Axis.Y;

        Elysium.log.info("canOutputTo: " + enumFacing.getName() + " > " + flag);

        return flag;
    }

    @Override
    public void setSuction(Aspect aspect, int i) {
        if(aspect != null)
            Elysium.log.info("setSuction: " + aspect.getName() + " " + i);
        else
            Elysium.log.info("setSuction: null");

        this.currentSuction = aspect;
    }

    @Override
    public Aspect getSuctionType(EnumFacing enumFacing) {
        Elysium.log.info("getSuctionType: > NULL");

        return null;
    }

    @Override
    public int getSuctionAmount(EnumFacing enumFacing) {//adjecent emptying 3
        int flag = 0;
        Elysium.log.info("getSuctionAmount: " + enumFacing.getName() + " > " + flag);

        return flag;
    }

    @Override
    public int takeEssentia(Aspect aspect, int i, EnumFacing enumFacing) {
        int flag = 0;
        Elysium.log.info("takeEssentia: " + aspect.getName() + " " + i + enumFacing.getName() + " " + " > " + flag);

        return flag;
    }

    @Override
    public int addEssentia(Aspect aspect, int i, EnumFacing enumFacing) {//adjecent emptying last
        int flag = 0;

        Elysium.log.info("addEssentia: " + aspect.getName() + " " + i +" " + enumFacing.getName() + " > " + flag);
        return flag;
    }

    @Override
    public Aspect getEssentiaType(EnumFacing enumFacing) {
        Aspect flag = Aspect.EARTH;

        Elysium.log.info("getEssentiaType: " + enumFacing.getName() + " > " + flag);

        return flag;
    }

    @Override
    public int getEssentiaAmount(EnumFacing enumFacing) {
        int flag = 0;

        Elysium.log.info("getEssentiaAmount: " + enumFacing.getName() + " > " + flag);

        return flag;
    }

    @Override
    public int getMinimumSuction() {
        int flag = 0;

        Elysium.log.info("getMinimumSuction: " + " > " + flag);

        return flag;    }
}
