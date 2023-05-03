package src.server;

import javafx.geometry.Pos;

import java.util.*;

public class GraphMoves implements List<GraphMoves> {
    Position position;
    List<GraphMoves> children = new ArrayList<>();

    public GraphMoves(Position p) {
        this.position = p;
    }

    public static List<List<Position>> AllPaths(List<GraphMoves> graphMoves) {
        List<List<Position>> result = new ArrayList<>();
        for(var graph : graphMoves){
            List<List<Position>> paths = new ArrayList<>();
            graph.listAllPaths(new ArrayList<>(), paths);
            result.addAll(paths);
        }
        return result;
    }

    public static List<List<Position>> AllPaths(GraphMoves graph) {
        var l = new ArrayList<GraphMoves>();
        l.add(graph);
        return AllPaths(l);
    }

    public static GraphMoves toGraph(List<Position> path) {
        GraphMoves nextNode;
        GraphMoves oldgraph = null;
        GraphMoves first = new GraphMoves(new Position(0,0));
        for (var node : path){
            if (oldgraph == null){
                oldgraph = new GraphMoves(node);
                first = oldgraph;
                continue;
            }
            nextNode = new GraphMoves(node);
            oldgraph.add(nextNode);
            oldgraph = nextNode;
        }
        return first;
    }

    public void PrettyPrint(String currPath, List<String> paths) {
        if (this.isEmpty()) {
            currPath += this.position;
            paths.add(currPath);
        }

        currPath += position + "--->";
        for (var child : children)
            child.PrettyPrint(currPath, paths);
    }

    @Override
    public String toString() {
        var paths = new ArrayList<String>();
        this.PrettyPrint("", paths);
        return paths.toString();
    }

    @Override
    public int size() {
        return children.size();
    }

    @Override
    public boolean isEmpty() {
        return this.children.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return this.children.contains(o);
    }

    @Override
    public Iterator<GraphMoves> iterator() {
        return this.children.iterator();
    }

    @Override
    public Object[] toArray() {
        return this.children.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return this.children.toArray(a);
    }

    @Override
    public boolean add(GraphMoves graphMoves) {
        return this.children.add(graphMoves);
    }

    @Override
    public boolean remove(Object o) {
        return this.children.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return new HashSet<>(this.children).containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends GraphMoves> c) {
        return this.children.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends GraphMoves> c) {
        return this.children.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return this.children.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return this.children.retainAll(c);
    }

    @Override
    public void clear() {
        this.children.clear();
    }

    @Override
    public GraphMoves get(int index) {
        return this.children.get(index);
    }

    @Override
    public GraphMoves set(int index, GraphMoves element) {
        return this.children.set(index, element);
    }

    @Override
    public void add(int index, GraphMoves element) {
        this.children.add(index, element);
    }

    @Override
    public GraphMoves remove(int index) {
        return this.children.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return this.children.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return this.children.lastIndexOf(o);
    }

    @Override
    public ListIterator<GraphMoves> listIterator() {
        return this.children.listIterator();
    }

    @Override
    public ListIterator<GraphMoves> listIterator(int index) {
        return this.children.listIterator(index);
    }

    @Override
    public List<GraphMoves> subList(int fromIndex, int toIndex) {
        return this.children.subList(fromIndex, toIndex);
    }

    public boolean isPathological() {
        if (isEmpty())
            return true;
        else if (size() == 1)
            return children.get(0).isPathological();
        else
            return false;
    }

    public void listAllPaths(List<Position> currPath, List<List<Position>> paths) {
        var path = new ArrayList<>(currPath);
        if (this.isEmpty()) {
            path.add(this.position);
            paths.add(path);
            return;
        }

        path.add(this.position);
        for (var child : children)
            child.listAllPaths(path, paths);
    }

    public List<Position> toList() throws NotPathologicalException {
        List<Position> moves = new ArrayList<>();
        moves.add(position);
        if (!isPathological())
            throw new NotPathologicalException();

        for (var child: children) {
           moves.addAll(child.toList());
        }
        return moves;
    }

    public boolean isPath(GraphMoves path) {
        if (this.position.equals(path.position)) {
            if (path.isEmpty() && isEmpty()) {
                return true;
            }
            else {
                var pathChild = path.get(0);
                for (var child : children)
                    if (child.isPath(pathChild))
                        return true;
            }
        }
        return false;
    }
}
