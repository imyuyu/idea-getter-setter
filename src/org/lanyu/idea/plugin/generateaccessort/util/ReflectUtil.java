package org.lanyu.idea.plugin.generateaccessort.util;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.ui.CollectionListModel;

import java.util.List;

public final class ReflectUtil {
    private ReflectUtil() {
    }

    /**
     * 首字母转大写
     *
     * @param fieldName
     * @return
     */
    public static String upperCaseFirst(String fieldName) {
        return fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    public static List<PsiMethod> getMethods(PsiClass psiClass) {
        return new CollectionListModel<PsiMethod>(psiClass.getMethods()).getItems();
    }

    public static List<PsiField> getFields(PsiClass psiClass) {
        return new CollectionListModel<PsiField>(psiClass.getFields()).getItems();
    }
}
