package com.nikpappas.algo.space;

import com.nikpappas.algo.logic.QuadTree2D;
import processing.core.PApplet;
import processing.core.PGraphics;

public class QuadTree2DSketch extends PApplet {

  private static final int PANEL_3D_W = 600;
  private static final int PANEL_3D_H = 600;

  private static final float LEVEL_H = 90.0f;

  public static void main(String[] args) {
    PApplet.main(Thread.currentThread().getStackTrace()[1].getClassName());
  }

  QuadTree2D tree = new QuadTree2D();

  PGraphics area3d;
  PGraphics area2d;

  float curX = 0;
  float curY = 0;

  float angle = 0;
  float minAngle = 0;
  float maxAngle = 3.14f / 2f;
  float angleIncD = 0.01f;
  float angleInc = angleIncD;

  int panel2dW = 0;

  @Override
  public void settings() {
    size(PANEL_3D_H*2, PANEL_3D_H, P3D);
  }

  @Override
  public void setup() {
    background(0);
    panel2dW = width - PANEL_3D_W;
    area3d = createGraphics(PANEL_3D_W, PANEL_3D_H, P3D);
    area2d = createGraphics(panel2dW, PANEL_3D_H, P2D);
    frameRate(10);
  }

  @Override
  public void mouseClicked() {
    tree.getRoot().add(QuadTree2D.Point.of(curX, curY));
  }

  @Override
  public void draw() {
    if (angle >= maxAngle) {
      angleInc = -angleIncD;
    }
    if (angle < minAngle) {
      angleInc = angleIncD;
    }
    angle += angleInc;

    background(50);
    curX = clamp(mouseX - PANEL_3D_W - panel2dW / 2f, -panel2dW / 2f, panel2dW / 2f);
    curY = clamp(mouseY - PANEL_3D_H / 2f, -PANEL_3D_H / 2f, PANEL_3D_H / 2f);

    drawArea3D();
    drawArea2D();
    image(area3d, 0, 0);
    image(area2d, PANEL_3D_W, 0);

    text("x:" + curX, width - 80, height - 20);
    text("y:" + curY, width - 40, height - 20);
  }

  // --- Left panel: 3D sphere tree ---

  private void drawArea3D() {
    float r = 500;
    float eyeX = r * sin(angle);
    float eyeZ = r * cos(angle);
    area3d.beginDraw();
    area3d.background(20);
    area3d.lights();
    area3d.perspective(PI / 3.0f, 1.0f, 1, 10000);
    area3d.camera(
            eyeX, -200, eyeZ,
            0, 100, 0,
            0, 1, 0
    );
    draw3DNode(tree.getRoot(), 0, -150, 0, 160);
    area3d.endDraw();
  }

  private void draw3DNode(QuadTree2D.QuadNode node, float cx, float cy, float cz, float spread) {
    float childY = cy + LEVEL_H;
    float h = spread / 2;

    area3d.stroke(80);
    if (node.n11 != null) area3d.line(cx, cy, cz, cx + h, childY, cz + h);
    if (node.n12 != null) area3d.line(cx, cy, cz, cx + h, childY, cz - h);
    if (node.n21 != null) area3d.line(cx, cy, cz, cx - h, childY, cz - h);
    if (node.n22 != null) area3d.line(cx, cy, cz, cx - h, childY, cz + h);

    area3d.pushMatrix();
    area3d.translate(cx, cy, cz);
    area3d.noStroke();
    boolean isLeaf = node.n11 == null && node.n12 == null && node.n21 == null && node.n22 == null;
    area3d.fill(isLeaf ? color(200, 80, 80) : color(80, 140, 200));
    area3d.sphere(max(4, spread / 10));
    area3d.popMatrix();

    if (node.n11 != null) draw3DNode(node.n11, cx + h, childY, cz + h, h);
    if (node.n12 != null) draw3DNode(node.n12, cx + h, childY, cz - h, h);
    if (node.n21 != null) draw3DNode(node.n21, cx - h, childY, cz - h, h);
    if (node.n22 != null) draw3DNode(node.n22, cx - h, childY, cz + h, h);
  }

  // --- Right panel: 2D cell boundary tree ---

  private void drawArea2D() {
    area2d.beginDraw();
    area2d.background(50);
    area2d.translate(panel2dW / 2.0f, PANEL_3D_H / 2.0f);
    area2d.noFill();
    area2d.stroke(100);
    drawNode2D(tree.getRoot());
    area2d.fill(200, 80, 80);
    area2d.noStroke();
    for (var p : tree.all()) {
      area2d.circle(p.x, p.y, 6);
    }
    area2d.endDraw();
  }

  private void drawNode2D(QuadTree2D.QuadNode node) {
    float vizHalf = panel2dW/2f;
    float ox = node.getOrigin().x;
    float oy = node.getOrigin().y;
    float hs = node.getHalfSize();
    if (ox + hs < -vizHalf || ox - hs > vizHalf) return;
    if (oy + hs < -vizHalf || oy - hs > vizHalf) return;
    float x1 = max(ox - hs, -vizHalf);
    float y1 = max(oy - hs, -vizHalf);
    float x2 = min(ox + hs, vizHalf);
    float y2 = min(oy + hs, vizHalf);
    area2d.rect(x1, y1, x2 - x1, y2 - y1);
    if (node.n11 != null) drawNode2D(node.n11);
    if (node.n12 != null) drawNode2D(node.n12);
    if (node.n21 != null) drawNode2D(node.n21);
    if (node.n22 != null) drawNode2D(node.n22);
  }

  private float clamp(float val, float min, float max) {
    return max(min, min(max, val));
  }
}
