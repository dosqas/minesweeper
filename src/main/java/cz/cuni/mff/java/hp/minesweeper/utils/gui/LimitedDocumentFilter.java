package cz.cuni.mff.java.hp.minesweeper.utils.gui;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * The `LimitedDocumentFilter` class is a custom `DocumentFilter` that limits the number of characters
 * that can be inserted into a document. Used to limit the length of text in an `InputField`.
 */
public class LimitedDocumentFilter extends DocumentFilter {
    private final int maxCharacters;

    /**
     * Constructs a `LimitedDocumentFilter` with the specified maximum number of characters.
     *
     * @param maxCharacters the maximum number of characters allowed in the document
     */
    public LimitedDocumentFilter(int maxCharacters) {
        this.maxCharacters = maxCharacters;
    }

    /**
     * Inserts a string into the document if it does not exceed the maximum number of characters.
     *
     * @param fb the `FilterBypass` that can be used to mutate the document
     * @param offset the offset into the document to insert the content
     * @param string the string to insert
     * @param attr the attributes to associate with the inserted content
     * @throws BadLocationException if the insert would result in an invalid location
     */
    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        if ((fb.getDocument().getLength() + string.length()) <= maxCharacters) {
            super.insertString(fb, offset, string, attr);
        }
    }

    /**
     * Replaces a portion of the document with the given text if it does not exceed the maximum number of characters.
     *
     * @param fb the `FilterBypass` that can be used to mutate the document
     * @param offset the offset into the document to replace the content
     * @param length the length of the text to replace
     * @param text the text to replace with
     * @param attrs the attributes to associate with the replaced content
     * @throws BadLocationException if the replacement would result in an invalid location
     */
    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if ((fb.getDocument().getLength() - length + text.length()) <= maxCharacters) {
            super.replace(fb, offset, length, text, attrs);
        }
    }
}