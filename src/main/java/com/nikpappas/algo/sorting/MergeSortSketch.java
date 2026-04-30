package com.nikpappas.algo.sorting;


import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MergeSortSketch extends PApplet {

  private static class TreeNode {
    volatile List<Integer> val = List.of();
    volatile boolean isSorted;
    volatile TreeNode l;
    volatile TreeNode r;
  }

  private static final int SIZE = 16;

  private static final int circleS = 35;
  private static final int margin = 50;


  public static void main(String[] args) {
    PApplet.main(Thread.currentThread().getStackTrace()[1].getClassName());
  }


  volatile List<Integer> list = List.of();
  volatile TreeNode root = new TreeNode();

  private final ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor(r -> {
    var t = new Thread(r, "merge-sort-worker");
    t.setDaemon(true);
    return t;
  });


  @Override
  public void settings() {
    size(800, 400);
  }

  private void reset() {
    var initial = new ArrayList<Integer>(SIZE);
    for (int i = 0; i < SIZE; i++) {
      initial.add((int) (random(100)));
    }
    var snapshot = List.copyOf(initial);
    list = snapshot;
    root.val = snapshot;
  }


  private void merge(TreeNode parent, TreeNode left, TreeNode right) {
    var a = new ArrayList<>(left.val);
    var b = new ArrayList<>(right.val);
    var out = new ArrayList<Integer>(a.size() + b.size());

    while (!a.isEmpty() && !b.isEmpty()) {
      if (a.get(0) < b.get(0)) {
        out.add(a.remove(0));
        left.val = List.copyOf(a);
      } else {
        out.add(b.remove(0));
        right.val = List.copyOf(b);
      }
    }
    while (!a.isEmpty()) {
      out.add(a.remove(0));
      left.val = List.copyOf(a);
    }
    while (!b.isEmpty()) {
      out.add(b.remove(0));
      right.val = List.copyOf(b);
    }
    System.out.println("ret:" + out);
    sleepQuietly(1000);
    parent.val = List.copyOf(out);
    parent.isSorted = true;
    sleepQuietly(1000);
  }

  private void sort(TreeNode node) {
    var data = node.val;
    if (data.size() <= 1) {
      node.isSorted = true;
      return;
    }
    int mid = data.size() / 2;

    var left = new TreeNode();
    left.val = List.copyOf(data.subList(0, mid));
    var right = new TreeNode();
    right.val = List.copyOf(data.subList(mid, data.size()));
    node.l = left;
    node.r = right;

    sort(left);
    sort(right);
    merge(node, left, right);
  }

  private void step() {
    sort(root);
    System.out.println(root.val);
  }

  @Override
  public void setup() {
    background(40);

    reset();
    worker.schedule(this::step, 1, TimeUnit.SECONDS);
  }

  @Override
  public void dispose() {
    worker.shutdownNow();
    super.dispose();
  }


  @Override
  public void draw() {
    background(40);
    var snapshot = list;
    var gap = (width - 2 * margin - circleS * SIZE) / (SIZE - 1);
    var y = height / 10;
    ellipseMode(CENTER);
    textAlign(CENTER, CENTER);
    drawList(snapshot, margin, y, gap, 255);

    drawTreeList(root, margin, y + circleS, gap);
  }

  private void drawList(List<Integer> l, int x0, int y, int gap, int fillColor) {

    for (int i = 0; i < l.size(); i++) {
      var x = x0 + i * (gap + circleS) + circleS / 2;
      noStroke();
      fill(fillColor);

      circle(x, y, circleS);
      fill(60);
      text("" + l.get(i), x, y);

    }
  }

  private void drawTreeList(TreeNode n, int x0, int y, int gap) {
    if (n == null) return;
    var v = n.val;
    if (v == null) return;
    drawList(v, x0, y, gap, n.isSorted ? 100 : 200);

    var left = n.l;
    var right = n.r;
    if (left != null) {
      stroke(200);
      line(x0, y + 0.5f * circleS, x0, y + 1.5f * circleS);
      drawTreeList(left, x0, y + circleS, gap);
    }
    if (right != null) {
      var leftVal = left != null ? left.val : null;
      var newX0 = leftVal != null ? leftVal.size() * (circleS + gap) : 0;
      stroke(200);
      line(x0 + newX0, y + 0.5f * circleS, x0 + newX0, y + 1.5f * circleS);
      drawTreeList(right, x0 + newX0, y + circleS, gap);
    }
  }

  private static void sleepQuietly(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }
}
