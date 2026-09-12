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
import com.hezhong.mccollisions.datatypes.ComplexCollisionBox;
import com.hezhong.mccollisions.datatypes.SimpleCollisionBox;

public class DynamicCollisionPane extends DynamicConnecting implements CollisionFactory {

    private static final CollisionBox[] COLLISION_BOXES = makeShapes(1.0F, 1.0F, 16.0F, 0.0F, 16.0F, true, 1);

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

        // On 1.7 and 1.8 clients, and 1.13+ clients on 1.7 and 1.8 servers, the glass pane is + instead of |
        if (!north && !south && !east && !west && (version.isOlderThanOrEquals(ClientVersion.V_1_8) || (context.serverVersion().isOlderThanOrEquals(ServerVersion.V_1_8_8) && version.isNewerThanOrEquals(ClientVersion.V_1_13)))) {
            north = south = east = west = true;
        }

        if (version.isNewerThanOrEquals(ClientVersion.V_1_9)) {
            return COLLISION_BOXES[getAABBIndex(north, east, south, west)].copy();
        } else { // 1.8 and below clients have pane bounding boxes one pixel less
            ComplexCollisionBox boxes = new ComplexCollisionBox(2);
            if ((!west || !east) && (west || east || north || south)) {
                if (west) {
                    boxes.add(new SimpleCollisionBox(0.0F, 0.0F, 0.4375F, 0.5F, 1.0F, 0.5625F));
                } else if (east) {
                    boxes.add(new SimpleCollisionBox(0.5F, 0.0F, 0.4375F, 1.0F, 1.0F, 0.5625F));
                }
            } else {
                boxes.add(new SimpleCollisionBox(0.0F, 0.0F, 0.4375F, 1.0F, 1.0F, 0.5625F));
            }

            if ((!north || !south) && (west || east || north || south)) {
                if (north) {
                    boxes.add(new SimpleCollisionBox(0.4375F, 0.0F, 0.0F, 0.5625F, 1.0F, 0.5F));
                } else if (south) {
                    boxes.add(new SimpleCollisionBox(0.4375F, 0.0F, 0.5F, 0.5625F, 1.0F, 1.0F));
                }
            } else {
                boxes.add(new SimpleCollisionBox(0.4375F, 0.0F, 0.0F, 0.5625F, 1.0F, 1.0F));
            }

            return boxes;
        }
    }

    @Override
    public boolean canConnectToGlassBlock() {
        return true;
    }

    @Override
    public boolean checkCanConnect(CollisionContext context, WrappedBlockState state, StateType one, StateType two, BlockFace direction) {
        if (BlockTags.GLASS_PANES.contains(one) || one == StateTypes.IRON_BARS || one == StateTypes.CHAIN && context.clientVersion().isOlderThan(ClientVersion.V_1_16))
            return true;
        else
            return CollisionData.getData(one).getMovementCollisionBox(context, context.clientVersion(), state, 0, 0, 0).isSideFullBlock(direction);
    }
}
