# VisualAlgos

A living collection of algorithms brought to life through interactive 2D and 3D visualizations. Each algorithm has both a clean logic implementation and a [Processing](https://processing.org/) sketch that lets you watch it run in real time.

Built in Java 20 with the Processing 4 core library.

---

## Philosophy

Understanding an algorithm deeply means being able to *see* it. This project pairs every implementation with a visual counterpart — not just to make things pretty, but to expose the structure, decisions, and edge cases that are invisible in code alone.

---

## Algorithms

### Space Partitioning

#### Quad Tree (`logic/QuadTree2D`, `space/QuadTree2DSketch`)
A 2D spatial index that recursively divides space into four quadrants. Points are stored in leaf nodes up to a configurable limit; once full, the node splits and redistributes.

- Left panel: 3D tree hierarchy — nodes as spheres (blue = internal, red = leaf), orbiting camera
- Right panel: 2D spatial view — live cell boundaries subdividing as you click to add points

### Pathfinding

#### Dijkstra's Algorithm (`logic/Dijkstra`, `searching/DijkstraSketch`)
Shortest-path algorithm over a weighted graph. Work in progress.

---

## Running a Sketch

Each sketch has a `main` method and can be run directly:

```
src/main/java/com/nikpappas/algo/space/QuadTree2DSketch.java
src/main/java/com/nikpappas/algo/searching/DijkstraSketch.java
```

Build with Maven:

```bash
mvn compile
```

Then run the sketch class from your IDE or via `java` with the compiled output.

---

## Structure

```
src/main/java/com/nikpappas/algo/
├── logic/       # Pure algorithm implementations (no UI)
├── space/       # Spatial algorithm sketches
└── searching/   # Search and pathfinding sketches
```

New categories (sorting, etc.) will follow the same pattern — logic separated from the sketch.

---

## Roadmap

- Sorting: bubble, merge, quicksort — visualized as animated bar comparisons
- Searching: complete Dijkstra, add A*
- Space: k-d tree, R-tree
- More to come as interesting problems appear
