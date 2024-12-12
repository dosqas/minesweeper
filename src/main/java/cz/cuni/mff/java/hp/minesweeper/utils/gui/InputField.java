package cz.cuni.mff.java.hp.minesweeper.utils.gui;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;

/**
 * The `InputField` class is a custom Swing component that encapsulates a `JTextField` with specific dimensions,
 * text length limit, initial text, and font size. It also customizes the painting of the component to include a background texture.
 */
public class InputField extends JComponent {
    private final JTextField textField;

    /**
     * Constructs an `InputField` with the specified dimensions, text length limit, initial text, and font size.
     *
     * @param width the width of the input field
     * @param height the height of the input field
     * @param maxTextLength the maximum length of the text
     * @param initialText the initial text to display in the input field
     * @param fontSize the font size of the text
     */
    public InputField(int width, int height, int maxTextLength, String initialText, float fontSize) {
        this.setPreferredSize(new Dimension(width, height));
        this.setMaximumSize(new Dimension(width, height));
        this.setMinimumSize(new Dimension(width, height));
        this.setOpaque(false);

        textField = GUIUtils.createTextField(width, height, fontSize);
        textField.setText(initialText);

        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new LimitedDocumentFilter(maxTextLength));

        textField.setBounds(0, 0, width, height);
        this.setLayout(null);
        this.add(textField);
    }

    /**
     * Customizes the painting of the component to include a background texture.
     *
     * @param g the `Graphics` object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        String path = "/textures/labels/" + GUIUtils.getThemeFileName();
        Image texture = Toolkit.getDefaultToolkit().getImage(getClass().getResource(path));

        g2.drawImage(texture, 0, -1, this);
    }

    /**
     * Returns the `JTextField` contained within this component.
     *
     * @return the `JTextField` contained within this component
     */
    public JTextField getTextField() {
        return textField;
    }
}