/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mindemia.codeinsert;

/**
 *
 * @author rickard
 */
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.spi.editor.completion.*;
import org.netbeans.spi.editor.completion.CompletionProvider;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.AsyncCompletionQuery;
import org.netbeans.spi.editor.completion.support.AsyncCompletionTask;
import org.openide.util.NbPreferences;

@MimeRegistration(mimeType = "text/x-java", service = CompletionProvider.class)
public class AICodeCompletionProvider implements CompletionProvider {
    
    private final int CONTEXT_LENGTH = NbPreferences.forModule(AICompletionOptionsPanel.class).getInt("context_length", 10);
    private final AICompletionClient aiClient = new AICompletionClient();

    public AICodeCompletionProvider() {
    }

    @Override
    public CompletionTask createTask(int queryType, JTextComponent component) {
        System.out.println("createTask");
        if (queryType != CompletionProvider.COMPLETION_QUERY_TYPE) {
            return null;
        }
        return new AsyncCompletionTask(new AsyncCompletionQuery() {

            @Override
            protected void query(CompletionResultSet resultSet, Document document, int caretOffset) {
                String prompt = CodeContextExtractor.extractPrompt(component, CONTEXT_LENGTH);

                String aiSuggestion = aiClient.fetchSuggestion(prompt);
                resultSet.setWaitText("Waiting for AI response.");
                resultSet.addItem(new AICodeCompletionItem(aiSuggestion, caretOffset));
                resultSet.finish();
            }
        }, component);

    }

    @Override
    public int getAutoQueryTypes(JTextComponent component, String typedText) {
        return 0;
    }

}
