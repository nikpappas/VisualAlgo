package com.nikpappas.algo.logic;

import java.util.ArrayList;
import java.util.List;

public class QuadTree2D {

//  QuadNode root = new QuadNode(Point.of(0, 0), Float.MAX_VALUE / 2);
  QuadNode root = new QuadNode(Point.of(0, 0), 400);
  private static final int limit = 2;


  public static class Point {
    private Point(float x, float y) {
      this.x = x;
      this.y = y;
    }

    public final float x;
    public final float y;

    public static Point of(float x, float y) {
      return new Point(x, y);
    }
  }

  public static class QuadNode {
    QuadNode(Point origin, float halfSize) {
      this.origin = origin;
      this.halfSize = halfSize;
    }

    private Point origin;
    private float halfSize;
    public List<Point> points = new ArrayList<>();

    public Point getOrigin() { return origin; }
    public float getHalfSize() { return halfSize; }

    public QuadNode n11 = null;
    public QuadNode n12 = null;
    public QuadNode n21 = null;
    public QuadNode n22 = null;


    public void add(Point p) {
      System.out.println("add");
      if (n11 == null && n12 == null && n21 == null && n22 == null &&
              (points.size() < limit || halfSize < 1e-6f)) {
        points.add(p);
        return;
      }

      if (n11 == null && n12 == null && n21 == null && n22 == null) {
        float h = halfSize / 2;
        n11 = new QuadNode(Point.of(origin.x + h, origin.y + h), h);
        n12 = new QuadNode(Point.of(origin.x + h, origin.y - h), h);
        n21 = new QuadNode(Point.of(origin.x - h, origin.y - h), h);
        n22 = new QuadNode(Point.of(origin.x - h, origin.y + h), h);
        for (var coord : points) {
          whichDirection(coord).add(coord);
        }
        points.clear();
      }
      whichDirection(p).add(p);

    }

    public void add(float x, float y) {
      add(Point.of(x, y));
    }

    private QuadNode whichDirection(Point p) {
      System.out.println("whichDirection");
      if (p.x >= origin.x && p.y >= origin.y) {
        return n11;
      }
      if (p.x >= origin.x && p.y < origin.y) {
        return n12;
      }
      if (p.x < origin.x && p.y < origin.y) {
        return n21;
      }
      if (p.x < origin.x && p.y >= origin.y) {
        return n22;
      }
      return null;
    }

    public List<Point> all() {
      List<Point> toRet = new ArrayList<>();
      if (points.isEmpty()) {
        if (n11 != null)
          toRet.addAll(n11.all());
        if (n12 != null)
          toRet.addAll(n12.all());
        if (n21 != null)
          toRet.addAll(n21.all());
        if (n22 != null)
          toRet.addAll(n22.all());
      } else {
        toRet.addAll(points);
      }

      return toRet;
    }
  }


  private void rebalance() {

  }

  public QuadNode getRoot() {
    return root;
  }

  public List<Point> all() {
    return root.all();
  }

}
