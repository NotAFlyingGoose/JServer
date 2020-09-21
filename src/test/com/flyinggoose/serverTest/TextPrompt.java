package com.flyinggoose.serverTest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class TextPrompt extends JTextField {
    boolean showingPrompt = false;
    final String prompt;
    Color lastColor = null;
    Color promptColor = Color.GRAY;

    public TextPrompt(String prompt) {
        this.prompt = prompt;
        addFocusListener(new PromptListener());
    }

    public TextPrompt(String prompt, Color color) {
        this.promptColor = color;
        this.prompt = prompt;
        addFocusListener(new PromptListener());
    }

    public class PromptListener implements FocusListener {

        public PromptListener() {
            focusLost(null);
        }

        @Override
        public void focusLost(FocusEvent e) {
            if(getText().isEmpty()) {
                lastColor = getForeground();
                setForeground(promptColor);
                setText(prompt);
                showingPrompt = true;
            }
        }

        @Override
        public void focusGained(FocusEvent e) {
            if(showingPrompt) {
                if (lastColor != null) {
                    setForeground(lastColor);
                }
                setText("");
                showingPrompt = false;
            }
        }
    }

}