package org.plantuml.idea.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import org.apache.commons.lang3.StringUtils;
import org.plantuml.idea.CopyPlantumlTextDialog;

import java.awt.*;

public class PsiNavigationDemoAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Project project = anActionEvent.getData(CommonDataKeys.PROJECT);
        Editor editor = anActionEvent.getData(CommonDataKeys.EDITOR);
        PsiFile psiFile = anActionEvent.getData(CommonDataKeys.PSI_FILE);
        VirtualFile virtualFile = psiFile.getVirtualFile();
        if (editor == null || psiFile == null || project == null) {
            return;
        }
        final StringBuilder infoBuilder = new StringBuilder();

        ProjectFileIndex fileIndex = ProjectRootManager.getInstance(project).getFileIndex();
        String projectBasePath = project.getBasePath();
        if (projectBasePath == null) {
            projectBasePath = "null";
        }
        final String TEMPLATE1 = "!$path_<PROJECT> = \"<PROJECT_PATH>/\"";
        String umlProject = TEMPLATE1.replace("<PROJECT>", project.getName()).replace("<PROJECT_PATH>", projectBasePath);
        infoBuilder.append(umlProject).append("\r\n");

        int offset = editor.getCaretModel().getOffset();

        PsiElement element = psiFile.findElementAt(offset);
        PsiMethod psiMethod = PsiTreeUtil.getParentOfType(element, PsiMethod.class);
        PsiClass psiClass;
        if (psiMethod != null) {
            psiClass = psiMethod.getContainingClass();
        } else {
            psiClass = PsiTreeUtil.getParentOfType(element, PsiClass.class);
        }

        if (psiClass != null) {
            final String TEMPLATE2 = "!$path_<CLASS> = %string($path_<PROJECT> + \"<CLASS_RELATIVE_PATH>\")\r\n" +
                    "url of <CLASS> is [[$path_<CLASS>]]";
            String className = psiClass.getName();
            if (className == null) {
                className = "";
            }
            String relativePath = VfsUtilCore.getRelativePath(virtualFile, project.getProjectFile().getParent().getParent());
//            relativePath = VfsUtilCore.getRelativePath(virtualFile, project.getBaseDir());
            String umlClass = TEMPLATE2.replace("<CLASS>", className)
                    .replace("<PROJECT>", project.getName())
                    .replace("<CLASS_RELATIVE_PATH>", relativePath);
            infoBuilder.append(umlClass).append("\r\n");
        }

        if (psiMethod != null) {
            final String TEMPLATE = "<CLASS> -> <CLASS>:[[$method_url($path_<CLASS>,\"<METHOD>\")]]";
            String umlMethod = TEMPLATE.replace("<CLASS>", psiClass.getName())
                    .replace("<METHOD>", psiMethod.getName());
            infoBuilder.append(umlMethod).append("\r\n");
        }

        CopyPlantumlTextDialog dialog = new CopyPlantumlTextDialog();
        String tempText = StringUtils.removeEnd(infoBuilder.toString(), "\r\n");
        dialog.setText(tempText);
        dialog.pack();
        // 这个只有放在pack后才起作用，待研究为啥会这样
        dialog.setBounds(0, 0, 600, 400);
        Point centerPoint = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
        Point topLeftPoint = new Point((centerPoint.x - dialog.getWidth() / 2),
                centerPoint.y - dialog.getHeight() / 2);
        dialog.setLocation(topLeftPoint);
        dialog.setVisible(true);
    }

    @Override
    public void update(AnActionEvent e) {
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        e.getPresentation().setEnabled(editor != null && psiFile != null);
    }

}
