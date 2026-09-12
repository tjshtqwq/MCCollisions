package com.hezhong.mccollisions.datatypes;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import com.hezhong.mccollisions.context.CollisionContext;
import lombok.Setter;

import java.util.List;

public class DynamicCollisionBox implements CollisionBox {

    private final CollisionContext context;
    private final CollisionFactory box;
    @Setter
    private ClientVersion version;
    @Setter
    private WrappedBlockState block;
    private int x, y, z;

    public DynamicCollisionBox(CollisionContext context, ClientVersion version, CollisionFactory box, WrappedBlockState block) {
        this.context = context;
        this.version = version;
        this.box = box;
        this.block = block;
    }

    // Untested but currently unused
    // *should* work because every single one of these eventually becomes a Complex, Simple, or NoCollision Box
//    @Override
    public CollisionBox union(SimpleCollisionBox other) {
        CollisionBox dynamicBox = box.fetch(context, version, block, x, y, z).offset(x, y, z);
        return dynamicBox.union(other);
    }

    @Override
    public boolean isCollided(SimpleCollisionBox other) {
        return box.fetch(context, version, block, x, y, z).offset(x, y, z).isCollided(other);
    }

    @Override
    public boolean isIntersected(SimpleCollisionBox other) {
        return box.fetch(context, version, block, x, y, z).offset(x, y, z).isIntersected(other);
    }

    @Override
    public CollisionBox copy() {
        return new DynamicCollisionBox(context, version, box, block).offset(x, y, z);
    }

    @Override
    public CollisionBox offset(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    @Override
    public int downCast(SimpleCollisionBox[] list) {
        return box.fetch(context, version, block, x, y, z).offset(x, y, z).downCast(list);
    }

    @Override
    public void downCast(List<SimpleCollisionBox> list) {
        box.fetch(context, version, block, x, y, z).offset(x, y, z).downCast(list);
    }

    @Override
    public boolean isNull() {
        return box.fetch(context, version, block, x, y, z).isNull();
    }

    @Override
    public boolean isFullBlock() {
        return box.fetch(context, version, block, x, y, z).offset(x, y, z).isFullBlock();
    }
}
