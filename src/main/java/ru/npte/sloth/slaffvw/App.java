package ru.npte.sloth.slaffvw;


import ru.npte.sloth.slaffvw.controller.NamedPipeController;
import ru.npte.sloth.slaffvw.model.Affects;
import ru.npte.sloth.slaffvw.viewer.SlothAffectsViewer;

public class App {

    public static void main( String[] args ) {
        Affects affects = new Affects();

        new Thread(new SlothAffectsViewer(affects)).start();

        Thread pipeController = new Thread(new NamedPipeController(affects));
        pipeController.setDaemon(true);
        pipeController.start();
    }
}
