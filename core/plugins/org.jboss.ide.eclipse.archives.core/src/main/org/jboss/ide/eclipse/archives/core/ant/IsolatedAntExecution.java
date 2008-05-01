package org.jboss.ide.eclipse.archives.core.ant;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;

import javax.xml.parsers.SAXParserFactory;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Ear;
import org.apache.tools.ant.taskdefs.Jar;
import org.apache.tools.ant.taskdefs.War;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.ZipFileSet;
import org.jboss.ide.eclipse.archives.core.model.IArchiveModel;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/************************************************************
 * IsolatedAntExecution is responsible for
 * IsolatedAntExecution functionality when antOnly=true
 * or a prerequisite is not met in the IsolatedTruezipExecution.
 *
 * @author  Tom Crosman
  ***********************************************************/
public class IsolatedAntExecution {
    /**
     * Packages file defaults to ".packages"
     */
    private String jbossPackages = IArchiveModel.DEFAULT_PACKAGES_FILE;
    private JBossArchivesTask parentTask;

    public IsolatedAntExecution(JBossArchivesTask parent) {
    	parentTask = parent;
    }
    
    /**
     * Invoked by the handler to create a task specific to
     * the JBoss package.  Initializes new task to the 
     * parent environment. Package scope so only the handler 
     * can invoke it.
     * @param type The ant task type to create.
     * @return the initialed task created.
     **/
    Zip createTask(String type) {
        Zip task = null;
        if ("jar".equals(type)) {
            task = new Jar();
        } else if ("war".equals(type)) {
            task = new War();
        } else if (type.indexOf("ear") != -1) {
            task = new Ear();
        } else if (type.indexOf("ejb") != -1) {
            task = new Jar();
        } else {
            throw new IllegalArgumentException("Unsupported type:  " + type);
        }

        /** copy as much over as possible **/
        task.setDescription(parentTask.getDescription());
        task.setLocation(parentTask.getLocation());
        task.setOwningTarget(parentTask.getOwningTarget());
        task.setProject(parentTask.getProject());
        task.setRuntimeConfigurableWrapper(parentTask.getRuntimeConfigurableWrapper());
        task.setTaskName(parentTask.getTaskName());
        task.setTaskType(parentTask.getTaskType());

        return task;
    }

    /**
     * Configures and launches the parsing of our xml file.
     *
     * @param handler the DocumentHandler that support parsing the
     *        authenticate.dtd formatted xml file.
     */
    private void parseMe(DefaultHandler handler) {
        try {
            SAXParserFactory parseFact = SAXParserFactory.newInstance();
            XMLReader parser = parseFact.newSAXParser().getXMLReader();
            parser.setFeature("http://xml.org/sax/features/namespaces", true);
            parser.setFeature("http://xml.org/sax/features/validation", false);
            parser.setContentHandler(handler);
            parser
                .parse(new InputSource(
                		new InputStreamReader(
                    	new FileInputStream(
                    			parentTask.getProject().getBaseDir()+ "/" +  jbossPackages))));
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    
    public void execute() throws BuildException {
        JBossPackagesHandler hndlr = new JBossPackagesHandler(parentTask);
        parseMe(hndlr);
        if (parentTask.isOutputAntTargetsOnly()) {
            parentTask.log("**** No Packages Created -- Ant Targets Only ****", Project.MSG_INFO);

            HashMap<String, StringBuilder> antDefs = hndlr.getArchives();
            if (parentTask.getProcessArchive() != null) {
                StringBuilder b = antDefs.get(parentTask.getProcessArchive());
                if (b != null) {
                    parentTask.log("Archive \"" + parentTask.getProcessArchive() + "\" as a string: \n" + b);
                } else {
                	String msg = "Archive \""+ parentTask.getProcessArchive() + "\" does not exist in the chosen project.";
                    parentTask.log(msg, Project.MSG_ERR);
                    throw new BuildException(msg);
                }
            } else {
                Collection<StringBuilder> antTasks = antDefs.values();
                for (StringBuilder b : antTasks) {
                    parentTask.log("\n" + b.toString());
                }
            }
            parentTask.log("**** No Packages Created -- Ant Targets Only ****");
        } else {
            HashMap<String, Zip> tasks = hndlr.getTasks();
            if (parentTask.getProcessArchive() != null) {
                Zip task = tasks.get(parentTask.getProcessArchive());
                if (task != null) {
                	parentTask.log("Executing ant task for " + parentTask.getProcessArchive());
                    task.execute();
                } else {
                    parentTask.log("No Archive found:  " + parentTask.getProcessArchive(), Project.MSG_ERR);
                }
            } else {
            	parentTask.log("Executing all tasks", Project.MSG_INFO);
                for (Zip tsk : tasks.values()) {
                    tsk.execute();
                }
            }
        }
    }
    
    public class JBossPackagesHandler extends DefaultHandler {

        private final HashMap<String, StringBuilder> antTaskDefs;
        private final HashMap<String, Zip> tasks;
        private StringBuilder currArchive;
        private String folder;
        private HashMap<String, String> fileset;
        private Zip currentTask;

        /**
         * Creates a new JBossPackagesHandler.
         * @param bossArchiveAntTask 
         */
        public JBossPackagesHandler(JBossArchivesTask jbossArchiveAntTask) {
            antTaskDefs = new HashMap<String, StringBuilder>();
            tasks = new HashMap<String, Zip>();
        }

        /**
         *  The doFileset method.
         */
        private void doFileset(String fldr) {
            ZipFileSet set = new ZipFileSet();
            set.setDir(new File(fileset.get("dir")));
            currArchive.append("        <zipfileset dir='");
            currArchive.append(fileset.get("xmlDir") + "' ");
            if (folder != null) {
                currArchive.append("prefix='" + folder + "' ");
                set.setPrefix(folder);

            }
            String includes = fileset.get("includes");
            String excludes = fileset.get("excludes");
            if (includes != null) {
                currArchive.append("includes='" + includes + "' ");
                set.setIncludes(includes);
            } else {
                set.setIncludes("");
            }
            if (excludes != null) {
                currArchive.append("excludes='" + excludes + "' ");
                set.setExcludes(excludes);
            } else {
                set.setExcludes("");
            }

            currArchive.append("/>\n");
            currentTask.addFileset(set);
            fileset = null;
        }

        /** 
         * {@inheritDoc}.
         * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
         */
        @Override
        public void endElement(String uri, String localName, String name)
                throws SAXException {
            if ("package".equals(localName) && (currArchive != null)) {
                currArchive.append("    </jar>\n");
                currArchive.append("</target>\n");

            } else if ("folder".equals(localName)) {
                if (folder != null) {
                    int slash = folder.lastIndexOf('/');
                    if (slash == -1) {
                        folder = null;
                    } else {
                        folder = folder.substring(0, slash);
                    }

                }

            } else if ("fileset".equals(localName)) {
                doFileset(folder);
            }
        }

        /**
         * Property getter for archives.
         * @return Returns the archives.
         */
        public HashMap<String, StringBuilder> getArchives() {
            return antTaskDefs;
        }

        /**
         * Property getter for tasks.
         * @return Returns the tasks.
         */
        public HashMap<String, Zip> getTasks() {
            return tasks;
        }

        /** 
         * {@inheritDoc}.
         * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
         */
        @Override
        public void startElement(String uri, String localName, String name,
                Attributes attributes) throws SAXException {
            if ("package".equals(localName)) {
                String type = attributes.getValue("type");
                String taskName = attributes.getValue("name");
                currentTask = createTask(type);
                tasks.put(taskName, currentTask);

                try {
                    currArchive = new StringBuilder("<target name='");

                    antTaskDefs.put(taskName, currArchive);

                    String tName = attributes.getValue("name");
                    currArchive.append(tName);
                    currArchive.append("' description='build " + tName + "' >\n");
                    String tmpToDir = attributes.getValue("todir");
                    String toDir =
                        tmpToDir.substring(tmpToDir.lastIndexOf("/") + 1);
                    currArchive.append("    <mkdir dir='" + toDir + "' />\n");

                    currArchive.append("    <jar destfile='" + toDir + "/" + tName
                            + "'>\n");

                    currentTask.setDestFile(new File(currentTask.getProject()
                        .getBaseDir()
                            + "/" + toDir + "/" + tName));
                } catch (IllegalArgumentException iae) {
                    currArchive = null;
                    currentTask = null;
                    iae.printStackTrace();
                }
            } else if ("folder".equals(localName)) {
                if (folder == null) {
                    folder = attributes.getValue("name");
                } else {
                    folder = folder + "/" + attributes.getValue("name");
                }
            } else if ("fileset".equals(localName)) {
                fileset = new HashMap<String, String>();
                String dir = attributes.getValue("dir").substring(1);
                String xmlDir = dir.substring(dir.indexOf("/") + 1);
                String includes = attributes.getValue("includes");
                String excludes = attributes.getValue("excludes");
                fileset.put("includes", includes);
                fileset.put("excludes", excludes);
                fileset.put("dir", xmlDir);
                fileset.put("xmlDir", xmlDir);
            }
        }
    }
}