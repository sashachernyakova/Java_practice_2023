package com.example.demo1;

import static org.junit.Assert.*;

public class KruscalApplicationTest {

    @org.junit.Test
    public void checkConnectivityComponents() {
        //TEST №1
        KruscalApplication application1 = new KruscalApplication();
        application1.graph.addVertex("a");
        application1.graph.addVertex("b");
        application1.graph.addVertex("c");
        application1.graph.addVertex("d");
        application1.graph.addEdge("a", "b");
        application1.graph.addEdge("a", "c");
        assertFalse(application1.checkConnectivityComponents());



        //TEST №2
        KruscalApplication application3 = new KruscalApplication();
        application3.graph.addVertex("a");
        application3.graph.addVertex("b");
        application3.graph.addVertex("c");
        application3.graph.addVertex("d");
        application3.graph.addEdge("d", "a");
        assertFalse(application3.checkConnectivityComponents());

        //TEST №3
        KruscalApplication application4 = new KruscalApplication();
        application4.graph.addVertex("a");
        assertFalse(application3.checkConnectivityComponents());
    }

    @org.junit.Test
    public void edgeExist() {
        //TEST №1

        KruscalApplication application1 = new KruscalApplication();
        application1.graph.addVertex("a");
        application1.graph.addVertex("b");
        application1.graph.addVertex("c");
        application1.graph.addEdge("a", "b");
        application1.graph.addEdge("a", "c");
        assertTrue(application1.edgeExist("a", "b"));
        assertTrue(application1.edgeExist("a", "c"));
        assertFalse(application1.edgeExist("c", "b"));
        application1.graph.addEdge("b", "c");
        assertTrue(application1.edgeExist("b", "c"));

        //TEST №2
        KruscalApplication application2 = new KruscalApplication();
        application2.graph.addVertex("a");
        application2.graph.addVertex("b");
        application2.graph.addVertex("c");
        application2.graph.addVertex("d");
        application2.graph.addEdge("b", "c");
        application2.graph.addEdge("a", "c");
        application2.graph.addEdge("a", "d");
        application2.graph.addEdge("b", "d");
        application2.graph.addEdge("d", "c");
        assertTrue(application2.edgeExist("c", "b"));
        assertTrue(application2.edgeExist("a", "c"));
        assertTrue(application2.edgeExist("a", "d"));
        assertTrue(application2.edgeExist("b", "d"));
        assertTrue(application2.edgeExist("d", "c"));

        //TEST №3
        KruscalApplication application3 = new KruscalApplication();
        application3.graph.addVertex("a");
        application3.graph.addVertex("b");
        application3.graph.addVertex("c");
        application3.graph.addVertex("d");
        application3.graph.addEdge("d", "a");
        assertTrue(application3.edgeExist("d", "a"));
        assertTrue(application3.edgeExist("a", "d"));

        //TEST №4
        KruscalApplication application4 = new KruscalApplication();
        assertFalse(application4.edgeExist("z", "t"));
        assertFalse(application4.edgeExist("s", "r"));
        assertFalse(application4.edgeExist("a", "w"));

    }

    @org.junit.Test
    public void addVertex() {
        //TEST №1
        KruscalApplication application1 = new KruscalApplication();
        application1.graph.addVertex("a");
        application1.graph.addVertex("b");
        application1.graph.addVertex("d");
        assertTrue(application1.graph.containsVertex("a"));
        assertTrue(application1.graph.containsVertex("b"));
        assertTrue(application1.graph.containsVertex("d"));
        assertFalse(application1.graph.containsVertex("z"));
        assertFalse(application1.graph.containsVertex("f"));
        application1.graph.addEdge("a", "b");
        application1.graph.addEdge("b", "d");
        application1.graph.addEdge("a", "d");
        assertTrue(application1.graph.containsVertex("a"));
        assertTrue(application1.graph.containsVertex("b"));
        assertTrue(application1.graph.containsVertex("d"));
        assertFalse(application1.graph.containsVertex("z"));
        assertFalse(application1.graph.containsVertex("f"));

        //TEST №2
        KruscalApplication application2 = new KruscalApplication();
        application2.graph.addVertex("a");
        application2.graph.addVertex("b");
        application2.graph.addEdge("a", "b");
        assertTrue(application2.graph.containsVertex("a"));
        assertTrue(application2.graph.containsVertex("b"));

        //TEST №3
        KruscalApplication application3 = new KruscalApplication();
        application3.graph.addVertex("a");
        assertTrue(application3.graph.containsVertex("a"));
        assertFalse(application3.graph.containsVertex("A"));

        //TEST №4
        KruscalApplication application4 = new KruscalApplication();
        assertFalse(application4.graph.containsVertex("Z"));
        assertFalse(application4.graph.containsVertex("j"));
    }

    @org.junit.Test
    public void deleteVertex() {
        //TEST №1
        KruscalApplication application1 = new KruscalApplication();
        application1.graph.addVertex("a");
        application1.graph.addVertex("b");
        application1.graph.addVertex("d");
        assertTrue(application1.graph.removeVertex("a"));
        assertTrue(application1.graph.removeVertex("b"));
        assertTrue(application1.graph.removeVertex("d"));
        assertFalse(application1.graph.removeVertex("a"));
        assertFalse(application1.graph.removeVertex("b"));
        assertFalse(application1.graph.removeVertex("d"));
        assertFalse(application1.graph.removeVertex("z"));
        assertFalse(application1.graph.removeVertex("f"));

        //TEST №2
        KruscalApplication application2 = new KruscalApplication();
        application2.graph.addVertex("a");
        application2.graph.addVertex("b");
        application2.graph.addEdge("a", "b");
        assertTrue(application2.graph.containsVertex("a"));
        assertTrue(application2.graph.containsVertex("b"));
        assertTrue(application2.graph.removeVertex("a"));
        assertTrue(application2.graph.removeVertex("b"));
        assertFalse(application2.graph.removeVertex("a"));
        assertFalse(application2.graph.removeVertex("b"));
        assertFalse(application2.graph.removeVertex("v"));

        //TEST №3
        KruscalApplication application3 = new KruscalApplication();
        application3.graph.addVertex("a");
        assertTrue(application3.graph.containsVertex("a"));
        assertTrue(application3.graph.removeVertex("a"));


        //TEST №4
        KruscalApplication application4 = new KruscalApplication();
        application4.graph.addVertex("Z");
        application4.graph.addVertex("f");
        application4.graph.addVertex("a");
        assertTrue(application4.graph.containsVertex("Z"));
        assertTrue(application4.graph.containsVertex("f"));
        assertTrue(application4.graph.removeVertex("Z"));
        assertTrue(application4.graph.removeVertex("f"));

    }

    @org.junit.Test
    public void addEdge() {
        //TEST №1
        KruscalApplication application1 = new KruscalApplication();
        application1.graph.addVertex("a");
        application1.graph.addVertex("b");
        application1.graph.addVertex("c");
        application1.graph.addEdge("a", "b");
        application1.graph.addEdge("b", "c");
        assertTrue(application1.graph.containsEdge("a", "b"));
        assertTrue(application1.graph.containsEdge("b", "c"));
        assertFalse(application1.graph.containsEdge("a", "c"));
        assertFalse(application1.graph.containsEdge("t", "s"));


        //TEST №2
        KruscalApplication application2 = new KruscalApplication();
        application2.graph.addVertex("a");
        application2.graph.addVertex("b");
        application2.graph.addVertex("d");
        application2.graph.addEdge("a", "b");
        assertTrue(application2.graph.containsEdge("a", "b"));
        assertFalse(application2.graph.containsEdge("a", "d"));
        assertFalse(application2.graph.containsEdge("b", "d"));


        //TEST №3
        KruscalApplication application3 = new KruscalApplication();
        application3.graph.addVertex("a");
        application3.graph.addVertex("c");
        application3.graph.addVertex("b");
        application3.graph.addVertex("t");
        application3.graph.addVertex("s");
        application3.graph.addEdge("t", "s");
        assertFalse(application3.graph.containsEdge("a", "c"));
        assertTrue(application3.graph.containsEdge("t", "s"));

        //TEST №4
        KruscalApplication application4 = new KruscalApplication();
        application4.graph.addVertex("a");
        application4.graph.addVertex("b");
        application4.graph.addVertex("c");
        application4.graph.addVertex("d");
        application4.graph.addEdge("a", "b");
        application4.graph.addEdge("a", "c");
        application4.graph.addEdge("a", "d");
        application4.graph.addEdge("b", "c");
        application4.graph.addEdge("b", "d");
        application4.graph.addEdge("c", "d");
        assertTrue(application4.graph.containsEdge("a", "b"));
        assertTrue(application4.graph.containsEdge("a", "c"));
        assertTrue(application4.graph.containsEdge("a", "d"));
        assertTrue(application4.graph.containsEdge("b", "c"));
        assertTrue(application4.graph.containsEdge("b", "d"));
        assertTrue(application4.graph.containsEdge("c", "d"));


    }

    @org.junit.Test
    public void deleteEdge() {
        //TEST №1
        KruscalApplication application1 = new KruscalApplication();
        application1.graph.addVertex("a");
        application1.graph.addVertex("b");
        application1.graph.addVertex("c");
        application1.graph.addEdge("a", "b");
        application1.graph.addEdge("b", "c");
        assertTrue(application1.graph.removeEdge(application1.graph.getEdge("b", "c")));
        assertTrue(application1.graph.removeEdge(application1.graph.getEdge("a", "b")));


        //TEST №2
        KruscalApplication application2 = new KruscalApplication();
        application2.graph.addVertex("a");
        application2.graph.addVertex("b");
        application2.graph.addVertex("d");
        application2.graph.addEdge("a", "b");
        application2.graph.addEdge("a", "d");
        assertTrue(application2.graph.removeEdge(application2.graph.getEdge("a", "b")));
        assertTrue(application2.graph.removeEdge(application2.graph.getEdge("a", "d")));


        //TEST №3
        KruscalApplication application3 = new KruscalApplication();
        application3.graph.addVertex("a");
        application3.graph.addVertex("c");
        application3.graph.addVertex("b");
        application3.graph.addVertex("t");
        application3.graph.addVertex("s");
        application3.graph.addEdge("t", "s");
        application3.graph.addEdge("t", "b");
        application3.graph.addEdge("a", "c");
        application3.graph.addEdge("b", "c");
        assertTrue(application3.graph.removeEdge(application3.graph.getEdge("t", "s")));
        assertTrue(application3.graph.removeEdge(application3.graph.getEdge("t", "b")));
        assertTrue(application3.graph.removeEdge(application3.graph.getEdge("a", "c")));
        assertTrue(application3.graph.removeEdge(application3.graph.getEdge("b", "c")));


        //TEST №4
        KruscalApplication application4 = new KruscalApplication();
        application4.graph.addVertex("a");
        application4.graph.addVertex("b");
        application4.graph.addVertex("c");
        application4.graph.addVertex("d");
        application4.graph.addEdge("a", "b");
        application4.graph.addEdge("a", "c");
        application4.graph.addEdge("a", "d");
        application4.graph.addEdge("b", "c");
        application4.graph.addEdge("b", "d");
        application4.graph.addEdge("c", "d");
        assertTrue(application4.graph.removeEdge(application4.graph.getEdge("a", "b")));
        assertTrue(application4.graph.removeEdge(application4.graph.getEdge("a", "c")));
        assertTrue(application4.graph.removeEdge(application4.graph.getEdge("a", "d")));
        assertTrue(application4.graph.removeEdge(application4.graph.getEdge("b", "c")));
        assertTrue(application4.graph.removeEdge(application4.graph.getEdge("b", "d")));
        assertTrue(application4.graph.removeEdge(application4.graph.getEdge("c", "d")));
    }
}