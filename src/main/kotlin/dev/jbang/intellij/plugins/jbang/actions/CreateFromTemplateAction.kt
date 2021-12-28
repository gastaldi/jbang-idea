package dev.jbang.intellij.plugins.jbang.actions

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.vfs.LocalFileSystem
import dev.jbang.intellij.plugins.jbang.JBangCli.generateScriptFrommTemplate

class CreateFromTemplateAction : AnAction(), DumbAware {
    override fun actionPerformed(e: AnActionEvent) {
        val dialogWrapper = JbangTemplatesDialogWrapper()
        if (dialogWrapper.showAndGet()) {
            val scriptName = dialogWrapper.getScriptFileName()
            val templateName = dialogWrapper.getTemplateName()
            if (scriptName.isNotEmpty()) {
                ApplicationManager.getApplication().runWriteAction {
                    val project = e.getData(CommonDataKeys.PROJECT)!!
                    try {
                        generateScriptFrommTemplate(templateName, scriptName, project.basePath!!)
                        LocalFileSystem.getInstance().refresh(true)
                    } catch (e: Exception) {
                        val errorText = "Failed to create script from template, please check template and script name!"
                        val jbangNotificationGroup = NotificationGroupManager.getInstance().getNotificationGroup("JBang Failure");
                        jbangNotificationGroup.createNotification("Failed to resolve DEPS", errorText, NotificationType.ERROR).notify(project)
                    }
                }
            }
        }
    }
}