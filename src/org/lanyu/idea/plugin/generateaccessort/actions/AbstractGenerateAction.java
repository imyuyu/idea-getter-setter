package org.lanyu.idea.plugin.generateaccessort.actions;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import org.lanyu.idea.plugin.generateaccessort.util.GenerateUtil;

public abstract class AbstractGenerateAction extends AnAction {
    private AnAction generateAction;

    public AbstractGenerateAction() {
        String actionId = getIdeaStandardActionId();
        ActionManager actionManager = ActionManager.getInstance();
        DefaultActionGroup myGenerateActionGroup = (DefaultActionGroup) ActionManager.getInstance().getAction("GenerateGroup");

        if (myGenerateActionGroup == null) {
            throw new NullPointerException("Could NOT find Generate Group!!");
        } else {
            generateAction = actionManager.getAction(actionId);
            if (generateAction == null) {
                throw new NullPointerException("Warning: No Idea Standard Action Found for: [" + actionId + "]");
            }
        }
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        //generateAction.actionPerformed(anActionEvent);
        GenerateUtil.generate(anActionEvent);
    }

    /**
     * 获取idea标准构建action的ID
     *
     * @return
     */
    public abstract String getIdeaStandardActionId();
}
