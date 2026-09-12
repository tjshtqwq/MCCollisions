package com.hezhong.mccollisions.blocks.connecting;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.world.BlockFace;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import com.github.retrooper.packetevents.protocol.world.states.defaulttags.BlockTags;
import com.github.retrooper.packetevents.protocol.world.states.enums.East;
import com.github.retrooper.packetevents.protocol.world.states.enums.North;
import com.github.retrooper.packetevents.protocol.world.states.enums.South;
import com.github.retrooper.packetevents.protocol.world.states.enums.West;
import com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import com.hezhong.mccollisions.CollisionData;
import com.hezhong.mccollisions.context.CollisionContext;
import com.hezhong.mccollisions.datatypes.CollisionBox;
import com.hezhong.mccollisions.datatypes.CollisionFactory;

public class DynamicCollisionFence extends DynamicConnecting implements CollisionFactory {
    private static final CollisionBox[] COLLISION_BOXES = makeShapes(2.0F, 2.0F, 24.0F, 0.0F, 24.0F, true, 1);

    @Override
    public CollisionBox fetch(CollisionContext context, ClientVersion version, WrappedBlockState block, int x, int y, int z) {
        boolean east;
        boolean north;
        boolean south;
        boolean west;

        // 1.13+ servers on 1.13+ clients send the full fence data
        if (context.serverVersion().isNewerThanOrEquals(ServerVersion.V_1_13)
                && version.isNewerThanOrEquals(ClientVersion.V_1_13)) {
            east = block.getEast() != East.FALSE;
            north = block.getNorth() != North.FALSE;
            south = block.getSouth() != South.FALSE;
            west = block.getWest() != West.FALSE;
        } else {
            east = connectsTo(context, version, x, y, z, BlockFace.EAST);
            north = connectsTo(context, version, x, y, z, BlockFace.NORTH);
            south = connectsTo(context, version, x, y, z, BlockFace.SOUTH);
            west = connectsTo(context, version, x, y, z, BlockFace.WEST);
        }

        return COLLISION_BOXES[getAABBIndex(north, east, south, west)].copy();
    }

    @Override
    public boolean checkCanConnect(CollisionContext context, WrappedBlockState state, StateType one, StateType two, BlockFace direction) {
        if (BlockTags.FENCES.contains(one))
            return !(one == StateTypes.NETHER_BRICK_FENCE) && !(two == StateTypes.NETHER_BRICK_FENCE);
        else
            return BlockTags.FENCES.contains(one) || CollisionData.getData(one).getMovementCollisionBox(context, context.clientVersion(), state, 0, 0, 0).isSideFullBlock(direction);
    }
}
