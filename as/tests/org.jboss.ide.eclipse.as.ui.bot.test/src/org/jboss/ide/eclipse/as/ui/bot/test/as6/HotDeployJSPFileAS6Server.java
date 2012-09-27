package org.jboss.ide.eclipse.as.ui.bot.test.as6;

import org.jboss.ide.eclipse.as.ui.bot.test.template.HotDeployJSPFileTemplate;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;

@Require(server=@Server(type=ServerType.JbossAS, version="6.1", state=ServerState.Running), clearWorkspace=false, clearProjects=false)
public class HotDeployJSPFileAS6Server extends HotDeployJSPFileTemplate {

}
