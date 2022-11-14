package com.cecer1.projects.mc.cecermclib.forge.modules.rendering.fbo;

import net.minecraft.client.renderer.texture.TextureUtil;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;

class FBOResources {

    /**
     * Handle to the frame buffer resource
     */
    private int frameBufferId;

    /**
     * Handle to the depth buffer resource
     */
    private int depthBufferId;

    /**
     * The ID of the texture that the FBO will render to
     */
    private int textureId = -1;

    /**
     * Width for the current frame buffer, used so we know to regenerate it if the frame size changes
     */
    private int bufferWidth = -1;
    /**
     * Height for the current frame buffer, used so we know to regenerate it if the frame size changes
     */
    private int bufferHeight = -1;

    public int getFrameBufferId() {
        return this.frameBufferId;
    }
    public int getDepthBufferId() {
        return this.depthBufferId;
    }
    public int getTextureId() {
        return this.textureId;
    }
    public int getBufferWidth() {
        return this.bufferWidth;
    }
    public int getBufferHeight() {
        return this.bufferHeight;
    }

    protected FBOResources() {
    }

    protected boolean ensureReady(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("An FBO cannot have zero or negative size");
        }

        if (width != this.bufferWidth || height != this.bufferHeight) {
            // Size changed - Dispose the old resources
            this.dispose();
        }

        if (this.isReady()) {
            // Already ready. Nothing to do.
            return false;
        }

        this.bufferWidth = width;
        this.bufferHeight = height;

        this.textureId = TextureUtil.glGenTextures();
        TextureUtil.allocateTextureImpl(this.textureId, 0, this.bufferWidth, this.bufferHeight);

        int restoreFrameBufferId = FBO.getCurrentFrameBufferId();
        int restoreDepthBufferId = FBO.getCurrentDepthBufferId();
        
        this.frameBufferId = GL30.glGenFramebuffers();
        this.depthBufferId = GL30.glGenRenderbuffers();

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, this.frameBufferId);
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, this.textureId, 0);

        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, this.depthBufferId);
        GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL14.GL_DEPTH_COMPONENT24, this.bufferWidth, this.bufferHeight); // .
        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, this.depthBufferId); // .
        
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, restoreFrameBufferId);
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, restoreDepthBufferId);
        
        return true;
    }

    public void dispose() {
        if (this.frameBufferId != -1) {
            GL30.glDeleteFramebuffers(this.frameBufferId);
            this.frameBufferId = -1;
        }
        if (this.depthBufferId != -1) {
            GL30.glDeleteRenderbuffers(this.depthBufferId);
            this.depthBufferId = -1;
        }
        if (this.textureId != -1) {
            TextureUtil.deleteTexture(this.textureId);
            this.textureId = -1;
        }
    }

    public boolean isReady() {
        // TODO: Add check for FBO support
//        if (!OpenGlHelper.framebufferSupported) {
//            return false;
//        }

        if (this.frameBufferId == -1) {
            return false;
        }
        if (this.depthBufferId == -1) {
            return false;
        }
        if (this.textureId == -1) {
            return false;
        }
        return true;
    }
}