package com.flyinggoose.serverTest;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.List;

public class Console {

    private final Map<String, String> variables = new HashMap<>();

    private boolean canInput;
    
    public JFrame frame;
    public JTextPane console;
    public JTextField input;
    public JScrollPane scrollpane;

    public StyledDocument document;

    int recentUsedId = 0;
    int recentUsedMax = 10;
    
    ArrayList<String> recentEntered = new ArrayList<>();
    private boolean gotInput = false;

    public Console() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        this.frame = new JFrame();
        this.frame.setTitle("Console");
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.console = new JTextPane() {
            @Override
            public boolean getScrollableTracksViewportWidth() {
                return getUI().getPreferredSize(this).width
                        <= getParent().getSize().width;
            }
        };
        this.console.setEditable(false);
        this.console.setFont(new Font("Courier New", Font.PLAIN, 12));
        this.console.setOpaque(false);

        this.document = this.console.getStyledDocument();

        this.input = new JTextField();
        this.input.setEditable(true);
        this.input.setCaretColor(Color.WHITE);
        this.input.setForeground(Color.WHITE);
        this.input.setFont(new Font("Courier New", Font.PLAIN, 12));
        this.input.setBackground(new Color(105, 105, 105));

        this.input.addActionListener(e -> {
            String text = Console.this.input.getText();
            this.recentEntered.add(text);
            this.recentUsedId = 0;
            this.input.setText("");
            gotInput = false;
        });

        this.input.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    if (Console.this.recentUsedId < ( Console.this.recentUsedMax - 1) &&  Console.this.recentUsedId < ( Console.this.recentEntered.size() - 1)) {
                         Console.this.recentUsedId++;
                    }
                     Console.this.input.setText( Console.this.recentEntered.get( Console.this.recentEntered.size() - 1 -  Console.this.recentUsedId));
                    System.out.println( Console.this.input.getText().length());
                    System.out.println( Console.this.input.getDocument().getLength());
                     Console.this.input.setCaretPosition( Console.this.input.getText().length());
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    if ( Console.this.recentUsedId > 0) {
                         Console.this.recentUsedId--;
                    }
                     Console.this.input.setText( Console.this.recentEntered.get( Console.this.recentEntered.size() - 1 -  Console.this.recentUsedId));
                     Console.this.input.setCaretPosition( Console.this.input.getDocument().getLength());
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        this.scrollpane = new JScrollPane(this.console);
        this.scrollpane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.scrollpane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.scrollpane.setBorder(null);
        this.scrollpane.setOpaque(false);
        this.scrollpane.getViewport().setOpaque(false);

        this.frame.add(this.input, BorderLayout.SOUTH);
        this.frame.add(this.scrollpane, BorderLayout.CENTER);

        canInput = true;

        this.frame.getContentPane().setBackground(new Color(50, 50, 50));

        this.frame.setSize(660, 350);
        this.frame.setLocationRelativeTo(null);

        this.frame.setResizable(true);
    }

    public JFrame getWindow() {
        return this.frame;
    }

    public String getLastEntered() {
        try {
            return recentEntered.get(recentEntered.size() - 1);
        } catch (IndexOutOfBoundsException e) {
            return "";
        }
    }

    public String getInput() {
        if (!gotInput && !getLastEntered().isEmpty()) {
            gotInput = true;
            return getLastEntered();
        }
        else {
            return "";
        }
    }

    public List<String> getRecentEntered() {
        return recentEntered;
    }

    public void show() {
        this.frame.setVisible(true);
    }

    public void hide() {
        this.frame.setVisible(false);
    }

    public void removeInput() {
        if (canInput) {
            frame.remove(this.input);
            canInput = false;
        }
    }

    public void addInput() {
        if (!canInput) {
            frame.add(this.input, BorderLayout.SOUTH);
            canInput = true;
        }
    }

    public void scrollTop() {
        this.console.setCaretPosition(0);
    }

    public void scrollBottom() {
        this.console.setCaretPosition(this.console.getDocument().getLength());
    }

    public void print(Object text, Color foreground, Color background) {
        Style style = this.console.addStyle("HtmlStyle", null);

        try {
            String[] words = text.toString().split(" ", -1);
            for (int i = 0; i < words.length; i++) {
                boolean printSpace = false;
                String word = words[i];

                if (background!=null) StyleConstants.setBackground(style, background);
                else StyleConstants.setBackground(style, this.frame.getContentPane().getBackground());

                if (foreground!=null) StyleConstants.setForeground(style, foreground);
                else StyleConstants.setForeground(style, Color.WHITE);

                if (i < words.length-1) word+=" ";

                this.document.insertString(this.document.getLength(), word, style);

            }

            scrollBottom();
        } catch (Exception ignored) {}
    }

    public void println(Object text, Color foreground, Color background) {
        this.print(text + "\n", foreground, background);
    }

    public void clear() {
        try {
            this.document.remove(0, this.document.getLength());
        } catch (Exception ignored) {}
    }

    public void println() {
        try {
            this.document.insertString(this.document.getLength(), "\n", null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
