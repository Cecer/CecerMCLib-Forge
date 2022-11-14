package com.cecer1.projects.mc.cecermclib.forge.modules.rendering.fbo;

import com.cecer1.projects.mc.cecermclib.forge.modules.rendering.context.RenderContext;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

//TODO: Consider throwing exception if not on render thread.
/**
 * Object-oriented wrapper for basic OpenGL FBOs with only a colour and depth buffer
 *
 * @see <a href="https://web.archive.org/web/20180302185611/http://git.voxelmodpack.com/voxellibs/VoxelCommon/blob/b43b5a91056ed033eb81a5071ab37b1c277b319c/java/com/voxelmodpack/common/gl/FBO.java" target="_BLANK">Adapted on VoxelCommon's FBO class</a>
 */
public class FBO {

	private FBOResources resources;

	private int width;
	private int height;

	/**
	 * Create a new FBO, the internal FBO itself is not created until the first call to begin() is made
	 */
	public FBO(int width, int height) {
		this.resources = new FBOResources();
		this.setSize(width, height);
		FBORef.track(this, this.resources);
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public boolean setSize(int width, int height) {
		if (this.resources == null) {
			throw new IllegalStateException("The FBO resources have been disposed!");
		}

		if (width <= 0 || height <= 0) {
			throw new IllegalArgumentException("An FBO cannot have zero or negative size");
		}
		
		boolean changed = false;
		if (this.width != width) {
			this.width = width;
			changed = true;
		}
		if (this.height != height) {
			this.height = height;
			changed = true;
		}
		return changed;
	}
	private void prepareToOpenSession() {
		if (this.resources == null) {
			throw new IllegalStateException("The FBO resources have been disposed!");
		}

		this.resources.ensureReady(this.width, this.height);
		if (!this.isReady()) {
			throw new RuntimeException("FBO is not ready after ensureReady!");
		}
	}
	/**
	 *
	 */
	public FBOSession openSession(RenderContext ctx) {
		this.prepareToOpenSession();
		return new FBOSession(this, this.resources, ctx);
	}
	public FBOSession openSession() {
		this.prepareToOpenSession();
		System.out.printf("Opening FBO session with texture %d%n", this.resources.getTextureId());
		return new FBOSession(this, this.resources);
	}

	/**
	 * Draw this FBO
	 */
	public void draw(int x, int y) {
		this.draw( x, y, this.resources.getBufferWidth(), this.resources.getBufferHeight(), 1.0f);
	}
	/**
	 * Draw this FBO
	 */
	public void draw(int x, int y, float alpha) {
		this.draw(x, y, this.resources.getBufferWidth(), this.resources.getBufferHeight(), alpha);
	}

	/**
	 * Draw this FBO
	 */
	public void draw(int atX, int atY, int width, int height, float alpha) {
		if (this.resources == null) {
			throw new IllegalStateException("The FBO resources have been disposed!");
		}

		if (!this.isReady()) {
			throw new IllegalStateException("FBO is not ready during draw!");
		}

		int x = atX;
		int x2 = atX + width;
		int y = atY;
		int y2 = atY + height;

		GlStateManager.bindTexture(this.resources.getTextureId());
		GlStateManager.color(1f, 1f, 1f, alpha);

		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
		worldrenderer.pos((float)x, (float)y2, 0f).tex(0f, 0f).endVertex();
		worldrenderer.pos((float)x2, (float)y2, 0f).tex(1f, 0f).endVertex();
		worldrenderer.pos((float)x2, (float)y, 0f).tex(1f, 1f).endVertex();
		worldrenderer.pos((float)x, (float)y, 0f).tex(0f, 1f).endVertex();
		tessellator.draw();
	}

	public boolean isReady() {
		if (this.resources == null) {
			throw new IllegalStateException("The FBO resources have been disposed!");
		}
		return this.resources.isReady();
	}

	/**
	 * Dispose of the resources of this FBO manually.
	 */
	public void dispose() {
		if (this.resources != null) {
			FBORef.manuallyDispose(this.resources);
			this.resources = null;
		}
	}

	protected static int getCurrentFrameBufferId() {
		return GL11.glGetInteger(GL30.GL_DRAW_FRAMEBUFFER_BINDING); // TODO: Add ARB and EXT support
	}
	protected static int getCurrentDepthBufferId() {
		return GL11.glGetInteger(GL30.GL_RENDERBUFFER_BINDING); // TODO: Add ARB and EXT support
	}
}