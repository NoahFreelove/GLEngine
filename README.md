# GL Engine

A OpenGL based Java game engine.

Check out `src/Example/SuzanneExample.java` to see a demo.

**WASD + Mouse for Camera Movement.**

**Arrow Keys + Shift for Monkey Movement**

=================================================



## GLEngine Structure

GLEngine works on a `GameObject + Component` structure. 

The `GameObject` class is final and to add behavior to GameObjects you can create `Components` which you can attach to
each GameObject

`GameObjects` have `MeshRenderer` components which render the mesh you provide it and a texture _(supports png, bmp, jpg, dds)_. 

_(Currently only support simple OBJ files, but you can also provide your own vertices, uvs, and normal data)_.

Physics and Collision is also Supported with the (partial) integration of JBullet. Simply Create a `Rigidbody` component
or a `Collider` component and add it to your `GameObject`.

Every component is Rendered then Updated once per frame.

================================================

### Credits
Created using the [LWJGL](https://www.lwjgl.org/) library.

Physics done with [JBullet](http://jbullet.advel.cz/).

Thanks to [Blunderchips](https://github.com/Blunderchips/LWJGL-OBJ-Loader) for the OBJ file and loader code.

Thanks to [Mudbill](https://github.com/Mudbill/dds-lwjgl) for the DDS file and loader code.


Engine by [Noah Freelove](https://github.com/NoahFreelove/GLEngine).