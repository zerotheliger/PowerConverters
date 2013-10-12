package powercrystals.powerconverters.position;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public class BlockPosition {
    public int x;
    public int y;
    public int z;
    public ForgeDirection orientation;

    public BlockPosition(int x, int y, int z) {
        this(x, y, z, ForgeDirection.UNKNOWN);
    }

    public BlockPosition(TileEntity tile) {
        this(tile.xCoord, tile.yCoord, tile.zCoord, ForgeDirection.UNKNOWN);
    }

    public BlockPosition(int x, int y, int z, ForgeDirection orientation) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.orientation = orientation;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void moveRight(int step) {
        switch (orientation) {
            case SOUTH:
                x -= step;
                break;
            case NORTH:
                x += step;
                break;
            case EAST:
                z += step;
                break;
            case WEST:
                z -= step;
                break;
            default:
                break;
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public void moveLeft(int step) {
        moveRight(-step);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void moveForwards(int step) {
        switch (orientation) {
            case UP:
                y += step;
                break;
            case DOWN:
                y -= step;
                break;
            case SOUTH:
                z += step;
                break;
            case NORTH:
                z -= step;
                break;
            case EAST:
                x += step;
                break;
            case WEST:
                x -= step;
                break;
            default:
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public void moveBackwards(int step) {
        moveForwards(-step);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void moveUp(int step) {
        switch (orientation) {
            case EAST:
            case WEST:
            case NORTH:
            case SOUTH:
                y += step;
                break;
            default:
                break;
        }

    }

    @SuppressWarnings("UnusedDeclaration")
    public void moveDown(int step) {
        moveUp(-step);
    }

    @Override
    public String toString() {
        if (orientation == null) {
            return "{" + x + ", " + y + ", " + z + "}";
        }
        return "{" + x + ", " + y + ", " + z + ";" + orientation.toString() + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BlockPosition)) {
            return false;
        }
        BlockPosition bp = (BlockPosition) obj;
        return bp.x == x && bp.y == y && bp.z == z && bp.orientation == orientation;
    }

    @Override
    public int hashCode() {
        return (x & 0xFFF) | (y & 0xFF << 8) | (z & 0xFFF << 12);
    }

    public List<BlockPosition> getAdjacent(boolean includeVertical) {
        List<BlockPosition> a = new ArrayList<BlockPosition>();
        a.add(new BlockPosition(x + 1, y, z, ForgeDirection.EAST));
        a.add(new BlockPosition(x - 1, y, z, ForgeDirection.WEST));
        a.add(new BlockPosition(x, y, z + 1, ForgeDirection.SOUTH));
        a.add(new BlockPosition(x, y, z - 1, ForgeDirection.NORTH));
        if (includeVertical) {
            a.add(new BlockPosition(x, y + 1, z, ForgeDirection.UP));
            a.add(new BlockPosition(x, y - 1, z, ForgeDirection.DOWN));
        }
        return a;
    }

    public static TileEntity getAdjacentTileEntity(TileEntity start, ForgeDirection direction) {
        BlockPosition p = new BlockPosition(start);
        p.orientation = direction;
        p.moveForwards(1);
        return start.worldObj.getBlockTileEntity(p.x, p.y, p.z);
    }
}