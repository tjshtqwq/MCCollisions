# MC Collisions
A minecraft java editon's collisions engine, including block's collision/hit shapes, entity collision box, moving collisions box.  
Include a collision box util interface, and simple/complex/hex/no collisions impls.  
### Skidded from Grim Anticheat, most work by Deepseek V4.1 Flash.

## How to use?
This project depends on PacketEvents.
Impl the `CollisionContext` interface to provide basic information (world blocks, vehicle, sneaking, etc.)
And, use the `McCollisions` main class to use.
