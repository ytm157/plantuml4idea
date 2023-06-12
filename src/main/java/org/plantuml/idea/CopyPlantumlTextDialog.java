package org.plantuml.idea;

import com.intellij.codeInsight.editorActions.TextBlockTransferable;
import com.intellij.codeInsight.editorActions.TextBlockTransferableData;
import com.intellij.openapi.ide.CopyPasteManager;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class CopyPlantumlTextDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonCopy;
    private JButton buttonCancel;
    private JEditorPane editorPane1;

    public CopyPlantumlTextDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonCopy);

        buttonCopy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCopy();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onCopy() {
        // add your code here
        final String txt = this.editorPane1.getText();
        List<TextBlockTransferableData> extraData = new ArrayList<>();
        TextBlockTransferable textBlockTransferable = new TextBlockTransferable(txt, extraData, null);
        CopyPasteManager.getInstance().setContents(textBlockTransferable);
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public void setText(String text) {
        this.editorPane1.setText(text);
    }

}
