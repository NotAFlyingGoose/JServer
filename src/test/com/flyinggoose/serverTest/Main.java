package com.flyinggoose.serverTest;

import com.googlecode.lanterna.*;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.TerminalResizeListener;
import com.googlecode.lanterna.terminal.ansi.UnixTerminal;

import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        third();
    }

    public static void fourth() throws IOException {
        // Setup terminal and screen layers
        Terminal terminal = new DefaultTerminalFactory().createTerminal();
        Screen screen = new TerminalScreen(terminal);
        screen.startScreen();

        // Create window to hold the panel
        BasicWindow window = new BasicWindow();
        window.setHints(Arrays.asList(Window.Hint.FULL_SCREEN, Window.Hint.NO_DECORATIONS));

        // Create gui and start gui
        MultiWindowTextGUI gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLUE));
        gui.addWindowAndWait(window);
    }

    public static void third() throws IOException {
        Terminal terminal = new DefaultTerminalFactory().createTerminal();
        Screen screen = new TerminalScreen(terminal);
        screen.startScreen();

        final WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);

        final Window login = new BasicWindow("Chatty Client");
        login.setHints(Arrays.asList(Window.Hint.CENTERED));

        // panels
        Panel signUpPanel = new Panel(new GridLayout(2));
        Panel signInPanel = new Panel(new GridLayout(2));

        // sign up panel

        Label signUpTitle = new Label("Sign Up");
        signUpTitle.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.BEGINNING, // Horizontal alignment in the grid cell if the cell is larger than the component's preferred size
                GridLayout.Alignment.BEGINNING, // Vertical alignment in the grid cell if the cell is larger than the component's preferred size
                true,       // Give the component extra horizontal space if available
                false,        // Give the component extra vertical space if available
                2,                  // Horizontal span
                1));                  // Vertical span
        signUpPanel.addComponent(signUpTitle);
        signUpPanel.addComponent(new Label("Username"));
        signUpPanel.addComponent(
                new TextBox()
                        .setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.END, GridLayout.Alignment.CENTER)));
        signUpPanel.addComponent(new Label("Password"));
        signUpPanel.addComponent(
                new TextBox()
                        .setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.END, GridLayout.Alignment.CENTER)));
        signUpPanel.addComponent(new Button("Already Have An Account?", new Runnable() {
            @Override
            public void run() {
                login.setComponent(signInPanel);
            }
        }).setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.BEGINNING, GridLayout.Alignment.CENTER)));
        signUpPanel.addComponent(new Button("Enter", new Runnable() {
            @Override
            public void run() {
                MessageDialog.showMessageDialog(textGUI, "MessageBox", "This is a message box", MessageDialogButton.OK);
            }
        }).setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.END, GridLayout.Alignment.CENTER)));

        // sign in panel

        Label signInTitle = new Label("Sign In");
        signInTitle.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.BEGINNING, // Horizontal alignment in the grid cell if the cell is larger than the component's preferred size
                GridLayout.Alignment.BEGINNING, // Vertical alignment in the grid cell if the cell is larger than the component's preferred size
                true,       // Give the component extra horizontal space if available
                false,        // Give the component extra vertical space if available
                2,                  // Horizontal span
                1));                  // Vertical span
        signInPanel.addComponent(signInTitle);
        signInPanel.addComponent(new Label("Username"));
        signInPanel.addComponent(
                new TextBox()
                        .setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.END, GridLayout.Alignment.CENTER)));
        signInPanel.addComponent(new Label("Password"));
        signInPanel.addComponent(
                new TextBox()
                        .setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.END, GridLayout.Alignment.CENTER)));
        signInPanel.addComponent(new Button("Don't Have An Account?", new Runnable() {
            @Override
            public void run() {
                login.setComponent(signUpPanel);
            }
        }).setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.BEGINNING, GridLayout.Alignment.CENTER)));
        signInPanel.addComponent(new Button("Enter", new Runnable() {
            @Override
            public void run() {
                MessageDialog.showMessageDialog(textGUI, "MessageBox", "This is a message box", MessageDialogButton.OK);
            }
        }).setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.END, GridLayout.Alignment.CENTER)));

        login.setComponent(signInPanel);

        /*final Window window = new BasicWindow("Client");
        window.setHints(Arrays.asList(Window.Hint.FULL_SCREEN, Window.Hint.NO_DECORATIONS));

        Panel contentPanel = new Panel(new GridLayout(2));

        GridLayout gridLayout = (GridLayout)contentPanel.getLayoutManager();
        gridLayout.setHorizontalSpacing(3);

        Label title = new Label("This is a label that spans two columns and it is\n really really really awesome and cool and amazxing.");
        title.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.BEGINNING, // Horizontal alignment in the grid cell if the cell is larger than the component's preferred size
                GridLayout.Alignment.BEGINNING, // Vertical alignment in the grid cell if the cell is larger than the component's preferred size
                true,       // Give the component extra horizontal space if available
                false,        // Give the component extra vertical space if available
                2,                  // Horizontal span
                1));                  // Vertical span
        contentPanel.addComponent(title);

        contentPanel.addComponent(new Label("Text Box (aligned)"));
        contentPanel.addComponent(
                new TextBox()
                        .setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.BEGINNING, GridLayout.Alignment.CENTER)));

        contentPanel.addComponent(new Label("Password Box (right aligned)"));
        contentPanel.addComponent(
                new TextBox()
                        .setMask('*')
                        .setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.END, GridLayout.Alignment.CENTER)));

        contentPanel.addComponent(new Label("Read-only Combo Box (forced size)"));
        List<String> timezonesAsStrings = new ArrayList<String>();
        timezonesAsStrings.addAll(Arrays.asList(TimeZone.getAvailableIDs()));
        ComboBox<String> readOnlyComboBox = new ComboBox<String>(timezonesAsStrings);
        readOnlyComboBox.setReadOnly(true);
        readOnlyComboBox.setPreferredSize(new TerminalSize(20, 1));
        contentPanel.addComponent(readOnlyComboBox);

        contentPanel.addComponent(new Label("Editable Combo Box (filled)"));
        contentPanel.addComponent(
                new ComboBox<String>("Item #1", "Item #2", "Item #3", "Item #4")
                        .setReadOnly(false)
                        .setLayoutData(GridLayout.createHorizontallyFilledLayoutData(1)));

        contentPanel.addComponent(new Label("Button (centered)"));
        contentPanel.addComponent(new Button("Button", new Runnable() {
            @Override
            public void run() {
                MessageDialog.showMessageDialog(textGUI, "MessageBox", "This is a message box", MessageDialogButton.OK);
            }
        }).setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.CENTER, GridLayout.Alignment.CENTER)));

        contentPanel.addComponent(
                new EmptySpace()
                        .setLayoutData(
                                GridLayout.createHorizontallyFilledLayoutData(2)));
        contentPanel.addComponent(
                new Separator(Direction.HORIZONTAL)
                        .setLayoutData(
                                GridLayout.createHorizontallyFilledLayoutData(2)));
        contentPanel.addComponent(
                new Button("Close", new Runnable() {
                    @Override
                    public void run() {
                        window.close();
                    }
                }).setLayoutData(
                        GridLayout.createHorizontallyEndAlignedLayoutData(2)));

        window.setComponent(contentPanel);*/

        textGUI.addWindowAndWait(login);

        screen.stopScreen();
    }

    public static void second() throws IOException {
        Terminal terminal = new DefaultTerminalFactory().createTerminal();
        Screen screen = new TerminalScreen(terminal);
        screen.startScreen();
        screen.setCursorPosition(null);
        Random random = new Random();
        TerminalSize terminalSize = screen.getTerminalSize();
        for (int column = 0; column < terminalSize.getColumns(); column++) {
            for (int row = 0; row < terminalSize.getRows(); row++) {
                screen.setCharacter(column, row, new TextCharacter(
                        ' ',
                        TextColor.ANSI.DEFAULT,
                        // This will pick a random background color
                        TextColor.ANSI.values()[random.nextInt(TextColor.ANSI.values().length)]));
            }
        }
        screen.refresh();
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < 2000) {
            // The call to pollInput() is not blocking, unlike readInput()
            if (screen.pollInput() != null) {
                break;
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException ignore) {
                break;
            }
        }
        while (true) {
            KeyStroke keyStroke = screen.pollInput();
            if (keyStroke != null && (keyStroke.getKeyType() == KeyType.Escape || keyStroke.getKeyType() == KeyType.EOF)) {
                break;
            }
            TerminalSize newSize = screen.doResizeIfNecessary();
            if (newSize != null) {
                terminalSize = newSize;
            }

            // Increase this to increase speed
            final int charactersToModifyPerLoop = 1;
            for (int i = 0; i < charactersToModifyPerLoop; i++) {
                TerminalPosition cellToModify = new TerminalPosition(
                        random.nextInt(terminalSize.getColumns()),
                        random.nextInt(terminalSize.getRows()));

                TextColor.ANSI color = TextColor.ANSI.values()[random.nextInt(TextColor.ANSI.values().length)];

                TextCharacter characterInBackBuffer = screen.getBackCharacter(cellToModify);
                characterInBackBuffer = characterInBackBuffer.withBackgroundColor(color);
                characterInBackBuffer = characterInBackBuffer.withCharacter(' ');   // Because of the label box further down, if it shrinks
                screen.setCharacter(cellToModify, characterInBackBuffer);
            }
            drawSingleLineWindow(screen.newTextGraphics(), "Terminal Size: " + terminalSize, 1, 1);
            drawSingleLineWindow(screen.newTextGraphics(), "my name is julian.", 1, 5);

            screen.refresh();
            Thread.yield();
        }
        screen.close();

    }

    public static void drawSingleLineWindow(TextGraphics tg, String data, int x, int y) {
        TerminalPosition labelBoxTopLeft = new TerminalPosition(x, y);
        TerminalSize labelBoxSize = new TerminalSize(data.length() + 2, 3);
        TerminalPosition labelBoxTopRightCorner = labelBoxTopLeft.withRelativeColumn(labelBoxSize.getColumns() - 1);
        //This isn't really needed as we are overwriting everything below anyway, but just for demonstrative purpose
        tg.fillRectangle(labelBoxTopLeft, labelBoxSize, ' ');

        tg.drawLine(
                labelBoxTopLeft.withRelativeColumn(1),
                labelBoxTopLeft.withRelativeColumn(labelBoxSize.getColumns() - 2),
                Symbols.DOUBLE_LINE_HORIZONTAL);
        tg.drawLine(
                labelBoxTopLeft.withRelativeRow(2).withRelativeColumn(1),
                labelBoxTopLeft.withRelativeRow(2).withRelativeColumn(labelBoxSize.getColumns() - 2),
                Symbols.DOUBLE_LINE_HORIZONTAL);
        tg.setCharacter(labelBoxTopLeft, Symbols.DOUBLE_LINE_TOP_LEFT_CORNER);
        tg.setCharacter(labelBoxTopLeft.withRelativeRow(1), Symbols.DOUBLE_LINE_VERTICAL);
        tg.setCharacter(labelBoxTopLeft.withRelativeRow(2), Symbols.DOUBLE_LINE_BOTTOM_LEFT_CORNER);
        tg.setCharacter(labelBoxTopRightCorner, Symbols.DOUBLE_LINE_TOP_RIGHT_CORNER);
        tg.setCharacter(labelBoxTopRightCorner.withRelativeRow(1), Symbols.DOUBLE_LINE_VERTICAL);
        tg.setCharacter(labelBoxTopRightCorner.withRelativeRow(2), Symbols.DOUBLE_LINE_BOTTOM_RIGHT_CORNER);

        tg.putString(labelBoxTopLeft.withRelative(1, 1), data);
    }

    public static void first() throws IOException {
        Terminal terminal = new DefaultTerminalFactory().createTerminal();

        terminal.enterPrivateMode();
        terminal.setCursorVisible(false);
        final TextGraphics textGraphics = terminal.newTextGraphics();
        textGraphics.putString(2, 1, "Lanterna Tutorial 2 - Press ESC to exit", SGR.BOLD);
        textGraphics.setForegroundColor(TextColor.ANSI.DEFAULT);
        textGraphics.setBackgroundColor(TextColor.ANSI.DEFAULT);
        textGraphics.putString(5, 3, "Terminal Size: ", SGR.BOLD);
        textGraphics.putString(5 + "Terminal Size: ".length(), 3, terminal.getTerminalSize().toString());
        terminal.addResizeListener(new TerminalResizeListener() {
            @Override
            public void onResized(Terminal terminal, TerminalSize newSize) {
                // Be careful here though, this is likely running on a separate thread. Lanterna is threadsafe in
                // a best-effort way so while it shouldn't blow up if you call terminal methods on multiple threads,
                // it might have unexpected behavior if you don't do any external synchronization
                textGraphics.drawLine(5, 3, newSize.getColumns() - 1, 3, ' ');
                textGraphics.putString(5, 3, "Terminal Size: ", SGR.BOLD);
                textGraphics.putString(5 + "Terminal Size: ".length(), 3, newSize.toString());
                try {
                    terminal.flush();
                } catch (IOException e) {
                    // Not much we can do here
                    throw new RuntimeException(e);
                }
            }
        });

        textGraphics.putString(5, 4, "Last Keystroke: ", SGR.BOLD);
        textGraphics.putString(5 + "Last Keystroke: ".length(), 4, "<Pending>");
        terminal.flush();
        KeyStroke keyStroke = terminal.readInput();
        while (keyStroke.getKeyType() != KeyType.Escape) {
            textGraphics.drawLine(5, 4, terminal.getTerminalSize().getColumns() - 1, 4, ' ');
            textGraphics.putString(5, 4, "Last Keystroke: ", SGR.BOLD);
            textGraphics.putString(5 + "Last Keystroke: ".length(), 4, keyStroke.toString());
            terminal.flush();
            keyStroke = terminal.readInput();
        }

        terminal.close();
    }
}
