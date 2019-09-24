import com.google.common.collect.Iterables;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.table.Table;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

public class MainWindow {

    public void initialize(Monitor monitor) throws IOException, InterruptedException {
        Terminal terminal = new DefaultTerminalFactory().createTerminal();
        Screen screen = new TerminalScreen(terminal);
        screen.startScreen();

        Panel panel = new Panel();
        panel.setLayoutManager(new BorderLayout());

        Table<String> table = new Table<>("SERVICE", "UP", "DOWN", "CURRENT ACTION");


        for (ServiceCheck serviceCheck : monitor.getServiceChecks()) {
            final Service service = serviceCheck.getService();
            table.getTableModel().addRow(
                    service.getHost() + " On " + service.getPort(),
                    "*",
                    "",
                    Iterables.getLast(serviceCheck.getMessages())
            );
        }

        panel.addComponent(table);
        // Create window to hold the panel
        BasicWindow window = new BasicWindow();
        window.setComponent(panel);

        // Create gui and start gui
        MultiWindowTextGUI gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLUE));
        gui.addWindowAndWait(window);
    }

}
