package org.lanyu.idea.plugin.generateaccessort.util;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.ui.CollectionListModel;

import static org.lanyu.idea.plugin.generateaccessort.common.Constants.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 构建工具
 */
public final class GenerateUtil {

    private GenerateUtil() {
    }

    /**
     * 执行构建
     *
     * @param anActionEvent
     */
    public static void generate(AnActionEvent anActionEvent) {
        final String type = anActionEvent.getPresentation().getText();
        final PsiClass psiClass = getSelectedClass(anActionEvent);
        //开一个线程
        if (psiClass != null) {
            new WriteCommandAction.Simple(psiClass.getProject(), psiClass.getContainingFile()) {
                @Override
                protected void run() throws Throwable {
                    List<PsiField> fields = ReflectUtil.getFields(psiClass);
                    if (fields.size() == 0) {
                        return;
                    }
                    generateAccessort(psiClass, fields, type);
                }
            }.execute();
        }
    }

    private static void generateAccessort(PsiClass psiClass, List<PsiField> fields, String type) {
        PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(psiClass.getProject());
        //获取所有函数
        List<PsiMethod> list = ReflectUtil.getMethods(psiClass);
        Set<String> methodSet = new HashSet<String>();
        for (PsiMethod m : list) {
            methodSet.add(m.getName());
        }
        for (PsiField field : fields) {
            StringBuilder methodText = new StringBuilder();
            //final就不在构建了
            if (field.getModifierList().hasModifierProperty(PsiModifier.FINAL)) {
                continue;
            }
            PsiMethod method = null;
            if (GENERATE_GETTER.equals(type)) {
                methodText.append(generateGetter(field));
                method = elementFactory.createMethodFromText(methodText.toString(), psiClass);
                //如果方法已经存在，就不再插入
                if (methodSet.contains(method.getName())) {
                    continue;
                }
                psiClass.add(method);
            }
            if (GENERATE_SETTER.equals(type)) {
                methodText.append(generateSetter(field));
                method = elementFactory.createMethodFromText(methodText.toString(), psiClass);
                //如果方法已经存在，就不再插入
                if (methodSet.contains(method.getName())) {
                    continue;
                }
                psiClass.add(method);
            }
            if (GENERATE_ALL.equals(type) || GENERATE_Accessort.equals(type)) {
                methodText.append(generateGetter(field));
                method = elementFactory.createMethodFromText(methodText.toString(), psiClass);
                //如果方法已经存在，就不再插入
                if (methodSet.contains(method.getName())) {
                    continue;
                }
                psiClass.add(method);
                methodText = new StringBuilder(generateSetter(field));
                method = elementFactory.createMethodFromText(methodText.toString(), psiClass);
                //如果方法已经存在，就不再插入
                if (methodSet.contains(method.getName())) {
                    continue;
                }
                psiClass.add(method);
            }
        }
    }

    /**
     * 构建getter
     *
     * @param field
     */
    private static String generateGetter(PsiField field) {
        String doc = JavaDocUtil.getFieldJavaDoc(field, GENERATE_GETTER);
        StringBuilder sb = new StringBuilder();
        if (doc != null) {
            sb.append(doc);
        }
        sb.append("public ");
        //判断字段是否是static
        if (field.getModifierList().hasModifierProperty(PsiModifier.STATIC)) {
            sb.append("static ");
        }
        sb.append(field.getType().getPresentableText()).append(" ");
        if (field.getType().getPresentableText().equals("boolean")) {
            sb.append("is");
        } else {
            sb.append("get");
        }
        sb.append(ReflectUtil.upperCaseFirst(field.getName()));
        sb.append("(){\n");
        sb.append(" return this.").append(field.getName()).append(";}\n");

        return sb.toString();
    }

    /**
     * 构建setter
     *
     * @param field
     */
    private static String generateSetter(PsiField field) {
        String doc = JavaDocUtil.getFieldJavaDoc(field, GENERATE_SETTER);
        StringBuilder sb = new StringBuilder();
        if (doc != null) {
            sb.append(doc);
        }
        sb.append("public ");
        //判断字段是否是static
        if (field.getModifierList().hasModifierProperty(PsiModifier.STATIC)) {
            sb.append("static ");
        }
        sb.append("void ");
        sb.append("set").append(ReflectUtil.upperCaseFirst(field.getName()));
        sb.append("(").append(field.getType().getPresentableText()).append(" ").append(field.getName()).append("){\n");
        sb.append("this.").append(field.getName()).append(" = ").append(field.getName()).append(";");
        sb.append("}");
        return sb.toString();

    }

    /**
     * 获取选择的类
     *
     * @param anActionEvent
     * @return
     */
    public static PsiClass getSelectedClass(AnActionEvent anActionEvent) {
        PsiFile psiFile = anActionEvent.getData(LangDataKeys.PSI_FILE);
        Editor editor = anActionEvent.getData(PlatformDataKeys.EDITOR);
        if (psiFile == null || editor == null) {
            anActionEvent.getPresentation().setEnabled(false);
            return null;
        }
        //需要检测一下当前光标是不是在类里
        int offset = editor.getCaretModel().getOffset();
        PsiElement elementAt = psiFile.findElementAt(offset);

        if (elementAt == null) {
            return null;

        }
        return PsiTreeUtil.getParentOfType(elementAt, PsiClass.class);
    }
}
