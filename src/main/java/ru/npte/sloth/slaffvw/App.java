package ru.npte.sloth.slaffvw;


import ru.npte.sloth.slaffvw.controller.NamedPipeController;
import ru.npte.sloth.slaffvw.model.Affects;
import ru.npte.sloth.slaffvw.viewer.SlothAffectsViewer;

public class App {

    public static void main( String[] args ) {
        Affects affects = new Affects();

        SlothAffectsViewer viewer = new SlothAffectsViewer(affects);
        NamedPipeController pipeController = new NamedPipeController(affects);

        new Thread(viewer).start();
        new Thread(pipeController).start();
    }
}
