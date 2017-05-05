package org.lanyu.idea.plugin.generateaccessort.util;

import com.intellij.psi.PsiField;
import org.lanyu.idea.plugin.generateaccessort.common.Constants;

/**
 * 注释工具
 */
public final class JavaDocUtil {
    private JavaDocUtil() {
    }

    /**
     * 获取字段的注释
     *
     * @param field 字段对象
     * @return 格式化后的注释
     */
    public static String getFieldJavaDoc(PsiField field, String type) {
        String doc;
        String prefix = Constants.GETTER_PREFIX;

        if (field.getDocComment() == null) {
            doc = "//" + field.getName();
        } else {
            doc = field.getDocComment().getText();
        }
        if (!"".equals(doc)) {
            if (doc.startsWith("//")) {
                if (Constants.GENERATE_SETTER.equals(type)) {
                    doc = doc.replace("//", "/**\n* " + prefix + " ") + "\n* @param " + field.getName() + " \n*/";
                } else {
                    doc = doc.replace("//", "/**\n* " + prefix + " ") + "\n* @return " + field.getName() + " \n*/";
                }
            } else if (doc.startsWith("/**")) {
                String docContent = doc.replace("/**\n", "").replace("*/", "").replace("\n", "").trim();
                if (Constants.GENERATE_SETTER.equals(type)) {
                    doc = doc.replace(docContent, docContent.replace("* ", "* " + prefix + " ") + "\n* @param " + field.getName());
                } else {
                    doc = doc.replace(docContent, docContent.replace("* ", "* " + prefix + " ") + "\n* @return " + field.getName());
                }
            }
        }
        return doc;
    }
}
