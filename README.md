# MC Collisions
A Minecraft Java Editon's collision engine, including blocks' collision/hit shapes, entity collision box, moving collision boxes.  
Include a collision box util interface, and simple/complex/hex/no collisions impls.  
### Skidded from Grim Anticheat, and most code is coded by Deepseek V4.1 Flash.

## How to use?
This project depends on PacketEvents.
Impl the `CollisionContext` interface to provide basic information (world blocks, vehicle, sneaking, etc.)
And, use the `McCollisions` main class to use.
