package com.nikpappas.algo.sorting;


import processing.core.PApplet;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

public class MergeSortSketch extends PApplet {

  private static class TreeNode<T> {
    T val;
    TreeNode<T> l;
    TreeNode<T> r;
  }

  private int SIZE = 16;

  int circleS = 35;
  int margin = 50;
  int steps = 0;

  public static void main(String[] args) {
    PApplet.main(Thread.currentThread().getStackTrace()[1].getClassName());
  }


  List<Integer> list = new ArrayList<>();

  TreeNode<List<Integer>> root = new TreeNode<>();


  @Override
  public void settings() {
    size(800, 400);
  }

  private void reset() {
    list = new ArrayList<>();
    for (int i = 0; i < SIZE; i++) {
      list.add((int) (random(100)));
    }
    root.val = list;
    steps = 0;
  }

  @Override
  public void keyPressed() {
    step();
  }

  private List<Integer> merge(TreeNode<List<Integer>> node, List<Integer> a, List<Integer> b) {
    var toRet = new LinkedList<Integer>();
    while (!a.isEmpty() && !b.isEmpty()) {
      if (a.get(0) < b.get(0)) {
        toRet.add(a.remove(0));
      } else {
        toRet.add(b.remove(0));
      }
    }
    while (!a.isEmpty()) {
      toRet.add(a.remove(0));
    }
    while (!b.isEmpty()) {
      toRet.add(b.remove(0));
    }
    System.out.println("ret:" + toRet);
    try {
      sleep(1000);
    } catch (InterruptedException e) {

    }
    node.val = toRet;
    try {
      sleep(1000);
    } catch (InterruptedException e) {

    }
    return toRet;
  }

  private List<Integer> sort(TreeNode<List<Integer>> node, List<Integer> list) {
    if (list.size() == 1) {
      return list;
    }

    if (list.size() == 2) {
      return merge(node, new ArrayList<>(list.subList(0, 1)), new ArrayList<>(list.subList(1, 2)));
    }

    var mid = list.size() / 2;

    node.r = new TreeNode<>();
    node.r.val = new ArrayList<>(list.subList(mid, list.size()));
    node.l = new TreeNode<>();
    node.l.val = new ArrayList<>(list.subList(0, mid));

    return merge(node, sort(node.l, new ArrayList<>(list.subList(0, mid))), sort(node.r, new ArrayList<>(list.subList(mid, list.size()))));
  }

  private void step() {
    var result = sort(root, list);
    System.out.println(result);
  }

  @Override
  public void setup() {
    background(40);

    reset();
    Executors.newSingleThreadScheduledExecutor().schedule(this::step, 1, TimeUnit.SECONDS);
  }


  @Override
  public void draw() {
    background(40);
    var gap = (width - 2 * margin - circleS * SIZE) / (SIZE - 1);
    var y = height / 10;
    ellipseMode(CENTER);
    textAlign(CENTER, CENTER);
    drawList(list, margin, y, gap);

    var curNode = root;

    drawTreeList(curNode, margin, y + circleS, gap);

    fill(160);
    text("steps: " + steps, width - 40, height / 10);

  }

  private void drawList(List<Integer> l, int x0, int y, int gap) {

    for (int i = 0; i < l.size(); i++) {
      var x = x0 + i * (gap + circleS) + circleS / 2;
      noStroke();
      fill(200);

      circle(x, y, circleS);
      fill(60);
      text("" + l.get(i), x, y);

    }
  }

  private void drawTreeList(TreeNode<List<Integer>> n, int x0, int y, int gap) {
    drawList(n.val, x0, y, gap);
    if (n.l != null) {
      stroke(200);
      line(x0, y + 0.5f * circleS, x0, y + 1.5f * circleS);
      drawTreeList(n.l, x0, y + circleS, gap);
    }
    if (n.r != null) {
      var newX0 = n.l != null ? (n.l.val.size()) * (circleS + gap) : 0;
      stroke(200);
      line(x0 + newX0, y + 0.5f * circleS, x0 + newX0, y + 1.5f * circleS);
      drawTreeList(n.r, x0 + newX0, y + circleS, gap);
    }
  }
}