package org.lanyu.idea.plugin.generateaccessort.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.lanyu.idea.plugin.generateaccessort.util.GenerateUtil;

/**
 * 构建getter
 */
public class GenerateGetterAction extends AbstractGenerateAction {

    @Override
    public String getIdeaStandardActionId() {
        return "GenerateGetter";
    }
}
